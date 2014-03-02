package strategy.planning;

import World.RobotType;
import Calculations.BallPossession;
import strategy.movement.MoveToPoint;
import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import comms.BluetoothRobot;

public class Attack extends StrategyInterface {

	public Attack(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!shouldidie && !Strategy.alldie) {
			if (ObjectLocations.getYellowUs()) {
				if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getYellowATTACKmarker())) {
									
					MoveToPointXY.moveToPointXY(attackRobot, ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBall());
					//Grab Ball
					double angleShoot = TurnToObject.shootAngle();
					System.out.println("Angle to Shoot: " + angleShoot);
					attackRobot.rotateLEFT("attack", (int) angleShoot);
					//Kick Ball
					
				}
			}
		}
	}

}




