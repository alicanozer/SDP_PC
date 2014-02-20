/**
 * 
 */
package world.support;

import geometry.Vector;

/**
 * @author apljungquist
 * 
 */
public class TimedVector extends Vector {

	protected long time;

	/**
	 * @param x
	 *            The x component of the vector of the timed vector.
	 * 
	 * @param y
	 *            The y component of the vector of the timed vector
	 * 
	 * @param time
	 *            The time at which the vector of the timed vector existed.
	 */
	public TimedVector(double x, double y, long time) {
		super(x, y);
		this.time = time;
	}

	/**
	 * Creates a timed vector with the current time as the time component
	 * 
	 * @param x
	 *            The x component of the vector of the timed vector.
	 * 
	 * @param y
	 *            The y component of the vector of the timed vector
	 */
	public TimedVector(double x, double y) {
		super(x, y);
		time = System.currentTimeMillis();
	}
	
	/**
	 * Creates a timed vector with the current time as the time component
	 * 
	 * @param vector The vector component of the timed vector.
	 */
	public TimedVector(Vector vector) {
		super(vector.getX(), vector.getY());
		time = System.currentTimeMillis();
	}
	
	/**
	 * Creates a new timed vector.
	 * 
	 * @param vector The vector component of the timed vector.
	 * 
	 * @param time The time component of the timed vector. 
	 */
	public TimedVector(Vector vector, long time) {
		super(vector.getX(), vector.getY());
		this.time = time;
	}
	
	/**
	 * Get the vector in this timed vector.
	 * 
	 * @return Returns the vector component of this timed vector.
	 */
	public Vector getVector() {
		return new Vector(getX(), getY());
	}
	
	/**
	 * Get the time of this timed vector.
	 * 
	 * @return Returns the time component of this timed vector.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Subtracts another timed vector from this one.
	 * 
	 * @param otherTimedVector
	 *            The timed vector that is to be subtracted (right side of subtraction)
	 * 
	 * @return Returns a new timed vector that is the result of the subtraction.
	 */
	public TimedVector subtract(TimedVector otherTimedVector) {
		return new TimedVector(getX() - otherTimedVector.getX(),
				getY() - otherTimedVector.getY(), time - otherTimedVector.time);
	}
}
