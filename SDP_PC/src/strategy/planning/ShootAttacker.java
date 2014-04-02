package strategy.planning;

import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import movement.RobotMover;

public class ShootAttacker {

	public static void shootAttacker(String type, RobotMover robotMover) {
		
		while (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getUSAttack())) {
			
			try {
				
				MoveToPointXY.moveRobotToBall(type, robotMover);
				System.out.println("this will be printed many times");
				//Grab the ball
				robotMover.grab(type);
				
				//Find Angle to Opponent Attacker
				double goal = TurnToObject.shootAngle(); // .OppenentAttacker(RobotType.AttackUs);
				
				//Rotate robot to an angle such that it is not intercepted by the attacker
				robotMover.rotate(type, goal);
				
				//Pass the ball
				System.out.println("trying to score!");
				robotMover.kick(type);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		

	}
	
}
