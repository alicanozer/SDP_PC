package Calculations;

public class DistanceCalculator {

    public static double Distance(int x1, int y1, int x2, int y2) {

        double xDistance = Math.abs(x1 - x2);
        double yDistance = Math.abs(y1 - y2);

        double distance = Math.sqrt(Math.pow(yDistance, 2) + (Math.pow(xDistance, 2)));

        return distance;

    }
	
}
