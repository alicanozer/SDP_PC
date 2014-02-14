package world.object;

import geometry.Frame;
import geometry.Vector;
import georegression.struct.point.Point2D_I32;

/**
 * @author apljungquist
 * 
 */
public class MobileObject implements MobilePixelObject, MobileRealObject {

	protected Frame[] positions;
	protected Frame[] velocities;
	protected boolean dirtyVelocities;
	protected Frame[] accelerations;
	protected boolean dirtyAccelerations;
	
	protected Frame[] orientations;
	
	protected double realUnitsPerPixel;
	
	/**
	 * Creates a new MobileObject. Features of this implementation include
	 * <br>
	 * - Ability to return distances and velocities in cm and cm/s.
	 * <br>
	 * - Ability to make a best guess of data where an actual reading is not available (i.e. it will not return null once warmed up).
	 * <br>
	 * TODO Correct real measurements for barrel distortion?
	 * <br>
	 * TODO Correct real measurements for perspective projection?
	 * <br>
	 * @param realUnitsPerPixel
	 */
	public MobileObject(double realUnitsPerPixel) {
		this.realUnitsPerPixel = realUnitsPerPixel;
		this.positions = new Frame[3];
		this.velocities = new Frame[2];
		this.accelerations = new Frame[1];
		
		dirtyVelocities = true;
		dirtyAccelerations = true;
		
		this.orientations = new Frame[1];		
	}
	
	@Override
	public Vector getRealPosition() throws Exception {
		//Check so that the history is sufficiently long to successfully complete request.
		//We need at least two past positions to determine our the current position.
		if (positions[1] == null) {
			throw new Exception("Not enough history available");
		}
		//Make sure all velocities are up to date.
		if (dirtyVelocities) {
			updateVelocities();
		}
		
		//Calculate the change in time since the last recorded position
		long deltaTime = System.currentTimeMillis()-positions[0].getTime();
		
		//Calculate the current position of an object traveling at velocities[0] deltaTime milliseconds ago
		Vector currentPosition = positions[0].add(velocities[0].getVector().scalarMultiplication(deltaTime/1000.0));
		
		//Return the current position in real units.
		return currentPosition.scalarMultiplication(realUnitsPerPixel);
	}

	@Override
	public Vector getRealOrientation() {
		return orientations[0] == null ? null : orientations[0].getVector();
	}

	@Override
	public Vector getRealVelocity() throws Exception {
		//Check so that the history is sufficiently long to successfully complete request.
		//We need three past positions to determine our the current velocity.
		if (positions[2] == null) {
			throw new Exception("Not enough history available");
		}
		//Make sure all accelerations are up to date.
		if (dirtyAccelerations) {
			updateAccelerations();
		}
		
		//Calculate the change in time since the last recorded velocity.
		long deltaTime = System.currentTimeMillis()-velocities[0].getTime(); 
		
		//Calculate how much the velocity would have changed in this time given that the last know acceleration was accelerations[0]
		Vector currentVelocity = velocities[0].getVector().add(accelerations[0].getVector().scalarMultiplication(deltaTime/1000.0));
		
		//Return this velocity in real units.
		return currentVelocity.scalarMultiplication(realUnitsPerPixel);
	}
	
	/**
	 * Updates the accelerations array.
	 */
	protected void updateAccelerations() {
		//Make sure the velocities are up to date as they will be used in calculations.
		if (dirtyVelocities) {
			updateVelocities();
		}
		//Shift the history one step
		shiftAccelerations();
		//Calculate the latest acceleration from the latest velocities
		accelerations[0] = calculateDerivative(velocities[0], velocities[1]);
		//Indicate that all acceleration values are up to date.
		dirtyAccelerations = false;
	}
	/**
	 * Updates the velocities array.
	 */
	protected void updateVelocities() {
		//Shift the history one step
		for (int i = velocities.length-1; i > 0; i--) {
			velocities[i] = velocities[i-1]; 
		}
		//Calculate the latest velocity from the latest positions.
		velocities[0] = calculateDerivative(positions[0], positions[1]);
		//Indicate that velocities are up to date.
		dirtyVelocities = false;
	}

	@Override
	public Vector getPixelPosition() {
		return positions[0] == null ? null : positions[0].getVector();
	}

	@Override
	public void setPixelPosition(Vector position) {
		if (position != null) {
			setPixelPosition(position.getX(), position.getY());
		}
	}

	@Override
	public Vector getPixelOrientation() {
		return orientations[0] == null ? null : orientations[0].getVector();
	}

	@Override
	public void setPixelOrientation(Vector orientation) {
		if (orientation != null) {
			setPixelOrientation(orientation.getX(), orientation.getY());
		}
	}

	@Override
	public void setPixelPosition(Point2D_I32 position) {
		if (position != null) {
			setPixelPosition(position.getX(), position.getY());
		}
	}
	
	private void setPixelPosition(double x, double y) {
		Frame newFrame = new Frame(x, y);
		// Only shift the history if the time has changed. If it hasn't trying
		// to calculate the velocity cause division by zero.
		if (positions[0] != null && newFrame.subtract(positions[0]).getTime() > 1) {
			positions[2] = positions[1];
			positions[1] = positions[0];
		}
		//Even if the time has not changed we allow changing the position.
		positions[0] = newFrame;
		//Whenever the last known position changes it makes accelerations and velocities outdated.
		dirtyAccelerations = true;
		dirtyVelocities = true;
	}
	
	private void setPixelOrientation(double x, double y) {
		Frame newFrame = new Frame(x, y);
		// Only shift the history if the time has changed. If it hasn't trying
		// to calculate the velocity cause division by zero.
		if (orientations[0] != null && newFrame.subtract(orientations[0]).getTime() > 1) {
			orientations[2] = orientations[1];
			orientations[1] = orientations[0];
		}
		//Even if the time has not changed we allow changing the orientation.
		orientations[0] = newFrame;
	}
	
	public String toString() {
		String orientation, position, velocity;
		
		orientation = "Orientation: " + getRealOrientation();
		
		try {
			position = "Position: " + getRealPosition();
		} catch (Exception e) {
			position = "Position: " +e.getMessage();
		}
		
		try {
			velocity = "Velocity: " + getRealVelocity();
		} catch (Exception e) {
			velocity = "Velocity: " +e.getMessage();
		}				
		return orientation+"\n"+position+"\n"+velocity;
	}
	
	/**
	 * Calculates the rate of change between two frames. The vector in the
	 * result represents the rate of change per second. The time in the
	 * resulting frame represents the time at which this rate of change existed.
	 * This time is taken as the time of the later frame.
	 * 
	 * @param frame0
	 *            The later of the two frames (e.g. the most recent position)
	 * @param frame1
	 *            The earlier of the two frames (e.g. some historical position)
	 * @return Returns a frame representing the rate of change at some point in
	 *         time.
	 */
	protected Frame calculateDerivative(Frame frame0, Frame frame1) {
		//Calculate the change in the vector and in the time.
		Frame delta = frame0.subtract(frame1);
		//Normalize the vector to represent the change over one second.
		Vector rateOfChange = delta.getVector().scalarMultiplication(1000.0 / delta.getTime());
		//Return a frame with the above rate of change as the vector and the time of the first given frame as time.
		return new Frame(rateOfChange, frame0.getTime());
	}
	
	/**
	 * Shifts the position history one step. Do this before adding a new value
	 * to the history.
	 */
	protected void shiftPositions() {
		for (int i = positions.length-1; i > 0; i--) {
			positions[i] = positions[i-1]; 
		}
	}
	
	/**
	 * Shifts the velocity history one step. Do this before adding a new value
	 * to the history.
	 */
	protected void shiftVelocities() {
		for (int i = velocities.length-1; i > 0; i--) {
			velocities[i] = velocities[i-1]; 
		}
	}
	
	/**
	 * Shifts the acceleration history one step. Do this before adding a new value
	 * to the history.
	 */
	protected void shiftAccelerations() {
		for (int i = accelerations.length-1; i > 0; i--) {
			accelerations[i] = accelerations[i-1]; 
		}
	}
}
