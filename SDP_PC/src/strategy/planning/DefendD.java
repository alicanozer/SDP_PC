package strategy.planning;

import comms.BluetoothRobot;

public class DefendD extends StrategyInterface {

	public DefendD(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Defence Defender Strategy when their defender has the ball...");
	}

}

