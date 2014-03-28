package testing.executable;

import static org.junit.Assert.*;

import org.junit.Test;

import world.World;
import world.object.Zones;

public class TestWorld {
	@Test
	public void testCorrespondances() {
		World world;
		
		world = new World(World.TeamColor.BLUE, World.TeamSide.LEFT, 0, null, null, 0, Zones.Pitch.ONE);
		//Make sure all methods that should return the same object returns the same object
		assertEquals(world.getBlueAttacker(), world.getBlueAttacker());
		assertEquals(world.getBlueAttacker(), world.getHeroAttacker());
		assertEquals(world.getBlueAttacker(), world.getRobot2());
		//Make sure it equals no other objects (assumes getRobotX works)
		assertNotEquals(world.getBlueAttacker(), world.getRobot0());
		assertNotEquals(world.getBlueAttacker(), world.getRobot1());
		assertNotEquals(world.getBlueAttacker(), world.getRobot3());
		assertNotEquals (world.getBlueAttacker(), world.getBall());
		
		world = new World(World.TeamColor.BLUE, World.TeamSide.RIGHT, 0, null, null, 0, Zones.Pitch.ONE);
		//Make sure all methods that should return the same object returns the same object
		assertEquals (world.getBlueAttacker(), world.getBlueAttacker());
		assertEquals (world.getBlueAttacker(), world.getHeroAttacker());
		assertEquals (world.getBlueAttacker(), world.getRobot1());
		//Make sure it equals no other objects (assumes getRobotX works)
		assertNotEquals (world.getBlueAttacker(), world.getRobot0());
		assertNotEquals (world.getBlueAttacker(), world.getRobot2());
		assertNotEquals (world.getBlueAttacker(), world.getRobot3());
		assertNotEquals (world.getBlueAttacker(), world.getBall());
		
		world = new World(World.TeamColor.YELLOW, World.TeamSide.RIGHT, 0, null, null, 0, Zones.Pitch.ONE);
		//Make sure all methods that should return the same object returns the same object
		assertEquals (world.getBlueAttacker(), world.getBlueAttacker());
		assertEquals (world.getBlueAttacker(), world.getVillainAttacker());
		assertEquals (world.getBlueAttacker(), world.getRobot2());
		//Make sure it equals no other objects (assumes getRobotX works)
		assertNotEquals (world.getBlueAttacker(), world.getRobot0());
		assertNotEquals (world.getBlueAttacker(), world.getRobot1());
		assertNotEquals (world.getBlueAttacker(), world.getRobot3());
		assertNotEquals (world.getBlueAttacker(), world.getBall());
		
		world = new World(World.TeamColor.YELLOW, World.TeamSide.LEFT, 0, null, null, 0, Zones.Pitch.ONE);
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
