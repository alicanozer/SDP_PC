package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.gui.image.ShowImages;

public class TestVision {
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
		
		float[] hues = {6.21f,0.7f,3.31f};
		float[] saturations = {0.88f,0.95f,0.538f};
		
		img1 = VisionOps.segmentMultiHSV(img1, hues, saturations)[0];
		img1 = VisionOps.contourOps("ball", img1);
		//ObjectLocations obs = VisionOps.getObjectLocations(img1);
			
		
		Graphics2D g = (Graphics2D) img1.getGraphics();
		
		
		// drawing X over ball
//		g.drawLine(obs.ball.x - 10, obs.ball.y , obs.ball.x + 10, obs.ball.y );
//		g.drawLine(obs.ball.x , obs.ball.y - 10, obs.ball.x , obs.ball.y + 10);
		
		

		// drawing X over ball
//		g.drawLine(ball.x - 10, ball.y , ball.x + 10, ball.y );
//		g.drawLine(ball.x , ball.y - 10, ball.x , ball.y + 10);
//		// drawing X over yellow markers
//		g.drawLine(yellowMarkers[0].x - 10, yellowMarkers[0].y, yellowMarkers[0].x + 10, yellowMarkers[0].y);
//		g.drawLine(yellowMarkers[0].x , yellowMarkers[0].y + 10, yellowMarkers[0].x , yellowMarkers[0].y - 10);
//		
//		g.drawLine(yellowMarkers[1].x - 10, yellowMarkers[1].y, yellowMarkers[1].x + 10, yellowMarkers[1].y);
//		g.drawLine(yellowMarkers[1].x , yellowMarkers[1].y + 10, yellowMarkers[1].x , yellowMarkers[1].y - 10);
//		// drawing X over blue markers
//		g.drawLine(blueMarkers[0].x - 10, blueMarkers[0].y, blueMarkers[0].x + 10, blueMarkers[0].y);
//		g.drawLine(blueMarkers[0].x , blueMarkers[0].y + 10, blueMarkers[0].x , blueMarkers[0].y - 10);
//		
//		g.drawLine(blueMarkers[1].x - 10, blueMarkers[1].y, blueMarkers[1].x + 10, blueMarkers[1].y);
//		g.drawLine(blueMarkers[1].x , blueMarkers[1].y + 10, blueMarkers[1].x , blueMarkers[1].y - 10);
		ShowImages.showWindow(img1,"identifying objects");
	}
}
