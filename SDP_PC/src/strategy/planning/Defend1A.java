package strategy.planning;

import movement.RobotMover;
import comms.BluetoothRobot;

public class Defend1A extends StrategyInterface {

	public Defend1A(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Defence Attacker Strategy when their attacker has the ball...");
	}

}
