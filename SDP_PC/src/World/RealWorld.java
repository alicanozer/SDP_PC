package world;

/*
 * An interface defining what methods will be available to get a representation of the world. Real implies 
 * that units will be cm rather than pixels and that the output may have been processed to compensate for 
 * perspective projection, barrel distortion etc.
 */
public interface RealWorld {
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
	
	/**
	 * Get one of the mobile objects in the world.
	 * 
	 * @param object The integer that identifies the object of interest. Options are defined as constants in this class.
	 * 
	 * @return Returns the mobile object.
	 */
	public MobileRealObject getMobileRealObject(int object);
	/**
	 * Get one of the stationary objects in the world.
	 * 
	 * @param object The integer that identifies the object of interest. Options are defined as constants in this class.
	 * 
	 * @return Returns the mobile object.
	 */
	public StationaryRealObject getStationaryRealObject(int object);
}
