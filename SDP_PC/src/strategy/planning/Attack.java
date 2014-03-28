package strategy.planning;

import movement.RobotMover;
import Calculations.BallPossession;
import strategy.movement.MoveToPoint;
import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import comms.BluetoothRobot;

public class Attack extends StrategyInterface {

	public Attack(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!shouldidie && !Strategy.alldie) {
			if (ObjectLocations.getYellowUs()) {
				if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getYellowATTACKmarker())) {
									
					MoveToPointXY.moveToPointXY("attack", attackMover, ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBall());
					
					//Grab Ball
					double angleShoot = TurnToObject.shootAngle();
					System.out.println("Angle to Shoot: " + angleShoot);
					attackMover.rotate("attack", (int) angleShoot);
					//Kick Ball
					
				}
			}
		}
	}

}




