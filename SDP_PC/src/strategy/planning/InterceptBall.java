package strategy.planning;

import lejos.nxt.Button;
import movement.RobotMover;
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
	private Robot ourDefenseRobot = new Robot(RobotType.DefendUs);
	private Point2D_I32 ball;
	private Point2D_I32 dot;

	public InterceptBall(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover,defenceMover);
		System.out.println("Starting InterceptBall...");
	}

	@Override
	public void run() {
		
//		//set ball
//		if (ObjectLocations.getBall()!=null){
//			ball = ObjectLocations.getBall();
//			System.out.println("Set position of ball..." + ball);
//		}
//		
//		//set robot vectors and dots
//		if (ObjectLocations.getYellowUs()) { //if we are yellow
//			ourAttackRobot.setPosition(new Vector(ObjectLocations.getYellowATTACKmarker().x,ObjectLocations.getYellowATTACKmarker().y));
//			System.out.println("Set position of our yellow attacker..." + ourAttackRobot.getPosition());
//			ourDefenseRobot.setPosition(new Vector(ObjectLocations.getYellowDEFENDmarker().x,ObjectLocations.getYellowDEFENDmarker().y));
//			System.out.println("Set position of our yellow defender..." + ourDefenseRobot.getPosition());
//			theirAttackRobot.setPosition(new Vector(ObjectLocations.getBlueATTACKmarker().x,ObjectLocations.getBlueATTACKmarker().y));
//			System.out.println("Set position of their blue attacker..." + theirAttackRobot.getPosition());
//			
//			//check dot is not null
//			if (ObjectLocations.getYellowATTACKdot()!=null){
//				dot = ObjectLocations.getYellowATTACKdot();
//				System.out.println("Set position of our yellow dot..." + dot);
//			}
//		
//		} else { //if we are blue
//			ourAttackRobot.setPosition(new Vector(ObjectLocations.getBlueATTACKmarker().x,ObjectLocations.getBlueATTACKmarker().y));
//			System.out.println("Set position of our blue attacker..." + ourAttackRobot.getPosition());
//			ourDefenseRobot.setPosition(new Vector(ObjectLocations.getBlueDEFENDmarker().x,ObjectLocations.getBlueDEFENDmarker().y));
//			System.out.println("Set position of our blue defender..." + ourDefenseRobot.getPosition());
//			theirAttackRobot.setPosition(new Vector(ObjectLocations.getYellowATTACKmarker().x,ObjectLocations.getBlueATTACKmarker().y));
//			System.out.println("Set position of their yellow attacker..." + theirAttackRobot.getPosition());
//			
//			//checks dot is not null
//			if (ObjectLocations.getBlueATTACKdot()!=null) {
//				dot = ObjectLocations.getBlueATTACKdot();
//				System.out.println("Set position of our blue dot..." + dot);	
//			}
//		
//		}
//
//		Vector intersection = Intersection.IntersectionBall(ObjectLocations.getBall(), ourAttackRobot.getPosition(), dot);
//		System.out.println("intersection point: " + intersection);
//		double distance = DistanceCalculator.DistanceQuadruple(intersection.getX(), intersection.getY(), ourAttackRobot.getPosition().getX(), ourAttackRobot.getPosition().getY());
//		System.out.println("distance: " + distance);
//		try {
//			MoveToPoint.moveToPoint(attackRobot, ObjectLocations.getYellowATTACKmarker(), dot, intersection.getX(), intersection.getY());
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
