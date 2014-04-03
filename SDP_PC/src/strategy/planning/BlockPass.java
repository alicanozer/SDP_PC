package strategy.planning;

import movement.RobotMover;
import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import Calculations.Intersection;

public class BlockPass {

	public static void blockPass(String type, RobotMover robotMover) {

		double angle;
		
		while (BallPossession.hasPossession(RobotType.DefendThem, ObjectLocations.getTHEMDefend())) {
			
			angle = TurnToObject.alignHorizontal(RobotType.AttackUs);
			
			if(ObjectLocations.getUSAttack() != null && ObjectLocations.getUSAttackDot() != null){

				if (angle > 20) {
					robotMover.rotate(type, angle);
				} 
				
				Point2D_I32 ourDot;
				Point2D_I32 ourMarker;
				Point2D_I32 theirDot;
				Point2D_I32 theirMarker;

				ourDot = ObjectLocations.getUSAttackDot();
				ourMarker = ObjectLocations.getUSAttack();

				theirDot = ObjectLocations.getTHEMDefendDot();
				theirMarker = ObjectLocations.getTHEMDefend();

				Vector intersect = Intersection.IntersectionRobots(theirMarker, theirDot, ourMarker, ourDot);				
				System.out.println("Intersection: (" + intersect.getX() + ", " + intersect.getY() + ")");

				Point2D_I32 point = new Point2D_I32((int) intersect.getX(), (int) intersect.getY());
					
				double distance = DistanceCalculator.Distance(ObjectLocations.getUSAttack(), point);
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
						e.printStackTrace();
					}
				}
				
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
