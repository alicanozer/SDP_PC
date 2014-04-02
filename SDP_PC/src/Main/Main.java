package Main;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import Calculations.Intersection;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;

public class Main {
	
	public static void main(String[] args) {
<<<<<<< HEAD
		VisionRunner.startDebugVision(PitchConstants.oldPitch,10,true);
		PitchConstants.setPitch = 1; //1 for old, 2 for new

		System.out.println("Entering strategy");
=======

		VisionRunner.startDebugVision(PitchConstants.newPitch,10,true);
		
		while (true) {
			if (ObjectLocations.getUSDefend() != null && ObjectLocations.getUSDefendDot() != null && ObjectLocations.getTHEMAttack() != null && ObjectLocations.getTHEMAttackDot() != null) {
				
				Point2D_I32 ourDot;
				Point2D_I32 ourMarker;
				Point2D_I32 theirDot;
				Point2D_I32 theirMarker;

				ourDot = ObjectLocations.getUSDefendDot();
				ourMarker = ObjectLocations.getUSDefend();

				theirDot = ObjectLocations.getTHEMAttackDot();
				theirMarker = ObjectLocations.getTHEMAttack();

				Vector intersect = Intersection.IntersectionRobots(theirMarker, theirDot, ourMarker, ourDot);
				
				System.out.println("Our Robot: (" + ourMarker.x + ", " + ourMarker.y + ")");
				System.out.println("Their Robot: (" + theirMarker.x + ", " + theirMarker.y + ")");
				System.out.println("Intersection: (" + intersect.getX() + ", " + intersect.getY() + ")");
				
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

>>>>>>> Added Block Shot Sterategy
	}
	
}
