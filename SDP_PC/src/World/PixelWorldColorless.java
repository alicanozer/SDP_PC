package world;

import world.object.MobilePixelObject;
import world.object.StationaryPixelObject;

/*
 * An interface defining what methods will be available to get a representation of the world. Pixel 
 * implies that everything is exactly as it appears in the image and the primary unit is pixels.
 */
public interface PixelWorldColorless {

	/**
	 * 
	 * @return Returns the first robot counting from the left. (This will be a
	 *         defender).
	 */
	public MobilePixelObject getRobot0();

	/**
	 * 
	 * @return Returns the second robot counting from the left. (This will be an
	 *         attacker).
	 */
	public MobilePixelObject getRobot1();

	/**
	 * 
	 * @return Returns the third robot counting from the left. (This will be an
	 *         attacker).
	 */
	public MobilePixelObject getRobot2();

	/**
	 * 
	 * @return Returns the forth robot counting from the left. (This will be a
	 *         defender).
	 */
	public MobilePixelObject getRobot3();

	/**
	 * 
	 * @return Returns the ball.
	 */
	public MobilePixelObject getBall();

	/**
	 * 
	 * @return Returns the first zone counting from the left. (This will be an
	 *         defender's zone).
	 */
	public StationaryPixelObject getZone0();

	/**
	 * 
	 * @return Returns the second zone counting from the left. (This will be an
	 *         attacker's zone).
	 */
	public StationaryPixelObject getZone1();

	/**
	 * 
	 * @return Returns the third zone counting from the left. (This will be an
	 *         attacker's zone).
	 */
	public StationaryPixelObject getZone2();

	/**
	 * 
	 * @return Returns the forth zone counting from the left. (This will be an
	 *         defender's zone).
	 */
	public StationaryPixelObject getZone3();

	/**
	 * 
	 * @return Returns the left goal.
	 */
	public StationaryPixelObject getLeftGoal();

	/**
	 * 
	 * @return Returns the left goal.
	 */
	public StationaryPixelObject getRightGoal();
}
