package comms;

import lejos.nxt.Button;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;
import World.RobotType;

public class Test3 {

	static BluetoothRobot bRobot;
	public static final String HERCULES = "0016530D4ED8";
	private static Bluetooth connection;
	
	public static void main(String[] args) {
 	
	ObjectLocations.setYellowDefendingLeft(true);
	ObjectLocations.setYellowUs(true);
	
	VisionRunner.start(true,PitchConstants.newPitch);
	
	bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
	bRobot.connect();
	bRobot.setSpeed(20);
	
	while (true) {
		if (ObjectLocations.getBall() != null && ObjectLocations.getYellowATTACKmarker() != null && ObjectLocations.getYellowATTACKdot() != null) {
						
			try {
				//Turn to Ball
				double turn = TurnToObject.Ball(RobotType.AttackUs);
				System.out.println(turn);
//				bRobot.rotateLEFT((int) turn);
				Button.waitForAnyPress();
			} catch (Exception e) {
				e.printStackTrace();
			}

//			Move to Ball
//			double distance = DistanceCalculator.Distance(ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBall());
//			bRobot.forward(distance);
//			System.out.println(distance);
//			Button.waitForAnyPress();
			
		}
	}
	
	}
	
}




