package strategy.planning;

import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;
import world.RobotType;
import movement.RobotMover;
import Calculations.AngleCalculator;
import strategy.movement.MoveToPointXY;
import Calculations.BallPossession;

public class Pass {

	public static void passing(RobotMover mover, Point2D_I32 ball, String team) throws Exception {
		
		
		
		boolean task = false;
		MoveToPointXY.moveRobotToBall("defence", mover);
		while((BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend()))) {
			
			if (Math.abs(ObjectLocations.getBall().getX() - ball.getX()) < 30 && 
					Math.abs(ObjectLocations.getBall().getY() - ball.getY()) < 30) {}
			
		}
		
	}
	
	public static void aim(RobotMover mover) {
		
		
		
	}
	
	public static void shoot(RobotMover mover) {
		
		
		
	}
	
	
	
}
