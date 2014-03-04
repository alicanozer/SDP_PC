package strategy.planning;

import movement.RobotMover;

public class DefendPassD extends StrategyInterface{

	public DefendPassD(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Starting DefendPassD (Defending against a pass with the defender)...");
		//Align 90 degree to horizontal
		//Stay in the centre of the goal
	}

}