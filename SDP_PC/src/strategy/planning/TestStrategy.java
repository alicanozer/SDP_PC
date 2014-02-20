package strategy.planning;

import lejos.nxt.Button;
import vision.ObjectLocations;
import comms.BluetoothRobot;

import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import World.Ball;
import World.Robot;
import World.RobotType;
import World.WorldState;

public class TestStrategy extends StrategyInterface{

	private Robot ourAttackRobot;
	private WorldState world;
	private Ball ball;
	static BluetoothRobot bRobot;

	public TestStrategy(WorldState world, BluetoothRobot bRobot) {
		super(world, bRobot);
		ball = world.ball;
		ourAttackRobot = world.ourAttackRobot;
	}

	@Override
	public void run() {
		while (!shouldidie && !Strategy.alldie) {
			while(true) {

				if(ball!=null && ourAttackRobot!=null) {
					int x1 = (int) ourAttackRobot.x;
					int y1 = (int) ourAttackRobot.y;

					int x2 = (int) ball.x;
					int y2 = (int) ball.y;

					double distance = DistanceCalculator.Distance(x1, y1, x2, y2);
					System.out.println("distance: ");
					System.out.println(distance);
					System.out.println("ball: ");
					System.out.println(ball.x + " " + ball.y);				
					System.out.println(ObjectLocations.getBall());
					System.out.println("robot: ");
					System.out.println(ourAttackRobot.x + " " + ourAttackRobot.y);
					System.out.println(ObjectLocations.getYellowATTACKmarker());
					System.out.println("possession: ");
					System.out.println(BallPossession.hasPossession(RobotType.AttackUs));
					Button.waitForAnyPress();
				}
			}
		}
		bRobot.stop();
	}
}
