package Calculations;

import georegression.struct.point.Point2D_I32;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import World.RobotType;

public class BallPossession {
	
	public static boolean hasPossession(RobotType type, Point2D_I32 robotMarker) {
		
		boolean possession = false;
		
		int possessionThreshold = 50;
		Point2D_I32 temp = ObjectLocations.getBall();
		// TODO - add sleep
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double distanceBall = DistanceCalculator.Distance(temp, ObjectLocations.getBall());
		
		double speed = (distanceBall/0.01);
		
		//if the robot is near the ball and facing the ball it has possession
		if (DistanceCalculator.Distance(robotMarker, ObjectLocations.getBall()) < possessionThreshold) { 
			possession = true;
		} else if (speed < 10 && DistanceCalculator.Distance(robotMarker, ObjectLocations.getBall()) < 70) {
			possession = true;;
		}
		return possession;
	}

}
