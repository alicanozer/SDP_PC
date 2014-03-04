package strategy.planning;

import movement.RobotMover;

public class DefendShootA extends StrategyInterface{

	public DefendShootA(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Starting DefendShootA (Defending against a shoot with our attacker)...");
	}

}
