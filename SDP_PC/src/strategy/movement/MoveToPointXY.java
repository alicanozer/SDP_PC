package strategy.movement;

import vision.ObjectLocations;
import world.RobotType;
import movement.RobotMover;
import Calculations.DistanceCalculator;
import comms.BluetoothRobot;
import georegression.struct.point.Point2D_I32;

public class MoveToPointXY {

	private static final int distanceFromPointToStop = 5;
	
	public static void moveToPointXY(String type, RobotMover robotMover, Point2D_I32 dot, Point2D_I32 marker, Point2D_I32 point) throws Exception {
		
		double distance = DistanceCalculator.Distance(marker, point) - distanceFromPointToStop;
		
		double angle;
		
		// Initially turn to ball
		if (type.equals("attack")) {
			angle = TurnToObject.Ball(RobotType.AttackUs);
		} else {
			angle =  TurnToObject.Ball(RobotType.DefendUs);
		}
		robotMover.rotate(type, (int) angle+5);
		robotMover.waitForCompletion();
		System.out.println("Angle to Point:" + angle);										
		System.out.println("Distance to Point:" + distance);
		
		robotMover.setSpeedCoef(12);
		robotMover.forward(type,distance); // we tell it to start moving forward
		robotMover.waitForCompletion();
		
		// Move towards the ball till the distance is only the length of the robot to the ball
//		while (distance > distanceFromPointToStop) {
//			
//			double angle;
//			
//			//Turn to Ball
//			if (type.equals("attack")) {
//				angle = TurnToObject.Ball(RobotType.AttackUs);
//			} else {
//				angle =  TurnToObject.Ball(RobotType.DefendUs);
//			}
//			
//			robotMover.rotate(type, (int) angle+5);
//			System.out.println("Angle to Point:" + angle);										
//			System.out.println("Distance to Point:" + distance);
//			robotMover.setSpeedCoef(20);
//			robotMover.forward(type, distance);
////			defenseMover.forward("defence", distance);
//			robotMover.run();
//			robotMover.waitForCompletion();
//			System.out.println("All movements completed");
////			defenseMover.waitForCompletion();
//			
//		}
						
	}
public static void moveOurAttackToBall(String type, RobotMover robotMover) throws Exception {
		
		double distance = DistanceCalculator.Distance(ObjectLocations.getUSAttack(), ObjectLocations.getBall());
		
		double angle;
		
		// Initially turn to ball
		if (type.equals("attack")) {
			angle = TurnToObject.Ball(RobotType.AttackUs);
		} else {
			angle =  TurnToObject.Ball(RobotType.DefendUs);
		}
		robotMover.rotate(type, (int) angle+5);
		robotMover.waitForCompletion();
		System.out.println("Angle to Point:" + angle);										
		System.out.println("Distance to Point:" + distance);
		robotMover.setSpeed(type, 12);
		robotMover.forward(type, distance - 1);
		robotMover.waitForCompletion();
		
						
	}
	
}









