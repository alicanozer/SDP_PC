package strategy.movement;

import vision.ObjectLocations;
import world.RobotType;
import movement.RobotMover;
import Calculations.DistanceCalculator;
import comms.BluetoothRobot;
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
	
	public static void moveOurAttackToBall(String type, RobotMover robotMover) throws Exception {
		
		double angle;
		
		// Turn to ball
		if (type.equals("attack")) {
			angle = TurnToObject.Ball(RobotType.AttackUs);
		} else {
			angle =  TurnToObject.Ball(RobotType.DefendUs);
		}
		
		robotMover.rotate(type, (int) angle+3);
		System.out.println("Angle to Ball:" + angle);										

		//Move to Ball
		double distance = DistanceCalculator.Distance(ObjectLocations.getUSAttack(), ObjectLocations.getBall());
		System.out.println("Distance to Ball:" + distance);
		robotMover.forward(type, distance - 19);
		
	}
	
}









