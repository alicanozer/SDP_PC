package comms;

import Calculations.DistanceCalculator;
import lejos.nxt.Button;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;
import world.RobotType;

public class Test3 {

	static BluetoothRobot bRobot;
	private static Bluetooth connection;
	
	public static void main(String[] args) throws Exception {
 		
//	connection = new Bluetooth("attack");
//	bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
//	bRobot.connect();
	
	VisionRunner.startDebugVision(PitchConstants.newPitch, 10, false);
		
	while (true) {

		if (ObjectLocations.getYellowATTACKmarker() != null && ObjectLocations.getBall() != null) {
//				System.out.println("Calculating Angle to Ball");
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
	
	}
	
}




