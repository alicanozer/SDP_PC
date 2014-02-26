package Calculations;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;

public class Intersection {

	public static Vector IntersectionVector(Point2D_I32 ball, Vector marker, Point2D_I32 dot) {
		ball = ObjectLocations.getBall();
		double ballDirection = ObjectLocations.getBallDirectionAngle();

		System.out.println("ball: " + ball);
		System.out.println("marker: " + marker);
		System.out.println("dot: " + dot);
		
		double xDiff1 = marker.getX() - dot.x;
		double yDiff1 = marker.getY() - dot.y;

		Vector dotToMarker = new Vector(xDiff1, yDiff1);

		Vector ballDirectionVector = new Vector(ballDirection);

		Vector result = IntersectionPoint(ball,ballDirectionVector,marker,dotToMarker);

		return result;
	}

	public static Vector IntersectionPoint(Point2D_I32 point, Vector pointDirection, Vector point1, Vector point1Direction) {

		//		v1 = (a,b)
		//		v2 = (c,d)
		//		d1 = (e,f)
		//		d2 = (h,i)

		double a = point.x;
		double b = point.y;
		double c = point1.getX();
		double d = point1.getY();
		double e = pointDirection.getX();
		double f = pointDirection.getY();
		double h = point1Direction.getX();
		double i = point1Direction.getY();

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
