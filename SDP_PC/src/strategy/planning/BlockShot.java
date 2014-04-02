package strategy.planning;

import java.awt.Point;

import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import Calculations.Intersection;
import Calculations.IntersectionLines;
import strategy.movement.MoveToPointXY;
import vision.ObjectLocations;
import world.RobotType;
import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import movement.RobotMover;

public class BlockShot {

	public static void blockShot(String type, RobotMover robotMover) {

		//BallPossession.hasPossession(RobotType.AttackThem, ObjectLocations.getTHEMAttack()
		while (true) {
			if(ObjectLocations.getUSDefend() != null && ObjectLocations.getUSDefendDot() != null){

				Point2D_I32 ourDot;
				Point2D_I32 ourMarker;
				Point2D_I32 theirDot;
				Point2D_I32 theirMarker;

				ourDot = ObjectLocations.getUSDefendDot();
				ourMarker = ObjectLocations.getUSDefend();

				theirDot = ObjectLocations.getTHEMAttackDot();
				theirMarker = ObjectLocations.getTHEMAttack();

				Vector intersect = Intersection.IntersectionRobots(theirMarker, theirDot, ourMarker, ourDot);
				
//				System.out.println("Our Robot: (" + ourMarker.x + ", " + ourMarker.y + ")");
//				System.out.println("Their Robot: (" + theirMarker.x + ", " + theirMarker.y + ")");
				System.out.println("Intersection: (" + intersect.getX() + ", " + intersect.getY() + ")");

				Point2D_I32 point = new Point2D_I32((int) intersect.getX(), (int) intersect.getY());
				
				double distance = DistanceCalculator.Distance(ObjectLocations.getUSDefend(), point);
				System.out.println("Distance to Point: " + distance);
				
				if (point.y > 69 && point.y < 205) {
					if (point.y < ObjectLocations.getUSDefendDot().y) {
						robotMover.forward(type, -distance);
					} else {
						robotMover.forward(type, distance);
					}
				} else {
					robotMover.stopRobot(type);
					try {
						robotMover.resetQueue(type);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
