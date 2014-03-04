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
		//Turn 90 degree to horizontal
		//Calculate intersection between defender marker and attaker marker line with our robot
		//Move to intersection point
	}

}
