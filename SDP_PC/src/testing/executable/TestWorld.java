package testing.executable;

import static org.junit.Assert.*;

import world.World;
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
		
		World world = new World(World.TeamColor.YELLOW, World.TeamSide.LEFT, World.REAL_UNITS_PER_PIXEL);
		MobileRealObject realObj = world.getHeroDefender();
		MobilePixelObject pixelObj = world.getYellowDefender();
		
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
		World world;
		
		world = new World(World.TeamColor.BLUE, World.TeamSide.LEFT);
		//Make sure all methods that should return the same object returns the same object
		assertEquals(world.getBlueAttacker(), world.getBlueAttacker());
		assertEquals(world.getBlueAttacker(), world.getHeroAttacker());
		assertEquals(world.getBlueAttacker(), world.getRobot2());
		//Make sure it equals no other objects (assumes getRobotX works)
		assertNotEquals(world.getBlueAttacker(), world.getRobot0());
		assertNotEquals(world.getBlueAttacker(), world.getRobot1());
		assertNotEquals(world.getBlueAttacker(), world.getRobot3());
		assertNotEquals (world.getBlueAttacker(), world.getBall());
		
		world = new World(World.TeamColor.BLUE, World.TeamSide.RIGHT);
		//Make sure all methods that should return the same object returns the same object
		assertEquals (world.getBlueAttacker(), world.getBlueAttacker());
		assertEquals (world.getBlueAttacker(), world.getHeroAttacker());
		assertEquals (world.getBlueAttacker(), world.getRobot1());
		//Make sure it equals no other objects (assumes getRobotX works)
		assertNotEquals (world.getBlueAttacker(), world.getRobot0());
		assertNotEquals (world.getBlueAttacker(), world.getRobot2());
		assertNotEquals (world.getBlueAttacker(), world.getRobot3());
		assertNotEquals (world.getBlueAttacker(), world.getBall());
		
		world = new World(World.TeamColor.YELLOW, World.TeamSide.RIGHT);
		//Make sure all methods that should return the same object returns the same object
		assertEquals (world.getBlueAttacker(), world.getBlueAttacker());
		assertEquals (world.getBlueAttacker(), world.getVillainAttacker());
		assertEquals (world.getBlueAttacker(), world.getRobot2());
		//Make sure it equals no other objects (assumes getRobotX works)
		assertNotEquals (world.getBlueAttacker(), world.getRobot0());
		assertNotEquals (world.getBlueAttacker(), world.getRobot1());
		assertNotEquals (world.getBlueAttacker(), world.getRobot3());
		assertNotEquals (world.getBlueAttacker(), world.getBall());
		
		world = new World(World.TeamColor.YELLOW, World.TeamSide.LEFT);
		//Make sure all methods that should return the same object returns the same object
		assertEquals (world.getBlueAttacker(), world.getBlueAttacker());
		assertEquals (world.getBlueAttacker(), world.getVillainAttacker());
		assertEquals (world.getBlueAttacker(), world.getRobot1());
		//Make sure it equals no other objects (assumes getRobotX works)
		assertNotEquals (world.getBlueAttacker(), world.getRobot0());
		assertNotEquals (world.getBlueAttacker(), world.getRobot2());
		assertNotEquals (world.getBlueAttacker(), world.getRobot3());
		assertNotEquals (world.getBlueAttacker(), world.getBall());
	}
}
