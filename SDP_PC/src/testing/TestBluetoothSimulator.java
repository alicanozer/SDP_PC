package testing;

import comms.RobotController;
import world.MobilePixelObject;
import world.World;

public class TestBluetoothSimulator {

	public static void main(String[] args) {
		try {
			World world = new World(true, true, World.REAL_UNITS_PER_PIXEL);
			RobotController btRobot = new BluetoothSimulator(world, World.YELLOW_DEFENDER);
			MobilePixelObject pRobot = world.getMobileObject(World.YELLOW_DEFENDER);
			
			System.out.println(pRobot);
			btRobot.forward();
			Thread.sleep(1000);
			System.out.println(pRobot);
			btRobot.stop();
			Thread.sleep(500);
			System.out.println(pRobot);
			btRobot.rotateLEFT(90);
			Thread.sleep(4000);
			System.out.println(pRobot);
			btRobot.rotateLEFT(360);
			Thread.sleep(4000);
			System.out.println(pRobot);
			btRobot.forward();
			Thread.sleep(1000);
			System.out.println(pRobot);
			btRobot.backwards();
			Thread.sleep(1000);
			System.out.println(pRobot);
			btRobot.stop();
			
			
		} catch (InterruptedException e) {

		}
	}

}
