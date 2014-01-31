package vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import boofcv.gui.image.ShowImages;

public class TestVision {
	public static void displayImage() throws IOException{
		BufferedImage img = ImageIO.read(new File("test_images/00000006.jpg"));
		img = VisionOps.showSelectedColor("Lines", img, 0.5f, 0.4f);
		img = VisionOps.contourOps("", img);
		
		ShowImages.showWindow(img,"segmented field");
	}
}
