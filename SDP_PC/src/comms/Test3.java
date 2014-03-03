package comms;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.pc.comm.NXTCommException;
import strategy.movement.MoveToPointXY;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;
import World.RobotType;

public class Test3 {

	static BluetoothRobot bRobot;
	private static Bluetooth connection;
	
	public static void main(String[] args) throws IOException, NXTCommException {
 	
	ObjectLocations.setYellowDefendingLeft(true);
	ObjectLocations.setYellowUs(true);
	
	connection = new Bluetooth("defence");
	bRobot = new BluetoothRobot(RobotType.DefendUs, connection);
	bRobot.connect();
	
	//VisionRunner.startDebugVision(PitchConstants.newPitch, 10, true);
	
	System.out.println("asd");
	while (true) {
				//Turn to Ball
				bRobot.grab("defence");
				bRobot.kick("defence");
		
		Button.waitForAnyPress();
	
	}
	
	}
	
}




