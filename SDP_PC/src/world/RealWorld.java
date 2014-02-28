package world;

import world.object.MobileRealObject;
import world.object.StationaryRealObject;

/*
 * An interface defining what methods will be available to get a representation of the world. Real implies 
 * that units will be cm rather than pixels and that the output may have been processed to compensate for 
 * perspective projection, barrel distortion etc.
 */
public interface RealWorld {
	/**
	 * 
	 * @return Returns the attacking robot on our team.
	 */
	public MobileRealObject getHeroAttacker();

	/**
	 * 
	 * @return Returns the defending robot on our team.
	 */
	public MobileRealObject getHeroDefender();

	/**
	 * 
	 * @return Returns the attacking robot on the opposing team.
	 */
	public MobileRealObject getVillainAttacker();

	/**
	 * 
	 * @return Returns the defending robot on the opposing team.
	 */
	public MobileRealObject getVillainDefender();

	/**
	 * 
	 * @return Returns the ball.
	 */
	public MobileRealObject getBall();

	/**
	 * 
	 * @return Returns the zone of the attacking robot on our team.
	 */
	public StationaryRealObject getHeroAttackerZone();

	/**
	 * 
	 * @return Returns the zone of the defending robot on our team.
	 */
	public StationaryRealObject getHeroDefenderZone();

	/**
	 * 
	 * @return Returns the zone of the attacking robot on the opposing team.
	 */
	public StationaryRealObject getVillainAttackerZone();

	/**
	 * 
	 * @return Returns the zone of the defending robot on the opposing team.
	 */
	public StationaryRealObject getVillainDefenderZone();

	/**
	 * 
	 * @return Returns the goal that we want to defend.
	 */
	public StationaryRealObject getHeroGoal();

	/**
	 * 
	 * @return Returns the goal that we want to attack.
	 */
	public StationaryRealObject getVillainGoal();
}
