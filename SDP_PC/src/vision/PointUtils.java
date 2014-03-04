package vision;



import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ddogleg.struct.FastQueue;

import boofcv.alg.filter.binary.Contour;
import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_I32;

/**
 * This class contains static methods for computing different things
 * about Contours like centroids and area
 * 
 * @author bilyan
 *
 */
public class PointUtils {
	
	public static Point2D_I32 getListCentroid(ArrayList<Point2D_I32> l){
		if (l == null || l.size() == 0) return null;
		int xMean = 0;
		int yMean = 0;
		
		for(Point2D_I32 p: l){
			xMean += p.x;
			yMean += p.y;
		}
		xMean /= l.size();
		yMean /= l.size();

		return new Point2D_I32(xMean,yMean);
	}
	/*
	 * Gets the centroid of a Contour
	 */
	public static Point2D_I32 getContourCentroid(Contour c){
		int xMean = 0;
		int yMean = 0;
		
		List<Point2D_I32> ext = c.external;
		if(ext == null){
			System.out.println("fuck this shit");
			
		}
		int extSize = ext.size();
		if(extSize == 0){
			System.out.println("FUCK IU");
			
		}
		
		for(Point2D_I32 p: ext){
			xMean += p.x;
			yMean += p.y;
		}
		xMean /= extSize;
		yMean /= extSize;

		return new Point2D_I32(xMean,yMean);
	}

	/**
	 * Returns whether a point is inside a Contour
	 */
	public static boolean isInside(Point2D_I32 p, Contour c){
		List<Point2D_I32> external = c.external;

		int[] xs = new int [external.size()];
		int[] ys = new int [external.size()];

		for(int i = 0; i < xs.length; i++){
			xs[i] = external.get(i).getX();
			ys[i] = external.get(i).getY();
		}
		Polygon pol = polygonFromContour(c.external);

		return pol.contains(p.getX(), p.getY());
	}
	/**
	 * Constructs a polygon from a list of points
	 */
	public static Polygon polygonFromContour(List<Point2D_I32> plist){

		int[] xs = new int [plist.size()];
		int[] ys = new int [plist.size()];

		for(int i = 0; i < xs.length; i++){
			xs[i] = plist.get(i).getX();
			ys[i] = plist.get(i).getY();
		}

		return new Polygon(xs,ys,plist.size());
	}
	/**
	 * Returns whether a point is inside a Polygon
	 */
	public static boolean isInside(Point2D_I32 p, Polygon pol){
		return pol.contains(p.getX(), p.getY());
	}
	public static Point2D_I32 getPolygonCentroid(Polygon p){
		int x = 0;
		int y = 0;
		int npoints = p.npoints;
		for(int i=0;i<npoints;i++){
			 x += p.xpoints[i];
			 y += p.ypoints[i];
		}
		x /= npoints;
		y /= npoints;
		return new Point2D_I32(x,y);
	}
	/**
	 * returns euclidean distance between targets
	 * @param p
	 * @param q
	 * @return
	 */
	public static double euclideanDistance(Point2D_I32 p, Point2D_I32 q){
		return pnormDistance(p,q,2.0);
	}
	/**
	 * return manhattan distance between points
	 * @param p
	 * @param q
	 * @return
	 */
	public static double manhattanDistance(Point2D_I32 p, Point2D_I32 q){
		double x = p.x - q.x;
		double y = p.y - q.y;
		return Math.abs(x) + Math.abs(y);
	}
	/**
	 * returns arbitrary p-norm distance between 2 points
	 * @param p
	 * @param q
	 * @param pnorm
	 * @return
	 */
	public static double pnormDistance(Point2D_I32 p, Point2D_I32 q, double pnorm){
		double x = Math.pow(p.x - q.x, pnorm);
		double y = Math.pow(p.y - q.y, pnorm);
		
		return Math.pow(x + y, 1.0/pnorm);
	}
	
	
	public static Point2D_I32 getPointMedian(ArrayList<Point2D_I32> l){
		int size = l.size();
		if(size == 0 || l == null) return null;
		
		int[] xs = new int[size];
		int[] ys = new int[size];

		
		for(int i = 0; i < size; i++){
			xs[i] = l.get(i).x;
			ys[i] = l.get(i).y;
		}
		Arrays.sort(xs);
		Arrays.sort(ys);
		
		return new Point2D_I32(xs[size/2],ys[size/2]);
	}
	
	public static Polygon findPolygonFromCorners(ArrayList<Point2D_I32> l){
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for(Point2D_I32 p : l){
			if(p.x < minX) minX = p.x;
			if(p.x > maxX) maxX = p.x;
			
			if(p.y < minY) minY = p.y;
			if(p.y > maxY) maxY = p.y;
		}
		
		ArrayList<int[]> corners = new ArrayList<int[]>();
		
		int[] xs = new int[4];
		xs[0] = minX;
		xs[1] = maxX;
		xs[2] = maxX;
		xs[3] = minX;
		
		int[] ys = new int[4];
		ys[0] = minY;
		ys[1] = minY;
		ys[2] = maxY;
		ys[3] = maxY;
		
		corners.add(0, xs);
		corners.add(1, ys);
		
		return new Polygon(xs,ys,4);
	}
}





