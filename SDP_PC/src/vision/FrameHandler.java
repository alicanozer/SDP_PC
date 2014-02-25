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
import Calculations.GoalInfo;
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
public class FrameHandler extends WindowAdapter implements CaptureCallback{
	private static int      width = 640, height = 480, std = V4L4JConstants.STANDARD_WEBCAM, channel = 0;
	private static String   device = "/dev/video1";
	private long lastFrame = System.currentTimeMillis(); 
	private VideoDevice     videoDevice;
	private FrameGrabber    frameGrabber;
	private JLabel          label;
	private JFrame          frame;
	private long frameCounter = 0;
	private boolean debug;
	private PitchConstants consts;
	private PitchColours colors;
	private ArrayList<Point2D_I32> whitePoints;



	public FrameHandler(boolean debug, PitchConstants consts){
		this.debug = debug;
		this.consts = consts;


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

		width = consts.getCroppedWidth();
		height = consts.getCroppedHeight();

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
		img = img.getSubimage(consts.getUpperLeftX(), consts.getUpperLeftY(), consts.getCroppedWidth(), consts.getCroppedHeight());
		if(frameCounter < 3){
			frame.recycle();
			frameCounter++;
			return;
		}
		else if (frameCounter == 3){
			frameCounter++;
			colors = ExampleSegmentColor.selectColoursOfPitch(img);
			try {
				List<Color> colorsList = new ArrayList<Color>();
				BufferedImage pitchImg = img;
				//set white
				float[] fsW = new float[3];
				ColorHsv.hsvToRgb(
						(colors.getWhiteValue()[0][0]+colors.getWhiteValue()[1][0])/2,
						(colors.getWhiteValue()[0][1]+colors.getWhiteValue()[1][1])/2,
						(colors.getWhiteValue()[0][2]+colors.getWhiteValue()[1][2])/2,
						fsW);

				Color white = new Color((int)fsW[0],(int)fsW[1],(int)fsW[2]);
				List<Color> colorList = new ArrayList<Color>();
				colorList.add(white);
				//add pitch
				float[] fsP = new float[3];
				ColorHsv.hsvToRgb(
						(colors.getGreenPitchValue()[0][0]+colors.getGreenPitchValue()[1][0])/2,
						(colors.getGreenPitchValue()[0][1]+colors.getGreenPitchValue()[1][1])/2,
						(colors.getGreenPitchValue()[0][2]+colors.getGreenPitchValue()[1][2])/2,
						fsP);

				Color pitch = new Color((int)fsP[0],(int)fsP[1],(int)fsP[2]);

				colorList.add(pitch);
				
				whitePoints = KMeans.Cluster(pitchImg, 2, 1, colorList).get(0);
				ShowImages.showWindow(pitchImg,"pitchImg");
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
		
		
		long thisFrame = System.currentTimeMillis();
		int frameRate = (int) (1000 / (thisFrame - lastFrame));
		
		VisionRunner.sendFrame(new Frame(img,thisFrame));
		
//		System.out.println("max blue:" + colors.getBlueValue()[0][0] + " " + colors.getBlueValue()[0][1] + " " + colors.getBlueValue()[0][2]);
//		System.out.println("min blue:" + colors.getBlueValue()[1][0] + " " + colors.getBlueValue()[1][1] + " " + colors.getBlueValue()[1][2]);
//		
//		System.out.println("max black:" + colors.getBlackValue()[0][0] + " " + colors.getBlackValue()[0][1] + " " + colors.getBlackValue()[0][2]);
//		System.out.println("min black:" + colors.getBlackValue()[1][0] + " " + colors.getBlackValue()[1][1] + " " + colors.getBlackValue()[1][2]);
//		
//		System.out.println("max yellow:" + colors.getYellowValue()[0][0] + " " + colors.getYellowValue()[0][1] + " " + colors.getYellowValue()[0][2]);
//		System.out.println("min yellow:" + colors.getYellowValue()[1][0] + " " + colors.getYellowValue()[1][1] + " " + colors.getYellowValue()[1][2]);
//		
//		System.out.println("max red:" + colors.getRedValue()[0][0] + " " + colors.getRedValue()[0][1] + " " + colors.getRedValue()[0][2]);
//		System.out.println("min red:" + colors.getRedValue()[1][0] + " " + colors.getRedValue()[1][1] + " " + colors.getRedValue()[1][2]);
		
		
		//KMeans.ClusterHeaps(img, 6, 1, null,15);
		img = VisionOps.newDisplay(VisionOps.extractContour(img, colors.getRedValue()),img.getWidth(), img.getHeight());
		Graphics2D g = (Graphics2D) label.getGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.setColor(Color.white);
		g.drawString("FPS " + frameRate , 10, 10);
		

		try {
			ObjectLocations.updateObjectLocations(img,colors);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(debug){
			g.setColor(Color.BLACK);
			g.drawLine(consts.getRegion12X(), 0, consts.getRegion12X(), img.getHeight());
			g.drawLine(consts.getRegion23X(), 0, consts.getRegion23X(), img.getHeight());
			g.drawLine(consts.getRegion34X(), 0, consts.getRegion34X(), img.getHeight());
			
			//Drawing Goal Markers
			g.setColor(Color.WHITE);
			
			g.drawLine(GoalInfo.getLeftGoalCenterNew().getX()-10, GoalInfo.getLeftGoalCenterNew().getY(), GoalInfo.getLeftGoalCenterNew().getX()+10, GoalInfo.getLeftGoalCenterNew().getY());
			g.drawLine(GoalInfo.getLeftGoalTopNew().getX()-10, GoalInfo.getLeftGoalTopNew().getY(), GoalInfo.getLeftGoalTopNew().getX()+10, GoalInfo.getLeftGoalTopNew().getY());
			g.drawLine(GoalInfo.getLeftGoalBottomNew().getX()-10, GoalInfo.getLeftGoalBottomNew().getY(),GoalInfo.getLeftGoalBottomNew().getX()+10, GoalInfo.getLeftGoalBottomNew().getY());

			g.drawLine(GoalInfo.getRightGoalCenterNew().getX()-10, GoalInfo.getRightGoalCenterNew().getY(), GoalInfo.getRightGoalCenterNew().getX()+10, GoalInfo.getRightGoalCenterNew().getY());
			g.drawLine(GoalInfo.getRightGoalTopNew().getX()-10, GoalInfo.getRightGoalTopNew().getY(), GoalInfo.getRightGoalTopNew().getX()+10, GoalInfo.getRightGoalTopNew().getY());
			g.drawLine(GoalInfo.getRightGoalBottomNew().getX()-10, GoalInfo.getRightGoalBottomNew().getY(), GoalInfo.getRightGoalBottomNew().getX()+10, GoalInfo.getRightGoalBottomNew().getY());
			
			
			try {
				ObjectLocations.drawCrosses(g);
			} catch (Exception e) {
			}
		}
		g.dispose();
		frame.recycle();
		lastFrame = thisFrame;
	}
}
