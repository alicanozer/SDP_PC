package world;

import geometry.Vector;
import world.object.MobileObject;
import world.object.MobilePixelObject;
import world.object.MobileRealObject;
import world.object.StationaryObject;
import world.object.StationaryPixelObject;
import world.object.StationaryRealObject;

public class World implements PixelWorld, RealWorld, PixelWorldColorless {
	public static final int NUM_MOBILE_OBJECTS = 5;
	public static final int NUM_STATIONARY_OBJECTS = 6;
	
	public static final double REAL_UNITS_PER_PIXEL = 0.455769231;
	protected double realUnitsPerPixel;
	
	//The distance from the table to the camera in cm.
	protected double realCameraElevation;
	// The position in the image that the camera sits right above (in an
	// uncropped image this would be the center of the image 320,240).
	protected Vector pixelCameraPosition;

	public enum TeamColor {
		BLUE,
		YELLOW
	}
	
	public enum TeamSide {
		LEFT,
		RIGHT
	}
	
	TeamColor ourColor;
	TeamSide ourSide;
	boolean yellowLeft;

	MobileObject[] mobileObjects;
	StationaryObject[] stationaryObjects;

	/**
	 * A simple world model with the following features:
	 * <br>
	 * - Allows access to object by role (HERO_DEFENDER) or by appearance (YELLOW_DEFENDER).
	 * <br>
	 * - Converts distances etc as they appear in an image to what they are in reality as in
	 * {@link world.object.MobileObject#MobileObject(double) MobileObject} .
	 * 
	 * @param ourColor
	 *            The color we are playing as.
	 * 
	 * @param ourSide
	 *            The side we are playing from (i.e. the side on which our
	 *            defender is).
	 * @param realUnitsPerPixel
	 *            A conversion ration that will be used to convert pixel
	 *            distances into real distances (cm)
	 */
	public World(TeamColor ourColor, TeamSide ourSide, double realUnitsPerPixel) {
		this.ourColor = ourColor;
		this.ourSide = ourSide;
		this.yellowLeft = (ourColor ==TeamColor.YELLOW&& ourSide == TeamSide.LEFT) || (ourColor == TeamColor.BLUE && ourSide == TeamSide.RIGHT);
		
		mobileObjects = new MobileObject[NUM_MOBILE_OBJECTS];
		for (int i = 0; i < NUM_MOBILE_OBJECTS; i++) {
			mobileObjects[i] = new MobileObject(this);
		}
		stationaryObjects = new StationaryObject[NUM_STATIONARY_OBJECTS];
		for (int i = 0; i < NUM_STATIONARY_OBJECTS; i++) {
			//TODO Has to be properly initialised. 
		}
	}

	/**
	 * A simple world model with the following features:
	 * - Allows access to object by role (HERO_DEFENDER) or by appearance (YELLOW_DEFENDER).
	 * - Can return distances and velocities in cm and cm/s.
	 * - Makes a best guess of data where an actual reading is not available (i.e. it will not return null once warmed up).
	 * 
	 * @param ourColor
	 *            The color we are playing as.
	 * 
	 * @param ourSide
	 *            The side we are playing from (i.e. the side on which our
	 *            defender is).
	 */
	public World(TeamColor ourColor, TeamSide ourSide) {
		this(ourColor, ourSide, REAL_UNITS_PER_PIXEL);
	}

	@Override
	public MobilePixelObject getRobot0() {
		return mobileObjects[0];
	}

	@Override
	public MobilePixelObject getRobot1() {
		return mobileObjects[1];
	}

	@Override
	public MobilePixelObject getRobot2() {
		return mobileObjects[2];
	}

	@Override
	public MobilePixelObject getRobot3() {
		return mobileObjects[4];
	}

	@Override
	public StationaryPixelObject getZone0() {
		return stationaryObjects[0];
	}

	@Override
	public StationaryPixelObject getZone1() {
		return stationaryObjects[1];
	}

	@Override
	public StationaryPixelObject getZone2() {
		return stationaryObjects[2];
	}

	@Override
	public StationaryPixelObject getZone3() {
		return stationaryObjects[3];
	}

	@Override
	public MobileRealObject getHeroAttacker() {
		if (ourSide == TeamSide.LEFT) {
			return mobileObjects[2];
		}else {
			return mobileObjects[1];
		}
	}

	@Override
	public MobileRealObject getHeroDefender() {
		if (ourSide == TeamSide.LEFT) {
			return mobileObjects[0];
		}else {
			return mobileObjects[3];
		}
	}

	@Override
	public MobileRealObject getVillainAttacker() {
		if (ourSide == TeamSide.LEFT) {
			return mobileObjects[1];
		}else {
			return mobileObjects[2];
		}
	}

	@Override
	public MobileRealObject getVillainDefender() {if (ourSide == TeamSide.LEFT) {
		return mobileObjects[3];
	}else {
		return mobileObjects[0];
	}
	}

	@Override
	public StationaryRealObject getHeroAttackerZone() {
		if (ourSide == TeamSide.LEFT) {
			return stationaryObjects[2];
		}else {
			return stationaryObjects[1];
		}
	}

	@Override
	public StationaryRealObject getHeroDefenderZone() {
		if (ourSide == TeamSide.LEFT) {
			return stationaryObjects[0];
		}else {
			return stationaryObjects[3];
		}
	}

	@Override
	public StationaryRealObject getVillainAttackerZone() {
		if (ourSide == TeamSide.LEFT) {
			return stationaryObjects[1];
		}else {
			return stationaryObjects[2];
		}
	}

	@Override
	public StationaryRealObject getVillainDefenderZone() {
		if (ourSide == TeamSide.LEFT) {
			return stationaryObjects[3];
		}else {
			return stationaryObjects[0];
		}
	}

	@Override
	public StationaryRealObject getHeroGoal() {
		if (ourSide == TeamSide.LEFT) {
			return stationaryObjects[4];
		}else {
			return stationaryObjects[5];
		}
	}

	@Override
	public StationaryRealObject getVillainGoal() {
		if (ourSide == TeamSide.LEFT) {
			return stationaryObjects[5];
		}else {
			return stationaryObjects[4];
		}
	}

	@Override
	public MobilePixelObject getBlueAttacker() {
		if (yellowLeft) {
			return mobileObjects[1];
		}else {
			return mobileObjects[2];
		}
	}

	@Override
	public MobilePixelObject getBlueDefender() {
		if (yellowLeft) {
			return mobileObjects[3];
		}else {
			return mobileObjects[0];
		}
	}

	@Override
	public MobilePixelObject getYellowAttacker() {
		if (yellowLeft) {
			return mobileObjects[2];
		}else {
			return mobileObjects[1];
		}
	}

	@Override
	public MobilePixelObject getYellowDefender() {
		if (yellowLeft) {
			return mobileObjects[0];
		}else {
			return mobileObjects[3];
		}
	}

	@Override
	public MobileObject getBall() {
		return mobileObjects[4];
	}

	@Override
	public StationaryPixelObject getBlueAttackerZone() {
		if (yellowLeft) {
			return stationaryObjects[1];
		}else {
			return stationaryObjects[2];
		}
	}

	@Override
	public StationaryPixelObject getBlueDefenderZone() {
		if (yellowLeft) {
			return stationaryObjects[0];
		}else {
			return stationaryObjects[3];
		}
	}

	@Override
	public StationaryPixelObject getYellowAttackerZone() {
		if (yellowLeft) {
			return stationaryObjects[2];
		}else {
			return stationaryObjects[1];
		}
	}

	@Override
	public StationaryPixelObject getYellowDefenderZone() {
		if (yellowLeft) {
			return stationaryObjects[0];
		}else {
			return stationaryObjects[3];
		}
	}

	@Override
	public StationaryPixelObject getLeftGoal() {
		return stationaryObjects[4];
	}

	@Override
	public StationaryPixelObject getRightGoal() {
		return stationaryObjects[5];
	}

	public double getRealUnitsPerPixel() {
		return realUnitsPerPixel;
	}
	
	public double getRealCameraElevation() {
		return realCameraElevation;
	}
	
	public Vector getPixelCameraPosition() {
		return pixelCameraPosition;
	}

}
