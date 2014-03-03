package strategy.planning;

import comms.BluetoothRobot;

public class PassA extends StrategyInterface {

	public PassA(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Pass Attacker Strategy...");
	}

}
