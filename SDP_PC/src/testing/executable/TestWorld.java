package testing.executable;

import world.PixelWorld;
import world.PixelWorldColorless;
import world.RealWorld;
import world.World;
import world.object.MobileObject;
import world.object.MobilePixelObject;
import world.object.MobileRealObject;
import geometry.Vector;

public class TestWorld {

	
	
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		testCorrespondances();
		
		// Notice how no history is accumulated since no time passes inbetween
		// updates of position
//		testPosition(0);
//		// This test emulates updates for every frame in the video stream
//		// (25fps). The velocities will be crazy high.
//		testPosition(1000/25);
//		// This test emulates updates every second. The velocities should come
//		// out as expected.
//		testPosition(1000);
		
		
	}
	
	/**
	 * 
	 * @param updatePeriod The minimum amount of time in between two updates of the world
	 * 
	 * @throws InterruptedException
	 */
	private static void testPosition(long updatePeriod) throws InterruptedException {
		Vector[] waypoints = new Vector[10];
		
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
		//RealPost:		18.4, 18.4 	//If extrapolation using acceleration is implemented
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
		
		World world = new World(World.YELLOW, World.LEFT, World.REAL_UNITS_PER_PIXEL);
		MobileRealObject realObj = world.getMobileObject(RealWorld.HERO_DEFENDER);
		MobilePixelObject pixelObj = world.getMobileObject(PixelWorld.YELLOW_DEFENDER);
		
		System.out.println("getPixelPosition(): "+pixelObj.getPixelPosition());
		System.out.println(realObj);
		System.out.println();
		
		for (Vector vector : waypoints) {
			long t0 = System.currentTimeMillis();
			System.out.println("setPixelPosition "+vector);
			pixelObj.setPixelPosition(vector);
			
			System.out.println("getPixelPosition(): "+pixelObj.getPixelPosition());
			System.out.println(realObj);
			System.out.println();
			long t1 = System.currentTimeMillis();
			long dt = (t1 - t0);
			if (updatePeriod > 0) {
				Thread.sleep(Math.max(updatePeriod-dt, 0));
			}
		}
	}

	private static void testCorrespondances() {
		World world = new World(World.BLUE, World.LEFT);
		
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorld.YELLOW_ATTACKER, "yellow attacker", world);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", RealWorld.VILLAIN_ATTACKER, "villain attacker", world);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", RealWorld.HERO_DEFENDER, "hero defender", world);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", RealWorld.BALL, "ball", world);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorldColorless.ROBOT0, "robot0", world);
		
		testCorrespondance(PixelWorld.BALL, "ball", PixelWorldColorless.BALL, "ball", world);
		testCorrespondance(PixelWorld.BALL, "ball", RealWorld.BALL, "ball", world);

		System.out.println("\nWe play BLUE from the LEFT");
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorld.BLUE_ATTACKER, "blue attacker", world);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", RealWorld.HERO_ATTACKER, "hero attacker", world);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorldColorless.ROBOT2, "robot 2", world);
		
		System.out.println("\nWe play BLUE from the RIGHT");
		World world2 = new World(World.BLUE, World.RIGHT);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorld.BLUE_ATTACKER, "blue attacker", world2);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", RealWorld.HERO_ATTACKER, "hero attacker", world2);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorldColorless.ROBOT1, "robot1", world2);
		
		System.out.println();
		testCorrespondance(PixelWorldColorless.ROBOT1, "robot 1", PixelWorldColorless.ROBOT1, "robot1", world2);
		testCorrespondance(PixelWorldColorless.ROBOT1, "robot 1", PixelWorld.BLUE_ATTACKER, "blue attacker", world2);
		testCorrespondance(PixelWorldColorless.ROBOT1, "robot 1", PixelWorld.BLUE_DEFENDER, "blue defender", world2);
		testCorrespondance(PixelWorldColorless.ROBOT1, "robot 1", PixelWorld.YELLOW_ATTACKER, "yellow attacker", world2);
		testCorrespondance(PixelWorldColorless.ROBOT1, "robot 1", PixelWorld.YELLOW_DEFENDER, "yellow defender", world2);
		
		System.out.println("\nWe play YELLOW from the RIGHT");
		World world3 = new World(World.YELLOW, World.RIGHT);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorld.BLUE_ATTACKER, "blue attacker", world3);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", RealWorld.VILLAIN_ATTACKER, "villain attacker", world3);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorldColorless.ROBOT2, "robot2", world3);
		
		System.out.println("\nWe play YELLOW from the LEFT");
		World world4 = new World(World.YELLOW, World.LEFT);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorld.BLUE_ATTACKER, "blue attacker", world3);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", RealWorld.VILLAIN_ATTACKER, "villain attacker", world3);
		testCorrespondance(PixelWorld.BLUE_ATTACKER, "blue attacker", PixelWorldColorless.ROBOT1, "robot1", world3);
		
	}
	
	private static void testCorrespondance(int object1, String name1, int object2, String name2, World world) {
		MobileObject mObj1 = world.getMobileObject(object1);
		MobileObject mObj2 = world.getMobileObject(object2);
		
		if (mObj1 != mObj2) {
			System.out.println(name1 + " != " + name2);
		}else {
			System.out.println(name1 + " == " + name2);
		}
	}
}
