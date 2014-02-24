package strategy.planning;

import Calculations.DistanceCalculator;
import vision.ObjectLocations;
import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import Calculations.Intersection;
import World.Robot;
import World.RobotType;
import comms.BluetoothRobot;

public class InterceptBall extends StrategyInterface{
	
	private Robot theirAttackRobot = new Robot(RobotType.AttackThem);
	private Robot ourAttackRobot = new Robot(RobotType.AttackUs);
	private Robot ourDefendRobot = new Robot(RobotType.DefendUs);
	private Point2D_I32 ball;
	private Point2D_I32 dot;
	static BluetoothRobot bRobot;

	public InterceptBall(BluetoothRobot bRobot) {
		super(bRobot);
		ball = ObjectLocations.getBall();
		dot = ObjectLocations.getYellowATTACKdot();
//		ourDefendRobot.setPosition(new Vector(ObjectLocations.getYellowDEFENDmarker().x,ObjectLocations.getYellowDEFENDmarker().y));
		ourAttackRobot.setPosition(new Vector(ObjectLocations.getYellowATTACKmarker().x,ObjectLocations.getYellowATTACKmarker().y));
//		theirAttackRobot.setPosition(new Vector(ObjectLocations.getBlueDEFENDmarker().x,ObjectLocations.getBlueDEFENDmarker().y));
	}

	@Override
	public void run() {
		
		System.out.println("our attack robot position: " +ourAttackRobot.getPosition());
		System.out.println("ball position: " + ball);
		Vector intersection = Intersection.IntersectionVector(ball, ourAttackRobot.getPosition(), dot);
		System.out.println("intersection point: " + intersection);
		double distance = DistanceCalculator.DistanceQuadruple(ourAttackRobot.x, ourAttackRobot.y, intersection.getX(), intersection.getY());
		System.out.println("distance: " + distance);
		bRobot.forward(distance);
		
	}

}
