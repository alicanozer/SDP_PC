package world.object;

import geometry.Vector;

/*
 * An interface defining what methods will be available to get a representation of one of the worlds
 * mobile objects. Real implies that units will be cm rather than pixels and that the output
 * may have been processed to compensate for perspective projection, barrel distortion etc.
 * 
 * getSpeed and getDirection (as an angle) have been left out. Instead I would opt for implementing those in the vector class;
 * thus one would do something like foo.getVelocity().getAngle(); or foo.getVelocity().getMagnitude();
 */
public interface MobileRealObject {
	/**
	 * Get the position of the object. Guaranteed to return a non-null Vector.
	 * 
	 * @return Returns the position of the object as a Vector.
	 * @throws Exception Throws an exception if there is not enough history to complete the request.
	 */
	public Vector getRealPosition() throws Exception;

	/**
	 * Get the orientation of the object. (Orientation == the way the object is
	 * facing)
	 * 
	 * @return Returns the orientation of the object as a Vector. This vector is
	 *         parallel to one that can be draw from the dot of the 'i' to the
	 *         base of the 'i' (remember the dot of the i is the back of the
	 *         robot). It's magnitude is arbitrary.
	 * @throws Exception 
	 */
	public Vector getRealOrientation() throws Exception;

	/**
	 * Get the velocity of the object.Guaranteed to return a non-null Vector.
	 * 
	 * @return Returns the velocity of the object as a Vector. This vector is
	 *         parallel to one that can be draw from the dot of the 'i' to the
	 *         base of the 'i' (remember the dot of the i is the back of the
	 *         robot). It's magnitude is equal to the speed.
	 * @throws Exception Throws an exception if there is not enough history to complete the request. 
	 */
	public Vector getRealVelocity() throws Exception;
}
