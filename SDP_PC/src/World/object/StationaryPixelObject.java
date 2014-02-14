package world.object;

import java.awt.Shape;

/*
 * An interface defining what methods will be available to get and set a representation of one of the worlds
 * stationary elements. Pixel implies that everything is exactly as it appears in the image and the primary
 * unit is pixels.
 */
public interface StationaryPixelObject {
	/**
	 * Get the shape of this object. Likely to be a rectangle (attacker zone), polygon (goalie zone) or maybe line (goal).
	 * 
	 * @return Returns a shape representing the element as it appears in the image.
	 */
	public Shape getPixelShape();
	
	/**
	 * Set the shape of this object. 
	 * 
	 * @param shape A shape mathcing the shape of the object as it appears in the image.
	 */
	public void setPixelShape(Shape shape);
}
