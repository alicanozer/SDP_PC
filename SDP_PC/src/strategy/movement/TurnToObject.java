package strategy.movement;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;
import vision.PitchConstants;
import Calculations.GoalInfo;
import World.RobotType;

/**
 * This class contains functions that return the angle of the robot to an object
 *
 * @author Iman Majumdar
 */
public class TurnToObject {
	
	/**
	 * Gets the angle of robot to Ball
	 *
	 * @return angle to Ball
	 */
	public static double Ball(RobotType type) throws Exception {
	
		if (ObjectLocations.getYellowUs()) {
			if (type == RobotType.AttackUs) {
				return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBall());
			} else {
				return getAngleToObject(ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getYellowDEFENDmarker(), ObjectLocations.getBall());			
			}
		}else {
			if (type == RobotType.AttackUs) {
				return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), ObjectLocations.getBall());
			} else {
				return getAngleToObject(ObjectLocations.getBlueDEFENDdot(), ObjectLocations.getBlueDEFENDmarker(), ObjectLocations.getBall());			
			}
		}
		
	}
	
	/**
	 * Gets the angle of robot to team mate
	 *
	 * @return angle to team mate
	 */
	public static double Teammate(RobotType type) throws Exception {
		if (ObjectLocations.getYellowUs()) {
			if (type == RobotType.AttackUs) {
				return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getYellowDEFENDmarker());
			} else {
				return getAngleToObject(ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getYellowDEFENDmarker(), ObjectLocations.getYellowATTACKmarker());			
			}
		}else {
			if (type == RobotType.AttackUs) {
				return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), ObjectLocations.getBlueDEFENDmarker());
			} else {
				return getAngleToObject(ObjectLocations.getBlueDEFENDdot(), ObjectLocations.getBlueDEFENDmarker(), ObjectLocations.getBlueATTACKmarker());			
			}
		}
	}
	
	/**
	 * Gets the angle of robot to opponent goal keeper
	 *
	 * @return angle to opponent goal keeper
	 */
	public static double OppenentGoalie(RobotType type) throws Exception {
		if (ObjectLocations.getYellowUs()) {
			if (type == RobotType.AttackUs) {
				return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBlueDEFENDmarker());
			} else {
				return getAngleToObject(ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getYellowDEFENDmarker(), ObjectLocations.getBlueDEFENDmarker());			
			}
		}else {
			if (type == RobotType.AttackUs) {
				return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), ObjectLocations.getYellowDEFENDmarker());
			} else {
				return getAngleToObject(ObjectLocations.getBlueDEFENDdot(), ObjectLocations.getBlueDEFENDmarker(), ObjectLocations.getYellowDEFENDmarker());			
			}
		}
	}
	
	/**
	 * Gets the angle of robot to opponent attacker
	 *
	 * @return angle to opponent attacker
	 */
	public static double OppenentAttacker(RobotType type) throws Exception {
		if (ObjectLocations.getYellowUs()) {
			if (type == RobotType.AttackUs) {
				return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBlueATTACKmarker());
			} else {
				return getAngleToObject(ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getYellowDEFENDmarker(), ObjectLocations.getBlueATTACKmarker());			
			}
		}else {
			if (type == RobotType.AttackUs) {
				return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), ObjectLocations.getYellowATTACKmarker());
			} else {
				return getAngleToObject(ObjectLocations.getBlueDEFENDdot(), ObjectLocations.getBlueDEFENDmarker(), ObjectLocations.getYellowATTACKmarker());			
			}
		}
	}
	
	public static double shootAngle() {
		
		GoalInfo info = new GoalInfo(PitchConstants.newPitch);
		
		//Only works for left goal at the moment
		
		if (ObjectLocations.getYellowUs()) {
			if (ObjectLocations.getYellowDefendingLeft()) {
				double LG = getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), info.getRightGoalTop());
				double RG = getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), info.getRightGoalBottom());
				double CG = getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), info.getRightGoalCenter());
					
				double goalie = getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBlueDEFENDmarker());
									
				if (goalie < LG && goalie > CG) {
					//Denfender on the left to cetnre of goal
					return (RG+5);
				} else if (goalie > RG && goalie < CG) {
					//Denfender on the right to centre of goal
					return (LG-5);					
				} else {
					//Goalie not blocking the goal
					return CG;
				}
			} else {
				double LG = getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), info.getLeftGoalTop());
				double RG = getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), info.getLeftGoalBottom());
				double CG = getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), info.getLeftGoalCenter());
					
				double goalie = getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getBlueDEFENDmarker());
									
				if (goalie < LG && goalie > CG) {
					//Denfender on the left to cetnre of goal
					return (RG+5);
				} else if (goalie > RG && goalie < CG) {
					//Denfender on the right to centre of goal
					return (LG-5);					
				} else {
					//Goalie not blocking the goal
					return CG;
				}
			}
		} else {
			//Add same code only for blue marker and blue dot
			return 0.0;
		}
		
		
	}
	
	/**
	 * Gets the angle of robot to a Point2D_I32 object
	 *
	 * @return angle to object
	 */
	public static double getAngleToObject(Point2D_I32 dot, Point2D_I32 marker, Point2D_I32 object) {
		
		// Vector from dot to the object		
		int xDiff = object.x - dot.x;
		int yDiff = object.y - dot.y;

		Vector dotToObject = new Vector(xDiff, yDiff);
		
		// Vector from dot to marker centre
		
		int xDiff1 = marker.x - dot.x;
		int yDiff1 = marker.y - dot.y;
		
		Vector dotToMarker = new Vector(xDiff1, yDiff1);

		double dotProduct = Vector.dotProduct(dotToMarker, dotToObject);
		double magnitude = Vector.magnitude(dotToMarker);
		double magnitude2 = Vector.magnitude(dotToObject);
		double totalMagnitude = magnitude * magnitude2;
		
		double angleBetweenDotObject = Math.acos(dotProduct/totalMagnitude);
		
		//Check which quadrant the object is in with respect to the robot
		Vector subtraction = Vector.subtract(dotToMarker, dotToObject);
		
		//If object is right of the marker return negative angle
		if (subtraction.getX() < 0) {
			System.out.println(subtraction.getX());
			return (Math.toDegrees((Math.PI*2) - angleBetweenDotObject));
			//return (-Math.toDegrees(angleBetweenDotObject));
		}

		//If object is left of the marker return original angle
		return Math.toDegrees(angleBetweenDotObject);
		
	}
	
}