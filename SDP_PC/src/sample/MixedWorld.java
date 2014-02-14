package sample;

import geometry.Vector;
import world.PixelWorld;
import world.RealWorld;
import world.object.MobilePixelObject;
import world.object.MobileRealObject;
import world.object.StationaryPixelObject;
import world.object.StationaryRealObject;

public class MixedWorld implements RealWorld, PixelWorld {
	public static final boolean YELLOW = true;
	public static final boolean BLUE = false;

	public static final boolean LEFT = true;
	public static final boolean RIGHT = false;

	/**
	 * 
	 * @param RealCameraElevation The distance from the table to the camera in cm
	 * @param PixelCameraPosition The position of pixel in the image which is right below the camera.
	 * @param ourColor A constant indicating as what color we are playing.
	 */
	public MixedWorld(double RealCameraElevation, Vector PixelCameraPosition, boolean ourColor, boolean sideOfPitch) {
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see world.PixelWorld#getMobilePixelObject(int)
	 */
	@Override
	public Robot getMobileObject(int object) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.PixelWorld#getStationaryPixelObject(int)
	 */
	@Override
	public MixedStationaryObject getStationaryObject(int object) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMobileRealObject(int objectIdentifier, MobileRealObject object) {
		// TODO Auto-generated method stub
		
	}

}
