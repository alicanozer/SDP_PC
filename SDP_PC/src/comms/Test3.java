package comms;

import lejos.nxt.Button;
import movement.RobotMover;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;
import world.RobotType;

public class Test3 {

	static BluetoothRobot bRobot;
	private static Bluetooth connection;

	public static void main(String[] args) throws Exception {

		connection = new Bluetooth("defence");
		bRobot = new BluetoothRobot(RobotType.DefendUs, connection);
		bRobot.connect();
		RobotMover attackMover = new RobotMover(bRobot);

		


		attackMover.start();
		System.out.println("0");
		attackMover.setSpeed("defence", 15);
		
		attackMover.forward("defence", 20);
		attackMover.rotate("defence", 90);
		
		attackMover.forward("defence", 20);
		attackMover.rotate("defence", 90);
		
		attackMover.forward("defence", 20);
		attackMover.rotate("defence", 90);
		
		attackMover.forward("defence", 20);
		attackMover.rotate("defence", 90);
		Button.waitForAnyPress();
		System.out.println("4");
		bRobot.disconnect("defence");
		//	System.out.println("Entering While Loop");
		//	while (true) {
		//		int x = 42;
		//		try {
		//			if (ObjectLocations.getYellowATTACKmarker() != null && ObjectLocations.getYellowATTACKdot() != null && ObjectLocations.getBall() != null) {
		//				System.out.println("Calculating Angle to Ball");
		//				//Turn to Ball
		//				double angle = TurnToObject.Ball(RobotType.AttackUs);
		//				bRobot.rotateLEFT("attack", (int) angle);
		//				System.out.println("Angle to Ball:" + angle);
		//				//break;
		//				//Button.waitForAnyPress();
		//			}
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//	}

	}

}




