package world;

/*
 * An interface defining what methods will be available to get a representation of the world. Pixel 
 * implies that everything is exactly as it appears in the image and the primary unit is pixels.
 */
public interface PixelWorld{
	//Integers constants to be used to reference the various moving elements in the world.
	//(I.e. the four robots and the ball)
	public static final int YELLOW_ATTACKER = 1;
	public static final int YELLOW_DEFENDER = 2;
	public static final int BLUE_ATTACKER = 3;
	public static final int BLUE_DEFEMDER = 4;
	public static final int BALL = 5;
	
	//Integers constants to be used to reference the various stationary elements in the world.
	//(I.e. the four zones and the two goals)
	public static final int LEFT_DEFENDER_ZONE = 6;
	public static final int RIGHT_ATTACKER_ZONE = 7;
	public static final int LEFT_ATTACKER_ZONE = 8;
	public static final int RIGHT_DEFENDER_ZONE = 9;
	
	public static final int LEFT_GOAL = 10;
	public static final int RIGHT_GOAL = 11;
	
	/**
	 * Get one of the mobile objects in the world.
	 * 
	 * @param object The integer that identifies the object of interest. Options are defined as constants in this class.
	 * 
	 * @return Returns the mobile object.
	 */
	public MobilePixelObject getMobilePixelObject(int object);
	/**
	 * Get one of the stationary objects in the world.
	 * 
	 * @param object The integer that identifies the object of interest. Options are defined as constants in this class.
	 * 
	 * @return Returns the mobile object.
	 */
	public StationaryPixelObject getStationaryPixelObject(int object);
}
