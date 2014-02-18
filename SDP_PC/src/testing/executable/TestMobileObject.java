package testing.executable;

import static org.junit.Assert.*;
import geometry.Vector;

import org.junit.Test;

import world.World;
import world.object.MobileObject;
import world.object.MobileRealObject;

public class TestMobileObject {
	@Test
	public void test() throws InterruptedException {
		World world = new World(World.TeamColor.BLUE, World.TeamSide.LEFT, 1);
		world.setPixelCameraPosition(new Vector(320,240));
		world.setRealCameraElevation(200);
		
		MobileObject mobileObject = new MobileObject(world);
		mobileObject.setHeight(18);
		
		mobileObject.setPixelPosition(new Vector(5,5));
		try {
			System.out.println(mobileObject.getRealPosition());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(10);
		mobileObject.setPixelPosition(new Vector(5,5));
		try {
			System.out.println(mobileObject.getRealPosition());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(10);
		mobileObject.setPixelPosition(new Vector(5,5));
		try {
			System.out.println(mobileObject.getRealPosition());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
