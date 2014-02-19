package strategy.movement;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;
import World.RobotType;

public class TurnToObject {

	public TurnToObject() {
		
	}
	
	public double Ball(RobotType type) throws Exception {
	
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
	
	public double Teammate(RobotType type) throws Exception {
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
	
	public double OppenentGoalie(RobotType type) throws Exception {
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
	
	public double OppenentAttacker(RobotType type) throws Exception {
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
	
	public static double getAngleToObject(Point2D_I32 dot, Point2D_I32 marker, Point2D_I32 object) {
		
		// Vector from dot to the object
		
		int xDiff= object.x - dot.x;
		int yDiff= object.y - dot.y;

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
			return (-Math.toDegrees(angleBetweenDotObject));
		}

		//If object is left of the marker return original angle
		return Math.toDegrees(angleBetweenDotObject);
		
	}
	
}