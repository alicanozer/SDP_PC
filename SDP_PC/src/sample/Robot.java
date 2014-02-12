/**
 * 
 */
package sample;

import geometry.Vector;
import world.MobilePixelObject;
import world.MobileRealObject;

/**
 * @author apljungquist
 *
 */
public class Robot implements MobilePixelObject, MobileRealObject {

	/**
	 * 
	 */
	public Robot(double height) {
		// TODO Auto-generated constructor stub
	}
	public Robot() {
		this(18.0);
	}

	/* (non-Javadoc)
	 * @see world.MobileRealObject#getRealPosition(long)
	 */
	@Override
	public Vector getRealPosition(long time) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.MobileRealObject#getRealOrientation(long)
	 */
	@Override
	public Vector getRealOrientation(long time) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.MobileRealObject#getRealVelocity(long)
	 */
	@Override
	public Vector getRealVelocity(long time) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.MobilePixelObject#getPixelPosition(long)
	 */
	@Override
	public Vector getPixelPosition(long time) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.MobilePixelObject#setPixelPosition(geometry.Vector)
	 */
	@Override
	public void setPixelPosition(Vector position) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see world.MobilePixelObject#setPixelPosition(geometry.Vector, long)
	 */
	@Override
	public void setPixelPosition(Vector position, long time) {
		// TODO Auto-generated method stub

	}

}
