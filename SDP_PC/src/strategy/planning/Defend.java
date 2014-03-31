package strategy.planning;

import lejos.nxt.Button;
import movement.RobotMover;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import Calculations.Intersection;
import strategy.movement.MoveToPoint;
import vision.ObjectLocations;
import world.Robot;
import world.RobotType;
import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import comms.BluetoothRobot;
import comms.BluetoothRobotOld;

public class Defend extends StrategyInterface {

	private Robot theirAttackRobot = new Robot(RobotType.AttackThem);
	private Robot ourAttackRobot = new Robot(RobotType.AttackUs);
	private Robot ourDefenseRobot = new Robot(RobotType.DefendUs);
	
	private Point2D_I32 ball;
	private Point2D_I32 ourDefenseDot;
	private Point2D_I32 ourAttackDot;
	private Point2D_I32 theirAttackDot;

	public Defend(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		
	}

	@Override
	public void run() {

		while (!shouldidie && !Strategy.alldie) {
			if(ObjectLocations.getYellowDEFENDmarker()!=null && ObjectLocations.getBlueATTACKmarker()!=null && ObjectLocations.getYellowDEFENDdot()!=null && ObjectLocations.getBlueATTACKdot()!=null) {
				if (!BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getYellowDEFENDmarker())) {

					System.out.println("Their Attacker has the ball! Starting to mark them...");
					
					Vector intersectionPoint = Intersection.IntersectionRobots(ObjectLocations.getYellowDEFENDmarker(), ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getBlueATTACKmarker(), ObjectLocations.getBlueATTACKdot());
					double ycoord = intersectionPoint.getY();
					System.out.println("Point of intersection: " + ycoord);
	
//					try {
//						MoveToPoint.moveToPointXY(defenseRobot, ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowDEFENDmarker(), (double) ourDefenseRobot.x, ycoord);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

				} else {

					System.out.println("Someone else has the ball.");
					attackMover.forward("AttackUs", 10);

				}
			}
			Button.waitForAnyPress();

		}

	}

}
