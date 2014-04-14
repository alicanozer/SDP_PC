package strategy.planning;

import movement.RobotMover;
import comms.BluetoothRobot;

public class DefendD extends StrategyInterface {

	public DefendD(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Defence Defender Strategy when their defender has the ball...");
	}

}

