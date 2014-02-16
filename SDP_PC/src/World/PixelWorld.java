package world;

import world.object.MobilePixelObject;
import world.object.StationaryPixelObject;

/*
 * An interface defining what methods will be available to get a representation of the world. Pixel 
 * implies that everything is exactly as it appears in the image and the primary unit is pixels.
 */
public interface PixelWorld {
	/**
	 * 
	 * @return Returns the attacking robot playing in blue color.
	 */
	public MobilePixelObject getBlueAttacker();

	/**
	 * 
	 * @return Returns the defending robot playing in blue color.
	 */
	public MobilePixelObject getBlueDefender();

	/**
	 * 
	 * @return Returns the attacking robot playing in yellow color.
	 */
	public MobilePixelObject getYellowAttacker();

	/**
	 * 
	 * @return Returns the defending robot playing in yellow color.
	 */
	public MobilePixelObject getYellowDefender();

	/**
	 * 
	 * @return Returns the ball.
	 */
	public MobilePixelObject getBall();

	/**
	 * 
	 * @return Returns the zone of the blue attacker.
	 */
	public StationaryPixelObject getBlueAttackerZone();

	/**
	 * 
	 * @return Returns the zone of the blue defender.
	 */
	public StationaryPixelObject getBlueDefenderZone();

	/**
	 * 
	 * @return Returns the zone of the yelow attacker.
	 */
	public StationaryPixelObject getYellowAttackerZone();

	/**
	 * 
	 * @return Returns the zone of the yellow defender.
	 */
	public StationaryPixelObject getYellowDefenderZone();

	/**
	 * 
	 * @return Returns the left goal.
	 */
	public StationaryPixelObject getLeftGoal();

	/**
	 * 
	 * @return Returns the right goal.
	 */
	public StationaryPixelObject getRightGoal();
}
