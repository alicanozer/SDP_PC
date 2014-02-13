package world;

public class World implements PixelWorld, RealWorld {

	public static final boolean YELLOW = true;
	public static final boolean BLUE = false;

	public static final boolean LEFT = true;
	public static final boolean RIGHT = false;

	public static final int NUM_MOBILE_OBJECTS = 5;
	public static final int NUM_STATIONARY_OBJECTS = 6;

	boolean ourColor;
	boolean ourSide;

	MobileObject[] mobileObjects;
	StationaryObject[] stationaryObjects;

	/**
	 * 
	 * @param ourColor The color we are playing as.
	 * 
	 * @param ourSide The side we are playing from (i.e. the side on which our defender is).
	 */
	public World(boolean ourColor, boolean ourSide) {
		this.ourColor = ourColor;
		this.ourSide = ourSide;

		mobileObjects = new MobileObject[NUM_MOBILE_OBJECTS];
		stationaryObjects = new StationaryObject[NUM_STATIONARY_OBJECTS];
	}

	@Override
	public MobileRealObject getMobileRealObject(int object) {
		// Holds the index that will be used to retrieve the mobile object.
		int pixelObject = object;
		// Sets the above index appropriately depending on what object is
		// requested and what color we are playing as.
		switch (object) {
		case HERO_ATTACKER:
			if (ourColor == YELLOW) {
				pixelObject = YELLOW_ATTACKER;
			} else {
				pixelObject = BLUE_ATTACKER;
			}
			break;
		case HERO_DEFENDER:
			if (ourColor == YELLOW) {
				pixelObject = YELLOW_DEFENDER;
			} else {
				pixelObject = BLUE_DEFEMDER;
			}
			break;
		case VILLAIN_ATTACKER:
			if (ourColor == YELLOW) {
				pixelObject = BLUE_ATTACKER;
			} else {
				pixelObject = YELLOW_ATTACKER;
			}
			break;
		case VILLAIN_DEFENDER:
			if (ourColor == YELLOW) {
				pixelObject = BLUE_ATTACKER;
			} else {
				pixelObject = YELLOW_ATTACKER;
			}
			break;
		case RealWorld.BALL:
			pixelObject = PixelWorld.BALL;
			break;
		default:
			break;
		}
		return mobileObjects[pixelObject];
	}

	@Override
	public StationaryRealObject getStationaryRealObject(int object) {
		// Holds the index that will be used to retrieve the mobile object.
		int pixelObject = object;
		// Sets the above index appropriately depending on what object is
		// requested and what side we are playing from.
		switch (object) {
		case HERO_DEFENDER_ZONE:
			if (ourSide == LEFT) {
				pixelObject = LEFT_TEAM_DEFENDER_ZONE;
			} else {
				pixelObject = RIGHT_TEAM_DEFENDER_ZONE;
			}
		case VILLAIN_ATTACKER_ZONE:
			if (ourSide == LEFT) {
				pixelObject = RIGHT_TEAM_ATTACKER_ZONE;
			} else {
				pixelObject = LEFT_TEAM_ATTACKER_ZONE;
			}
		case HERO_ATTACKER_ZONE:
			if (ourSide == LEFT) {
				pixelObject = LEFT_TEAM_ATTACKER_ZONE;
			} else {
				pixelObject = RIGHT_TEAM_ATTACKER_ZONE;
			}
		case VILLAIN_DEFENDER_ZONE:
			if (ourSide == LEFT) {
				pixelObject = RIGHT_TEAM_DEFENDER_ZONE;
			} else {
				pixelObject = LEFT_TEAM_ATTACKER_ZONE;
			}
		case HERO_GOAL:
			if (ourSide == LEFT) {
				pixelObject = LEFT_TEAM_GOAL;
			} else {
				pixelObject = RIGHT_TEAM_GOAL;
			}
		case VILLAIN_GOAL:
			if (ourSide == LEFT) {
				pixelObject = RIGHT_TEAM_GOAL;
			} else {
				pixelObject = LEFT_TEAM_GOAL;
			}
			break;
		}
		return stationaryObjects[pixelObject];
	}

	@Override
	public MobilePixelObject getMobilePixelObject(int object) {
		return mobileObjects[object];
	}

	@Override
	public StationaryPixelObject getStationaryPixelObject(int object) {
		return stationaryObjects[object];
	}

}
