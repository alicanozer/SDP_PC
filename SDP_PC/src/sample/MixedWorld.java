package sample;

import geometry.Vector;
import world.MobilePixelObject;
import world.MobileRealObject;
import world.PixelWorld;
import world.RealWorld;
import world.StationaryPixelObject;
import world.StationaryRealObject;

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
	public MobilePixelObject getMobilePixelObject(int object) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.PixelWorld#getStationaryPixelObject(int)
	 */
	@Override
	public StationaryPixelObject getStationaryPixelObject(int object) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.RealWorld#getMobileRealObject(int)
	 */
	@Override
	public MobileRealObject getMobileRealObject(int object) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see world.RealWorld#getStationaryRealObject(int)
	 */
	@Override
	public StationaryRealObject getStationaryRealObject(int object) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMobileRealObject(int objectIdentifier, MobileRealObject object) {
		// TODO Auto-generated method stub
		
	}

}
