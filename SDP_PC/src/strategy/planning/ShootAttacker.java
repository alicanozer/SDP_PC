package strategy.planning;

import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.BallPossession;
import movement.RobotMover;

public class ShootAttacker {

	public static void shootAttacker(String type, RobotMover robotMover) {
		
		while (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getUSAttack())) {
			
			try {
				
				MoveToPointXY.moveRobotToBall(type, robotMover);

				//Grab the ball
				robotMover.grab(type);
				
				//Find angle to goal
				double goal = TurnToObject.shootAngle();
				
				//Rotate to shoot
				robotMover.rotate(type, goal);
				
				//Kick the ball
				System.out.println("Trying to score!");
				robotMover.kick(type);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}		

	}
	
}
