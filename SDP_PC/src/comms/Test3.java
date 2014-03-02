package comms;

import lejos.nxt.Button;
import strategy.movement.MoveToPointXY;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;
import World.RobotType;

public class Test3 {

	static BluetoothRobot bRobot;
	private static Bluetooth connection;
	
	public static void main(String[] args) {
 	
	ObjectLocations.setYellowDefendingLeft(true);
	ObjectLocations.setYellowUs(true);
	
	bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
	bRobot.connect();
	
	VisionRunner.startDebugVision(PitchConstants.newPitch, 10, true);
	
	System.out.println("asd");
	while (true) {
		if (ObjectLocations.getBall() != null && ObjectLocations.getYellowATTACKmarker() != null && ObjectLocations.getYellowATTACKdot() != null) {
			try {
				//Turn to Ball
				MoveToPointXY.moveToPointXY(bRobot, ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBall());
				Button.waitForAnyPress();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	}
	
}




