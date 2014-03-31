package strategy.planning;

import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import movement.RobotMover;
import comms.BluetoothRobot;

public class PassD extends StrategyInterface {

	public PassD(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Pass Defender Strategy...");
		
//		// Move to Ball
//		MoveToPointXY.moveToPointXY("defence", defenceMover, ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getYellowDEFENDmarker(), ObjectLocations.getBall());
//		
//		// Grab Ball
//		defenceMover.grab("defence");
//		
//		// Turn to open Angle or angle to attacker
//		try {
//			double angleToTeammate = TurnToObject.Teammate(RobotType.DefendUs);
//			double angleToOpponent = TurnToObject.OppenentAttacker(RobotType.DefendUs);
//			
//			if (angleToTeammate < angleToOpponent) {
//				defenceMover.rotate("defence", angleToTeammate);
//			} else {
//				defenceMover.rotate("defence", angleToOpponent + 30);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		// Kick Ball
//		defenceMover.kick("defence");
//		
//		//TODO
//		// Move to Ball
//		// Grab Ball
//		// Turn to open angle
//		// Wait and change angle
//		// Kick Ball
	}

}















