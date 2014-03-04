package strategy.planning;

import movement.RobotMover;

public class DefendShootD extends StrategyInterface{

	public DefendShootD(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Starting DefendShootD (Defending against a shoot with our defender)...");
	}

}