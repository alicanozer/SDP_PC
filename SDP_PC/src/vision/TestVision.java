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
		
		BufferedImage img3 = ImageIO.read(new File("test_images/cropped8.jpg"));
		
		BufferedImage img4 = ImageIO.read(new File("test_images/pitch2_1.png"));
		
		BufferedImage img5 = ImageIO.read(new File("test_images/000000013.jpg"));
//		ObjectLocations.setYellowDefendingLeft(true);
//		ObjectLocations.setYellowUs(true);
//		
//		ObjectLocations.updateObjectLocations(img2);
		
		//ObjectLocations.drawCrosses((Graphics2D) img2.getGraphics());
		float[] hues = {0.5f};
		float[] saturations = {0.4f};
		//img2 = ConvertBufferedImage.convertTo_F32(VisionOps.segmentMultiHSV(img2, hues, saturations)[0], null, true);
		//img2 = VisionOps.contourOps("lines", VisionOps.segmentMultiHSV(img2, hues, saturations)[0]);
		
//		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(img3,null,true,ImageFloat32.class);
//		
//		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,img3.getWidth(),img3.getHeight(),3);
//		ImageUInt8 binary = new ImageUInt8(input.width,input.height);
//
//		// Convert into HSV
//		ColorHsv.rgbToHsv_F32(input,hsv);
//
//		ThresholdImageOps.threshold(hsv.getBand(2),binary,(float)65,true);
//		
//		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
//		filtered = BinaryImageOps.dilate8(filtered, null);
//		List<Contour> contours = BinaryImageOps.contour(filtered, 8, null);
//		
//		
//		System.out.println(VisionOps.getMeanDotNearMarker(img3, new Point2D_I32(19,19), 30));
//		
//		img3 = VisualizeBinaryData.renderContours(
//				contours,
//				0xFFFFFF,
//				0xFF20FF,
//				input.getWidth(),
//				input.getHeight(),
//				null);
//		
//		ShowImages.showWindow(img3,"identifying objects");
//
//		
//		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(img3,null,true,ImageFloat32.class);
//		
//
//		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,img3.getWidth(),img3.getHeight(),3);
//		ImageUInt8 binary = new ImageUInt8(input.width,input.height);
//
//
//		// Convert into HSV
//		ColorHsv.rgbToHsv_F32(input,hsv);
//
//		ThresholdImageOps.threshold(hsv.getBand(2),binary,(float)65,true);
//		
//		ImageUInt8 filtered = BinaryImageOps.erode8(binary,null);
//		filtered = BinaryImageOps.dilate8(filtered, null);
//		List<Contour> contours = BinaryImageOps.contour(filtered, 8, null);
//		
//		
//		System.out.println(VisionOps.getMeanDotNearMarker(img3, new Point2D_I32(19,19), 30));
//		
//		img3 = VisualizeBinaryData.renderContours(
//				contours,
//				0xFFFFFF,
//				0xFF20FF,
//				input.getWidth(),
//				input.getHeight(),
//				null);
		
		//img4 = VisionOps.newDisplay(VisionOps.newHSVSegment("yellow",img4),img4.getWidth(), img4.getHeight());
		img5 = img5.getSubimage(50, 70, 540, 316);
		img5 = VisionOps.newDisplay(VisionOps.newHSVSegment("ball",img5),img5.getWidth(), img5.getHeight());
//		ObjectLocations.updateObjectLocations(img4);
//		ObjectLocations.drawCrosses((Graphics2D) img4.getGraphics());
		//ShowImages.showWindow(img4,"img4");
		ShowImages.showWindow(img5,"img5");
		
		
		

	}
}
