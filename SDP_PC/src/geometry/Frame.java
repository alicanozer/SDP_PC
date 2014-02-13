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
	
	/**
	 * Subtracts another frame from this one.
	 * 
	 * @param otherFrame The frame that is to be subtracted (right side of subtraction)
	 * 
	 * @return Returns a new frame that is the result of the subtraction.
	 */
	public Frame subtract(Frame otherFrame) {
		return new Frame(getX()-otherFrame.getX(), getY()-otherFrame.getY(), time-otherFrame.time);
	}
}
