package vision;

import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.ddogleg.struct.FastQueue;
import org.ddogleg.struct.FastQueueList;

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
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

/**
 * This class demonstrates how to perform a simple push-mode capture.
 * It starts the capture and display the video stream in a JLabel
 * @author bilyan, jason
 *
 */
public class SimpleViewer extends WindowAdapter implements CaptureCallback{
	private static int      width = 640, height = 480, std = V4L4JConstants.STANDARD_WEBCAM, channel = 0;
	private static String   device = "/dev/video0";
	private long lastFrame = System.currentTimeMillis(); // used for calculating FPS
	private int frameCounter = 0; // we let device capture frames from the 3rd onwards
	private static ArrayList<Polygon> regions = new ArrayList<Polygon>();

	private VideoDevice     videoDevice;
	private FrameGrabber    frameGrabber;

	private JLabel          label;
	private JFrame          frame;
	
	
	private static BufferedImage segOutputBall;
	private static List<Point2D_I32> ballPos;

	private static boolean lock = true;

	public static void main(String args[]){

		ObjectLocations.setYellowDefendingLeft(true);

		ObjectLocations.setYellowUs(true);
		
		
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new SimpleViewer();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("i failed miserably and now I must die to repent for my sins... ");
		}

	}

	/**
	 * Builds a WebcamViewer object
	 * @throws V4L4JException if any parameter if invalid
	 */
	public SimpleViewer(){
		// Initialise video device and frame grabber
		try {
			initFrameGrabber();
		} catch (V4L4JException e1) {
			System.err.println("Error setting up capture");
			e1.printStackTrace();

			// cleanup and exit
			cleanupCapture();
			return;
		}

		// create and initialise UI
		initGUI();

		// start capture
		try {
			frameGrabber.startCapture();
		} catch (V4L4JException e){
			System.err.println("Error starting the capture");
			e.printStackTrace();
		}
	}

	/**
	 * Initialises the FrameGrabber object
	 * @throws V4L4JException if any parameter if invalid
	 */
	private void initFrameGrabber() throws V4L4JException{
		videoDevice = new VideoDevice(device);
		frameGrabber = videoDevice.getJPEGFrameGrabber(width, height, channel, std, 80);
		frameGrabber.setCaptureCallback(this);
		width = frameGrabber.getWidth();
		height = frameGrabber.getHeight();
		System.out.println("Starting capture at "+width+"x"+height);
	}

	/** 
	 * Creates the UI components and initialises them
	 */
	private void initGUI(){
		frame = new JFrame();
		label = new JLabel();
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(this);
		frame.setVisible(true);
		frame.setSize(width, height);
	}

	/**
	 * this method stops the capture and releases the frame grabber and video device
	 */
	private void cleanupCapture() {
		try {
			frameGrabber.stopCapture();
		} catch (StateException ex) {
			// the frame grabber may be already stopped, so we just ignore
			// any exception and simply continue.
		}

		// release the frame grabber and video device
		videoDevice.releaseFrameGrabber();
		videoDevice.release();
	}

	/**
	 * Catch window closing event so we can free up resources before exiting
	 * @param e
	 */
	public void windowClosing(WindowEvent e) {
		cleanupCapture();

		// close window
		frame.dispose();            
	}


	@Override
	public void exceptionReceived(V4L4JException e) {
		// This method is called by v4l4j if an exception
		// occurs while waiting for a new frame to be ready.
		// The exception is available through e.getCause()
		e.printStackTrace();
	}
	/**
	 * gets new frames and applies the vision on them
	 */
	@Override
	public void nextFrame(VideoFrame frame) {
		BufferedImage img = frame.getBufferedImage();
		//pitch constants
		img = img.getSubimage(50, 70, 540, 316);
		width = 540;
		height = 316;
		long thisFrame = System.currentTimeMillis();
		int frameRate = (int) (1000 / (thisFrame - lastFrame));

		lastFrame = thisFrame;
		float[] hues = {0.5f}; 
		float[] saturations = {0.4f};

//		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(img,null,true,ImageFloat32.class);
//		BlurImageOps.gaussian(input.getBand(0), input.getBand(0), 5, -1, null);
//		BlurImageOps.gaussian(input.getBand(1), input.getBand(1), 5, -1, null);
//		img = ConvertBufferedImage.convertTo_F32(input, null, true);
		
		//img = VisionOps.newDisplay(VisionOps.newHSVSegment("yellow",img),img.getWidth(), img.getHeight());

		
		//img = VisionOps.contourOps("lines", VisionOps.segmentMultiHSV(img, hues, saturations)[0]);
		
//		ImageUInt8 gray = ConvertBufferedImage.convertFrom(img,(ImageUInt8)null);
//		ImageUInt8 edgeImage = new ImageUInt8(gray.width,gray.height);
//		ImageUInt8 filtered = BinaryImageOps.erode8(edgeImage,null);
//		filtered = BinaryImageOps.dilate8(filtered, null);
 
		// Create a canny edge detector which will dynamically compute the threshold based on maximum edge intensity
		// It has also been configured to save the trace as a graph.  This is the graph created while performing
		// hysteresis thresholding.
		// First parameter is edge blurring threshold
//		CannyEdge<ImageUInt8,ImageSInt16> canny = FactoryEdgeDetectors.canny(2,true, true, ImageUInt8.class, ImageSInt16.class);
// 
//		// The edge image is actually an optional parameter.  If you don't need it just pass in null
//		canny.process(gray,0.06f,0.15f,filtered); //0.08 , 0.15
 
		// First get the contour created by canny
		//List<EdgeContour> edgeContours = canny.getContours();
		// The 'edgeContours' is a tree graph that can be difficult to process.  An alternative is to extract
		// the contours from the binary image, which will produce a single loop for each connected cluster of pixels.
		// Note that you are only interested in external contours.
		
		Graphics2D g = (Graphics2D) label.getGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.setColor(Color.white);
		g.drawString("FPS " + frameRate , 10, 10);

		g.setColor(Color.BLACK);
		// the 3 regions
		g.drawLine(115, 0, 115, img.getHeight());
		g.drawLine(260, 0, 260, img.getHeight());
		g.drawLine(410, 0, 410, img.getHeight());
		
		
//		List<Contour> contoursUnfiltered = BinaryImageOps.contour(filtered, 8, null);
//		List<Contour> contours = new ArrayList<Contour>();
//		
//		try {
//			for(Contour c: contoursUnfiltered){
//				if(c.external.size() > 40 && c.external.size() < 130){
//					Point2D_I32 p = ContourUtils.getContourCentroid(c);
//					double Area = c.external.size()*c.external.size()/(4.0*Math.PI);
//
//					if(Area > 120.0 && Area < 350.0){ //270
//						g.setColor(Color.white);
//						g.drawLine(p.x - 10 , p.y, p.x + 10, p.y);
//						g.drawLine(p.x , p.y -10, p.x , p.y +10);
//					}
//				}
//				if(c.external.size() > 12 && c.external.size() < 22){
//					p = ContourUtils.getContourCentroid(c);
//					g.setColor(Color.white);
//					g.drawLine(p.x - 10 , p.y, p.x + 10, p.y);
//					g.drawLine(p.x , p.y -10, p.x , p.y +10);
//				}
//			}
//		} catch (Exception e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		

		
		// display the results
		//BufferedImage visualBinary = VisualizeBinaryData.renderBinary(edgeImage, null); 
		//BufferedImage visualCannyContour = VisualizeBinaryData.renderContours(edgeContours,null,gray.width,gray.height,null); 
//		try {
//			img = VisualizeBinaryData.renderExternal(contours, null,gray.width, gray.height, null);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			System.exit(0);
//		}
		
		try {



			ObjectLocations.updateObjectLocations(img);

			//System.out.print(VisionOps.getDirection(ballPrvPos, ballCurPos)); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		try {
			String[] objs = {"ball"};
			//ObjectLocations.drawAllDirections(g, objs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		try {
			ObjectLocations.drawCrosses(g);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		


		frame.recycle();
	}



}
