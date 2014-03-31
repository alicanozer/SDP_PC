package strategy.planning;

import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.BallPossession;
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
	}
}