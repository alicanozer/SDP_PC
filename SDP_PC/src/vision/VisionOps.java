package vision;

import georegression.metric.UtilAngle;

import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import boofcv.alg.color.ColorHsv;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
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
	public static BufferedImage showSelectedColor( String name , BufferedImage image , float hue , float saturation ){
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

		//ImageUInt8 binary = new ImageUInt8(input.width,input.height);


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
	public static BufferedImage contourOps(String type, BufferedImage inputImg) {
		List<Contour> contours = getContours(type,inputImg);

		//System.out.println("Number of contours " + contours.size());
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
	public static ArrayList<Polygon> getRegions(BufferedImage inputImg) {

		List<Contour> contours = getContours("",inputImg);
		ArrayList<Polygon> pols = new ArrayList<Polygon>();

		// in initial conditions, contours has only 1 element and it is the 
		// pitch. it has  at least 4 list of points as internal contours
		for(int i=0; i< contours.get(0).internal.size(); i++){
			Polygon p = ContourUtils.polygonFromContour(contours.get(0).internal.get(i));
			// since our polygons consists of individual pixel coords then the num
			// of points a good indication of the perimeter

			if(p.npoints > 100){
				pols.add(i,p); 
			}
		}
		return pols;
	}
	/**
	 * Gets the list of contours from applying binary thresholding to
	 * 
	 */
	public static List<Contour> getContours(String type, BufferedImage inputImg) {

		MultiSpectral<ImageFloat32> input= ConvertBufferedImage.convertFromMulti(inputImg, null, true, ImageFloat32.class);
		ImageUInt8 binary = new ImageUInt8(input.width,input.height);
		ImageSInt32 label = new ImageSInt32(input.width,input.height);
		if (type.equals("ball")){
			ThresholdImageOps.threshold(input.getBand(0),binary,(float)180,false);
		}
		else if (type.equals("")) {
			ThresholdImageOps.threshold(input.getBand(0),binary,(float)100,false);
		}
		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
		filtered = BinaryImageOps.dilate8(filtered, null);
		List<Contour> contours = BinaryImageOps.contour(filtered, 8, label);

		return contours;
	}
}
