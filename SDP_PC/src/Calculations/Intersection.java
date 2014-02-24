package Calculations;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;

public class Intersection {

	public static Vector IntersectionVector(Point2D_I32 ball, Vector marker, Point2D_I32 dot) {
		ball = ObjectLocations.getBall();
		double ballDirection = ObjectLocations.getBallDirectionAngle();

		double xDiff1 = marker.getX() - dot.x;
		double yDiff1 = marker.getY() - dot.y;

		Vector dotToMarker = new Vector(xDiff1, yDiff1);

		Vector ballDirectionVector = new Vector(ballDirection);

		Vector result = IntersectionPoint(ball,ballDirectionVector,marker,dotToMarker);

		return result;
	}

	public static Vector IntersectionPoint(Point2D_I32 ball, Vector ballDirection, Vector robot, Vector robotDirection) {

		//		v1 = (a,b)
		//		v2 = (c,d)
		//		d1 = (e,f)
		//		d2 = (h,i)

		double a = ball.x;
		double b = ball.y;
		double c = robot.getX();
		double d = robot.getY();
		double e = ballDirection.getX();
		double f = ballDirection.getY();
		double h = robotDirection.getX();
		double i = robotDirection.getY();

		//		l1: v1 + λd1
		//		l2: v2 + µd2

		//		Equation to find vector intersection of l1 and l2 by re-arranging for lambda.

		//		(a,b) + λ(e,f) = (c,d) + µ(h,i)
		//		mu = (b + lambda*(f) - d)/i;
		double lambda = ((b - d)/i - (a - c)/h)/(e/h - f/i);

		Vector intersectionPoint = new Vector(a + lambda*e,b + lambda*f);

		return intersectionPoint;
	}
}
