package strategy.planning;

import java.awt.Polygon;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import world.RobotType;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import movement.RobotMover;

public class PassingDefender {

	public static void passingDefender(String type, RobotMover robotMover) throws InterruptedException {

		while (BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend())) {

			if (ObjectLocations.getUSDefend() != null && ObjectLocations.getUSDefendDot() != null && ObjectLocations.getBall() != null) {

				System.out.println(robotMover.numQueuedJobs());
				robotMover.setSpeed(type, 8);
				robotMover.setRotateSpeed(type, 100);

				Polygon polygon = null;

				if (ObjectLocations.getYellowDefendingLeft()) {
					//Shooting Right
					polygon = PitchConstants.getRegion4();
				} else {
					//Shooting Left
					polygon = PitchConstants.getRegion1();
				}

				if (ObjectLocations.getBall() != null && BallPossession.BallRegion(ObjectLocations.getBall(), polygon)) {

					double angle = 0.0;
					double distance = 0.0;

					try {
						angle = TurnToObject.Ball(RobotType.DefendUs);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (Math.abs(angle) > 12) {
						robotMover.rotate(type, angle);
					}

					distance = DistanceCalculator.Distance(ObjectLocations.getUSDefend(), ObjectLocations.getBall());

					if (distance > 14 && distance < 25) {
						robotMover.setSpeed(type, 5);
						robotMover.forward(type, distance - 12);
						robotMover.stopRobot(type);
					} else if (distance > 25) {
						robotMover.forward(type, distance - 19);
						robotMover.stopRobot(type);						
					} else {
						robotMover.stopRobot(type);
					}

					robotMover.grab(type);
					while (robotMover.numQueuedJobs() > 0) {

					}

					robotMover.setSpeed(type, 8);
					robotMover.setRotateSpeed(type, 100);

					if (ObjectLocations.getUSDefend() != null && ObjectLocations.getBall() != null) {
						distance = DistanceCalculator.Distance(ObjectLocations.getUSDefend(), ObjectLocations.getBall());
						System.out.println("Distance to Ball after grabbing: " + distance);
					} else {
						distance = 0.0;
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (distance < 13 && distance != 0.0) {

						double angleOurAttacker = 0.0;
						
						if (ObjectLocations.getUSDefend() != null) {
							
						try {
							angleOurAttacker = TurnToObject.Teammate(RobotType.DefendUs);
						} catch (Exception e) {
							e.printStackTrace();
						}			

						System.out.println("Defender Angle to Pass: " + angleOurAttacker);
						//Rotate robot to an angle such that it is not intercepted by the attacker
						robotMover.rotate(type, angleOurAttacker);
						
						} else {
							robotMover.stopRobot(type);
						}
						
					}

					double angleToOwnGoal = 0.0;
					
					if (ObjectLocations.getYellowUs()) {
						//We are Yellow
						if (ObjectLocations.getYellowDefendingLeft()) {
							//defending left
							angleToOwnGoal = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), ObjectLocations.getConsts().getLeftGoalCentre());
						} else {
							//defending right
							angleToOwnGoal = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), ObjectLocations.getConsts().getRightGoalCentre());							
						}
					} else {
						//We are Blue
						if (ObjectLocations.getYellowDefendingLeft()) {
							//defending right
							angleToOwnGoal = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), ObjectLocations.getConsts().getRightGoalCentre());
						} else {
							//defending left
							angleToOwnGoal = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), ObjectLocations.getConsts().getLeftGoalCentre());							
						}
					}
					
					if (Math.abs(angleToOwnGoal) > 90) {
						robotMover.kick(type);
					}


				}

				while (robotMover.numQueuedJobs() > 0) {
					//DO NOTHING
				}

			}

		}

	}
}