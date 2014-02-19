package testing.executable;

import static org.junit.Assert.*;
import geometry.Vector;

import org.junit.Test;

import world.World;
import world.object.MobileObject;
import world.object.MobileRealObject;
import world.support.PerspectiveCorrection;

public class TestMobileObject {
	@Test
	public void test() throws InterruptedException {
		World world = new World(World.TeamColor.BLUE, World.TeamSide.LEFT, 1, 200, new Vector(320, 640));
		world.setPixelCameraPosition(new Vector(320,240));
		world.setRealCameraElevation(200);
		
		MobileObject mobileObject = new MobileObject(world);
		mobileObject.setHeight(18);
		
		mobileObject.setPixelPosition(new Vector(5,5));
		try {
			System.out.println(mobileObject.getRealPosition());
		} catch (Exception e) {
		}
		Thread.sleep(10);
		mobileObject.setPixelPosition(new Vector(5,5));
		try {
			System.out.println(mobileObject.getRealPosition());
		} catch (Exception e) {
		}
		Thread.sleep(10);
		mobileObject.setPixelPosition(new Vector(5,5));
		try {
			System.out.println(mobileObject.getRealPosition());
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testPerspectiveCorrection() throws Exception {
		MobileObject mobileObject1, mobileObject2;
		World world1, world2;
		Vector position1, position2;
		
		Vector p1, p2;
		boolean eq;

		// If the robot height is 0 then the camera position should have no
		// effect
		world1 = new World(World.TeamColor.BLUE, World.TeamSide.LEFT, 1, 200, new Vector(7, 7));
		world2 = new World(World.TeamColor.BLUE, World.TeamSide.LEFT, 1, 200, new Vector(7, 7));
		mobileObject1 = new MobileObject(world1);
		mobileObject2 = new MobileObject(world2);
		position1 = new Vector(3,3);
		mobileObject1.setPixelPosition(position1);
		mobileObject1.setPixelPosition(position1);
		mobileObject2.setPixelPosition(position1);
		mobileObject2.setPixelPosition(position1);
		
		p1 = mobileObject1.getRealPosition();
		p2 = mobileObject2.getRealPosition();
		eq = p1.equals(p2);
		assertTrue(eq);
		
		// If the robot height is 0 then the camera elevation should have no
		// effect
		p1 = PerspectiveCorrection.correctPosition(200, 0, new Vector(3, 3),
				new Vector(4, 4));
		p2 = PerspectiveCorrection.correctPosition(100, 0, new Vector(3, 3),
				new Vector(4, 4));
		eq = p1.equals(p2);
		assertTrue(eq);

		// If the robot is in the same position as the camera then camera
		// elevation should have no effect
		p1 = PerspectiveCorrection.correctPosition(200, 0, new Vector(3, 3),
				new Vector(3, 3));
		p2 = PerspectiveCorrection.correctPosition(100, 0, new Vector(3, 3),
				new Vector(3, 3));
		eq = p1.equals(p2);
		assertTrue(eq);

		// If the robot is in the same position as the camera then robot height
		// should have no effect
		p1 = PerspectiveCorrection.correctPosition(200, 0, new Vector(3, 3),
				new Vector(3, 3));
		p2 = PerspectiveCorrection.correctPosition(100, 20, new Vector(3, 3),
				new Vector(3, 3));
		eq = p1.equals(p2);
		assertTrue(eq);

		// If the robot is NOT in the same position as the camera then robot
		// height should have an effect.
		p1 = PerspectiveCorrection.correctPosition(200, 0, new Vector(3, 3),
				new Vector(7, 7));
		p2 = PerspectiveCorrection.correctPosition(100, 20, new Vector(3, 3),
				new Vector(7, 7));
		eq = p1.equals(p2);
		assertFalse(eq);
	}

}
