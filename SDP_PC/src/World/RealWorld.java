package world;

/*
 * An interface defining what methods will be available to get a representation of the world. Real implies 
 * that units will be cm rather than pixels and that the output may have been processed to compensate for 
 * perspective projection, barrel distortion etc.
 */
public interface RealWorld {
	//Integers constants to be used to reference the various moving elements in the world.
	//(I.e. the four robots and the ball)
	public static final int HERO_ATTACKER = 10;
	public static final int HERO_DEFENDER = 11;
	public static final int VILLAIN_ATTACKER = 12;
	public static final int VILLAIN_DEFENDER = 13;
	public static final int BALL = 14;
	
	//Integers constants to be used to reference the various stationary elements in the world.
	//(I.e. the four zones and the two goals)
	public static final int HERO_DEFENDER_ZONE = 10;
	public static final int VILLAIN_ATTACKER_ZONE = 11;
	public static final int HERO_ATTACKER_ZONE = 12;
	public static final int VILLAIN_DEFENDER_ZONE = 13;
	
	public static final int HERO_GOAL = 14;
	public static final int VILLAIN_GOAL = 15;
	
	/**
	 * Get one of the mobile objects in the world.
	 * 
	 * @param object The integer that identifies the object of interest. Options are defined as constants in this class.
	 * 
	 * @return Returns the mobile object.
	 */
	public MobileRealObject getMobileObject(int object);
	/**
	 * Get one of the stationary objects in the world.
	 * 
	 * @param object The integer that identifies the object of interest. Options are defined as constants in this class.
	 * 
	 * @return Returns the mobile object.
	 */
	public StationaryRealObject getStationaryObject(int object);
}
