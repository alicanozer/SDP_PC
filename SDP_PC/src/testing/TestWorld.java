package testing;

import world.MobilePixelObject;
import world.MobileRealObject;
import world.PixelWorld;
import world.RealWorld;
import world.World;
import geometry.Vector;

public class TestWorld {

	
	static Vector[] waypoints = new Vector[10];
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		// Bellow are the different waypoints which the robot will be placed at
		// as well as the property values expected in between the changes
		//PixelPos: 	None
		//RealPost:		None
		//RealVelocity: None
		waypoints[0] = new Vector(10, 10);
		//PixelPos: 	(10, 10)
		//RealPost:		(4.6, 4.6)
		//RealVelocity: None
		waypoints[1] = new Vector(20, 10);
		//PixelPos: 	(20, 10)
		//RealPost:		(9.2, 4.6)
		//RealVelocity: (4.6, 0)
		waypoints[2] = new Vector(20, 20);
		//PixelPos: 	(20, 20)
		//RealPost:		(9.2, 9.2)
		//RealVelocity: (0, 4.6)
		waypoints[3] = new Vector(30, 30);
		//PixelPos: 	(30, 30)
		//RealPost:		(13.8, 13.8)
		//RealVelocity: (4.6, 4.6)
		waypoints[4] = null;
		//PixelPos: 	50, 40 		//If extrapolation using acceleration is implemented
		//RealPost:		23, 18.4 	//If extrapolation using acceleration is implemented
		//RealVelocity: 9.2, 4.6 	//If extrapolation using acceleration is implemented
		waypoints[5] = new Vector(80, 80);
		//PixelPos: 	(80, 80)
		//RealPost:		(36.8, 36.8)
		//RealVelocity: (11.5, 11.5)
		waypoints[6] = new Vector(40, 80);
		//PixelPos: 	
		//RealPost:		
		//RealVelocity: 
		waypoints[7] = new Vector(0, 80);
		//PixelPos: 	
		//RealPost:		
		//RealVelocity: 
		waypoints[8] = new Vector(0, 40);
		//PixelPos: 	
		//RealPost:		
		//RealVelocity: 
		waypoints[9] = new Vector(0, 0);
		//PixelPos: 	0,0
		//RealPost:		0,0
		//RealVelocity: 0, ?
		
		// Notice how the speed always comes out to null because no time passes
		// between the position updates.
		testPosition(0);
		// This test emulates updates for every frame in the video stream
		// (25fps). The velocities will be crazy high.
		testPosition(1000/25);
		// This test emulates updates every second. The velocities should come
		// out as expected.
		testPosition(1000);
		
		
	}
	
	/**
	 * 
	 * @param updatePeriod The minimum amount of time in between two updates of the world
	 * 
	 * @throws InterruptedException
	 */
	private static void testPosition(long updatePeriod) throws InterruptedException {
		World world = new World(World.YELLOW, World.LEFT, 0.455769231);
		MobileRealObject realObj = world.getMobileObject(RealWorld.HERO_DEFENDER);
		MobilePixelObject pixelObj = world.getMobileObject(PixelWorld.YELLOW_DEFENDER);
		
		System.out.println("\ngetPixelPosition(): "+pixelObj.getPixelPosition());
		System.out.println("getRealPosition(): "+realObj.getRealPosition());
		System.out.println("getRealVelocity(): "+realObj.getRealVelocity());
		
		for (Vector vector : waypoints) {
			long t0 = System.currentTimeMillis();
			System.out.println("\nsetPixelPosition "+vector);
			pixelObj.setPixelPosition(vector);
			
			System.out.println("getPixelPosition(): "+pixelObj.getPixelPosition());
			System.out.println("getRealPosition(): "+realObj.getRealPosition());
			System.out.println("getRealVelocity(): "+realObj.getRealVelocity());
			long t1 = System.currentTimeMillis();
			long dt = (t1 - t0);
			if (updatePeriod > 0) {
				Thread.sleep(updatePeriod-dt);
			}
		}
	}

}
