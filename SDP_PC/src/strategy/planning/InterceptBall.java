package strategy.planning;

import movement.RobotMover;
import Calculations.BallPossession;
import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import georegression.struct.point.Point2D_I32;

public class InterceptBall{
	
	public static void intercept(String type, RobotMover robotMover) throws InterruptedException{
		
		try {
			robotMover.resetQueue();
			robotMover.stopRobot(type);
			robotMover.interruptMove();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(!BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend())){

			if(
					ObjectLocations.getBall() != null 
					&& ObjectLocations.getUSDefend() != null 
					&& ObjectLocations.getUSDefendDot() != null){
// TURN
				Point2D_I32 point = new Point2D_I32(ObjectLocations.getUSDefend().x, ObjectLocations.getBall().y);
				double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);
//				System.out.println("Angle to parrallel with goal: " + angle);
				if(Math.abs(angle) < 160 && Math.abs(angle) > 20){
//					System.out.println("correcting angle!!!!!!!!!!!!!!!");
					robotMover.stopRobot(type);
					robotMover.resetQueue();
					robotMover.rotate(type, angle);
				}

// MOVE
//				System.out.println("distance: " + Math.abs(ObjectLocations.getBall().y - ObjectLocations.getUSDefend().y));
				if(Math.abs(ObjectLocations.getBall().y - ObjectLocations.getUSDefend().y) > 10){
					if(type.equals("defence")){
						MoveToPointXY.moveAwayDefence(type, robotMover);
					}
					try {
						MoveToPointXY.moveRobotToBlockCont(type, robotMover);
					} catch (Exception e) {
					}
				}
				else {
//					System.out.println("WE ARE CLOSE TO THE BALL, STOP!");
					robotMover.stopRobot(type);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		robotMover.stopRobot(type);
	}
}


