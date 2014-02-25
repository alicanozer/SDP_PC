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
	
	/**
	 * Gets the angle of robot to score!
	 * 
	 * @returns angle to shoot
	 */
	public static double shootAngle() {
		
		GoalInfo info = new GoalInfo(PitchConstants.newPitch);
		
		//Only works for left goal at the moment
		
		if (ObjectLocations.getYellowUs()) {
			if (ObjectLocations.getYellowDefendingLeft()) {
			
				Point2D_I32 RGTop = info.getRightGoalTop();
				Point2D_I32 RGCentre = info.getRightGoalCenter();
				Point2D_I32 RGBottom = info.getRightGoalBottom();

				System.out.println("Defender: " + ObjectLocations.getBlueDEFENDmarker().y);
				System.out.println("Goal Top: " + RGTop.y);
				System.out.println("Goal Centre: " + RGCentre.y);
				System.out.println("Goal Bottom: " + RGBottom.y);				
				
				if (ObjectLocations.getBlueDEFENDmarker().y > RGTop.y && ObjectLocations.getBlueDEFENDmarker().y < RGCentre.y ) {
					Point2D_I32 point = new Point2D_I32(RGBottom.x, RGBottom.y-10);
					return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), point);
				} else if (ObjectLocations.getBlueDEFENDmarker().y < RGBottom.y && ObjectLocations.getBlueDEFENDmarker().y > RGCentre.y ) {
					return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), new Point2D_I32(RGTop.x, RGTop.y+10));
				} else if (ObjectLocations.getBlueDEFENDmarker().y == RGCentre.y ) {
					return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), new Point2D_I32(RGTop.x, RGTop.y+10));
				} else if (ObjectLocations.getBlueDEFENDmarker().y > RGBottom.y || ObjectLocations.getBlueDEFENDmarker().y < RGTop.y) {
					return getAngleToObject(ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowATTACKmarker(), RGCentre);
				}
			
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
		if ((object.y - dot.y) < (object.x - dot.x)) {
			return -Math.toDegrees(angleBetweenDotObject);
		}
				
		return Math.toDegrees(angleBetweenDotObject);
		
	}
	
}