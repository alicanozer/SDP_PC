/**
 * 
 */
package sample;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import world.object.MobilePixelObject;
import world.object.MobileRealObject;

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
	public Vector getRealPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.MobileRealObject#getRealOrientation(long)
	 */
	@Override
	public Vector getRealOrientation() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.MobileRealObject#getRealVelocity(long)
	 */
	@Override
	public Vector getRealVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.MobilePixelObject#getPixelPosition(long)
	 */
	@Override
	public Vector getPixelPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPixelPosition(Point2D_I32 findBall) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Vector getPixelOrientation() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPixelOrientation(Vector position) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPixelPosition(Vector position) {
		// TODO Auto-generated method stub
		
	}

}
