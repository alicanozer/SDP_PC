/**
 * 
 */
package geometry;

/**
 * @author apljungquist
 *
 */
public class Frame extends Vector {

	protected long time;
	
	/**
	 * @param x The x component of the vector of the frame.
	 * 
	 * @param y The y component of the vector of the frame
	 * 
	 * @param time The time at which the vector of the frame existed.
	 */
	public Frame(double x, double y, long time) {
		super(x, y);
		this.time = time;
	}
	
	/**
	 * Creates a frame with the current time as the time component
	 * 
	 * @param x The x component of the vector of the frame.
	 * 
	 * @param y The y component of the vector of the frame
	 */
	public Frame(double x, double y) {
		super(x, y);
		time = System.currentTimeMillis();
	}
	
	public long timeDifference(Frame otherFrame) {
		return Math.abs(time-otherFrame.time);
	}
}
