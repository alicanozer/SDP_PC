package strategy.planning;

import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import Calculations.Intersection;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import movement.RobotMover;

public class BlockShot {

	public static void blockShot(String type, RobotMover robotMover) {

		double angle;
		
		while (BallPossession.hasPossession(RobotType.AttackThem, ObjectLocations.getTHEMAttack())) {

			angle = TurnToObject.alignHorizontal(RobotType.DefendUs);
			
			if(ObjectLocations.getUSDefend() != null && ObjectLocations.getUSDefendDot() != null){

				if (angle > 20) {
					robotMover.rotate(type, angle);
				}
								
				Point2D_I32 ourDot;
				Point2D_I32 ourMarker;
				Point2D_I32 theirDot;
				Point2D_I32 theirMarker;
				Point2D_I32 moveToCentreGoal;
				
				ourDot = ObjectLocations.getUSDefendDot();
				ourMarker = ObjectLocations.getUSDefend();
				
				if (ObjectLocations.getYellowUs()) {
					// We are Yellow
					if (ObjectLocations.getYellowDefendingLeft()) {
						//Defending Left
						moveToCentreGoal = new Point2D_I32(ourMarker.x, ObjectLocations.getConsts().getLeftGoalCentre().y); 
					} else {
						//Defending Right
						moveToCentreGoal = new Point2D_I32(ourMarker.x, ObjectLocations.getConsts().getRightGoalCentre().y); 						
					}
				}else {
					//We are BLUE
					if (ObjectLocations.getYellowDefendingLeft()) {
						//Defending Right
						moveToCentreGoal = new Point2D_I32(ourMarker.x, ObjectLocations.getConsts().getRightGoalCentre().y); 						
					} else {
						//Defending left
						moveToCentreGoal = new Point2D_I32(ourMarker.x, ObjectLocations.getConsts().getLeftGoalCentre().y); 											
					}
				}
				
				double distanceGoal = DistanceCalculator.Distance(ourMarker, moveToCentreGoal);
				
				if (ourDot.y < moveToCentreGoal.y) {
					robotMover.forward(type, distanceGoal);
				} else {
					robotMover.forward(type, distanceGoal);					
				}
				
				while (robotMover.numQueuedJobs() > 0) {
					//DO NOTHING
				}
				
				theirDot = ObjectLocations.getTHEMAttackDot();
				theirMarker = ObjectLocations.getTHEMAttack();

				Vector intersect = Intersection.IntersectionRobots(theirMarker, theirDot, ourMarker, ourDot);
				
//				System.out.println("Our Robot: (" + ourMarker.x + ", " + ourMarker.y + ")");
//				System.out.println("Their Robot: (" + theirMarker.x + ", " + theirMarker.y + ")");
				System.out.println("Intersection: (" + intersect.getX() + ", " + intersect.getY() + ")");

				Point2D_I32 point = new Point2D_I32((int) intersect.getX(), (int) intersect.getY());
				
				double distance = DistanceCalculator.Distance(ObjectLocations.getUSDefend(), point);
				System.out.println("Distance to Point: " + distance);
				
				if (point != null ) {
					if (point.y > 69 && point.y < 209) {
						if (point.y < ObjectLocations.getUSDefendDot().y) {
							robotMover.forward(type, -distance);
						} else {
							robotMover.forward(type, distance);
						}
					} else {
						robotMover.stopRobot(type);
						while (robotMover.numQueuedJobs() > 0) {
							//Do Nothing
						}
					}
				} 
				
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			while (robotMover.numQueuedJobs() > 0) {
				//DO NOTHING
			}
			
		}
	}

}
