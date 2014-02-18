/**
 * 
 */
package world;

/**
 * @author apljungquist
 * 
 */
public interface PositionProvider {
	/**
	 * Get the position of the 1st (/4) robot from the left in the plating
	 * field.
	 * 
	 * @param frameNumber
	 *            The frame from which to get the position. 0 is the latest
	 *            frame, 1 the frame before that and so on.
	 * @return Returns the position of the object in the desired frame and the
	 *         time at which the frame was captured.
	 * @throws Exception
	 *             If the requested frame is not available or the object could
	 *             not be found in the requested frame, an exception will be
	 *             thrown.
	 */
	public TimedVector getRobot0Position(int frameNumber) throws Exception;

	/**
	 * Get the position of the 2nd (/4) robot from the left in the plating
	 * field.
	 * 
	 * @param frameNumber
	 *            The frame from which to get the position. 0 is the latest
	 *            frame, 1 the frame before that and so on.
	 * @return Returns the position of the object in the desired frame and the
	 *         time at which the frame was captured.
	 * @throws Exception
	 *             If the requested frame is not available or the object could
	 *             not be found in the requested frame, an exception will be
	 *             thrown.
	 */
	public TimedVector getRobot1Position(int frameNumber) throws Exception;

	/**
	 * Get the position of the 3rd (/4) robot from the left in the plating
	 * field.
	 * 
	 * @param frameNumber
	 *            The frame from which to get the position. 0 is the latest
	 *            frame, 1 the frame before that and so on.
	 * @return Returns the position of the object in the desired frame and the
	 *         time at which the frame was captured.
	 * @throws Exception
	 *             If the requested frame is not available or the object could
	 *             not be found in the requested frame, an exception will be
	 *             thrown.
	 */
	public TimedVector getRobot2Position(int frameNumber) throws Exception;

	/**
	 * Get the position of the 4th (/4) robot from the left in the plating
	 * field.
	 * 
	 * @param frameNumber
	 *            The frame from which to get the position. 0 is the latest
	 *            frame, 1 the frame before that and so on.
	 * @return Returns the position of the object in the desired frame and the
	 *         time at which the frame was captured.
	 * @throws Exception
	 *             If the requested frame is not available or the object could
	 *             not be found in the requested frame, an exception will be
	 *             thrown.
	 */
	public TimedVector getRobot3Position(int frameNumber) throws Exception;

	/**
	 * Get the position of the ball.
	 * 
	 * @param frameNumber
	 *            The frame from which to get the position. 0 is the latest
	 *            frame, 1 the frame before that and so on.
	 * @return Returns the position of the object in the desired frame and the
	 *         time at which the frame was captured.
	 * @throws Exception
	 *             If the requested frame is not available or the object could
	 *             not be found in the requested frame, an exception will be
	 *             thrown.
	 */
	public TimedVector getBallPosition(int frameNumber) throws Exception;

}
