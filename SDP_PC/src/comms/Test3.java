package comms;

import Calculations.DistanceCalculator;
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
<<<<<<< HEAD
 		
<<<<<<< HEAD
<<<<<<< HEAD
	connection = new Bluetooth("attack");
	bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
=======
	connection = new Bluetooth("defence");
	bRobot = new BluetoothRobot(RobotType.DefendUs, connection);
>>>>>>> qweqwe
	bRobot.connect();
	RobotMover robot = new RobotMover(bRobot);
	
/*	VisionRunner.startDebugVision(PitchConstants.newPitch, 10, false);*/
	
	System.out.println("Entering While Loop");
	/*while (true) {
		int x = 42;
		try {
			if (ObjectLocations.getYellowATTACKmarker() != null && ObjectLocations.getYellowATTACKdot() != null && ObjectLocations.getBall() != null) {
				System.out.println("Calculating Angle to Ball");
				//Turn to Ball
//				double angle = TurnToObject.Ball(RobotType.AttackUs);
//				bRobot.rotateLEFT("attack", (int) angle);
//				System.out.println("Angle to Ball:" + angle);
				//break;
				//Button.waitForAnyPress();
						
		System.out.println("Why does this happen?");
		System.out.println("Point2D: (" + ObjectLocations.getYellowATTACKmarker().x + ", " + ObjectLocations.getYellowATTACKmarker().y + ")");
				
		double distance = DistanceCalculator.Distance(ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBall());
		System.out.println("Distance to Ball:" + distance);
		Thread.sleep(200);
				
		}
		
	}
	
	}*/
	
	robot.setSpeed("attack", 10);
	robot.forward("attack", 20);
	robot.run();
	bRobot.disconnect("attack");
	
	
	}	
=======

		connection = new Bluetooth("defence");
		bRobot = new BluetoothRobot(RobotType.DefendUs, connection);
		bRobot.connect();
		RobotMover attackMover = new RobotMover(bRobot);

		


		attackMover.start();
		System.out.println("0");
		attackMover.setSpeed("defence", 15);
//		
//		attackMover.forward("defence", 20);
//		attackMover.rotate("defence", 90);
//		
//		attackMover.forward("defence", 20);
//		attackMover.rotate("defence", 90);
//		
//		attackMover.forward("defence", 20);
//		attackMover.rotate("defence", 90);
//		
//		attackMover.forward("defence", 20);
//		attackMover.rotate("defence", 90);
		Button.waitForAnyPress();
		
		attackMover.forwardsC("defence");
		Thread.sleep(2000);
		attackMover.backwardsC("defence");
		Thread.sleep(2000);
		System.out.println("4");
		
		attackMover.stopRobot("defence");
		Button.waitForAnyPress();
		
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

>>>>>>> best sdp ever
}




