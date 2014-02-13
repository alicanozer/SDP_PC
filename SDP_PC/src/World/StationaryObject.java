/**
 * 
 */
package world;

import java.awt.Shape;

/**
 * @author apljungquist
 *
 */
public class StationaryObject implements StationaryPixelObject, StationaryRealObject {

	protected Shape shape;
	
	/**
	 * 
	 */
	public StationaryObject(Shape shape) {
		this.shape = shape;
	}

	@Override
	public Shape getRealShape() {
		return shape;
	}

	@Override
	public Shape getPixelShape() {
		return shape;
	}

	@Override
	public void setPixelShape(Shape shape) {
		this.shape = shape;
	}

}
