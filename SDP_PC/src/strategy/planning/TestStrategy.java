package strategy.planning;

import comms.BluetoothRobot;

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
		ourAttackRobot = world.getOurAttackRobot();
	}

	@Override
	public void run() {
		while (!shouldidie && !Strategy.alldie) {

			int x1 = (int) ourAttackRobot.x;
			int y1 = (int) ourAttackRobot.y;

			int x2 = (int) ball.x;
			int y2 = (int) ball.y;

			System.out.println("distance: ");
		}
		bRobot.stop();
	}
}
