package vision;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.alg.color.ColorHsv;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.gui.image.ShowImages;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;


public class TestVision {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException{
		// finding the field
		BufferedImage img1 = ImageIO.read(new File("test_images/00000007.jpg"));
		
//		VisionOps v = new VisionOps(img1);
//		try {
//			v.getObjectLocations();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		long begTest = new java.util.Date().getTime();
//		Point2D_I32 ball = VisionOps.findBall(img1);
//		Point2D_I32[] yellowMarkers = VisionOps.findYellowMarkers(img1);
//		Point2D_I32[] blueMarkers = VisionOps.findBlueMarkers(img1);
//		Point2D_I32[] dots = VisionOps.findDots(img1);
//		Double secs = new Double((new java.util.Date().getTime() - begTest)*0.001);
//		System.out.println("run time no threads" + secs + " secs");
//		
//		float[] hues = {6.21f,0.7f,3.14f}; // was 3.31
//		float[] saturations = {0.88f,0.95f,0.895f}; //0.538f
//		
//		//img1 = ConvertBufferedImage.convertTo_F32(VisionOps.segmentMultiHSV(img1, hues, saturations)[2], null ,true);
//		//img1 = VisionOps.contourOps("blue", VisionOps.segmentMultiHSV(img1, hues, saturations)[2]);
//		
//		
//		
//		//img1 = VisionOps.contourOps("blue", img1);
//
//
//		Graphics2D g = (Graphics2D) img1.getGraphics();
//		ObjectLocations obs = VisionOps.getObjectLocations(img1);
//		obs.drawCrosses(g);
//		ShowImages.showWindow(img1,"identifying objects");
		//cropped test
		BufferedImage img2 = ImageIO.read(new File("test_images/00000006.jpg"));
//		
//		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(img2,null,true,ImageFloat32.class);
//		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,img2.getWidth(),img2.getHeight(),3);
//		ImageUInt8 binary = new ImageUInt8(img2.getWidth(),img2.getHeight());

		// Convert into HSV
//		ColorHsv.rgbToHsv_F32(input,hsv);
//		
//		ThresholdImageOps.threshold(hsv.getBand(2),binary,(float)55,true);
//		
//		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
//		filtered = BinaryImageOps.dilate8(filtered, null);
//		
//		List<Contour> contours = BinaryImageOps.contour(filtered, 8, null);
//
//		img2 = VisualizeBinaryData.renderContours(
//				contours,
//				0xFFFFFF,
//				0xFF20FF,
//				img2.getWidth(),
//				img2.getHeight(),
//				null);
		//List<Contour> contours = VisionOps.getContours("dots",VisionOps.segmentMultiHSV(img2,hues2,saturations2)[0])   ;
		
		//img2 = VisionOps.contourOps("dots",VisionOps.segmentMultiHSV(img2,hues2,saturations2)[0]);
		//img2 = ConvertBufferedImage.convertTo_F32(VisionOps.segmentMultiHSV(img2,hues2,saturations2)[0],null,true);
		
		ObjectLocations obs = VisionOps.getObjectLocations(img2);
		
		obs.drawCrosses((Graphics2D) img2.getGraphics());
		
		ShowImages.showWindow(img2,"identifying objects");
		
		
		
	}
}
