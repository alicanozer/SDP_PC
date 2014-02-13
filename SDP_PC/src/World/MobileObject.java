package world;

import geometry.Frame;
import geometry.Vector;

/**
 * @author apljungquist
 * 
 */
public class MobileObject implements MobilePixelObject, MobileRealObject {

	protected Frame previousPosition;
	protected Frame currentPosition;
	protected Frame previousOrientation;
	protected Frame currentOrientation;

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
		Frame delta = currentPosition.subtract(previousPosition);
		return delta.getVector().scalarMultiplication(1/delta.getTime());
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
}
