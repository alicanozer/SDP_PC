package strategy.planning;

import java.awt.Polygon;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import world.RobotType;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import movement.RobotMover;

public class ShootAttacker {

	public static void shootAttacker(String type, RobotMover robotMover) {
		
		while (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getUSAttack())) {
			
			if (ObjectLocations.getUSAttack() != null && ObjectLocations.getBall() != null && ObjectLocations.getUSAttackDot() != null) {

				System.out.println(robotMover.numQueuedJobs());
    			robotMover.setSpeed("attack", 8);
				robotMover.setRotateSpeed("attack", 100);
				
				Polygon polygon = null;
				
				if (ObjectLocations.getYellowDefendingLeft()) {
					//Shooting Right
					polygon = PitchConstants.getRegion2();
				} else {
					//Shooting Left
					polygon = PitchConstants.getRegion3();
				}
				
				if (ObjectLocations.getBall() != null && BallPossession.BallRegion(ObjectLocations.getBall(), polygon)) {
					
					double angle = 0.0;
					double distance = 0.0;
					
					try {
						angle = TurnToObject.Ball(RobotType.AttackUs);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (Math.abs(angle) > 12) {
						robotMover.rotate("attack", angle);
					}

					distance = DistanceCalculator.Distance(ObjectLocations.getUSAttack(), ObjectLocations.getBall());
					
					if (distance > 17 && distance < 25) {
						robotMover.setSpeed("attack", 5);
						robotMover.forward("attack", distance - 14);
						robotMover.stopRobot("attack");
					} else if (distance > 25) {
						robotMover.forward("attack", distance - 22);
						robotMover.stopRobot("attack");						
					} else {
						robotMover.stopRobot("attack");
					}
					
					robotMover.grab("attack");
					while (robotMover.numQueuedJobs() > 0) {
						
					}
					
					robotMover.setSpeed("attack", 8);
					robotMover.setRotateSpeed("attack", 100);
					
					if (ObjectLocations.getUSAttack() != null && ObjectLocations.getBall() != null) {
						distance = DistanceCalculator.Distance(ObjectLocations.getUSAttack(), ObjectLocations.getBall());
						System.out.println("Distance to Ball after grabbing: " + distance);
					} else {
						distance = 0.0;
					}
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if (distance < 17 && distance != 0.0) {
						
						double angleShoot = TurnToObject.shootAngle();
						
						if (Math.abs(angleShoot) > 10) {
							robotMover.rotate("attack", angleShoot);
						}
						
						System.out.println("Angle to Shoot: " + angleShoot);
						
					}
					
					robotMover.kick("attack");
					
				}

				while (robotMover.numQueuedJobs() > 0) {
					//DO NOTHING
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					System.out.println("Got Here");
				}
				
			}
					
		}		

	}
	
}
