package world;

import geometry.Frame;
import geometry.Vector;
import georegression.struct.point.Point2D_I32;

/**
 * @author apljungquist
 * 
 */
public class MobileObject implements MobilePixelObject, MobileRealObject {

	protected Frame previousPosition;
	protected Frame currentPosition;
	protected Frame previousOrientation;
	protected Frame currentOrientation;
	
	public MobileObject() {
		previousOrientation = new Frame(1,1);
		currentOrientation = new Frame(1,1);
		previousPosition = new Frame(1,1);
		currentPosition = new Frame(1,1);
	}
	
	@Override
	public Vector getRealPosition() {
		return currentPosition.getVector();
	}

	@Override
	public Vector getRealOrientation() {
		return currentOrientation.getVector();
	}

	@Override
	public Vector getRealVelocity() {
		if (currentPosition == null || previousPosition == null) {
			return new Vector(0, 0);
			//TODO Decide and figure out how to handle null velocities
		}
		Frame delta = currentPosition.subtract(previousPosition);
		if (delta.getTime() != 0) {
			//TODO Figure out how to handle division by zero. One way is to make setting two frames at the same time disallowed.
			return delta.getVector().scalarMultiplication(1000/delta.getTime()); }
		else {
			return new Vector(0,0);
		}
	}

	@Override
	public Vector getPixelPosition() {
		return currentPosition.getVector();
	}

	@Override
	public void setPixelPosition(Vector position) {
		if (position != null) {
			previousPosition = currentPosition;
			currentPosition = new Frame(position);
		}
	}

	@Override
	public Vector getPixelOrientation() {
		return currentOrientation;
	}

	@Override
	public void setPixelOrientation(Vector orientation) {
		if (orientation != null) {
			previousOrientation = currentOrientation;
			currentOrientation = new Frame(orientation);
		}
	}

	@Override
	public void setPixelPosition(Point2D_I32 position) {
		if (position != null) {
			previousPosition = currentPosition;
			currentPosition = new Frame(position.x, position.y);
		}
	}
	
	public String toString() {
		return "Orientation: " + getRealOrientation() + 
				"\nPosition: " + getRealPosition() +
				"\nVelocity: " + getRealVelocity();
				
	}
}
