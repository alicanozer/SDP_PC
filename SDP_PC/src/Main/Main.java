package Main;

import java.io.IOException;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import lejos.pc.comm.NXTCommException;
import movement.RobotMover;
import comms.Bluetooth;
import comms.BluetoothRobot;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;
import world.RobotType;


public class Main {

	static Bluetooth bluetooth;
	static BluetoothRobot bRobot;
	static RobotMover robotMover;
	
	public static void main(String[] args) {

		VisionRunner.startDebugVision(PitchConstants.oldPitch,10,true);
		PitchConstants.setPitch = 1; //1 for old, 2 for new

		System.out.println("Entering strategy");
	}
	
}
