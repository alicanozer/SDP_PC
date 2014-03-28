package testing.executable;

import static org.junit.Assert.*;
import geometry.Vector;

import org.junit.Test;

import testing.support.DummyPositionProvider;
import world.PositionProvider;
import world.World;
import world.object.MobileObject;
import world.object.Zones;
import world.support.TimedVector;

public class TestMobileObject {	
	@Test	
	public void testPositions() {
		DummyPositionProvider positions = new DummyPositionProvider();
		World world = new World(World.TeamColor.BLUE, World.TeamSide.LEFT, 0.5, positions, new Vector(3, 3), 200, Zones.Pitch.ONE);
		MobileObject mobileObject = world.getBall();
		mobileObject.setHeight(0);
		
		//While still on the first frame all methods should throw an exception
		try {
			System.out.println(mobileObject.getPixelPosition());
			fail("Expected an exception");
		} catch (Exception e) {
			
		}
		try {
			mobileObject.getRealPosition();
			fail("Expected an exception");
		} catch (Exception e) {
			
		}
		try {
			mobileObject.getRealVelocity();
			fail("Expected an exception");
		} catch (Exception e) {
			
		}
		try {
			mobileObject.getRealOrientation();
			fail("Expected an exception");
		} catch (Exception e) {
			
		}
		positions.step();
		//Now we proceed to check that the expected values are returned
		try {
		assertEquals(new TimedVector(1, 1, 40), mobileObject.getPixelPosition());
		assertEquals(new TimedVector(0.5, 0.5, 40), mobileObject.getRealPosition());
		assertEquals(new TimedVector(12.5, 12.5, 40), mobileObject.getRealVelocity());
		positions.step();
		} catch (Exception e) {
			fail("Unexpected exception");
		}
		try {
		assertEquals(new TimedVector(2, 2, 80), mobileObject.getPixelPosition());
		assertEquals(new TimedVector(1, 1, 80), mobileObject.getRealPosition());
		assertEquals(new TimedVector(12.5, 12.5, 80), mobileObject.getRealVelocity());
		positions.step();
		} catch (Exception e) {
			fail("Unexpected exception");
		}
		try {
		assertEquals(new TimedVector(4, 4, 160), mobileObject.getPixelPosition());
		assertEquals(new TimedVector(2, 2, 160), mobileObject.getRealPosition());
		assertEquals(new TimedVector(12.5, 12.5, 160), mobileObject.getRealVelocity());
		positions.step();
		} catch (Exception e) {
			fail("Unexpected exception");
		}
		try {
		assertEquals(new TimedVector(6, 6, 200), mobileObject.getPixelPosition());
		assertEquals(new TimedVector(3, 3, 200), mobileObject.getRealPosition());
		assertEquals(new TimedVector(25, 25, 200), mobileObject.getRealVelocity());
		positions.step();
		} catch (Exception e) {
			fail("Unexpected exception");
		}
		try {
		assertEquals(new TimedVector(14, 14, 1000), mobileObject.getPixelPosition());
		assertEquals(new TimedVector(7, 7, 1000), mobileObject.getRealPosition());
		assertEquals(new TimedVector(5, 5, 1000), mobileObject.getRealVelocity());
		positions.step();
		} catch (Exception e) {
			fail("Unexpected exception");
		}
		try {
		System.out.println(mobileObject.getPixelPosition());
		System.out.println(mobileObject.getRealPosition());
		System.out.println(mobileObject.getRealVelocity());
		positions.step();
		} catch (Exception e) {
			fail("Unexpected exception");
		}
		try {
		assertEquals(new TimedVector(15, 15, 1001), mobileObject.getPixelPosition());
		assertEquals(new TimedVector(7.5, 7.5, 1001), mobileObject.getRealPosition());
		assertEquals(new TimedVector(0, 0, 1001), mobileObject.getRealVelocity());
		positions.step();
		} catch (Exception e) {
			fail("Unexpected exception");
		}
		try {
		System.out.println(mobileObject.getPixelPosition());
		System.out.println(mobileObject.getRealPosition());
		System.out.println(mobileObject.getRealVelocity());
		positions.step();
		} catch (Exception e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void testReachablePixel() {
		World world = new World(World.TeamColor.BLUE, World.TeamSide.LEFT, 0.5, null, new Vector(3, 3), 200, Zones.Pitch.ONE);
		try {
			assertFalse(world.getHeroAttacker().reachablePosition(new Vector(110*World.REAL_UNITS_PER_PIXEL, 220*World.REAL_UNITS_PER_PIXEL)));
			assertTrue(world.getHeroDefender().reachablePosition(new Vector(170*World.REAL_UNITS_PER_PIXEL-11, 85*World.REAL_UNITS_PER_PIXEL+11)));
			assertFalse(world.getHeroDefender().reachablePosition(new Vector(170*World.REAL_UNITS_PER_PIXEL, 85*World.REAL_UNITS_PER_PIXEL+11)));
			assertFalse(world.getHeroDefender().reachablePosition(new Vector(170*World.REAL_UNITS_PER_PIXEL+11, 85*World.REAL_UNITS_PER_PIXEL+11)));
		} catch (Exception e) {
			fail("Unexpected exception");
		}
	}

}
