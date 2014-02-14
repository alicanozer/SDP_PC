package strategy.planning;

import comms.RobotController;

import vision.ObjectLocations;
import World.RealWorld;


public class TestStrategy extends StrategyInterface{

//	private Robot ourAttackRobot;
//	private Robot ourDefenseRobot;
//	private Ball ball;
	
	ObjectLocations objs;
	
	public TestStrategy(RealWorld world, RobotController robot) {
		super(world, robot);
		
//		ball = new Ball();
//		ball.setPosition(new Vector(ObjectLocations.getBall().x,ObjectLocations.getBall().y));


		
		//need line to get our robot.
	}
	
	public void run() {
		while (!shouldidie && !Strategy.alldie) {
			
			
		}
	}
}
