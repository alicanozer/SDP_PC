/**
 * 
 */
package world;

import geometry.Vector;

/**
 * @author apljungquist
 * 
 */
public class TimedVector extends Vector {

	private long time;

	/**
	 * Create a TimedVector using default time (i.e. now). Use x- and
	 * y-component to initialize the vector.
	 * 
	 * @param x
	 *            The x component of the vector component.
	 * @param y
	 *            The y component of the vector component.
	 */
	public TimedVector(double x, double y) {
		super(x, y);
		this.time = System.currentTimeMillis();
	}

	/**
	 * Create a TimedVector using default time (i.e. now). Use bearing to
	 * initialize the vector (See {@link Vector#Vector(double)}).
	 * 
	 * @param bearing
	 */
	public TimedVector(double bearing) {
		super(bearing);
		this.time = System.currentTimeMillis();
	}

	/**
	 * Create a TimedVector with specific vector and time.
	 * 
	 * @param vector
	 *            The vector component.
	 * @param time
	 *            The time component.
	 */
	public TimedVector(Vector vector, long time) {
		this(vector.getX(), vector.getY(), time);
	}

	/**
	 * Create a TimedVector with specific vector x & y components and time.
	 * 
	 * @param x
	 *            The x component of the vector component.
	 * @param y
	 *            The y component of the vector component.
	 * @param time
	 *            The time component.
	 */
	public TimedVector(double x, double y, long time) {
		super(x, y);
		this.time = time;
	}

	/**
	 * @return Returns the time component of this TimedVector.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param Set the time component of this TimedVector.
	 */
	public void setTime(long time) {
		this.time = time;
	}

}
