package Main;

import lejos.nxt.Button;
import World.RobotType;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;

public class Main {
	
	public static void main(String[] args) {
	    VisionRunner.start(true,PitchConstants.newPitch,10);
	}
}
