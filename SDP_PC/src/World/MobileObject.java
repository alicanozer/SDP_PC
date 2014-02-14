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
	}
	
	@Override
	public Vector getRealPosition() {
		return currentPosition == null ? null : currentPosition.getVector();
	}

	@Override
	public Vector getRealOrientation() {
		return currentOrientation.getVector();
	}

	@Override
	public Vector getRealVelocity() {
		if (previousPosition == null) {
			return null;
		}else {
			Frame delta = currentPosition.subtract(previousPosition);
			return delta.getVector().scalarMultiplication(1000/delta.getTime());
		}
	}

	@Override
	public Vector getPixelPosition() {
		return currentPosition == null ? null : currentPosition.getVector();
	}

	@Override
	public void setPixelPosition(Vector position) {
		//TODO If time is the same, change only current to avoid division by 0 for velocity
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
