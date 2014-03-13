package world;

import geometry.Vector;
import world.object.MobileObject;
import world.object.MobilePixelObject;
import world.object.StationaryObject;
import world.object.StationaryPixelObject;
import world.object.StationaryRealObject;
import world.object.Zones;

public class World implements PixelWorld, RealWorld, PixelWorldColorless {
	public static final int NUM_MOBILE_OBJECTS = 5;
	public static final int NUM_STATIONARY_OBJECTS = 6;
	
	//Default conversion ratio. Measured on pitch 1.
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
	 * A simple world model that routes the user to the correct object and
	 * provides some information about the world.
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
	public World(TeamColor ourColor, TeamSide ourSide, double realUnitsPerPixel, PositionProvider positionProvider, Vector cameraPixelPosition, double cameraElevation, Zones.Pitch pitch) {
		this.ourColor = ourColor;
		this.ourSide = ourSide;
		this.yellowLeft = (ourColor ==TeamColor.YELLOW&& ourSide == TeamSide.LEFT) || (ourColor == TeamColor.BLUE && ourSide == TeamSide.RIGHT);
		this.realUnitsPerPixel = realUnitsPerPixel;
		this.pixelCameraPosition = cameraPixelPosition;
		this.realCameraElevation = cameraElevation;
		
		mobileObjects = new MobileObject[NUM_MOBILE_OBJECTS];
		for (int i = 0; i < NUM_MOBILE_OBJECTS; i++) {
			mobileObjects[i] = new MobileObject(i, positionProvider, this, (i==4?5:18), Zones.zone(pitch, i));
		}
		stationaryObjects = new StationaryObject[NUM_STATIONARY_OBJECTS];
		for (int i = 0; i < NUM_STATIONARY_OBJECTS; i++) {
			//TODO Has to be properly initialised. 
		}
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
	public MobileObject getHeroAttacker() {
		if (ourSide == TeamSide.LEFT) {
			return mobileObjects[2];
		}else {
			return mobileObjects[1];
		}
	}

	@Override
	public MobileObject getHeroDefender() {
		if (ourSide == TeamSide.LEFT) {
			return mobileObjects[0];
		}else {
			return mobileObjects[3];
		}
	}

	@Override
	public MobileObject getVillainAttacker() {
		if (ourSide == TeamSide.LEFT) {
			return mobileObjects[1];
		}else {
			return mobileObjects[2];
		}
	}

	@Override
	public MobileObject getVillainDefender() {if (ourSide == TeamSide.LEFT) {
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

	/**
	 * @return the realUnitsPerPixel
	 */
	public double getRealUnitsPerPixel() {
		return realUnitsPerPixel;
	}

	/**
	 * @param realUnitsPerPixel the realUnitsPerPixel to set
	 */
	public void setRealUnitsPerPixel(double realUnitsPerPixel) {
		this.realUnitsPerPixel = realUnitsPerPixel;
	}

	/**
	 * Get the realCameraElevation.
	 * 
	 * @return the realCameraElevation
	 */
	public double getRealCameraElevation() {
		return realCameraElevation;
	}

	/**
	 * @param realCameraElevation the realCameraElevation to set
	 */
	public void setRealCameraElevation(double realCameraElevation) {
		this.realCameraElevation = realCameraElevation;
	}

	/**
	 * Get the pixelCameraPosition. If not set returns a default value (0, 0).
	 * @return the pixelCameraPosition
	 */
	public Vector getPixelCameraPosition() {
		return pixelCameraPosition != null ? pixelCameraPosition : new Vector(0,0);
	}

	/**
	 * @param pixelCameraPosition the pixelCameraPosition to set
	 */
	public void setPixelCameraPosition(Vector pixelCameraPosition) {
		this.pixelCameraPosition = pixelCameraPosition;
	}
	
}
