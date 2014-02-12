package world;

/*
 * An interface defining what methods will be available to get a representation of the world. Pixel 
 * implies that everything is exactly as it appears in the image and the primary unit is pixels.
 */
public interface PixelWorld extends World{
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
