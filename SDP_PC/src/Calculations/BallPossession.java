package Calculations;

import georegression.struct.point.Point2D_I32;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import World.RobotType;

public class BallPossession {
	
	public static boolean hasPossession(RobotType type, Point2D_I32 robotMarker) {
		
		boolean possession = false;
		
		int possessionThreshold = 50;
		
		double angle = 0.0;
		
		try {
			angle = TurnToObject.Ball(type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if the robot is near the ball and facing the ball it has possession
		if ((DistanceCalculator.Distance(robotMarker, ObjectLocations.getBall()) < possessionThreshold)
				&& angle < 180) {
				possession = true;
			}
		return possession;
	}

}
