package world;

import world.object.MobilePixelObject;
import world.object.StationaryPixelObject;

/*
 * An interface defining what methods will be available to get a representation of the world. Pixel 
 * implies that everything is exactly as it appears in the image and the primary unit is pixels.
 */
public interface PixelWorldColorless {
	// Integers constants to be used to reference the various moving elements in
	// the world. The robots are numberd as they are placed in the field
	// starting with robot 0 on the very left and robot 3 on the very right.
	// (I.e. the four robots and the ball)
	public static final int ROBOT0 = 0;
	public static final int ROBOT1 = 1;
	public static final int ROBOT2 = 2;
	public static final int ROBOT3 = 3;
	public static final int BALL = 4;
	
	// Integers constants to be used to reference the various stationary
	// elements in the world. The zones are numberd as they are placed in the
	// field starting with zone 0 on the very left and zone 3 on the very right.
	// (I.e. the four zones and the two goals)
	public static final int ZONE0 = 0;
	public static final int ZONE1 = 1;
	public static final int ZONE2 = 2;
	public static final int ZONE3 = 3;
	
	public static final int GOAL1 = 4;
	public static final int GOAL2 = 5;

	/**
	 * Get one of the mobile objects in the world.
	 * 
	 * @param object
	 *            The integer that identifies the object of interest. Options
	 *            are defined as constants in this class.
	 * 
	 * @return Returns the mobile object.
	 */
	public MobilePixelObject getMobileObject(int object);

	/**
	 * Get one of the stationary objects in the world.
	 * 
	 * @param object
	 *            The integer that identifies the object of interest. Options
	 *            are defined as constants in this class.
	 * 
	 * @return Returns the mobile object.
	 */
	public StationaryPixelObject getStationaryObject(int object);
}
