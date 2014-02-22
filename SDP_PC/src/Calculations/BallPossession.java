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
		Point2D_I32 dot;
		
		//Currently only works if they are blue and we are yellow.
		if(type == RobotType.AttackThem) {
			robot = ObjectLocations.getBlueATTACKmarker();
			dot = ObjectLocations.getBlueATTACKdot();
		}
		if(type == RobotType.DefendThem) {
			robot = ObjectLocations.getBlueDEFENDmarker();
			dot = ObjectLocations.getBlueDEFENDdot();
		}
		if(type == RobotType.AttackUs) {
			robot = ObjectLocations.getYellowATTACKmarker();
			dot = ObjectLocations.getYellowATTACKdot();
		}
		else { //else (type == RobotType.DefendUs)
			robot = ObjectLocations.getYellowDEFENDmarker();
			dot = ObjectLocations.getYellowATTACKdot();
		}
		
		try {
			angle = TurnToObject.getAngleToObject(dot, robot, ball);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if the robot is near the ball and facing the ball it has possession
		if ((DistanceCalculator.Distance(robot,ball) < possessionThreshold)
				&& angle < 180) {
				possession = true;
			}
		return possession;
	}

}
