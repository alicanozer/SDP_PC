package strategy.planning;

import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import movement.RobotMover;

public class PassingDefender {

	public static void passingDefender(String type, RobotMover robotMover) throws InterruptedException {
		
		robotMover.resetQueue(type);

		while (BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend())) {
			
			try {
				
				System.out.println("Number of commands in queue: " + robotMover.numQueuedJobs());
				
				MoveToPointXY.moveRobotToBall(type, robotMover);
				//Grab the ball
				robotMover.grab(type);
				
				//Find Angle to Opponent Attacker
				double angleThemAttacker = TurnToObject.OppenentAttacker(RobotType.DefendUs);
				
				double angleTurn;
				
				//Find angle to turn before passing
				if (Math.abs(angleThemAttacker) < 15)  {
					if (angleThemAttacker < 0) {
						angleTurn = angleThemAttacker - 30;
					} else {						
						angleTurn = angleThemAttacker + 30;
					}
				} else if (Math.abs(angleThemAttacker) > 160) {
					if (angleThemAttacker < 0) {
						angleTurn = -90;
					} else {						
						angleTurn = 90;
					}
				} else {
					angleTurn = angleThemAttacker/2;
				}
				
				System.out.println("Defender Angle to Pass: " + angleTurn);
				//Rotate robot to an angle such that it is not intercepted by the attacker
				robotMover.rotate(type, angleTurn);
				
				//Pass the ball
				System.out.println("trying to kick");
				robotMover.kick(type);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
				robotMover.resetQueue(type);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		

	}
	
}
