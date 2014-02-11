package strategy.movement;

import vision.ObjectLocations;
import World.*;

public class DistanceToBall {

    public static double Distance(double x1, double y1, double x2, double y2) {

        double xDistance = Math.abs(x1 - x2);
        double yDistance = Math.abs(y1 - y2);

        double distance = Math.sqrt(Math.pow(yDistance, 2) + (Math.pow(xDistance, 2)));

        return distance;

    }
    public static double Distance(RobotType r , ObjectLocations objs) {
    	
    	Robot rc = new Robot(r);
        double xDistance = Math.abs(rc.x - objs.getBall().x);
        double yDistance = Math.abs(rc.y - objs.getBall().y);

        double distance = Math.sqrt(Math.pow(yDistance, 2) + (Math.pow(xDistance, 2)));

        return distance;

    }

}
