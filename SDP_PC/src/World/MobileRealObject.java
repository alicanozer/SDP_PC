package world;

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
	 * Get the position of the object at a given time.
	 * 
	 * @param time The time for which to get the position. This is relative to now, thus
	 * positive values represent the future, negative values the past. The unit is milliseconds.
	 * 
	 * @return Returns the position of the object at the given time as a Vector.
	 */
	public Vector getRealPosition(long time);
	/**
	 * Get the orientation of the object at a given time. (Orientation == the way the object is facing)
	 * 
	 * @param time The time for which to get the orientation. This is relative to now, thus
	 * positive values represent the future, negative values the past. The unit is milliseconds.
	 * 
	 * @return Returns the orientation of the object at the given time as a Vector. This vector is parallel 
	 * to one that can be draw from the dot of the 'i' to the base of the 'i' (remember the dot of the i is 
	 * the back of the robot). It's magnitude is arbitrary.
	 */
	public Vector getRealOrientation(long time);
	/**
	 * Get the velocity of the object at a given time.
	 * 
	 * @param time The time for which to get the velocity. This is relative to now, thus
	 * positive values represent the future, negative values the past. The unit is milliseconds.
	 * 
	 * @return Returns the velocity of the object at the given time as a Vector. This vector is parallel 
	 * to one that can be draw from the dot of the 'i' to the base of the 'i' (remember the dot of the i is 
	 * the back of the robot). It's magnitude is equal to the speed.
	 */
	public Vector getRealVelocity(long time);
}
