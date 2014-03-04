package strategy.planning;

import movement.RobotMover;
import comms.BluetoothRobot;

public class AttackD extends StrategyInterface {

	public AttackD(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Attack Defender Strategy...");
	}

}
