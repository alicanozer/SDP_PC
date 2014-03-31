package strategy.movement;

import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.GoalInfo;

public class Facing {
	
	public static boolean FacingTeammateRobot(RobotType type) throws Exception {

		double angleToOppenent = TurnToObject.Teammate(type);
		
		if (Math.abs(angleToOppenent) > 90) {
			return false;
		}
		
		return true;
		
	}
	
	public static boolean FacingOpponentAttacker(RobotType type) throws Exception {
		
		if (RobotType.AttackUs == type) { 
			return FacingTeammateRobot(type);
		} else if (RobotType.DefendUs == type) {
			return FacingTeammateRobot(type);
		} 
		
		System.out.println("Default Value");
		return true;
		
	}
	
	public static boolean FacingOppenentGoal(RobotType type) {
		
		Point2D_I32 centre;
		double angleToGoal = 0.0;
		
		if (ObjectLocations.getYellowDefendingLeft()) {
			centre = GoalInfo.getRightGoalCenterNew();
		} else {
			centre = GoalInfo.getLeftGoalCenterNew();
		}
		
		if (ObjectLocations.getYellowUs()) {
			if (RobotType.AttackUs == type) {
				angleToGoal = TurnToObject.getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), centre);
			} else if (RobotType.DefendUs == type) {
				angleToGoal = TurnToObject.getAngleToObject(ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getYellowDEFENDmarker(), centre);				
			} else if (RobotType.AttackThem == type) {
				angleToGoal = TurnToObject.getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), centre);				
			} else if (RobotType.DefendThem == type) {
				angleToGoal = TurnToObject.getAngleToObject(ObjectLocations.getBlueDEFENDdot(), ObjectLocations.getBlueDEFENDmarker(), centre);				
			} 
		} else {
			if (RobotType.AttackUs == type) {
				angleToGoal = TurnToObject.getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), centre);
			} else if (RobotType.DefendUs == type) {
				angleToGoal = TurnToObject.getAngleToObject(ObjectLocations.getBlueDEFENDdot(), ObjectLocations.getBlueDEFENDmarker(), centre);				
			} else if (RobotType.AttackThem == type) {
				angleToGoal = TurnToObject.getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), centre);				
			} else if (RobotType.DefendThem == type) {
				angleToGoal = TurnToObject.getAngleToObject(ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getYellowDEFENDmarker(), centre);				
			}
		}
		
		if (Math.abs(angleToGoal) > 90) {
			return false;
		}
		
		return true;
	}
	
	public static boolean FacingOurGoal(RobotType type) {
		return !FacingOppenentGoal(type);
	}
	
}
