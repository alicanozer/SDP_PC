/**
 * 
 */
package world.support;

import world.World;
import geometry.Vector;

/**
 * @author apljungquist
 *
 */
public class Corrections {

	/**
	 * Given the perceived position of the robot, the height of the robot, the
	 * position in the image of the camera and the height of the camera above
	 * the table, calculate the actual position of the robot on the table. NB
	 * that all heights need to be in the same unit and all positions have to be
	 * in the same unit.
	 * 
	 * @param robotPosition
	 *            The position of the robot in the image.
	 * @param robotHeight
	 *            The distance from the table to the robot plate.
	 * @param cameraPosition
	 *            The position of the camera in the image.
	 * @param cameraHeight
	 *            The distance from the table to the camera.
	 * @return The actual position of the robot on the table.
	 */
	public static Vector correctPerspective(Vector robotPosition, double robotHeight, Vector cameraPosition, double cameraHeight) {
		double scalingFactor = (cameraHeight-robotHeight)/cameraHeight;
		Vector positionRelativeToCamera = robotPosition.subtract(cameraPosition);
		Vector adjustedPositionRelativeToCamera = positionRelativeToCamera.scalarMultiplication(scalingFactor);
		Vector adjustedPosition = adjustedPositionRelativeToCamera.add(cameraPosition);
		return adjustedPosition;
	}
	
	/**
	 * Converts pixel positions to cm positions
	 * 
	 * @param robotPosition The position to be converted
	 * @param cmPerPixel The number of cm that is equivalent to 1 px.
	 * @return The converted position.
	 */
	public static Vector correctUnit(Vector robotPosition, double cmPerPixel) {
		return robotPosition.scalarMultiplication(cmPerPixel);
	}
	
	/**
	 * Converts pixel positions to cm positions using the default value for pitch 1.
	 * 
	 * @param robotPosition The position to be converted
	 * @return The converted position.
	 */
	public static Vector correctUnit(Vector robotPosition) {
		return robotPosition.scalarMultiplication(World.REAL_UNITS_PER_PIXEL);
	}
	
	/**
	 * Applies correctPerspective and correctUnit to the given robotPosition.
	 * 
	 * @param robotPosition The position that is to be corrected.
	 * @param robotHeight The height from the table of that position.
	 * @param cameraPosition The position of the camera.
	 * @param cameraHeight The height from the table of the camera.
	 * @param cmPerPixel The number of cm that is equivalent to 1 px. 
	 * @return
	 */
	public static Vector applyAll(Vector robotPosition, double robotHeight, Vector cameraPosition, double cameraHeight, double cmPerPixel) {
		robotPosition = correctPerspective(robotPosition, robotHeight, cameraPosition, cameraHeight);
		robotPosition = correctUnit(robotPosition, cmPerPixel);
		return robotPosition;
	}

}
