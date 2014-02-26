package strategy.planning;

import georegression.struct.point.Point2D_I32;
import lejos.nxt.Button;
import vision.ObjectLocations;
import comms.BluetoothRobot;
import comms.BluetoothRobotOld;

import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import World.RobotType;

public class TestStrategy extends StrategyInterface{

	private Point2D_I32 ourAttackRobot;
	private Point2D_I32 ball;
	static BluetoothRobotOld bRobot;

	public TestStrategy(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		ball = ObjectLocations.getBall();
		ourAttackRobot = ObjectLocations.getYellowATTACKmarker();
	}

	@Override
	public void run() {
		while (!shouldidie && !Strategy.alldie) {

			while(true) {

				if(ball!=null && ourAttackRobot!=null) {

					double distance = DistanceCalculator.Distance(ourAttackRobot, ball);
					System.out.println("distance: ");
					System.out.println(distance);
					System.out.println("ball: ");
					System.out.println(ball.x + " " + ball.y);				
					System.out.println(ObjectLocations.getBall());
					System.out.println("robot: ");
					System.out.println(ourAttackRobot.x + " " + ourAttackRobot.y);
					System.out.println(ObjectLocations.getYellowATTACKmarker());
					System.out.println("possession: ");
					//System.out.println(BallPossession.hasPossession(RobotType.AttackUs));
					Button.waitForAnyPress();
				}
			}

		}
		bRobot.stop();
	}
}
