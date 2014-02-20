package Calculations;

import georegression.struct.point.Point2D_I32;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import World.RobotType;
import World.WorldState;

public class BallPossession {
	
	public static boolean hasPossession(RobotType type) {
		
		boolean possession = false;
		
		int possessionThreshold = 50;
		
		double angle = 0.0;
		
		Point2D_I32 ball = ObjectLocations.getBall();
		Point2D_I32 robot;
		
		//Currently only works if they are blue and we are yellow.
		if(type == RobotType.AttackThem) {
			robot = ObjectLocations.getBlueATTACKmarker();
		}
		if(type == RobotType.DefendThem) {
			robot = ObjectLocations.getBlueDEFENDmarker();
		}
		if(type == RobotType.AttackUs) {
			robot = ObjectLocations.getYellowATTACKmarker();
		}
		else { //else (type == RobotType.DefendUs)
			robot = ObjectLocations.getYellowDEFENDmarker();
		}
		
		try {
			angle = TurnToObject.getDirection(robot, ball);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if the robot is near the ball and facing the ball it has possession
		if ((DistanceCalculator.Distance(robot.x,robot.y,ball.x,ball.y) < possessionThreshold)
				&& angle < 180) {
				possession = true;
			}
		return possession;
	}

}
