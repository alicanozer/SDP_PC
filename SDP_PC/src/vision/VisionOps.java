package vision;

import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public static MultiSpectral<ImageFloat32>[] segmentMultiHSV(BufferedImage image, float[] hues , float[] saturations, float[] distanceThresholds){

		if(!(hues.length == saturations.length)){
			return null;
		}
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(image,null,true,ImageFloat32.class);
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);


		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);

		// Pixels which are more than this different from the selected color are set to black

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
					if( dist2 <= distanceThresholds[k] ) {
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
			Polygon p = PointUtils.polygonFromContour(contours.get(0).internal.get(i));
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
			if(true){//new pitch
				// hue goes 0 to 2pi
				ImageUInt8 lowerHueBall = ThresholdImageOps.threshold(hsv.getBand(0),null, 6.10f,false);
				ImageUInt8 upperHueBall = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.10f,true);
				
				ImageUInt8 lowerSaturationBall = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.47f,false);
				BinaryImageOps.logicOr(lowerHueBall, upperHueBall, binary);
				BinaryImageOps.logicAnd(binary, lowerSaturationBall, binary);
				break;
			}
			else{
				// hue goes 0 to 2pi
				ImageUInt8 lowerHueBall = ThresholdImageOps.threshold(hsv.getBand(0),null, 6.10f,false);
				ImageUInt8 upperHueBall = ThresholdImageOps.threshold(hsv.getBand(0),binary, 0.10f,true);
				
				ImageUInt8 lowerSaturationBall = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.66f,false);
				BinaryImageOps.logicOr(lowerHueBall, upperHueBall, binary);
				BinaryImageOps.logicAnd(binary, lowerSaturationBall, binary);
				break;	
			}

		case "blue":
			if(true){ // new pitch but doesnt work
//				BlurImageOps.gaussian(hsv.getBand(0), hsv.getBand(0), 6, 6, null);
//				BlurImageOps.gaussian(hsv.getBand(1), hsv.getBand(1), 6, 6, null);

				
				ImageUInt8 lowerHueBlue = ThresholdImageOps.threshold(hsv.getBand(0),null, 3.00f,false); // was 3.00
				ImageUInt8 upperHueBlue = ThresholdImageOps.threshold(hsv.getBand(0),null, 3.90f,true); // was 3.60

				ImageUInt8 lowerSaturationBlue = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.01f,false); //was 0.50
				ImageUInt8 upperSaturationBlue = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.99f,true); //was 0.25

				
				
				//ImageUInt8 upperValueBlue = ThresholdImageOps.threshold(hsv.getBand(2),null, 140f,true); //was 0.25

				BinaryImageOps.logicAnd(lowerHueBlue, upperHueBlue, binary);
				BinaryImageOps.logicAnd(binary, upperSaturationBlue, binary);
				BinaryImageOps.logicAnd(binary, lowerSaturationBlue, binary);
				//BinaryImageOps.logicAnd(binary, upperValueBlue, binary);
			}
			else{// old pitch
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
			if(true){ // new pitch
				if(false) { // bright
//					BlurImageOps.gaussian(hsv.getBand(0), hsv.getBand(0), 4, 4, null);
//					BlurImageOps.gaussian(hsv.getBand(1), hsv.getBand(1), 4, 4, null);
					ImageUInt8 lowerHueYellow = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.34f,false); // was 0.34
					ImageUInt8 upperHueYellow = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.69f,true); // was 0.90

					ImageUInt8 lowerSaturationYellow = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.47f,false); // was 0.47
					ImageUInt8 upperSaturationYellow = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.86f,true); // was 0.86

					//values are 0..255

					BinaryImageOps.logicAnd(lowerHueYellow, upperHueYellow, binary);
					BinaryImageOps.logicAnd(binary, lowerSaturationYellow, binary);
					BinaryImageOps.logicAnd(binary, upperSaturationYellow, binary);
				}
				else{// dark
					BlurImageOps.gaussian(hsv.getBand(0), hsv.getBand(0), 2, 2, null);
					BlurImageOps.gaussian(hsv.getBand(1), hsv.getBand(1), 2, 7, null);
					
					ImageUInt8 lowerHueYellow = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.50f,false); // was 0.34
					ImageUInt8 upperHueYellow = ThresholdImageOps.threshold(hsv.getBand(0),null, 0.95f,true); // was 0.69

					ImageUInt8 lowerSaturationYellow = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.60f,false); // was 0.62
					ImageUInt8 upperSaturationYellow = ThresholdImageOps.threshold(hsv.getBand(1),null, 0.8f,true); // was 0.86

					//values are 0..255

					BinaryImageOps.logicAnd(lowerHueYellow, upperHueYellow, binary);
					BinaryImageOps.logicAnd(binary, lowerSaturationYellow, binary);
					BinaryImageOps.logicAnd(binary, upperSaturationYellow, binary);
				}


				
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
			Point2D_I32 p = PointUtils.getContourCentroid(c);
			if(type == "yellow"){
				if(

						c.external.size() > 18 && c.external.size() < 60 &&

						p.x > 15 && p.x < img.getWidth() - 15
						) contoursFiltered.add(c);
			}
			else{
				if(

						c.external.size() > 10 && c.external.size() < 60 &&


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
	
	
	public static List<Contour> extractContour(BufferedImage img){
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(img,null,true,ImageFloat32.class);
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);
		float[][] fs = new float[3][3];
		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);

		ImageUInt8 binary = new ImageUInt8(input.width,input.height);

		//BlurImageOps.gaussian(hsv.getBand(0), hsv.getBand(0), 4, 4, null);
		//BlurImageOps.gaussian(hsv.getBand(1), hsv.getBand(1), 4, 4, null);
		ImageUInt8 lowerHue = ThresholdImageOps.threshold(hsv.getBand(0),null, fs[1][0],false); 
		ImageUInt8 upperHue = ThresholdImageOps.threshold(hsv.getBand(0),null, fs[0][0],true); 

		ImageUInt8 lowerSaturation = ThresholdImageOps.threshold(hsv.getBand(1),null, fs[1][1],false); 
		ImageUInt8 upperSaturation = ThresholdImageOps.threshold(hsv.getBand(1),null, fs[0][1],true); 

		
		ImageUInt8 lowerValue = ThresholdImageOps.threshold(hsv.getBand(2),null, fs[1][2],false); 
		ImageUInt8 upperValue = ThresholdImageOps.threshold(hsv.getBand(2),null, fs[0][2],true); 

		BinaryImageOps.logicAnd(lowerHue, upperHue, binary);
		BinaryImageOps.logicAnd(binary, lowerSaturation, binary);
		BinaryImageOps.logicAnd(binary, upperSaturation, binary);
		BinaryImageOps.logicAnd(binary, lowerValue, binary);
		BinaryImageOps.logicAnd(binary, upperValue, binary);


		ImageUInt8 filtered = BinaryImageOps.erode4(binary,null);
		filtered = BinaryImageOps.dilate8(filtered, null);
		
		List<Contour> contoursUnfiltered = BinaryImageOps.contour(filtered, 8, null);
		List<Contour> contoursFiltered = new ArrayList<Contour>();
		/*
		 * Size filtering
		 */
//		boolean flag = true;
//		for(Contour c: contoursUnfiltered) {
//			Point2D_I32 p = PointUtils.getContourCentroid(c);
//			if(c.external.size() > 10 && c.external.size() < 300 && p.x > 15 && p.x < img.getWidth() - 15) contoursFiltered.add(c);
//		}

		return contoursUnfiltered;
	}
	
//	/**
//	 * Performs HSV color segmentation on the given frame for each color in the array colors and returns a list of list of contours
//	 * @param img frame image, as received from FrameHandler
//	 * @param colors a X(3)x3 array of floats, each row is a color, elements are H, S, and V, respectively. You should only ever give it RED, YELLOW, and BLUE
//	 * @param constatns the Pitch context - needed to infer which points belong to a defender and which points belong to an attacker for both yellow and blue
//	 * @param yellowLeft context - are we defending left, used to infer which points belong to a defender and which points belong to an attacker for both yellow and blue
//	 * @return a list of lists of contours with the order of the lists following the order of the contours
//	 */
//	List<List<Contour>> getSpecificObjects(BufferedImage img, float[][] colors, PitchConstants constatns, boolean yellowLeft, float[] distanceThresholds){
//		// initialising hue and sat arrays
//		float[] hues = new float[colors.length];
//		float[] sats = new float[colors.length];
//		
//		// populating hue and sats array. note that colors has 1 color per row, and in each row
//		// element 0 is hue, element 1 is saturation
//		for(int i = 0; i < colors.length; i++){
//			hues[i] = colors[i][0];
//			sats[i] = colors[i][1];
//		}
//		// getting segmented multispectral images
//		MultiSpectral<ImageFloat32>[] output = segmentMultiHSV(img, hues, sats, distanceThresholds);
//		ImageSInt32[] 
//		// creating binary images
//				
//		output[0].getBand(0).
//		ImageUInt8[] binary = new ImageUInt8[colors.length];
//		for(int i = 0; i < colors.length; i++){
//			binary[i] = new ImageUInt8(img.getWidth(), img.getHeight());
//		}
//		// get binary images from multispectral images by thresholding over any image band
//		// say 0 and only allowing those pixels to pass who have a value of greater than 0.0001
//		// this relies on the fact that multiSegmentHSV sets all relevant pixels to some != 0
//		// value and all not relevant pixels to 0
//		// after this operation
//		for(int i = 0; i < colors.length; i ++){
//			ThresholdImageOps.threshold(output[i].getBand(0), binary[i], 0.0001f, false);
//		}
//		List<ArrayList<Contour>> contours = new ArrayList<ArrayList<Contour>>(colors.length);
//		for(int i = 0; i < colors.length; i++){
//			contours.get(i) = BinaryImageOps.contour(binary[i],8,null);
//		}
//		return null;
//	}
//	
	
	public static HashMap<Integer,ArrayList<Point2D_I32>> getMultipleObjects(
			BufferedImage image, 
			float[][] colors,
			float[] distanceThresholds,
			boolean debug,
			int radius)
	{
		if(!(colors.length == distanceThresholds.length) || colors.length != 3){
			return null;
		}
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(image,null,true,ImageFloat32.class);
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);

		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);

		// Extract hue and saturation bands which are independent of intensity
		ImageFloat32 H = hsv.getBand(0);
		ImageFloat32 S = hsv.getBand(1);
		
//		BlurImageOps.gaussian(H, H, -1,radius,null);
//		BlurImageOps.gaussian(S, S, -1, radius,null);
		
		
		// initialising hue and sat arrays
		float[] hues = new float[colors.length];
		float[] sats = new float[colors.length];
		
		// populating hue and sats array. note that colors has 1 color per row, and in each row
		// element 0 is hue, element 1 is saturation
		for(int i = 0; i < colors.length; i++){
			hues[i] = colors[i][0];
			sats[i] = colors[i][1];
		}
		// Adjust the relative importance of Hue and Saturation
		float adjustUnits = (float)(Math.PI/2.0);

		HashMap<Integer,ArrayList<Point2D_I32>> objectsToLocations = new HashMap<Integer,ArrayList<Point2D_I32>>();
		objectsToLocations.put(0, new ArrayList<Point2D_I32>());
		objectsToLocations.put(1, new ArrayList<Point2D_I32>());
		objectsToLocations.put(2, new ArrayList<Point2D_I32>());
		
		boolean isBlack = true; // for drawing all non-object pixels as black
		for(int y = 0; y < hsv.height; y++ ) {
			for(int x = 0; x < hsv.width; x++ ) {
				for(int k = 0; k < hues.length; k++){
					// remember Hue is an angle in radians, so simple subtraction doesn't work
					float dh = UtilAngle.dist(H.unsafe_get(x,y), hues[k]);
					float ds = (S.unsafe_get(x,y) - sats[k])*adjustUnits;
					
					float dist = dh*dh + ds*ds;
					if(dist <= distanceThresholds[k] && PitchConstants.pitchPolygon.contains(x, y)) {
						// simply add all the points wherever they are
						objectsToLocations.get(k).add(new Point2D_I32(x,y));
						isBlack = false; // pixel is fron object, don't make it black
					}
				}
				// we've processed all colors for the given pixel and see
				// if it belongs to any object. if it doesn't and debug 
				// is on, we color it black
				if(debug && isBlack) {
					image.setRGB(x, y, 0);
				}
				isBlack = true;
			}
		}
		return objectsToLocations;
	}

	public static Point2D_I32 findBallFromMapping(HashMap<Integer,ArrayList<Point2D_I32>> objectsToLocations){
		return PointUtils.getListCentroid(objectsToLocations.get(0));
	}
	
	public static ArrayList<Point2D_I32> findYellowMarkersFromMapping(HashMap<Integer,ArrayList<Point2D_I32>> objectsToLocations, int middleLine){
		return findMarkersFromMapping(objectsToLocations,middleLine,1);
	}
	
	public static ArrayList<Point2D_I32> findBlueMarkersFromMapping(HashMap<Integer,ArrayList<Point2D_I32>> objectsToLocations, int middleLine){
		return findMarkersFromMapping(objectsToLocations,middleLine,2);
	}
	
	public static ArrayList<Point2D_I32> findMarkersFromMapping(HashMap<Integer,ArrayList<Point2D_I32>> objectsToLocations, int middleLine, int key){
		ArrayList<Point2D_I32> leftPoints = new ArrayList<Point2D_I32>();
		ArrayList<Point2D_I32> rightPoints = new ArrayList<Point2D_I32>();
		
		ArrayList<Point2D_I32> points = objectsToLocations.get(key);
		
		for(Point2D_I32 p: points){
			if(p.x < middleLine) leftPoints.add(p);
			else rightPoints.add(p);
		}
		ArrayList<Point2D_I32> markers = new ArrayList<Point2D_I32>(2);
		markers.add(PointUtils.getListCentroid(leftPoints));
		markers.add(PointUtils.getListCentroid(rightPoints));
		return markers;
	}
	
	
	/**
	 * 
	 * @param img
	 * @return
	 */
	public static Point2D_I32 findBall(BufferedImage img){
		List<Contour> contours = extractContour(img);
		if(contours.size() > 1 ){
//			System.out.println("WARNING: MORE THAN 1 ball detected");
			return null;
		}
		else if(contours.size() == 0){
//			System.out.println("WARNING: NO ball detected");
			return null;
		}
		else return PointUtils.getContourCentroid(contours.get(0));
	}
	
	
	
	
	/**
	 * finds the marker according to colour
	 * @param img
	 * @param type
	 * @return
	 */
	private static ArrayList<Point2D_I32> findMarkers(BufferedImage img, String type){
		if(type == "yellow"){
			List<Contour> contours = extractContour(img);//segmentHSV(img, 0.7f, 0.95f));
			ArrayList<Point2D_I32> ret = new ArrayList<Point2D_I32>();
			if(contours.size() == 1 ){
//				System.out.println("WARNING: ONLY ONE yellow marker was detected");
				ret.add(PointUtils.getContourCentroid(contours.get(0)));
				return ret;
			}
			else if(contours.size() != 2){
//				System.out.println("WARNING: " + contours.size() + " yellow marker were detected");
				return null;
			}

			ret.add(PointUtils.getContourCentroid(contours.get(0)));
			ret.add(PointUtils.getContourCentroid(contours.get(1)));

			return ret;
		}
		else if(type == "blue"){
			List<Contour> contours = extractContour(img);//segmentHSV(img, 3.31f, 0.538f));
			ArrayList<Point2D_I32> ret = new ArrayList<Point2D_I32>();
			if(contours.size() == 1 ){
//				System.out.println("WARNING: ONLY ONE blue marker was detected");
				ret.add(PointUtils.getContourCentroid(contours.get(0)));
				return ret;
			}
			else if(contours.size() != 2){
//				System.out.println("WARNING: " + contours.size() + " blue marker were detected");
				return null;
			}


			ret.add(PointUtils.getContourCentroid(contours.get(0)));
			ret.add(PointUtils.getContourCentroid(contours.get(1)));

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
 * @param fs 
 * @return
 */
	
	
	public static Point2D_I32 getDotPosition(
			BufferedImage img, 
			Point2D_I32 markerPosition, 
			int windowSize,
			int sigma,
			int maxIters,
			float[][] blackRange, 
			float[][] markerRange, 
			float[][] plateRange)
	{
		/*
		 * blurring teh image
		 */
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(img,null,true,ImageFloat32.class);
		BlurImageOps.gaussian(input.getBand(0), input.getBand(0), sigma, -1, null); // sigma is 2
		BlurImageOps.gaussian(input.getBand(1), input.getBand(1), sigma, -1, null);
		BlurImageOps.gaussian(input.getBand(2), input.getBand(2), sigma, -1, null);
		img = ConvertBufferedImage.convertTo_F32(input, null, true);
		/*
		 * converting HSV to RGB
		 */
		float[] dotColors = new float[3];
		ColorHsv.hsvToRgb(
				(blackRange[0][0]+blackRange[1][0])/2,
				(blackRange[0][1]+blackRange[1][1])/2,
				(blackRange[0][2]+blackRange[1][2])/2,
				dotColors);
		
		float[] markerColors = new float[3];
		ColorHsv.hsvToRgb(
				(markerRange[0][0]+markerRange[1][0])/2,
				(markerRange[0][1]+markerRange[1][1])/2,
				(markerRange[0][2]+markerRange[1][2])/2,
				markerColors);
		
		float[] plateColors = new float[3];
		ColorHsv.hsvToRgb(
				(plateRange[0][0]+plateRange[1][0])/2,
				(plateRange[0][1]+plateRange[1][1])/2,
				(plateRange[0][2]+plateRange[1][2])/2,
				plateColors);
		/*
		 * populating seeds array
		 */
		List<Color> seeds = new ArrayList<Color>();
		seeds.add(new Color((int)dotColors[0],(int)dotColors[1],(int)dotColors[2])); // black dot
		seeds.add(new Color((int)markerColors[0],(int)markerColors[1],(int)markerColors[2])); //green stuff
		seeds.add(new Color((int)plateColors[0],(int)plateColors[1],(int)plateColors[2])); //yellow
		/*
		 * running kMeans color clustering
		 */
		Map<Integer,ArrayList<Point2D_I32>> map =  KMeans.Cluster(img, 3, maxIters, seeds);
		/*
		 * filter all points that suck
		 */
		ArrayList<Point2D_I32> list = map.get(0); // points that map to the DOT cluster
		ArrayList<Point2D_I32> newList = new ArrayList<Point2D_I32>(); // filtered list of see above
		Point2D_I32 centre = new Point2D_I32(windowSize/2,windowSize/2);
		for(Point2D_I32 p: list){
			if(PointUtils.euclideanDistance(p, centre) > 14 && PointUtils.euclideanDistance(p, centre) < 15) newList.add(p);
		}
		
		Point2D_I32 mean = PointUtils.getListCentroid(newList);
		
		mean.x = mean.x + markerPosition.x - windowSize/2;
		mean.y = mean.y + markerPosition.y - windowSize/2;
		return mean;
	}

	public static Point2D_I32 getMeanDotNearMarker(
			BufferedImage img, 
			Point2D_I32 p, // this is a marker position 
			int windowSize)
	{
		if(p == null) return null;
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

		
		ImageUInt8 lowerValue = ThresholdImageOps.threshold(hsv.getBand(2),null, 20,false); 
		ImageUInt8 upperValue = ThresholdImageOps.threshold(hsv.getBand(2),null, 80,true); 

		BinaryImageOps.logicAnd(upperValue, lowerValue, binary);
		
		ImageUInt8 filtered = BinaryImageOps.dilate8(binary,null);
//		filtered = BinaryImageOps.dilate8(filtered, null);

		List<Contour> contoursFull = BinaryImageOps.contour(filtered, 8, null);

		List<Contour> contours = new ArrayList<Contour>();
		
		for(int i = 0; i < contoursFull.size(); i++){
			if(contoursFull.get(i).external.size() > 10 ){
				contours.add(contoursFull.get(i));
			}
		}
		if(contours.size() == 0){
			return null;
		}
		else if(contours.size() > 1){
			// using the mean
			Point2D_I32 mean = new Point2D_I32();
			Point2D_I32 iwm = new Point2D_I32();
			double totalDistance = 0.0;
			for(Contour c: contours){
				Point2D_I32 curPoint = PointUtils.getContourCentroid(c);
				double distanceTo = 1.0/Math.pow(
						Math.sqrt(Math.pow((windowSize/2 - curPoint.x),2.0) +
								  Math.pow((windowSize/2 - curPoint.y),2.0)),
						2.0);
				totalDistance += distanceTo;
				iwm.x += curPoint.x*distanceTo;
				iwm.y += curPoint.y*distanceTo;

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
		}
		else {
			Point2D_I32 p1 = PointUtils.getContourCentroid(contours.get(0));
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
		if(Math.abs(dx) < 2 && Math.abs(dy) < 2) return 0.0;

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
//			throw new Exception("no direction");
			return 0.0;
		return theta;
		
	}

	public static double distanceRGB(Color C1, Color C2){
		int rmean = (C1.getRed() + C2.getRed())/2; // the 255 here comes from implicit True Red
		int red = C1.getRed() - C2.getRed();
		int blue = C1.getBlue() - C2.getBlue();
		int green = C1.getGreen() - C2.getGreen();

		//reference - http://www.compuphase.com/cmetric.htm
		//return Math.sqrt((2.0 + rmean/256.0)*red*red + 4*green*green + (2 + (256 - rmean)/256.0)*blue*blue);
		return Math.sqrt(red*red + green*green + blue*blue);
	}
	public static Color getColorCentroid(BufferedImage img, ArrayList<Point2D_I32> l){
		if(l.size() == 0 || l == null) return null;
		int redMean = 0;
		int greenMean = 0;
		int blueMean = 0;
		for(Point2D_I32 p : l){
			Color c = new Color(img.getRGB(p.x, p.y));
			redMean += c.getRed();
			greenMean += c.getGreen();
			blueMean += c.getBlue();
		}
		int size = l.size();
		return new Color(redMean/size, greenMean/size, blueMean/size);
	}
	
	public static Color getColorMedian(BufferedImage img, ArrayList<Point2D_I32> l){
		int size = l.size();
		if(size == 0 || l == null) return null;
		
		int[] reds = new int[size];
		int[] greens = new int[size];
		int[] blues = new int[size];
		
		for(int i = 0; i < size; i++){
			Color c = new Color(img.getRGB(l.get(i).x, l.get(i).y));
			reds[i] = c.getRed();
			greens[i] = c.getGreen();
			blues[i] = c.getBlue();
		}
		Arrays.sort(reds);
		Arrays.sort(greens);
		Arrays.sort(blues);
		
		return new Color(reds[size/2],greens[size/2],blues[size/2]);
	}
}








