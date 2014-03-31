package strategy.planning;

import georegression.struct.point.Point2D_I32;
import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.GoalInfo;
import movement.RobotMover;
import comms.BluetoothRobot;

public class AttackD extends StrategyInterface {

	public AttackD(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Attack Defender Strategy...");
		
	}

}
