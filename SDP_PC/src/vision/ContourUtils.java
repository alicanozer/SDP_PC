package vision;



import java.util.List;

import boofcv.alg.filter.binary.Contour;
import georegression.struct.point.Point2D_I32;

/**
 * This class contains static methods for computing different things
 * about Contours like centroids and area
 * 
 * @author bilyan
 *
 */
public class ContourUtils {
	public static Point2D_I32 getContourCentroid(Contour c){
		int xMean = 0;
        int yMean = 0;
        
        List<Point2D_I32> ext = c.external;
        int extSize = ext.size();
        
		for(Point2D_I32 p: c.external){
        	xMean += p.x;
        	yMean += p.y;
        }
        xMean /= extSize;
        yMean /= extSize;
        
        return new Point2D_I32(xMean,yMean);
	}
	/*
	 * To be implemented
	 */
	public static int getContourArea(Contour c){
		return 0;
	}
	
}
