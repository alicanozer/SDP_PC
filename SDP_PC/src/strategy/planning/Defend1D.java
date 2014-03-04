package strategy.planning;

import comms.BluetoothRobot;

public class Defend1D extends StrategyInterface {

	public Defend1D(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Defence Defender Strategy when their attacker has the ball...");
	}

}

