/**
 * 
 */
package testing.executable;

import static org.junit.Assert.*;
import geometry.Vector;

import org.junit.Test;

import world.support.Corrections;

/**
 * @author apljungquist
 *
 */
public class TestCorrections {

	@Test
	public void testCorrectPerspective() {
		Vector[] positions = new Vector[5];
		positions[0] = new Vector(0,0);
		positions[1] = new Vector(1,1);
		positions[2] = new Vector(2,2);
		positions[3] = new Vector(3,3);
		positions[4] = new Vector(7,7);
		
		double[] heights = new double[5];
		heights[0] = 2;
		heights[1] = 20;
		heights[2] = 200;
		heights[3] = 2000;
		heights[4] = 20000;
		
		//If the height of the robot is zero the adjusted position should equal the original position.
		for (Vector robotPosition : positions) {
			for (Vector cameraPosition : positions) {
				for (double cameraHeight : heights) {
					assertEquals(robotPosition, Corrections.correctPerspective(robotPosition, 0, cameraPosition, cameraHeight));
				}
			}
		}
		
		//If the robot is in the same position as the camera nothing the adjusted position should equal the original position.
		for (Vector robotPosition : positions) {
			for (double robotHeight : heights) {
				for (double cameraHeight : heights) {
					assertEquals(robotPosition, Corrections.correctPerspective(robotPosition, robotHeight, robotPosition, cameraHeight));
				}
			}
		}
		
		//If the robot is not in the same position as the camera and it's height is not 0 then the original position should never be the same as the adjusted position
		//Also the robot must not be the same height as the camera.
		for (Vector robotPosition : positions) {
			for (Vector cameraPosition : positions) {
				for (double robotHeight : heights) {
					for (double cameraHeight : heights) {
						if (robotHeight != 0 && !robotPosition.equals(cameraPosition) && robotHeight != cameraHeight) {
							assertNotEquals(robotPosition, Corrections.correctPerspective(robotPosition, robotHeight, cameraPosition, cameraHeight));
						}
					}
				}
			}	
		}
		
		//Also perform a calculation that has been checked manually.
		Vector cameraPosition = new Vector(13,13);
		double cameraHeight = 200;
		Vector actualPosition = new Vector(7.6, 7.6);
		Vector perceivedPosition = new Vector(7,7);
		double robotHeight = 20;
		
		assertTrue(actualPosition.equals(Corrections.correctPerspective(perceivedPosition, robotHeight, cameraPosition, cameraHeight)));
	}

}
