package vision;

import georegression.struct.point.Point2D_I32;

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
	public static void main(String[] args) throws Exception{
		// finding the field

		BufferedImage img1 = ImageIO.read(new File("test_images/00000008.jpg"));

		BufferedImage img2 = ImageIO.read(new File("test_images/000000011.jpg"));

		ObjectLocations.setRegions(img2);
		ObjectLocations.setYellowDefendingLeft(true);
		ObjectLocations.setYellowUs(true);
		
		//ObjectLocations.updateObjectLocations(img2);
		
		//ObjectLocations.drawCrosses((Graphics2D) img2.getGraphics());
		float[] hues = {0.5f};
		float[] saturations = {0.4f};
		//img2 = ConvertBufferedImage.convertTo_F32(VisionOps.segmentMultiHSV(img2, hues, saturations)[0], null, true);
		img2 = VisionOps.contourOps("lines", VisionOps.segmentMultiHSV(img2, hues, saturations)[0]);
		
		ShowImages.showWindow(img2,"identifying objects");
		
		
		

		//img1 = VisionOps.contourOps("blue", img1);


		Graphics2D g = (Graphics2D) img1.getGraphics();
		ObjectLocations obs = VisionOps.getObjectLocations(img1);
		obs.drawCrosses(g);
		ShowImages.showWindow(img1,"identifying objects");
		//video test

		
		Point2D_I32 start = new Point2D_I32(15,6);
		Point2D_I32 end = new Point2D_I32(10,2);
		System.out.println(VisionOps.getDirection(start, end));


	}
}
