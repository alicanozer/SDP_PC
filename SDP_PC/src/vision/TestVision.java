package vision;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.gui.image.ShowImages;


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
		Double secs = new Double((new java.util.Date().getTime() - begTest)*0.001);
		System.out.println("run time no threads" + secs + " secs");
		
		float[] hues = {6.21f,0.7f,3.14f}; // was 3.31
		float[] saturations = {0.88f,0.95f,0.895f}; //0.538f
		
		//img1 = ConvertBufferedImage.convertTo_F32(VisionOps.segmentMultiHSV(img1, hues, saturations)[2], null ,true);
		//img1 = VisionOps.contourOps("blue", VisionOps.segmentMultiHSV(img1, hues, saturations)[2]);
		
		
		
		//img1 = VisionOps.contourOps("blue", img1);


		Graphics2D g = (Graphics2D) img1.getGraphics();
		ObjectLocations obs = VisionOps.getObjectLocations(img1);
		obs.drawCrosses(g);
		ShowImages.showWindow(img1,"identifying objects");
		//video test

	}
}
