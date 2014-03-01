package vision;

import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_I32;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	private static String   device = "/dev/video0";
	private long lastFrame = System.currentTimeMillis(); 
	private VideoDevice     videoDevice;
	private FrameGrabber    frameGrabber;
	private JLabel          label;
	private JFrame          frame;
	private long frameCounter = 0;
	private boolean debug;
	private PitchConstants consts;
	static JPanel panel = new JPanel();
	static JPanel panel2 = new JPanel();
	static JPanel panel3 = new JPanel();
	static JSlider slider = new JSlider(JSlider.VERTICAL,0,1000,25);
	static JSlider slider2 = new JSlider(JSlider.VERTICAL,0,1000,25);
	static JSlider slider3 = new JSlider(JSlider.VERTICAL,0,1000,25);
	private PitchColours colors;
	private ArrayList<Point2D_I32> whitePoints;
	private int frameLoop = 1;

	float red = 0.0f;
	public synchronized float getRed() {
		return red;
	}

	public synchronized void setRed(float red) {
		this.red = red;
	}

	public synchronized float getYellow() {
		return yellow;
	}

	public synchronized void setYellow(float yellow) {
		this.yellow = yellow;
	}

	public synchronized float getBlue() {
		return blue;
	}

	public synchronized void setBlue(float blue) {
		this.blue = blue;
	}

	float yellow = 0.0f;
	float blue = 0.0f;


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
		CreateSlider();
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
	public void nextFrame(VideoFrame frame){
		if (frameLoop == 101) frameLoop = 1;
		BufferedImage img = frame.getBufferedImage();
//		BufferedImage img = null;
//		try {
//			img = ImageIO.read(new File("static_vision_images_2/image" + frameLoop+".jpg"));
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}//frame.getBufferedImage();
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
				BufferedImage pitchImg = img;
				List<Color> colorList = new ArrayList<Color>();
				//set white
				float[] whiteRGB = new float[3];
				ColorHsv.hsvToRgb(colors.getWhiteValue()[0], colors.getWhiteValue()[1], colors.getWhiteValue()[2], whiteRGB);
				Color white = new Color((int) whiteRGB[0], (int) whiteRGB[1], (int)whiteRGB[2]);
				colorList.add(white);
				//add pitch
				float[] pitchRGB = new float[3];
				ColorHsv.hsvToRgb(colors.getGreenPitchValue()[0], colors.getGreenPitchValue()[1], colors.getGreenPitchValue()[2], pitchRGB);
				Color pitch = new Color((int) pitchRGB[0], (int) pitchRGB[1], (int) pitchRGB[2]);
				colorList.add(pitch);
				whitePoints = KMeans.Cluster(pitchImg, 2, 1, colorList).get(0);
				ShowImages.showWindow(pitchImg,"pitchImg");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}
		long thisFrame = System.currentTimeMillis();
		int frameRate = (int) (1000 / (thisFrame - lastFrame));
		VisionRunner.sendFrame(new Frame(img,thisFrame));
		

		
		
		float[] distanceThresholds = new float[3];
		distanceThresholds[0] = red;
		distanceThresholds[1] = yellow;
		distanceThresholds[2] = blue;
		
		try {
			ObjectLocations.updateObjectLocations(img,colors.getRedYellowBlue(),distanceThresholds,3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Graphics2D g = (Graphics2D) label.getGraphics();
		g.drawImage(img, 0, 0, width,height, null);
		g.setColor(Color.white);
		g.drawString("FPS " + frameRate , 10, 10);


		CreateSlider();
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (slider.getValueIsAdjusting()){
					float sliderValue = (float)slider.getValue()/10000; //get slider value and use it from here
					setRed(sliderValue);
					System.out.println("slider "+sliderValue);
				}
			}
		});
		
		slider2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (slider2.getValueIsAdjusting()){
					float sliderValue = (float)slider2.getValue()/10000; //get slider value and use it from here
					setYellow(sliderValue);
					System.out.println("slider2 "+sliderValue);
				}
			}
		});
		
		slider3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (slider3.getValueIsAdjusting()){
					float sliderValue = (float)slider3.getValue()/10000; //get slider value and use it from here
					setBlue(sliderValue);
					System.out.println("slider3 "+sliderValue);
				}
			}
		});


		try {
			//ObjectLocations.updateObjectLocations(img,distanceThresholds);
		} catch (Exception e) {

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
				e.printStackTrace();
			}
		}
		g.dispose();
		frame.recycle();
		lastFrame = thisFrame;
		frameLoop++;
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
		
		
		
		panel2.setLayout(new BorderLayout());
		slider2.setMinorTickSpacing(1);
		slider2.setMajorTickSpacing(25);
		slider2.setPaintTicks(true);
		slider2.setLabelTable(labelTable);
		slider2.setPaintLabels(true);
		panel2.add(slider2);
		
		
		panel3.setLayout(new BorderLayout());
		slider3.setMinorTickSpacing(1);
		slider3.setMajorTickSpacing(25);
		slider3.setPaintTicks(true);
		slider3.setLabelTable(labelTable);
		slider3.setPaintLabels(true);
		panel3.add(slider3);

		JFrame frame = new JFrame("Red|slider Yellow|slider2 Blue|slider3");
		frame.setLayout(new GridLayout(1,3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setBounds(0, 0, 400, 650);
		frame.add(panel);
		frame.add(panel2);
		frame.add(panel3);
		//frame.pack();
		frame.setVisible(true);
		
	}
}
