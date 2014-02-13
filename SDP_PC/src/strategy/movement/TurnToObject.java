package strategy.movement;

import vision.ObjectLocations;
import georegression.struct.point.Point2D_I32;

public class TurnToObject {

	public TurnToObject() {
		
	}
	
	public double Ball(Point2D_I32 point) throws Exception {
		return Math.toDegrees(getDirection(point, ObjectLocations.getBall()));
	}
	
	public double GoalKeeper(Point2D_I32 point) throws Exception {
		return Math.toDegrees(getDirection(point, ObjectLocations.getYellowDEFENDmarker()));
	}
	
	public double Oppenent(Point2D_I32 point) throws Exception {
		return Math.toDegrees(getDirection(point, ObjectLocations.getBlueATTACKmarker()));
	}
	
	public static double getDirection (Point2D_I32 prevPos, Point2D_I32 curPos) throws Exception{
		
		double theta = 0;
		double dx = prevPos.x - curPos.x;
		double dy = prevPos.y - curPos.y;
		if(Math.abs(dx) < 2 && Math.abs(dy) < 2) return 0.0;

		if (prevPos == null || curPos == null)
			throw new Exception("math error - either point is null");
		else if (dx < 0 && dy > 0)
			theta = Math.PI/2 + Math.atan2(Math.abs(dy), Math.abs(dx));
		else if (dx < 0 && dy < 0)
			theta  = Math.PI/2 - Math.atan2(Math.abs(dy), Math.abs(dx));
		else if (dx > 0 && dy < 0)
			theta = Math.PI*2 - Math.atan2(Math.abs(dx), Math.abs(dy));
		else if (dx > 0 && dy > 0)
			theta = Math.PI + Math.atan2(Math.abs(dy), Math.abs(dx));
		else if (dx == 0 && dy == 0)
//			throw new Exception("no direction");
			return 0.0;
		return theta;
		
	}
	
}