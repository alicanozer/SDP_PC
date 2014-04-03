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

		if(type.equals("defence")){
			while(!BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend())){
				if(
						ObjectLocations.getBall() != null 
						&& ObjectLocations.getUSDefend() != null 
						&& ObjectLocations.getUSDefendDot() != null){
					// TURN
					Point2D_I32 point = new Point2D_I32(ObjectLocations.getUSDefend().x, ObjectLocations.getBall().y);
					double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);

					if(Math.abs(angle) < 160 && Math.abs(angle) > 20){

						robotMover.stopRobot(type);
						robotMover.rotate(type, angle);
					}

					// MOVE
					if(ObjectLocations.getBall() != null && ObjectLocations.getUSDefend()!= null && 
							Math.abs(ObjectLocations.getBall().y - ObjectLocations.getUSDefend().y) > 10){
						if(type.equals("defence")){
							MoveToPointXY.moveAwayDefence(type, robotMover);
						}
						try {
							MoveToPointXY.moveRobotToBlockCont(type, robotMover);
						} catch (Exception e) {
						}
					}
					else {
						robotMover.stopRobot(type);
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			robotMover.stopRobot(type);
			robotMover.resetQueue(type);
		}
		else{
			while(!BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getUSAttack())){
				if(
						ObjectLocations.getBall() != null 
						&& ObjectLocations.getUSAttack() != null 
						&& ObjectLocations.getUSAttackDot() != null){
					// TURN
					Point2D_I32 point = new Point2D_I32(ObjectLocations.getUSAttack().x, ObjectLocations.getBall().y);
					double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSAttackDot(), ObjectLocations.getUSAttack(), point);

					if(Math.abs(angle) < 160 && Math.abs(angle) > 20){

						robotMover.stopRobot(type);
						robotMover.rotate(type, angle);
					}

					// MOVE
					if(Math.abs(ObjectLocations.getBall().y - ObjectLocations.getUSAttack().y) > 10){
						if(type.equals("defence")){
							MoveToPointXY.moveAwayAttack(type, robotMover);
						}
						try {
							MoveToPointXY.moveRobotToBlockCont(type, robotMover);
						} catch (Exception e) {
						}
					}
					else {
						robotMover.stopRobot(type);
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			robotMover.stopRobot(type);
			robotMover.resetQueue(type);
		}
	}
}




