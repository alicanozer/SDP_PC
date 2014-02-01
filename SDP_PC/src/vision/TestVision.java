package vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import boofcv.gui.image.ShowImages;

public class TestVision {
	public static void main(String[] args) throws IOException{
		// finding the field
		BufferedImage img1 = ImageIO.read(new File("test_images/00000001.jpg"));
		img1 = VisionOps.segmentHSV("Lines", img1, 0.5f, 0.4f);
		img1 = VisionOps.contourOps("", img1);
		ShowImages.showWindow(img1,"segmented field");
		// finding the yellow markers
		BufferedImage img2 = ImageIO.read(new File("test_images/00000006.jpg"));
		img2 = VisionOps.segmentHSV("Marker(I) Yellow", img2, 0.7f, 0.95f);
		img2 = VisionOps.contourOps("", img2);
		ShowImages.showWindow(img2,"segmented yellow is");
		//finding the ball
		BufferedImage img3 = ImageIO.read(new File("test_images/00000002.jpg"));
		img3= VisionOps.segmentHSV("Ball", img3, 0, 0.8f);
		img3 = VisionOps.contourOps("ball", img3);
		ShowImages.showWindow(img3,"segmented ball");
		img3 = ImageIO.read(new File("test_images/00000002.jpg"));
		System.out.println(VisionOps.findBall(img3));
	}
}
