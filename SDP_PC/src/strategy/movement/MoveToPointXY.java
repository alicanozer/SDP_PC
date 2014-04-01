package strategy.movement;

import vision.ObjectLocations;
import vision.PointUtils;
import world.RobotType;
import movement.RobotMover;
import Calculations.DistanceCalculator;
import Calculations.GoalInfo;
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

		distance = 15;
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
	
	public static void moveRobotToBlockCont(String type, RobotMover robotMover) throws Exception {
			
		boolean flag = false;
		
		if (ObjectLocations.getYellowUs()) {
			//We are YELLOW
			if (ObjectLocations.getYellowDefendingLeft()) {
				//Defending LEFT
				if (ObjectLocations.getBall().y > GoalInfo.getLeftGoalTopNew().y || ObjectLocations.getBall().y < GoalInfo.getLeftGoalBottomNew().y) {
					//Add code
					flag = true;
				}
			} else {
				//Defending RIGHT
				if (ObjectLocations.getBall().y > GoalInfo.getRightGoalTopNew().y || ObjectLocations.getBall().y < GoalInfo.getRightGoalBottomNew().y) {
					//Add code
					flag = true;
				}
			}
		} else {
			//We are BLUE
			if (ObjectLocations.getYellowDefendingLeft()) {
				//Defending LEFT
				if (ObjectLocations.getBall().y > GoalInfo.getLeftGoalTopNew().y || ObjectLocations.getBall().y < GoalInfo.getLeftGoalBottomNew().y) {
					//Add code
					flag = true;
				}
			} else {
				//Defending RIGHT
				if (ObjectLocations.getBall().y > GoalInfo.getRightGoalTopNew().y || ObjectLocations.getBall().y < GoalInfo.getRightGoalBottomNew().y) {
					//Add code
					flag = true;
				}
			}
		}
		
		if (flag) {
			if (type.equals("attack")) {
				if (ObjectLocations.getUSAttackDot().y > ObjectLocations.getUSAttack().y) {
					if (ObjectLocations.getBall().y > ObjectLocations.getUSAttack().y) {
						robotMover.backwardsC(type);
					} else {				
						robotMover.forwardsC(type);
					}
				} else {
					if (ObjectLocations.getBall().y > ObjectLocations.getUSAttack().y) {
						robotMover.forwardsC(type);
					} else {				
						robotMover.backwardsC(type);
					}
				}
			} else {
				if (ObjectLocations.getUSDefendDot().y > ObjectLocations.getUSDefend().y) {
					if (ObjectLocations.getBall().y > ObjectLocations.getUSDefend().y) {
						robotMover.backwardsC(type);
					} else {				
						robotMover.forwardsC(type);
					}
				} else {
					if (ObjectLocations.getBall().y > ObjectLocations.getUSDefend().y) {
						robotMover.forwardsC(type);
					} else {				
						robotMover.backwardsC(type);
					}
				}
			}
		}
		
	}
	
	
	public static void moveAwayDefence(String type, RobotMover robotMover){
		if(ObjectLocations.getYellowUs()){
			//We are yellow
			if(ObjectLocations.getYellowDefendingLeft()){
				//Defending LEFT
				if(DistanceCalculator.Distance(ObjectLocations.getUSDefend(),new Point2D_I32(ObjectLocations.getConsts().getRegion12X(),ObjectLocations.getUSDefend().y)) < 6){
					Point2D_I32 point = new Point2D_I32(ObjectLocations.getConsts().getRegion12X(),ObjectLocations.getUSDefend().y);
					double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);
					System.out.println("Angle to parrallel with goal: " + angle);
					if(Math.abs(angle) < 160 && Math.abs(angle) > 20){
						robotMover.rotate("defence", angle);
					}
					if (ObjectLocations.getUSDefendDot().x < ObjectLocations.getUSDefend().x) {
						//Move Backward
						robotMover.forward(type, -15);
					} else {
						//Move Forward
						robotMover.forward(type, 15);
					}
				}
			} else {
				// Defending Right
				if(DistanceCalculator.Distance(ObjectLocations.getUSDefend(),new Point2D_I32(ObjectLocations.getConsts().getRegion34X(),ObjectLocations.getUSDefend().y)) < 12){
					Point2D_I32 point = new Point2D_I32(ObjectLocations.getConsts().getRegion34X(),ObjectLocations.getUSDefend().y);
					double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);
					System.out.println("Angle to parrallel with goal: " + angle);
					if(Math.abs(angle) < 160 && Math.abs(angle) > 20){
						robotMover.rotate("defence", angle);
					}
					if (ObjectLocations.getUSDefendDot().x < ObjectLocations.getUSDefend().x) {
						//Move Backward
						robotMover.forward(type, -15);
					} else {
						//Move Forward
						robotMover.forward(type, 15);
					}
				
				}
			} 
		} else {
			//We are blue
			if(!ObjectLocations.getYellowDefendingLeft()){
				// Defending LEFT
				if(DistanceCalculator.Distance(ObjectLocations.getUSDefend(),new Point2D_I32(ObjectLocations.getConsts().getRegion12X(),ObjectLocations.getUSDefend().y)) < 12){
					Point2D_I32 point = new Point2D_I32(ObjectLocations.getConsts().getRegion12X(),ObjectLocations.getUSDefend().y);
					double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);
					System.out.println("Angle to parrallel with goal: " + angle);
					if(Math.abs(angle) < 160 && Math.abs(angle) > 20){
						robotMover.rotate("defence", angle);
					}
					if (ObjectLocations.getUSDefendDot().x > ObjectLocations.getUSDefend().x) {
						//Move Backward
						robotMover.forward(type, -15);
					} else {
						//Move Forward
						robotMover.forward(type, 15);
					}
				}
			} else {
				// Defending RIGHT
				System.out.println("We are blue and defending right");
				if(DistanceCalculator.Distance(ObjectLocations.getUSDefend(),new Point2D_I32(ObjectLocations.getConsts().getRegion34X(),ObjectLocations.getUSDefend().y)) < 12){
					Point2D_I32 point = new Point2D_I32(ObjectLocations.getConsts().getRegion34X(),ObjectLocations.getUSDefend().y);
					double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);
					System.out.println("Angle to parrallel with goal: " + angle);
					if(Math.abs(angle) < 160 && Math.abs(angle) > 20){
						robotMover.rotate("defence", angle);
					}
					if (ObjectLocations.getUSDefendDot().x > ObjectLocations.getUSDefend().x) {
						//Move Backward
						robotMover.forward(type, -15);
					} else {
						//Move Forward
						robotMover.forward(type, 15);
					}
				
				}
			}
		}
	}
	
	private static boolean isLeft(Point2D_I32 a, Point2D_I32 b, Point2D_I32 c){
		return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) > 0;
	}

}









