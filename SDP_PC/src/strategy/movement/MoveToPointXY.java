package strategy.movement;

import World.RobotType;
import movement.RobotMover;
import Calculations.DistanceCalculator;
import comms.BluetoothRobot;
import georegression.struct.point.Point2D_I32;

public class MoveToPointXY {

	private static final int distanceFromPointToStop = 20;
	
	public static void moveToPointXY(String type, RobotMover robot, Point2D_I32 dot, Point2D_I32 marker, Point2D_I32 point) throws InterruptedException {
		
		double distance = DistanceCalculator.Distance(marker, point);
									
		// Move towards the ball till the distance is only the length of the robot to the ball
		while (distance > distanceFromPointToStop) {
			
			double angleToPoint = TurnToObject.getAngleToObject(dot, marker, point);
			
			//Rotate robot only if ball is at an angle greater than 15
			if (Math.abs(angleToPoint) > 15) {
				System.out.println("Angle to Point: " + angleToPoint);
				robot.stopRobot(type);
				robot.rotate(type, (int) angleToPoint);
			}
			
			System.out.println("Distance to Point: " + distance);
			robot.forward(type, distance);
			Thread.sleep(100);
		}
		
		robot.stopRobot(type);
		
		}
	
}









