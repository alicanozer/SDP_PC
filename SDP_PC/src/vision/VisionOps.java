package vision;

import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import boofcv.alg.color.ColorHsv;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;

/**
 * This class contains all the methods used in segmenting the ball, field, and is
 * @author bilyan
 *
 */
public class VisionOps {
	/**
	 * stable dont touch
	 * @param image
	 * @param hues
	 * @param saturations
	 * @return
	 */
	public static MultiSpectral<ImageFloat32>[] segmentMultiHSV(BufferedImage image, float[] hues , float[] saturations){

		if(!(hues.length == saturations.length)){
			return null;
		}
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(image,null,true,ImageFloat32.class);
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);


		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);

		// Pixels which are more than this different from the selected color are set to black
		float maxDist2 = 0.16f; // was 0.16f

		// Extract hue and saturation bands which are independent of intensity
		ImageFloat32 H = hsv.getBand(0);
		ImageFloat32 S = hsv.getBand(1);


		// Adjust the relative importance of Hue and Saturation
		float adjustUnits = (float)(Math.PI/2.0);

		// step through each pixel and mark how close it is to the selected color
		MultiSpectral<ImageFloat32>[] output = new MultiSpectral[hues.length];
		for(int k = 0; k < hues.length; k++){
			output[k] = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);
		}


		for( int y = 0; y < hsv.height; y++ ) {
			for( int x = 0; x < hsv.width; x++ ) {
				for(int k = 0; k < hues.length; k++){
					// remember Hue is an angle in radians, so simple subtraction doesn't work
					float dh = UtilAngle.dist(H.unsafe_get(x,y),hues[k]);
					float ds = (S.unsafe_get(x,y)-saturations[k])*adjustUnits;

					// this distance measure is a bit naive, but good enough for this demonstration
					float dist2 = dh*dh + ds*ds;
					if( dist2 <= maxDist2 ) {
						for(int z = 0; z < 3; z++){
							output[k].getBand(z).unsafe_set(x, y, input.getBand(z).unsafe_get(x, y));
						}
					}
				}
			}
		}
		return output;
	}




	/**
	 * performes ball and marker search together
	 * @param image , leaves it intact
	 * @param hues
	 * @param saturations
	 * @return a new image
	 */
	// instead of passing a single hue pass an array of hues and saturations!
	public static BufferedImage segmentHSV(BufferedImage image , float hue , float saturation ){
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(image,null,true,ImageFloat32.class);
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);

		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);

		// Pixels which are more than this different from the selected color are set to black
		float maxDist2 = 0.4f*0.4f;

		// Extract hue and saturation bands which are independent of intensity
		ImageFloat32 H = hsv.getBand(0);
		ImageFloat32 S = hsv.getBand(1);

		// Adjust the relative importance of Hue and Saturation
		float adjustUnits = (float)(Math.PI/2.0);

		// step through each pixel and mark how close it is to the selected color
		BufferedImage output = new BufferedImage(input.width,input.height,BufferedImage.TYPE_INT_RGB);

		for( int y = 0; y < hsv.height; y++ ) {
			for( int x = 0; x < hsv.width; x++ ) {
				// remember Hue is an angle in radians, so simple subtraction doesn't work
				float dh = UtilAngle.dist(H.unsafe_get(x,y),hue);
				float ds = (S.unsafe_get(x,y)-saturation)*adjustUnits;

				// this distance measure is a bit naive, but good enough for this demonstration
				float dist2 = dh*dh + ds*ds;
				if( dist2 <= maxDist2 ) {
					output.setRGB(x,y,image.getRGB(x,y));
				}
			}
		}
		return output;
	}

	/**
	 * draws contours of objects on the input image
	 */
	public static BufferedImage contourOps(String type, MultiSpectral<ImageFloat32> inputImg) {
		List<Contour> contours = getContours(type,inputImg);
		BufferedImage visualContour = VisualizeBinaryData.renderContours(
				contours,
				0xFFFFFF,
				0xFF20FF,
				inputImg.getWidth(),
				inputImg.getHeight(),
				null);

		return visualContour;
	}
	/**
	 * returns the regions of the pitch as a list of polygons
	 */
	public static ArrayList<Polygon> getRegions(MultiSpectral<ImageFloat32> inputImg) {

		List<Contour> contours = getContours("lines",inputImg);
		if(contours.size() != 1 || contours.get(0).internal.size() < 4){
			System.out.println(contours.size() + " " + contours.get(0).internal.size());
			return null;
		}
		
		ArrayList<Polygon> pols = new ArrayList<Polygon>();
		// in initial conditions, contours has only 1 element and it is the 
		// pitch. it has  at least 4 list of points as internal contours
		for(int i=0; i< contours.get(0).internal.size(); i++){
			Polygon p = ContourUtils.polygonFromContour(contours.get(0).internal.get(i));
			// since our polygons consists of individual pixel coords then the num
			// of points a good indication of the perimeter

			if(p.npoints > 100){
				pols.add(p); 
			}
		}
		if(pols.size() == 4) return pols;
		else return null;
	}
	/**
	 * Gets the list of contours from applying binary thresholding to an input image
	 * TODO: make it accept MultiSpectralImage instead of converting to/from BufferedImage
	 */
	public static List<Contour> getContours(String type, MultiSpectral<ImageFloat32> input) {
		ImageUInt8 binary = new ImageUInt8(input.width,input.height);
		ImageSInt32 label = new ImageSInt32(input.width,input.height);
		if (type.equals("ball")){
			ThresholdImageOps.threshold(input.getBand(0),binary,(float)150,false);
		}
		else if(type.equals("blue")){
			ThresholdImageOps.threshold(input.getBand(2),binary,(float)70,false);
			//BlurImageOps.gaussian(binary, binary, 4, 6, null);
		}
		else if(type.equals("yellow")){
			ThresholdImageOps.threshold(input.getBand(0),binary,(float)130,false);
		}
		else if(type.equals("lines")){
			//BlurImageOps.gaussian(input.getBand(0), input.getBand(0), -1, 3, null);
			ThresholdImageOps.threshold(input.getBand(0),binary,(float)100,false);
		}


		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
		filtered = BinaryImageOps.dilate8(filtered, null);
		List<Contour> contours = BinaryImageOps.contour(filtered, 8, label);

		return contours;
	}
	/**
	 * This method returns the center of the ball. Method uses hardcoded values
	 * that were obtained by testing
	 * @param img The input image
	 * @return the coordinates as a Point2D_I32
	 */
	public static Point2D_I32 findBall(MultiSpectral<ImageFloat32> img){
		List<Contour> contours = getContours("ball",img);//,segmentHSV(img, 6.21f, 0.88f));
		if(contours.size() > 1 ){
			System.out.println("WARNING: MORE THAN 1 ball detected");
			return null;
		}
		else if(contours.size() == 0){
			System.out.println("WARNING: NO ball detected");
			return null;
		}
		else return ContourUtils.getContourCentroid(contours.get(0));
	}
	/**
	 * finds the marker according to colour
	 * @param img
	 * @param type
	 * @return
	 */
	private static ArrayList<Point2D_I32> findMarkers(MultiSpectral<ImageFloat32> img, String type){
		if(type == "yellow"){
			List<Contour> contours = getContours("yellow",img);//segmentHSV(img, 0.7f, 0.95f));
			ArrayList<Point2D_I32> ret = new ArrayList<Point2D_I32>();
			if(contours.size() == 1 ){
				System.out.println("WARNING: ONLY ONE yellow marker was detected");
				ret.add(ContourUtils.getContourCentroid(contours.get(0)));
				return ret;
			}
			else if(contours.size() != 2){
				System.out.println("WARNING: " + contours.size() + " yellow marker were detected");
				return null;
			}

			ret.add(ContourUtils.getContourCentroid(contours.get(0)));
			ret.add(ContourUtils.getContourCentroid(contours.get(1)));

			return ret;
		}
		else if(type == "blue"){
			List<Contour> contours = getContours("blue",img);//segmentHSV(img, 3.31f, 0.538f));
			ArrayList<Point2D_I32> ret = new ArrayList<Point2D_I32>();
			if(contours.size() == 1 ){
				System.out.println("WARNING: ONLY ONE blue marker was detected");
				ret.add(ContourUtils.getContourCentroid(contours.get(0)));
				return ret;
			}
			else if(contours.size() != 2){
				System.out.println("WARNING: " + contours.size() + " blue marker were detected");
				return null;
			}


			ret.add(ContourUtils.getContourCentroid(contours.get(0)));
			ret.add(ContourUtils.getContourCentroid(contours.get(1)));

			return ret;
		}
		else return null;
	}
	/**
	 * Finds the positions of the Blue Markers
	 * @param img
	 * @return
	 */
	public static ArrayList<Point2D_I32> findBlueMarkers(MultiSpectral<ImageFloat32> img){
		return findMarkers(img,"blue");
	}
	/**
	 * Finds the positions of the Yellow Markers
	 * @param img
	 * @return
	 */
	public static ArrayList<Point2D_I32> findYellowMarkers(MultiSpectral<ImageFloat32> img){
		return findMarkers(img,"yellow");
	}
	/**
	 * 
	 * @param img
	 * @param p
	 * @param windowSize
	 * @return
	 */
	
/**
 * 
 * @param img
 * @param p
 * @param windowSize
 * @return
 */

	public static Point2D_I32 getMeanDotNearMarker(
			BufferedImage img, 
			Point2D_I32 p, // this is a marker position 
			int windowSize)
	{
		int x = p.getX();
		int y = p.getY();
		BufferedImage cropped;
		
		if(x >= windowSize/2 && y >= windowSize/2 && x + windowSize/2 < img.getWidth() && y + windowSize/2 < img.getHeight()){
			cropped = img.getSubimage(x - windowSize/2, y - windowSize/2, windowSize, windowSize);
		}
		else{
			return null;
		}

		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(cropped,null,true,ImageFloat32.class);
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,cropped.getWidth(),cropped.getHeight(),3);
		ImageUInt8 binary = new ImageUInt8(input.width,input.height);

		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);

		ThresholdImageOps.threshold(hsv.getBand(2),binary,(float)55,true);


		

		//
		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
		filtered = BinaryImageOps.dilate8(filtered, null);

		List<Contour> contours = BinaryImageOps.contour(binary, 8, null);


		if(contours.size() == 0){
			System.out.println("WARNING: " + contours.size() + " dots detected");
			return null;
		}
		else if(contours.size() > 1){
			System.out.println("WARNING: " + contours.size() + " dots detected, taking their mean");
			// Iverse distance weighting : http://en.wikipedia.org/wiki/Inverse_distance_weighting
			
			
System.out.println("WARNING: " + contours.size() + " dots detected, taking their mean!");
			
			class Tuple<S, T>
			{
				public S data;
				public T index;

				public Tuple(S s, T t)
				{
					this.data = s;
					this.index = t;
				}
			}
			
			
			// when more than 1 point is detected we take the mean of the points
			// since the windows are very small this won't skew things too much
			ArrayList<Tuple<Point2D_I32,Double>> list = new ArrayList<Tuple<Point2D_I32,Double>>();

			// using the mean
			Point2D_I32 mean = new Point2D_I32();

			for(Contour c: contours){
				Point2D_I32 p2 = ContourUtils.getContourCentroid(c);
				double distanceTo =  Math.sqrt((windowSize/2 - p2.x)*(windowSize/2 - p2.x) + (windowSize/2 - p2.y)*(windowSize/2 - p2.y));
				list.add(new Tuple<Point2D_I32,Double>(p2,distanceTo));

				mean.x += p2.x;
				mean.y += p2.y;
			}
			mean.x /= contours.size();
			mean.y /= contours.size();
			
			mean.x = mean.x + x - windowSize/2;
			mean.y = mean.y + y - windowSize/2;
			
			// using the median
			Comparator<Tuple<Point2D_I32,Double>> comparator = new Comparator<Tuple<Point2D_I32,Double>>()
					{

				public int compare(Tuple<Point2D_I32,Double> tupleA,
						Tuple<Point2D_I32,Double> tupleB)
				{
					if (tupleA.index < tupleB.index) return -1;
					else if (tupleA.index == tupleB.index) return 0;
					else return 1;
				}

					};
		    	    
		    Collections.sort(list, comparator);
		    
		    Point2D_I32 median =  list.get(list.size()/2).data; // get the median
		    median.x = median.x + x - windowSize/2;
		    median.y = median.y + y - windowSize/2;
		    
			return mean;
		}
		else {
			Point2D_I32 p1 = ContourUtils.getContourCentroid(contours.get(0));
			// transform coordinates
			p1.x = p1.x + x - windowSize/2;
			p1.y = p1.y + y - windowSize/2;

			return p1;
		}
	}
	
	public static double getDirection (Point2D_I32 prevPos, Point2D_I32 curPos) throws Exception{
		double theta = 0;
		double dx = prevPos.x - curPos.x;
		double dy = prevPos.y - curPos.y;
		if (dx < 0 && dy > 0)
			theta = Math.PI/2 + Math.atan2(Math.abs(dy), Math.abs(dx));
		else if (dx < 0 && dy < 0)
			theta  = Math.PI/2 - Math.atan2(Math.abs(dy), Math.abs(dx));
		else if (dx > 0 && dy < 0)
			theta = Math.PI*2 - Math.atan2(Math.abs(dx), Math.abs(dy));
		else if (dx > 0 && dy > 0)
			theta = Math.PI + Math.atan2(Math.abs(dy), Math.abs(dx));
		else 
			throw new Exception("math error");
		return theta;
	}

}








