package strategy.movement;

import georegression.struct.point.Point2D_I32;
import comms.BluetoothRobot;

public class MoveToPoint {
	
	private static final int distanceFromPointToStop = 20;

	public static void moveToPoint(BluetoothRobot bRobot, Point2D_I32 marker, Point2D_I32 dot, double pointX, double pointY) throws InterruptedException{
		
		double distance = DistanceToBall.Distance(marker.x, marker.y, pointX, pointY);
		System.out.println(String.format("Distance to point is %f", distance));
		Point2D_I32 pointXY = new Point2D_I32((int)pointX,(int)pointY);
		
		double angle = TurnToObject.getAngleToObject(dot, marker, pointXY);
		System.out.println(String
				.format("Angle of point to robot is %f", angle));

		if (Math.abs(angle) > 20) {
			// Turn to angle required
			System.out.println("Stop and turn");
			bRobot.stop();
			bRobot.rotateLEFT((int)angle);
		}

		while (distance > distanceFromPointToStop) {

			angle = TurnToObject.getAngleToObject(dot, marker, pointXY);

			if ((Math.abs(angle) > 15) && (Math.abs(angle) < 50)) {
				// Stop everything and turn
				System.out.println("The final angle is " + angle);
				bRobot.stop();
				bRobot.rotateLEFT((int) (angle / 2));
			} else if (Math.abs(angle) > 50) {
				bRobot.stop();
				bRobot.rotateLEFT((int) angle);
			}
		
			distance = DistanceToBall.Distance(marker.x, marker.y, pointX, pointY);
			System.out.println("Distance to ball: " + distance);
			Thread.sleep(100);
		}

		// Being close to the ball we can perform one last minor turn
		angle = TurnToObject.getAngleToObject(dot, marker, pointXY);
		if (Math.abs(angle) > 15) {
			// Stop everything and turn
			System.out.println("Making final correction");
			bRobot.stop();
			bRobot.rotateLEFT((int) angle);
		} else {
			bRobot.stop();
		}
		if (distance > distanceFromPointToStop) {
			moveToPoint(bRobot, marker, dot, pointX, pointY);
		}
		
	}
	
}
