package strategy.planning;

import lejos.nxt.Button;
import World.Robot;
import World.RobotType;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import vision.ObjectLocations;
import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import comms.BluetoothRobot;
import comms.BluetoothRobotOld;

public class Defend extends StrategyInterface {

	private Robot theirAttackRobot = new Robot(RobotType.AttackThem);
	private Robot ourAttackRobot = new Robot(RobotType.AttackUs);
	private Robot ourDefendRobot = new Robot(RobotType.DefendUs);
	
	//	private Point2D_I32 ourDefenseRobot;
	//	private Point2D_I32 theirAttackRobot;
	//	private Point2D_I32 ourAttackRobot;
	
	private Point2D_I32 ball;

	public Defend(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		ball = ObjectLocations.getBall();
	
		//		ourDefendRobot.setPosition(new Vector(ObjectLocations.getYellowDEFENDmarker().x,ObjectLocations.getYellowDEFENDmarker().y));
		ourAttackRobot.setPosition(new Vector(ObjectLocations.getYellowATTACKmarker().x,ObjectLocations.getYellowATTACKmarker().y));
		//		theirAttackRobot.setPosition(new Vector(ObjectLocations.getBlueDEFENDmarker().x,ObjectLocations.getBlueDEFENDmarker().y));
	
	}

	@Override
	public void run() {

		while (!shouldidie && !Strategy.alldie) {
			
			if (ObjectLocations.getYellowUs()) {
				if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getYellowATTACKmarker())) {
					while(true) {
						System.out.println("Our Attacker has the ball! Starting to mark them...");
						ball = ObjectLocations.getBall();
						ourAttackRobot.setPosition(new Vector(ObjectLocations.getYellowATTACKmarker().x,ObjectLocations.getYellowATTACKmarker().y));
	
						//double ycoord = theirAttackRobot.getPosition().getY();
						//double distance = DistanceCalculator.Distance(ourDefendRobot.x, ourDefendRobot.y, ourDefendRobot.x, ycoord);
						//bRobot.forward(distance);
						
						Button.waitForAnyPress();
						
					}
				} else {
					
					System.out.println("Someone else has the ball.");
					attackRobot.forward("AttackUs", 10);
				
				}
			}
		}

	}

}
