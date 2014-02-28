package Calculations;

import georegression.struct.point.Point2D_I32;

public class DistanceCalculator {

    public static double Distance(Point2D_I32 point, Point2D_I32 point1) {

    	int x1 = point.x;
    	int y1 = point.y;
    	
    	int x2 = point1.x;
    	int y2 = point1.y;
    	
        double xDistance = Math.abs(x1 - x2);
        double yDistance = Math.abs(y1 - y2);

        double distance = Math.sqrt(Math.pow(yDistance, 2) + (Math.pow(xDistance, 2)));

        return distance;

    }
    
    public static double DistanceQuadruple(double x1, double y1, double x2, double y2) {

        double xDistance = Math.abs(x1 - x2);
        double yDistance = Math.abs(y1 - y2);

        double distance = Math.sqrt(Math.pow(yDistance, 2) + (Math.pow(xDistance, 2)));

        return distance;

    }
	
}
