package strategy.planning;

import geometry.Vector;
import movement.RobotMover;
import vision.ObjectLocations;
import World.WorldState;
import World.Ball;
import World.Robot;

public class TestStrategy extends StrategyInterface{

	private Robot ourAttackRobot;
	private Robot ourDefenseRobot;
	private Ball ball;
	
	ObjectLocations objs;
	
	public TestStrategy(RobotMover mover) {
		super(mover);
		
		ball = new Ball();
		ball.setPosition(new Vector(ObjectLocations.getBall().x,ObjectLocations.getBall().y));


		
		//need line to get our robot.
	}
	
	public void run() {
		while (!shouldidie && !Strategy.alldie) {
			
			
		}
	}
}
