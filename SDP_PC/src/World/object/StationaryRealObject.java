package World.object;

import java.awt.Shape;

/*
 * An interface defining what methods will be available to get a representation of one of the worlds
 * stationary elements. Real implies that units will be cm rather than pixels and that the output
 * may have been processed to compensate for perspective projection, barrel distortion etc.
 */
public interface StationaryRealObject {
	/**
	 * Get the shape of this object. Likely to be a rectangle (attacker zone),
	 * polygon (goalie zone) or maybe line (goal)
	 * 
	 * @return Returns a shape representing the element as it appears in the
	 *         real world.
	 */
	public Shape getRealShape();
}
