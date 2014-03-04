package strategy.planning;

import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import Calculations.BallPossession;
import World.RobotType;
import movement.RobotMover;
import comms.BluetoothRobot;

public class AttackA extends StrategyInterface {

	public AttackA(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		System.out.println("Running Attack Attacker Strategy...");
		if (ObjectLocations.getYellowUs()) {
			if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getYellowATTACKmarker())) {
				//Move to Point
				//Grab Ball
				//Turn to open goal
				//Shoot Ball
				
				//TODO
				//Grab Ball
				//Turn to Open Goal
				//Wait
				//Turn the other way
				//Shoot
				MoveToPointXY.moveToPointXY(attackMover, ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBall());
				attackMover.grab("attack");
				double angleShoot = TurnToObject.shootAngle();
				System.out.println("Angle to Shoot: " + angleShoot);
				attackMover.rotate("attack", (int) angleShoot);
				attackMover.kick("attack");

			}
		}
	}
}