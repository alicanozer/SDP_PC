package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	static JPanel panel = new JPanel();
	static JSlider slider = new JSlider(JSlider.VERTICAL,0,1000,25);
	private static PitchConstants consts;
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		// finding the field

		BufferedImage img1 = ImageIO.read(new File("test_images/00000008.jpg"));

		BufferedImage img2 = ImageIO.read(new File("test_images/000000011.jpg"));

		BufferedImage img3 = ImageIO.read(new File("test_images/cropped8.jpg"));

		BufferedImage img4 = ImageIO.read(new File("test_images/pitch2_1.png"));


		BufferedImage img5 = ImageIO.read(new File("test_images/000000014.jpg"));


		img5 = img5.getSubimage(55, 90, 540, 320);
		//System.out.println(PitchConstants.newPitch.getRegion12X());

		/*float[] hues = {0.5f};
		float[] saturations = {0.4f};

		//img4 = VisionOps.newDisplay(VisionOps.newHSVSegment("yellow",img4),img4.getWidth(), img4.getHeight());
		img5 = img5.getSubimage(50, 70, 540, 316);
		img5 = VisionOps.newDisplay(VisionOps.newHSVSegment("ball",img5),img5.getWidth(), img5.getHeight());

		ShowImages.showWindow(img5,"img5")*/;



		ImageUInt8 gray = ConvertBufferedImage.convertFrom(img5,(ImageUInt8)null);
		ImageUInt8 edgeImage = new ImageUInt8(gray.width,gray.height);
		ImageUInt8 filtered = BinaryImageOps.erode8(edgeImage,null);
		filtered = BinaryImageOps.dilate8(filtered, null);

		CannyEdge<ImageUInt8,ImageSInt16> canny = FactoryEdgeDetectors.canny(5,true, true, ImageUInt8.class, ImageSInt16.class);
		canny.process(gray,0.08f,0.15f,filtered);

		List<Contour> contours = BinaryImageOps.contour(filtered, 8, null);
		BufferedImage visualEdgeContour = VisualizeBinaryData.renderExternal(contours, null,gray.width, gray.height, null);


		List<Contour> contoursT = new ArrayList<Contour>();

		Graphics2D g = visualEdgeContour.createGraphics();
		g.drawImage(visualEdgeContour, 0, 0, visualEdgeContour.getWidth(), visualEdgeContour.getHeight(), null);
		g.setColor(Color.RED);
		g.drawString("test ", 10, 10);

		ArrayList<Point2D_I32> dataPoints = new ArrayList<Point2D_I32>();

		for(int i = 0; i < contours.size(); i++){
			if(contours.get(i).external.size() > 10 && contours.get(i).external.size() < 200){
				contoursT.add(contours.get(i));
				Point2D_I32 p = PointUtils.getContourCentroid(contours.get(i));
				if (p.y>15&&p.y<280&&p.x>20&&p.x<510){ // filter points by horizontal and region boundaries
					System.out.println("p at "+p.x+" , "+p.y);
					dataPoints.add(p);
					g.setColor(Color.WHITE);
					g.drawLine(p.x-5, p.y-5, p.x+5, p.y+5);
					g.drawLine(p.x+5, p.y-5, p.x-5, p.y+5);}
			}

		}


		g.drawLine(PitchConstants.newPitch.getRegion12X(),0,PitchConstants.newPitch.getRegion12X(),500);
		g.drawLine(PitchConstants.newPitch.getRegion23X(),0,PitchConstants.newPitch.getRegion23X(),500);
		g.drawLine(PitchConstants.newPitch.getRegion34X(),0,PitchConstants.newPitch.getRegion34X(),500);

		// horizontal boundaries - table 2
		g.drawLine(0,15,600,15);
		g.drawLine(0,280,600,280);

		//inner table boundaries - table 2
		g.drawLine(20,0,20,500); 
		g.drawLine(510,0,510,500);

		ArrayList<Point2D_I32> clusterp = KMeans.Cluster2DPoints(dataPoints, 4, 200);
		System.out.println(clusterp);		

		for (int x = 0;x<clusterp.size();x++){
			g.setColor(Color.RED);
			Point2D_I32 p = clusterp.get(x);
			g.drawLine(p.x-5, p.y-5, p.x+5, p.y+5);
			g.drawLine(p.x+5, p.y-5, p.x-5, p.y+5);
		}

		

		g.setColor(Color.GREEN);
		g.drawLine(60, 150, 70, 150);
		g.drawLine(200, 150, 210, 150);
		g.drawLine(340, 150, 350, 150);
		g.drawLine(470, 150, 480, 150);
		
		/*double[] testList = {5.3,1.0,2,0};
		System.out.println(KMeans.min(testList ));*/


		//ShowImages.showWindow(visualEdgeContour,"K means cluster centers");
		
		CreateSlider();
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (slider.getValueIsAdjusting()){
					System.out.println((float)slider.getValue()/10000);
				}
			}
		});
		
	}
	

	public static void CreateSlider(){
		panel.setLayout(new BorderLayout());
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(25);
		slider.setPaintTicks(true);
		java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>(); 
	    labelTable.put(new Integer(1000), new JLabel("0.1"));
	    labelTable.put(new Integer(0), new JLabel("0.0")); 
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		panel.add(slider);
		
		JFrame frame = new JFrame("Slider");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setPreferredSize(new Dimension(200,650));
		frame.setContentPane(panel);;
		frame.pack();frame.setVisible(true);
		

	}
	
	
}
