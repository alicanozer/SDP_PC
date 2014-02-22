package world.object;

import world.PositionProvider;
import world.World;
import world.support.Corrections;
import world.support.RateOfChangePredictor;
import world.support.TimedVector;
import geometry.Vector;

/**
 * @author apljungquist
 * 
 */
public class MobileObject implements MobilePixelObject, MobileRealObject {
	World world;
	double height;
	int objectNumber;
	PositionProvider positionProvider;
	
	TimedVector[] pixelPositions;
	TimedVector[] realPositions;
	TimedVector[] realVelocities;
	long currentFrameNumber;
	
	public MobileObject(int objectNumber, PositionProvider positionProvider, World world, double height) {
		this.objectNumber = objectNumber;
		this.positionProvider = positionProvider;
		this.world = world;
		this.height = height;
		
		pixelPositions = new TimedVector[2];
		realPositions = new TimedVector[2];
		realVelocities = new TimedVector[1];
		currentFrameNumber = -1;
	}
	
	private void updateMaybe() throws Exception {
		//Check if already up to date
		if (currentFrameNumber == positionProvider.getCurrentFrameNumber()) {
			return;
		}
		//Try to populate the local history using the position provider.
		//This step is a bit complex to account for the scenario in which the position provider is unable to provide values for some frames.
		//Also, to avoid the unlikely scenario where there are no two positions available, only check the last 10 frames for positions.
		long frameNumber = positionProvider.getCurrentFrameNumber();
		int index = 0;
		for (int i = 0; i < 10 && index < pixelPositions.length; i++) {
			try {
				pixelPositions[index] = getPosition(frameNumber);
				index++;
			} catch (Exception e) {
				
			}
			frameNumber--;
		}
		//Check so that we were able to update all of the pixelPositions
		if (index != pixelPositions.length) {
			throw new Exception("Unable to populate local history by querying PositionProvider");
		}
		//Use the pixelPositions to calculate realPositions
		for (int i = 0; i < realPositions.length; i++) {
			realPositions[i] = new TimedVector(Corrections.applyAll(pixelPositions[i], height, world.getPixelCameraPosition(), world.getRealCameraElevation(), world.getRealUnitsPerPixel()), pixelPositions[i].getTime());
		}
		//Use the realPositions to calculate realVelocities
		realVelocities[0] = RateOfChangePredictor.findRateOfChange(realPositions[0], realPositions[1]);
		//Finally, set currentFrameNumber to indicate that everything is up to date.
		currentFrameNumber = positionProvider.getCurrentFrameNumber();
	}
	
	@Override
	public Vector getRealPosition() throws Exception {
		updateMaybe();
		return realPositions[0];
	}

	@Override
	public Vector getRealOrientation() throws Exception {
		return getRealVelocity();
	}

	@Override
	public Vector getRealVelocity() throws Exception {
		updateMaybe();
		return realVelocities[0];
	}

	@Override
	public Vector getPixelPosition() throws Exception {
		updateMaybe();
		return pixelPositions[0];
	}
	
	private TimedVector getPosition(long requestedFrameNumber) throws Exception {
		switch (objectNumber) {
		case 0:
			return positionProvider.getRobot0Position(requestedFrameNumber);
		case 1:
			return positionProvider.getRobot1Position(requestedFrameNumber);
		case 2:
			return positionProvider.getRobot2Position(requestedFrameNumber);
		case 3:
			return positionProvider.getRobot3Position(requestedFrameNumber);
		case 4:
			return positionProvider.getBallPosition(requestedFrameNumber);
		default:
			throw new Exception("Unhandled mobile object");
		}
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
}
