package vision;

import georegression.struct.point.Point2D_I32;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import boofcv.alg.color.ColorHsv;
import boofcv.alg.feature.detect.edge.CannyEdge;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.feature.detect.edge.FactoryEdgeDetectors;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.gui.image.ShowImages;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt16;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;


public class TestVision {
	private static PitchConstants consts;
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		// finding the field

		BufferedImage img1 = ImageIO.read(new File("test_images/00000008.jpg"));

		BufferedImage img2 = ImageIO.read(new File("test_images/000000011.jpg"));
		
		BufferedImage img3 = ImageIO.read(new File("test_images/cropped8.jpg"));
		
		BufferedImage img4 = ImageIO.read(new File("test_images/pitch2_1.png"));
		
		BufferedImage img5 = ImageIO.read(new File("test_images/00000023.jpg"));
		
		
		
		img5 = img5.getSubimage(55, 90, 540, 320);
		//System.out.println(PitchConstants.newPitch.getRegion12X());

		/*float[] hues = {0.5f};
		float[] saturations = {0.4f};

		//img4 = VisionOps.newDisplay(VisionOps.newHSVSegment("yellow",img4),img4.getWidth(), img4.getHeight());
		img5 = img5.getSubimage(50, 70, 540, 316);
		img5 = VisionOps.newDisplay(VisionOps.newHSVSegment("ball",img5),img5.getWidth(), img5.getHeight());

		ShowImages.showWindow(img5,"img5")*/;
		
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(img3,null,true,ImageFloat32.class);
		
		BlurImageOps.gaussian(input.getBand(0), input.getBand(0), 2, -1, null);
		BlurImageOps.gaussian(input.getBand(1), input.getBand(1), 2, -1, null);
		BlurImageOps.gaussian(input.getBand(2), input.getBand(2), 2, -1, null);
		

		img3 = ConvertBufferedImage.convertTo_F32(input, null, true);
		img3 = img3.getSubimage(1, 1, 29, 29);
		List<Color> seeds = new ArrayList<Color>(2);
		seeds.add(new Color(29,43,20)); // black dot
		seeds.add(new Color(13,91,48)); //green stuff
		seeds.add(new Color(148,98,22)); //yellow
		Map<Integer,ArrayList<Point2D_I32>> map =  KMeans.Cluster(img3, 3, 20, seeds);
		
		//didnt work
		//Point2D_I32 median = PointUtils.getPointMedian(map.get(0));
		//will it work?
		ArrayList<Point2D_I32> list = map.get(0);
		ArrayList<Point2D_I32> newList = new ArrayList<Point2D_I32>();
		Point2D_I32 centre = new Point2D_I32(17,23);
		for(Point2D_I32 p: list){
			if(PointUtils.euclideanDistance(p, centre) > 14 && PointUtils.euclideanDistance(p, centre) < 15) newList.add(p);
		}
		
		Point2D_I32 mean = PointUtils.getListCentroid(newList);
		
		img3.createGraphics().drawLine(mean.x -2, mean.y, mean.x+2, mean.y);
		img3.createGraphics().drawLine(mean.x , mean.y -2, mean.x, mean.y +2);
		
		ShowImages.showWindow(img3,"img3");
		
//		ImageUInt8 gray = ConvertBufferedImage.convertFrom(img5,(ImageUInt8)null);
//		ImageUInt8 edgeImage = new ImageUInt8(gray.width,gray.height);
//		ImageUInt8 filtered = BinaryImageOps.erode8(edgeImage,null);
//		filtered = BinaryImageOps.dilate8(filtered, null);
//		
//		CannyEdge<ImageUInt8,ImageSInt16> canny = FactoryEdgeDetectors.canny(5,true, true, ImageUInt8.class, ImageSInt16.class);
//		canny.process(gray,0.08f,0.15f,filtered);
//		
//		List<Contour> contours = BinaryImageOps.contour(filtered, 8, null);
//		BufferedImage visualEdgeContour = VisualizeBinaryData.renderExternal(contours, null,gray.width, gray.height, null);
//		
//		
//		List<Contour> contoursT = new ArrayList<Contour>();
//		
//		Graphics2D g = visualEdgeContour.createGraphics();
//		g.drawImage(visualEdgeContour, 0, 0, visualEdgeContour.getWidth(), visualEdgeContour.getHeight(), null);
//		g.setColor(Color.RED);
//		g.drawString("test ", 10, 10);
//		
//		ArrayList<Point2D_I32> dataPoints = new ArrayList<Point2D_I32>();
//		
//		for(int i = 0; i < contours.size(); i++){
//			if(contours.get(i).external.size() > 15 && contours.get(i).external.size() < 100){
//				contoursT.add(contours.get(i));
//				Point2D_I32 p = PointUtils.getContourCentroid(contours.get(i));
//				if (p.y>15&&p.y<280&&p.x>20&&p.x<510){ // filter points by horizontal and region boundaries
//				System.out.println("p at "+p.x+" , "+p.y);
//				dataPoints.add(p);
//				g.setColor(Color.WHITE);
//				g.drawLine(p.x-5, p.y-5, p.x+5, p.y+5);
//				g.drawLine(p.x+5, p.y-5, p.x-5, p.y+5);}
//			}
//		}
//		
//		g.drawLine(PitchConstants.newPitch.getRegion12X(),0,PitchConstants.newPitch.getRegion12X(),500);
//		g.drawLine(PitchConstants.newPitch.getRegion23X(),0,PitchConstants.newPitch.getRegion23X(),500);
//		g.drawLine(PitchConstants.newPitch.getRegion34X(),0,PitchConstants.newPitch.getRegion34X(),500);
//		
//		// horizontal boundaries - table 2
//		g.drawLine(0,15,600,15);
//		g.drawLine(0,280,600,280);
//		
//		//inner table boundaries - table 2
//		g.drawLine(20,0,20,500); 
//		g.drawLine(510,0,510,500);
//		
//		
//		System.out.println(KMeans.Cluster2DPoints(dataPoints, 5, 20));		
//		
//		ShowImages.showWindow(visualEdgeContour,"Contour from Canny Binary");

	}
}
