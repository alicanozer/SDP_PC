package vision;

import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

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

		List<Contour> contoursFromImage = getContours("lines",inputImg);
		List<Contour> contours = new ArrayList<Contour>();
		// removes contours that have external sizes < 300
		if(contoursFromImage != null && contoursFromImage.size() != 0){
			for(int w = 0; w < contoursFromImage.size(); w++){
				if (contoursFromImage.get(w).external.size() > 300){
					contours.add(contoursFromImage.get(w));
				}
			}
		}
		System.out.println("siiiize " + contours.size());
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
		System.out.println("pols size " + pols.size());
		//System.exit(0);
		if(pols.size() == 4) {
			System.out.println("SUCCEEDED");
			return pols;
		}
		else return null;
	}
	/**
	 * Method that does segmentation of objects purely in HSV space.
	 * @param type
	 * @param img
	 * @return
	 */
	public static List<Contour> newHSVSegment(String type,BufferedImage img){
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(img,null,true,ImageFloat32.class);
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);

			
		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);

		
		
		ImageUInt8 binary = new ImageUInt8(input.width,input.height);
		switch(type){
		case "ball":
			// hue goes 0 to 2pi
			ImageUInt8 lowerHueBall = ThresholdImageOps.threshold(hsv.getBand(0),null, 6.10f,false);
			ImageUInt8 upperHueBall = ThresholdImageOps.threshold(hsv.getBand(0),binary, 0.10f,true);
			
			ImageUInt8 lowerSaturationBall = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.66f,false);
			BinaryImageOps.logicOr(lowerHueBall, upperHueBall, binary);
			BinaryImageOps.logicAnd(binary, lowerSaturationBall, binary);
			break;	
		case "blue":
			if(false){ // new pitch but doesnt work
				BlurImageOps.gaussian(hsv.getBand(0), hsv.getBand(0), 4, 4, null);
				BlurImageOps.gaussian(hsv.getBand(1), hsv.getBand(1), 4, 4, null);

				ImageUInt8 lowerHueBlue = ThresholdImageOps.threshold(hsv.getBand(0),null, 1.97f,false); // was 1.97
				ImageUInt8 upperHueBlue = ThresholdImageOps.threshold(hsv.getBand(0),null, 3.14f,true); // was 3.14

				ImageUInt8 upperSaturationBlue = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.30f,true); //was 0.25
				BinaryImageOps.logicAnd(lowerHueBlue, upperHueBlue, binary);
				BinaryImageOps.logicAnd(binary, upperSaturationBlue, binary);
			}
			else{// old pitchj
				BlurImageOps.gaussian(hsv.getBand(0), hsv.getBand(0), 4, 4, null);
				BlurImageOps.gaussian(hsv.getBand(1), hsv.getBand(1), 4, 4, null);

				ImageUInt8 lowerHueBlue = ThresholdImageOps.threshold(hsv.getBand(0),null, 3.10f,false); // was 3.14
				ImageUInt8 upperHueBlue = ThresholdImageOps.threshold(hsv.getBand(0),null, 3.49f,true); // was 3.49

				ImageUInt8 lowerSaturationBlue = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.62f,false); //was 0.70
				ImageUInt8 upperSaturationBlue = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.862f,true); //was 0.82
				
				BinaryImageOps.logicAnd(lowerHueBlue, upperHueBlue, binary);
				BinaryImageOps.logicAnd(binary, lowerSaturationBlue, binary);
				BinaryImageOps.logicAnd(binary, upperSaturationBlue, binary);
			}
			break;
		case "yellow":
			if(false){ // new pitch
				ImageUInt8 lowerHueYellow = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.34f,false); // was 0.34
				ImageUInt8 upperHueYellow = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.69f,true); // was 0.69

				ImageUInt8 lowerSaturationYellow = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.65f,false); // was 0.62
				ImageUInt8 upperSaturationYellow = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.86f,true); // was 0.86
				//values are 0..255

				BinaryImageOps.logicAnd(lowerHueYellow, upperHueYellow, binary);
				BinaryImageOps.logicAnd(binary, lowerSaturationYellow, binary);
				BinaryImageOps.logicAnd(binary, upperSaturationYellow, binary);
				BlurImageOps.gaussian(binary, binary, 4, 5, null);
			}
			else{//old pitch
				ImageUInt8 lowerHueYellow = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.52f,false); // was 0.34
				ImageUInt8 upperHueYellow = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.87f,true); // was 0.69

				ImageUInt8 lowerSaturationYellow = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.74f,false); // was 0.62
				//ImageUInt8 upperSaturationYellow = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.86f,true); // was 0.86
				BinaryImageOps.logicAnd(lowerHueYellow, upperHueYellow, binary);
				BinaryImageOps.logicAnd(binary, lowerSaturationYellow, binary);
//				BinaryImageOps.logicAnd(binary, upperSaturationYellow, binary);
			}
			
		}
		
		
		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
		filtered = BinaryImageOps.dilate8(filtered, null);
		List<Contour> contoursUnfiltered = BinaryImageOps.contour(filtered, 8, null);
		List<Contour> contoursFiltered = new ArrayList<Contour>();
		for(Contour c: contoursUnfiltered) {
			Point2D_I32 p = ContourUtils.getContourCentroid(c);
			if(type == "yellow"){
				if(
						c.external.size() > 18 &&
						p.x > 15 && p.x < img.getWidth() - 15
						) contoursFiltered.add(c);
			}
			else{
				if(
						c.external.size() > 10 &&
						p.x > 15 && p.x < img.getWidth() - 15
						) contoursFiltered.add(c);
			}
		}

		return contoursFiltered;
	}
	
	public static BufferedImage newDisplay(List<Contour> contours, int width, int height) {
		BufferedImage visualContour = VisualizeBinaryData.renderContours(
				contours,
				0xFFFFFF,
				0xFF20FF,
				width,
				height,
				null);

		return visualContour;
	}
	
	
	/**
	 * Gets the list of contours from applying binary thresholding to an input image
	 * TODO: make it accept MultiSpectralImage instead of converting to/from BufferedImage
	 */
	public static List<Contour> getContours(String type, MultiSpectral<ImageFloat32> input) {
		
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);

		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);
		
		
		ImageUInt8 binary = new ImageUInt8(input.width,input.height);;
		if (type.equals("ball")){
			// hue goes 0 to 2pi
			ThresholdImageOps.threshold(hsv.getBand(0),binary, 1.0f ,false);
			
//			ImageUInt8 green = ThresholdImageOps.threshold(input.getBand(1),null,(float)100,true);
//			BinaryImageOps.logicAnd(red, green, binary);
			//BlurImageOps.gaussian(input.getBand(0), input.getBand(0), -1, 6, null);
		}
		else if(type.equals("blue")){
			ThresholdImageOps.threshold(input.getBand(2),binary,(float)70,false);
			//BlurImageOps.gaussian(binary, binary, 4, 6, null);
		}
		else if(type.equals("yellow")){
			ImageUInt8 red = ThresholdImageOps.threshold(input.getBand(0),null,(float)190,false);
			ImageUInt8 green = ThresholdImageOps.threshold(input.getBand(1),null,(float)90,false);
			ImageUInt8 blue = ThresholdImageOps.threshold(input.getBand(2),null,(float)50,true);
			BinaryImageOps.logicAnd(red, green, binary);
			BinaryImageOps.logicAnd(binary, blue, binary);
			//BlurImageOps.gaussian(binary, binary, 3, 6, null);
		}
		else if(type.equals("lines")){
			//BlurImageOps.gaussian(input.getBand(0), input.getBand(0), -1, 3, null);
			ThresholdImageOps.threshold(input.getBand(0),binary,(float)180,false);
		}


		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
		filtered = BinaryImageOps.dilate8(filtered, null);
		List<Contour> contours = BinaryImageOps.contour(filtered, 8, null);

		return contours;
	}
	/**
	 * 
	 * @param img
	 * @return
	 */
	public static Point2D_I32 findBall(BufferedImage img){
		List<Contour> contours = newHSVSegment("ball",img);//,segmentHSV(img, 6.21f, 0.88f));
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
	private static ArrayList<Point2D_I32> findMarkers(BufferedImage img, String type){
		if(type == "yellow"){
			List<Contour> contours = newHSVSegment("yellow",img);//segmentHSV(img, 0.7f, 0.95f));
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
			List<Contour> contours = newHSVSegment("blue",img);//segmentHSV(img, 3.31f, 0.538f));
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
	public static ArrayList<Point2D_I32> findBlueMarkers(BufferedImage img){
		return findMarkers(img,"blue");
	}
	/**
	 * Finds the positions of the Yellow Markers
	 * @param img
	 * @return
	 */
	public static ArrayList<Point2D_I32> findYellowMarkers(BufferedImage img){
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

		
		ImageUInt8 lowerValue = ThresholdImageOps.threshold(hsv.getBand(2),null,(float)90,true);
		ImageUInt8 upperValue = ThresholdImageOps.threshold(hsv.getBand(2),null,(float)50,false);
		
		ImageUInt8 lowerHue = ThresholdImageOps.threshold(hsv.getBand(0),null,0.69f,true);
		ImageUInt8 upperHue = ThresholdImageOps.threshold(hsv.getBand(0),null,1.13f,false);
		
		BinaryImageOps.logicAnd(lowerValue, upperValue, binary);
//		BinaryImageOps.logicAnd(lowerHue, binary, binary);
//		BinaryImageOps.logicAnd(upperHue, binary, binary);

		//
		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
		filtered = BinaryImageOps.dilate8(filtered, null);

		List<Contour> contoursFull = BinaryImageOps.contour(binary, 8, null);

		List<Contour> contours = new ArrayList<Contour>();
		
		for(int i = 0; i < contoursFull.size(); i++){
			System.out.println("contour size " + contoursFull.get(i).external.size());
			if(contoursFull.get(i).external.size() > 15 && contoursFull.get(i).external.size() < 30){
				contours.add(contoursFull.get(i));
			}
		}
		if(contours.size() == 0){
			//System.out.println("WARNING: " + contours.size() + " dots detected");
			return null;
		}
		else if(contours.size() > 1){
			System.out.println("WARNING: " + contours.size() + " dots detected, taking their mean");
			// Iverse distance weighting : http://en.wikipedia.org/wiki/Inverse_distance_weighting

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
			Point2D_I32 iwm = new Point2D_I32();
			double totalDistance = 0.0;
			for(Contour c: contours){
				Point2D_I32 curPoint = ContourUtils.getContourCentroid(c);
				double distanceTo = 1.0/Math.pow(
						Math.sqrt(Math.pow((windowSize/2 - curPoint.x),2.0) +
								  Math.pow((windowSize/2 - curPoint.y),2.0)),
						20.0);
				totalDistance += distanceTo;
				iwm.x += curPoint.x*distanceTo;
				iwm.y += curPoint.y*distanceTo;
//				list.add(new Tuple<Point2D_I32,Double>(p2,distanceTo));

				mean.x += curPoint.x;
				mean.y += curPoint.y;
			}
			iwm.x /= totalDistance;
			iwm.y /= totalDistance;
			
			iwm.x = iwm.x + x - windowSize/2;
			iwm.y = iwm.y + y - windowSize/2;
			
			
			mean.x /= contours.size();
			mean.y /= contours.size();
			
			mean.x = mean.x + x - windowSize/2;
			mean.y = mean.y + y - windowSize/2;
			
			// mean works better for now with window
			return mean;

			
//			return mean;
//			// using the median
//			Comparator<Tuple<Point2D_I32,Double>> comparator = new Comparator<Tuple<Point2D_I32,Double>>()
//					{
//
//				public int compare(Tuple<Point2D_I32,Double> tupleA,
//						Tuple<Point2D_I32,Double> tupleB)
//				{
//					if (tupleA.index < tupleB.index) return -1;
//					else if (tupleA.index == tupleB.index) return 0;
//					else return 1;
//				}
//
//					};
//		    	    
//		    Collections.sort(list, comparator);
//		    
//		    Point2D_I32 median =  list.get(list.size()/2).data; // get the median
//		    median.x = median.x + x - windowSize/2;
//		    median.y = median.y + y - windowSize/2;
//		    
//			return mean;
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
		if (prevPos == null || curPos == null)
			throw new Exception("math error - either point is null");
		else if (dx < 0 && dy > 0)
			theta = Math.PI/2 + Math.atan2(Math.abs(dy), Math.abs(dx));
		else if (dx < 0 && dy < 0)
			theta  = Math.PI/2 - Math.atan2(Math.abs(dy), Math.abs(dx));
		else if (dx > 0 && dy < 0)
			theta = Math.PI*2 - Math.atan2(Math.abs(dx), Math.abs(dy));
		else if (dx > 0 && dy > 0)
			theta = Math.PI + Math.atan2(Math.abs(dy), Math.abs(dx));
		else if (dx == 0 && dy == 0)
			throw new Exception("no direction");
			
		return theta;
	}


}








