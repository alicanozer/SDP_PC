package strategy.planning;

import lejos.nxt.Button;
import Calculations.DistanceCalculator;
import strategy.movement.MoveToPoint;
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

	public InterceptBall(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot,defenceRobot);
		while (ball==null) {
			if (ObjectLocations.getBall()!=null){
				ball = ObjectLocations.getBall();
				System.out.println("Set position of ball..." + ball);
			}
		}
				ourAttackRobot.setPosition(new Vector(ObjectLocations.getYellowATTACKmarker().x,ObjectLocations.getYellowATTACKmarker().y));
				System.out.println("Set position of our attacker..." + ourAttackRobot.getPosition());
		while (dot==null) { 
			if (ObjectLocations.getYellowATTACKdot()!=null){
				dot = ObjectLocations.getYellowATTACKdot();
				System.out.println("Set position of dot..." + dot);
			}
		}
	}


	@Override
	public void run() {

		Vector intersection = Intersection.IntersectionVector(ball, ourAttackRobot.getPosition(), dot);
		System.out.println("intersection point: " + intersection);
		double distance = DistanceCalculator.DistanceQuadruple(intersection.getX(), intersection.getY(), ourAttackRobot.getPosition().getX(), ourAttackRobot.getPosition().getY());
		System.out.println("distance: " + distance);
		try {
			MoveToPoint.moveToPoint(attackRobot, ObjectLocations.getYellowATTACKmarker(), dot, intersection.getX(), intersection.getY());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
