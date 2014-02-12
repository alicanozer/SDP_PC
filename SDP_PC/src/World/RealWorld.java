package world;

/*
 * An interface defining what methods will be available to get a representation of the world. Real implies 
 * that units will be cm rather than pixels and that the output may have been processed to compensate for 
 * perspective projection, barrel distortion etc.
 */
public interface RealWorld extends World{
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
