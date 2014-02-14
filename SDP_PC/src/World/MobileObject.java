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
		return currentOrientation == null ? null : currentOrientation.getVector();
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
		if (position != null) {
			setPixelPosition(position.getX(), position.getY());
		}
	}

	@Override
	public Vector getPixelOrientation() {
		return currentOrientation == null ? null : currentOrientation.getVector();
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
		if (currentPosition != null && newFrame.subtract(currentPosition).getTime() > 1) {
			previousPosition = currentPosition;
		}
		//Even if the time has not changed we allow changing the position.
		currentPosition = newFrame;
	}
	
	private void setPixelOrientation(double x, double y) {
		Frame newFrame = new Frame(x, y);
		// Only shift the history if the time has changed. If it hasn't trying
		// to calculate the velocity cause division by zero.
		if (currentOrientation != null && newFrame.subtract(currentOrientation).getTime() > 1) {
			previousOrientation = currentOrientation;
		}
		//Even if the time has not changed we allow changing the orientation.
		currentOrientation = newFrame;
	}
	
	public String toString() {
		return "Orientation: " + getRealOrientation() + 
				"\nPosition: " + getRealPosition() +
				"\nVelocity: " + getRealVelocity();
				
	}
}
