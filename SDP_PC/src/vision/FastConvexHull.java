package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FastConvexHull
{
		private FastConvexHull(){};
        private Polygon execute(ArrayList<Point> points) 
        {
        	
                ArrayList<Point> xSorted = (ArrayList<Point>) points.clone();
                Collections.sort(xSorted, new XCompare());
                
                int n = xSorted.size();
                
                Point[] lUpper = new Point[n];
                
                lUpper[0] = xSorted.get(0);
                lUpper[1] = xSorted.get(1);
                
                int lUpperSize = 2;
                
                for (int i = 2; i < n; i++)
                {
                        lUpper[lUpperSize] = xSorted.get(i);
                        lUpperSize++;
                        
                        while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - 3], lUpper[lUpperSize - 2], lUpper[lUpperSize - 1]))
                        {
                                // Remove the middle point of the three last
                                lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
                                lUpperSize--;
                        }
                }
                
                Point[] lLower = new Point[n];
                
                lLower[0] = xSorted.get(n - 1);
                lLower[1] = xSorted.get(n - 2);
                
                int lLowerSize = 2;
                
                for (int i = n - 3; i >= 0; i--)
                {
                        lLower[lLowerSize] = xSorted.get(i);
                        lLowerSize++;
                        
                        while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - 3], lLower[lLowerSize - 2], lLower[lLowerSize - 1]))
                        {
                                // Remove the middle point of the three last
                                lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
                                lLowerSize--;
                        }
                }
                
                ArrayList<Point> result = new ArrayList<Point>();
                
                for (int i = 0; i < lUpperSize; i++)
                {
                        result.add(lUpper[i]);
                }
                
                for (int i = 1; i < lLowerSize - 1; i++)
                {
                        result.add(lLower[i]);
                }
                
                int size = result.size();
                int[] xs = new int[size];
                int[] ys = new int[size];
                
                for(int i = 0; i < size; i++){
                	xs[i] = result.get(i).x;
                	ys[i] = result.get(i).y;
                }
                return new Polygon(xs,ys,size);
        }
        
        private boolean rightTurn(Point a, Point b, Point c)
        {
                return (b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x) > 0;
        }

        private class XCompare implements Comparator<Point>
        {
                @Override
                public int compare(Point o1, Point o2) 
                {
                        return (new Integer(o1.x)).compareTo(new Integer(o2.x));
                }
        }
        /**
         * Constructs the convex hull of a list of Point2D_I32 and returns it as a polygon
         * @param list
         * @return
         */
        public static Polygon getConvexHullOfPoints(ArrayList<Point2D_I32> list){
        	if (list.size() < 2) return null;
        	ArrayList<Point> plist = new ArrayList<Point>();
        	for(Point2D_I32 p : list){
        		plist.add(new Point(p.x,p.y));
        	}
        	FastConvexHull hull = new FastConvexHull();
			return hull.execute(plist);
        	
        }
}