package strategy.planning;

import georegression.struct.point.Point2D_I32;
import lejos.nxt.Button;
import movement.RobotMover;
import vision.ObjectLocations;
import world.RobotType;
import comms.BluetoothRobot;
import comms.BluetoothRobotOld;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;

public class TestStrategy extends StrategyInterface{

	private Point2D_I32 ourAttackRobot;
	private Point2D_I32 ball;

	public TestStrategy(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
	}

	@Override
	public void run() {
		System.out.println("Starting test strategy...");
		ObjectLocations.setYellowDefendingLeft(true);
		while (!shouldidie && !Strategy.alldie) {
			
			attackMover.kick("attack");

			if(ObjectLocations.getYellowATTACKmarker()!=null && ObjectLocations.getBall()!=null) {
				//Check if speed is too much then dont have ball otherwise if ball is still we have the ball
				double distance = DistanceCalculator.Distance(ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBall());
				System.out.println("attackmarker: " + ObjectLocations.getYellowATTACKmarker());
				System.out.println("distance: " +distance);
				System.out.println("possession: ");
				System.out.println(BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getYellowATTACKmarker()));
			}
			Button.waitForAnyPress();

		}
	}
}
