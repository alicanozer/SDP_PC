package strategy.planning;

import movement.RobotMover;
import comms.BluetoothRobot;

public class PassA extends StrategyInterface {

	public PassA(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Pass Attacker Strategy...");
	}

}
