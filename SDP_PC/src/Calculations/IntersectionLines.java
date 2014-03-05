package Calculations;

import georegression.struct.point.Point2D_I32;

public class IntersectionLines {

	  public static Point2D_I32 intersectLines(Point2D_I32 point1, Point2D_I32 point2, Point2D_I32 point3, Point2D_I32 point4) {
		 
		  int x1 = point1.x;
		  int y1 = point1.y;		  

		  int x2 = point2.x;
		  int y2 = point2.y;
		  
		  int x3 = point3.x;
		  int y3 = point3.y;
		  
		  int x4 = point4.x;
		  int y4 = point4.y;
		  
		  double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		  
		  if (denom == 0.0) { // Lines are parallel.
		     return null;
		  }
		  
		  double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
		  double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
		  
		  if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
		        // Get the intersection point.
		        return new Point2D_I32((int) (x1 + ua*(x2 - x1)), (int) (y1 + ua*(y2 - y1)));
		  }

		  return null;
		  
	}
	
}
