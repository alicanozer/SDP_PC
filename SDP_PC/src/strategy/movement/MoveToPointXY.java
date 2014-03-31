package strategy.movement;

import vision.ObjectLocations;
import world.RobotType;
import movement.RobotMover;
import Calculations.DistanceCalculator;
import georegression.struct.point.Point2D_I32;

public class MoveToPointXY {

	private static final int distanceFromPointToStop = 5;

	//	public static void moveToPointXY(String type, RobotMover robotMover, Point2D_I32 dot, Point2D_I32 marker, Point2D_I32 point) throws Exception {
	//		
	//		double distance = DistanceCalculator.Distance(marker, point) - distanceFromPointToStop;
	//		
	//		double angle;
	//		
	//		// Initially turn to ball
	//		if (type.equals("attack")) {
	//			angle = TurnToObject.Point(RobotType.AttackUs);
	//		} else {
	//			angle =  TurnToObject.Ball(RobotType.DefendUs);
	//		} 
	//
	//		robotMover.rotate(type, (int) angle+5);
	//		System.out.println("Angle to Point:" + angle);										
	//						
	//	}

	public static void moveRobotToPoint(String type, RobotMover robotMover, Point2D_I32 point) {

		double angle;
		double distance;

		// Turn to ball
		if (type.equals("attack")) {
			angle = TurnToObject.getAngleToObject(ObjectLocations.getUSAttackDot(), ObjectLocations.getUSAttack(), point);		
		} else {
			angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);					
		}

		robotMover.rotate(type, angle);
		System.out.println("Angle to Point: " + angle);

		if (type.equals("attack")) {
			distance = DistanceCalculator.Distance(ObjectLocations.getUSAttack(), point);
		} else {
			distance = DistanceCalculator.Distance(ObjectLocations.getUSDefend(), point);			
		}

		System.out.println("Distance to Point: " + distance);

		if (type.equals("attack")) {
			if (ObjectLocations.getUSAttack().y > ObjectLocations.getBall().y) {
				robotMover.forward(type, distance);
			} else {				
				robotMover.forward(type, -distance);
			}
		} else {
			if (ObjectLocations.getUSDefend().y > ObjectLocations.getBall().y) {
				robotMover.forward(type, distance);
			} else {				
				robotMover.forward(type, -distance);
			}
		}

	}

	public static void moveRobotToBall(String type, RobotMover robotMover) throws Exception {

		double angle;

		// Turn to ball
		angle = TurnToObject.Ball(RobotType.AttackUs);

		robotMover.rotate(type, (int) angle+3);
		System.out.println("Angle to Ball:" + angle);										

		//Move to Ball
		double distance = DistanceCalculator.Distance(ObjectLocations.getUSAttack(), ObjectLocations.getBall());
		System.out.println("Distance to Ball:" + distance);
		robotMover.forward(type, distance - 19);

	}

	public static void moveRobotToBlock(String type, RobotMover robotMover) throws Exception {

		double distance;

		//Move to Block Ball
		if (type.equals("attack")) {
			distance = DistanceCalculator.DistanceQuadruple(ObjectLocations.getUSAttack().x, ObjectLocations.getUSAttack().y, ObjectLocations.getUSAttack().x, ObjectLocations.getBall().y);
		} else {
			distance = DistanceCalculator.DistanceQuadruple(ObjectLocations.getUSDefend().x, ObjectLocations.getUSDefend().y, ObjectLocations.getUSDefend().x, ObjectLocations.getBall().y);
		}

		System.out.println("Distance to Block Point:" + distance);
				
		if (!(distance < 10)) {
			//Check if ball is left or right of robot
			if (type.equals("attack")) {
				if (ObjectLocations.getUSAttackDot().y > ObjectLocations.getUSAttack().y) {
					if (ObjectLocations.getBall().y > ObjectLocations.getUSAttack().y) {
						robotMover.forward(type, -distance);
					} else {				
						robotMover.forward(type, distance);
					}
				} else {
					if (ObjectLocations.getBall().y > ObjectLocations.getUSAttack().y) {
						robotMover.forward(type, distance);
					} else {				
						robotMover.forward(type, -distance);
					}
				}
			} else {
				if (ObjectLocations.getUSDefendDot().y > ObjectLocations.getUSDefend().y) {
					if (ObjectLocations.getBall().y > ObjectLocations.getUSDefend().y) {
						robotMover.forward(type, -distance);
					} else {				
						robotMover.forward(type, distance);
					}
				} else {
					if (ObjectLocations.getBall().y > ObjectLocations.getUSDefend().y) {
						robotMover.forward(type, distance);
					} else {				
						robotMover.forward(type, -distance);
					}
				}
			}
		}
				
	}

	private static boolean isLeft(Point2D_I32 a, Point2D_I32 b, Point2D_I32 c){
		return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) > 0;
	}

}









