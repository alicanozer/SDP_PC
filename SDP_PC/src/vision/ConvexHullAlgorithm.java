package vision;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;


public interface ConvexHullAlgorithm 
{
        Polygon execute(ArrayList<Point> points);
}
