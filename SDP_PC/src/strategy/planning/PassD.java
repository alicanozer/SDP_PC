package strategy.planning;

import comms.BluetoothRobot;

public class PassD extends StrategyInterface {

	public PassD(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Pass Defender Strategy...");
	}

}

