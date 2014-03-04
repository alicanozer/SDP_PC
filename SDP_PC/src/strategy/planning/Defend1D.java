package strategy.planning;

import movement.RobotMover;
import comms.BluetoothRobot;

public class Defend1D extends StrategyInterface {

	public Defend1D(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Defence Defender Strategy when their attacker has the ball...");
	}

}

