package Main;

import vision.PitchConstants;
import vision.VisionRunner;

public class Main {
	
	public static void main(String[] args) {

		VisionRunner.startDebugVision(PitchConstants.oldPitch,10,true);
		PitchConstants.setPitch = 1; //1 for old, 2 for new

		System.out.println("Entering strategy");

	}
	
}
