package World.object;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;

/*
 * An interface defining what methods will be available to get and set a representation of one of the worlds
 * mobile objects. Pixel implies that everything is exactly as it appears in the image and the primary unit is pixels.
 * 
 * 
 */
public interface MobilePixelObject {

	/**
	 * Get the position of the object at a given time.
	 * 
	 * @param time
	 *            The time for which to get the position. This is relative to
	 *            now, thus positive values represent the future, negative
	 *            values the past. The unit is milliseconds.
	 * 
	 * @return Returns the position of the object at the given time as a Vector.
	 */
	public Vector getPixelPosition();

	/**
	 * Set the current position of the object.
	 * 
	 * @param position
	 *            The position that the object is currently in.
	 */
	public void setPixelPosition(Vector position);
	
	/**
	 * Set the current position of the object.
	 * 
	 * @param position
	 *            The position that the object is currently in.
	 */
	public void setPixelPosition(Point2D_I32 findBall);

	/**
	 * Get the orientation of the object at a given time.
	 * 
	 * @param time
	 *            The time for which to get the orientation. This is relative to
	 *            now, thus positive values represent the future, negative
	 *            values the past. The unit is milliseconds.
	 * 
	 * @return Returns the orientation of the object at the given time as a
	 *         Vector.
	 */
	public Vector getPixelOrientation();

	/**
	 * Set the current orientation of the object.
	 * 
	 * @param position
	 *            The orientation that the object is currently in.
	 */
	public void setPixelOrientation(Vector position);
}
