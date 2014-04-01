package strategy.planning;

import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import movement.RobotMover;

public class PassingDefender {

	public static void passingDefender(String type, RobotMover robotMover) {
		
		while (BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend())) {
			
			try {

				//Turn to the ball
				double angleBall = TurnToObject.Ball(RobotType.DefendUs);
				System.out.println("Defender Angle to Ball: " + angleBall);
				robotMover.rotate(type, angleBall);
				
				//Move to the ball
				double distance = DistanceCalculator.Distance(ObjectLocations.getUSDefend(), ObjectLocations.getBall());
				System.out.println("Defender Distance to Ball: " + distance);				
				robotMover.forward(type, distance);
				
				//Grab the ball
				robotMover.grab(type);
				
				//Find Angle to Opponent Attacker
				double angleThemAttacker = TurnToObject.OppenentAttacker(RobotType.DefendUs);
				System.out.println("Defender Angle to Attacker: " + angleBall);
				
				double angleTurn;
				
				//Find angle to turn before passing
				if (Math.abs(angleThemAttacker) < 15)  {
					if (angleThemAttacker < 0) {
						angleTurn = angleThemAttacker - 30;
					} else {						
						angleTurn = angleThemAttacker + 30;
					}
				} else if (Math.abs(angleThemAttacker) > 150) {
					if (angleThemAttacker < 0) {
						angleTurn = -90;
					} else {						
						angleTurn = 90;
					}
				} else {
					angleTurn = angleThemAttacker/2;
				}
				
				System.out.println("Defender Angle to Pass: " + angleBall);
				//Rotate robot to an angle such that it is not intercepted by the attacker
				robotMover.rotate(type, angleTurn);
				
				//Pass the ball
				robotMover.kick(type);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}		
		
	}
	
}
