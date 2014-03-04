package strategy.planning;

import movement.RobotMover;

public class DefendPassA extends StrategyInterface{

	public DefendPassA(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Starting DefendPassA (Defending against a pass with the attacker)...");
	}

}
