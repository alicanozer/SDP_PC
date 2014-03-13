package strategy.movement;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;
import world.RobotType;
import Calculations.GoalInfo;

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
	
	/**
	 * Gets the angle of robot to score!
	 * 
	 * @returns angle to shoot
	 */
	public static double shootAngle() {
		
		Point2D_I32 Top;
		Point2D_I32 Centre;
		Point2D_I32 Bottom;
		
		if (ObjectLocations.getYellowDefendingLeft()) {
			
			//Right Goal
			Top = GoalInfo.getRightGoalTopNew();
			Centre = GoalInfo.getRightGoalCenterNew();
			Bottom = GoalInfo.getRightGoalBottomNew();
			
			System.out.println("Goal Top: " + Top.y);
			System.out.println("Goal Centre: " + Centre.y);
			System.out.println("Goal Bottom: " + Bottom.y);
		
		} else {
		
			//Left Goal
			Top = GoalInfo.getLeftGoalTopNew();
			Centre = GoalInfo.getLeftGoalCenterNew();
			Bottom = GoalInfo.getLeftGoalBottomNew();
			
			System.out.println("Goal Top: " + Top.y);
			System.out.println("Goal Centre: " + Centre.y);
			System.out.println("Goal Bottom: " + Bottom.y);
			
		}
			
		if (ObjectLocations.getYellowUs()) {
				//Yellow Attacking
				System.out.println("Defender: " + ObjectLocations.getBlueDEFENDmarker().y);				
				
				if (ObjectLocations.getBlueDEFENDmarker().y > Top.y && ObjectLocations.getBlueDEFENDmarker().y < Centre.y ) {
					Point2D_I32 point = new Point2D_I32(Bottom.x, Bottom.y-10);
					return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), point);
				} else if (ObjectLocations.getBlueDEFENDmarker().y < Bottom.y && ObjectLocations.getBlueDEFENDmarker().y > Centre.y ) {
					return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), new Point2D_I32(Top.x, Top.y+10));
				} else if (ObjectLocations.getBlueDEFENDmarker().y == Centre.y ) {
					return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), new Point2D_I32(Top.x, Top.y+10));
				} else if (ObjectLocations.getBlueDEFENDmarker().y > Bottom.y || ObjectLocations.getBlueDEFENDmarker().y < Top.y) {
					return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), Centre);
				}
			
			} else {
				//Blue Attacking
				System.out.println("Defender: " + ObjectLocations.getYellowDEFENDmarker().y);				
				
				if (ObjectLocations.getYellowDEFENDmarker().y > Top.y && ObjectLocations.getYellowDEFENDmarker().y < Centre.y ) {
					Point2D_I32 point = new Point2D_I32(Bottom.x, Bottom.y-10);
					return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), point);
				} else if (ObjectLocations.getYellowDEFENDmarker().y < Bottom.y && ObjectLocations.getYellowDEFENDmarker().y > Centre.y ) {
					return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), new Point2D_I32(Top.x, Top.y+10));
				} else if (ObjectLocations.getYellowDEFENDmarker().y == Centre.y ) {
					return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), new Point2D_I32(Top.x, Top.y+10));
				} else if (ObjectLocations.getYellowDEFENDmarker().y > Bottom.y || ObjectLocations.getYellowDEFENDmarker().y < Top.y) {
					return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), Centre);
				}
			}
		
		return 0.0;
	}

	/**
	 * Gets the angle of robot to be parallel to the y-axis
	 *
	 * @return angle
	 */
	public static double alignHorizontal(RobotType type) {
		if (ObjectLocations.getYellowUs()) {
			if (type == RobotType.AttackUs){
				//Attacking Yellow Robot
				Point2D_I32 ninty = new Point2D_I32(ObjectLocations.getYellowATTACKdot().x, ObjectLocations.getYellowATTACKdot().y + 5);
				return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), ninty);
			} else {
				//Defending Yellow Robot
				Point2D_I32 ninty = new Point2D_I32(ObjectLocations.getYellowDEFENDdot().x, ObjectLocations.getYellowDEFENDdot().y + 5);
				return getAngleToObject(ObjectLocations.getYellowDEFENDdot(), ObjectLocations.getYellowDEFENDmarker(), ninty);
			} 
		} else {
			if (type == RobotType.AttackUs){
				//Attacking Blue Robot
				Point2D_I32 ninty = new Point2D_I32(ObjectLocations.getBlueATTACKdot().x, ObjectLocations.getBlueATTACKdot().y + 5);
				return getAngleToObject(ObjectLocations.getBlueATTACKdot(), ObjectLocations.getBlueATTACKmarker(), ninty);				
			}else {
				//Defending Blue Robot
				Point2D_I32 ninty = new Point2D_I32(ObjectLocations.getBlueDEFENDdot().x, ObjectLocations.getBlueDEFENDdot().y + 5);
				return getAngleToObject(ObjectLocations.getBlueDEFENDdot(), ObjectLocations.getBlueDEFENDmarker(), ninty);

			}
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
		
		//Check if ball is right or left of the marker
		
		

		
		
		if (!isLeft(dot,marker,object)) {
			return Math.toDegrees(angleBetweenDotObject);
		} else {
			return -Math.toDegrees(angleBetweenDotObject);
		}
				
	}
	
	private static boolean isLeft(Point2D_I32 a, Point2D_I32 b, Point2D_I32 c){
	     return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) > 0;
	}
	
}