package strategy.movement;

import Calculations.DistanceCalculator;
import comms.BluetoothRobot;
import georegression.struct.point.Point2D_I32;

public class MoveToPointXY {

	private static final int distanceFromPointToStop = 20;
	
	public static void moveToPointXY(BluetoothRobot robot, Point2D_I32 dot, Point2D_I32 marker, Point2D_I32 point) {
		
		double distance = DistanceCalculator.Distance(marker, point);
									
		// Move towards the ball till the distance is only the length of the robot to the ball
		while (distance > distanceFromPointToStop) {
			
			double angleToPoint = TurnToObject.getAngleToObject(dot, marker, point);
			
			//Rotate robot only if ball is at an angle greater than 15
			if (Math.abs(angleToPoint) > 15) {
				System.out.println("Angle to Point: " + angleToPoint);
				robot.stop("attack");
				robot.rotateLEFT("attack", (int) angleToPoint);
			}
			
			System.out.println("Distance to Point: " + distance);
			robot.forward("attack", distance);
		
		}
		
		robot.stop("attack");
		
	}
	
}









