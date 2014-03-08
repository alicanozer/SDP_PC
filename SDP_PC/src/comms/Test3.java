package comms;

import lejos.nxt.Button;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;
import World.RobotType;

public class Test3 {

	static BluetoothRobot bRobot;
	private static Bluetooth connection;
	
	public static void main(String[] args) throws Exception {
 		
	connection = new Bluetooth("attack");
	bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
	bRobot.connect();
	
	VisionRunner.startDebugVision(PitchConstants.newPitch, 10, false);
	
	System.out.println("Entering While Loop");
	while (true) {
		int x = 42;
		try {
			if (ObjectLocations.getYellowATTACKmarker() != null && ObjectLocations.getYellowATTACKdot() != null && ObjectLocations.getBall() != null) {
				System.out.println("Calculating Angle to Ball");
				//Turn to Ball
				double angle = TurnToObject.Ball(RobotType.AttackUs);
				bRobot.rotateLEFT("attack", (int) angle);
				System.out.println("Angle to Ball:" + angle);
				//break;
				//Button.waitForAnyPress();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	}
	
}




