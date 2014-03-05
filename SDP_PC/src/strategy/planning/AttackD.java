package strategy.planning;

import georegression.struct.point.Point2D_I32;
import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import Calculations.GoalInfo;
import World.RobotType;
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
		
		Point2D_I32 dot;
		Point2D_I32 marker;
		
		//Align 90 degree to horizontal
		double angleNinety = TurnToObject.alignHorizontal(RobotType.DefendUs);
		defenceMover.rotate("defence", angleNinety);
				
		//Stay in the centre of the goal
		if (ObjectLocations.getYellowUs()) {
			marker = ObjectLocations.getYellowATTACKmarker();
			dot = ObjectLocations.getYellowATTACKdot();
		} else {
			marker = ObjectLocations.getBlueATTACKmarker();
			dot = ObjectLocations.getBlueATTACKdot();
		}
				
		Point2D_I32 point = new Point2D_I32(marker.x, GoalInfo.getLeftGoalCenterNew().y);
				
		try {
			MoveToPointXY.moveToPointXY("defence", defenceMover, dot, marker, point);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}

}
