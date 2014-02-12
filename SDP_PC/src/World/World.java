/*
 * This interface is not meant to be implemented. It is extended by other interfaces and 
 * will hopefully reduce code duplications a tiny bit.
 */
package world;

public interface World {
	//Integers constants to be used to reference the various moving elements in the world.
	//(I.e. the four robots and the ball)
	public static final int HERO_ATTACKER = 1;
	public static final int HERO_DEFENDER = 2;
	public static final int VILLAIN_ATTACKER = 3;
	public static final int VILLAIN_DEFEMDER = 4;
	public static final int BALL = 5;
	
	//Integers constants to be used to reference the various stationary elements in the world.
	//(I.e. the four zones and the two goals)
	public static final int HERO_DEFENDER_ZONE = 6;
	public static final int VILLAIN_ATTACKER_ZONE = 7;
	public static final int HERO_ATTACKER_ZONE = 8;
	public static final int VILLAIN_DEFENDER_ZONE = 9;
	
	public static final int HERO_GOAL = 10;
	public static final int VILLAIN_GOAL = 11;
}
