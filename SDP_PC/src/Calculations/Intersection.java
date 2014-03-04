package Calculations;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;

public class Intersection {

	public static Vector IntersectionBall(Point2D_I32 ball, Point2D_I32 marker, Point2D_I32 dot) {
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
	
	public static Vector IntersectionRobots(Point2D_I32 marker1, Point2D_I32 dot1, Point2D_I32 marker2, Point2D_I32 dot2) {
		
		System.out.println("marker1: " + marker1);
		System.out.println("marker2: " + marker2);
		System.out.println("dot1: " + dot1);
		System.out.println("dot2: " + dot2);
		
		double xDiff1 = marker1.getX() - dot1.x;
		double yDiff1 = marker1.getY() - dot1.y;
		Vector dotToMarker1 = new Vector(xDiff1, yDiff1);
		
		double xDiff2 = marker2.getX() - dot2.x;
		double yDiff2 = marker2.getY() - dot2.y;
		Vector dotToMarker2 = new Vector(xDiff2, yDiff2);
		
		Vector result = IntersectionPoint(marker1,dotToMarker1,marker2,dotToMarker2);

		return result;
	}
	
	

	public static Vector IntersectionPoint(Point2D_I32 point1, Vector point1Direction, Point2D_I32 point2, Vector point2Direction) {

		//		v1 = (a,b)
		//		v2 = (c,d)
		//		d1 = (e,f)
		//		d2 = (h,i)

		double a = point1.x;
		double b = point1.y;
		double c = point2.x;
		double d = point2.y;
		double e = point1Direction.getX();
		double f = point1Direction.getY();
		double h = point2Direction.getX();
		double i = point2Direction.getY();

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
