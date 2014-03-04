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
import javax.swing.JButton;
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
	private JButton         recalibrate;
	private long frameCounter = 0;
	private boolean debug;
	private PitchConstants consts;

	static JPanel panel1 = new JPanel();
	static JPanel panel2 = new JPanel();
	static JPanel panel3 = new JPanel();
	static JPanel panel4 = new JPanel();
	static JPanel panel5 = new JPanel();
	static JSlider slider1 = new JSlider(JSlider.VERTICAL,0,1000,25);
	static JSlider slider2 = new JSlider(JSlider.VERTICAL,0,1000,25);
	static JSlider slider3 = new JSlider(JSlider.VERTICAL,0,1000,25);
	static JSlider slider4 = new JSlider(JSlider.VERTICAL,0,1000,25);
	static JSlider slider5 = new JSlider(JSlider.VERTICAL,0,1000,25);
	
	private PitchColours colors;
	private ArrayList<Point2D_I32> whitePoints;
	private int frameLoop = 1;
	private Object lock = new Object();
	private Object lock2 = new Object();
	
	public synchronized float getRed() {
		synchronized(lock){
			return red;
		}
	}

	public synchronized void setRed(float red) {
		synchronized(lock){
			this.red = red;
		}
	}

	public synchronized float getYellow() {
		synchronized(lock){
			return yellow;
		}
	}

	public synchronized void setYellow(float yellow) {
		synchronized(lock){
			this.yellow = yellow;
		}
	}

	public synchronized float getBlue() {
		synchronized(lock){
			return blue;
		}
	}
	
	public synchronized float getPlate() {
		synchronized(lock){
			return plate;
		}
	}

	public synchronized float getBlack() {
		synchronized(lock){
			return black;
		}
	}
	
	public synchronized void setBlue(float blue) {
		synchronized(lock){
			this.blue = blue;
		}
	}
	
	public synchronized void setPlate(float plate) {
		synchronized(lock){
			this.plate = plate;
		}
	}
	
	public synchronized void setBlack(float black) {
		synchronized(lock){
			this.black = black;
		}
	}
	float red = 0.01f;
	float yellow = 0.001f;
	float blue = 0.001f;
	float plate = 0.01f;
	float black = 0.01f;

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
		recalibrate = new JButton("Recalibrate");
		recalibrate.setBounds(0, 380, 120, 40);
		frame.getContentPane().add(recalibrate);
		frame.getContentPane().add(label);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(this);
		frame.setVisible(true);
		frame.setSize(640, 480);
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
		
		
		float[] distanceThresholds = new float[5];
		distanceThresholds[0] = getRed();
		distanceThresholds[1] = getYellow();
		distanceThresholds[2] = getBlue();
		distanceThresholds[3] = getPlate(); // plate
		distanceThresholds[4] = getBlack(); //dot
		
//		System.out.println("red: " + colors.getRedValue()[0] + " " + colors.getRedValue()[1] + " " + colors.getRedValue()[2]);
//		System.out.println("yellow: " + colors.getYellowValue()[0] + " " + colors.getYellowValue()[1] + " " + colors.getYellowValue()[2]);
//		System.out.println("blue: " + colors.getBlueValue()[0] + " " + colors.getBlueValue()[1] + " " + colors.getBlueValue()[2]);
		
		try {
			ObjectLocations.updateObjectLocations(img,colors.getRedYellowBluePlateBlack(),distanceThresholds,3);
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
				if (slider1.getValueIsAdjusting()){
					synchronized(lock2){
						float sliderValue = (float)slider1.getValue()/100000; //get slider value and use it from here
						setRed(sliderValue);
						System.out.println("slider "+sliderValue);
					}
				}
			}
		});
		
		slider2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (slider2.getValueIsAdjusting()){
					synchronized(lock2){
						float sliderValue = (float)slider2.getValue()/100000; //get slider value and use it from here
						setYellow(sliderValue);
						System.out.println("slider2 "+sliderValue);
					}
				}
			}
		});
		
		slider3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				synchronized(lock2){
					if (slider3.getValueIsAdjusting()){
						float sliderValue = (float)slider3.getValue()/100000; //get slider value and use it from here
						setBlue(sliderValue);
						System.out.println("slider3 "+sliderValue);
					}
				}
			}
		});

		slider4.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				synchronized(lock2){
					if (slider4.getValueIsAdjusting()){
						float sliderValue = (float)slider4.getValue()/2000; //get slider value and use it from here
						setPlate(sliderValue);
						System.out.println("slider4 "+sliderValue);
					}
				}
			}
		});

		slider5.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				synchronized(lock2){
					if (slider5.getValueIsAdjusting()){
						float sliderValue = (float)slider5.getValue()/1000; //get slider value and use it from here
						setBlack(sliderValue);
						System.out.println("slider5 "+sliderValue);
					}
				}
			}
		});
		try {
			//ObjectLocations.updateObjectLocations(img,distanceThresholds);
		} catch (Exception e) {

		}
		g.draw(PitchConstants.region1);
		g.draw(PitchConstants.region2);
		g.draw(PitchConstants.region3);
		g.draw(PitchConstants.region4);
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
		java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>(); 
	    labelTable.put(new Integer(1000), new JLabel("0.01"));
	    labelTable.put(new Integer(0), new JLabel("0.0")); 
	    
		java.util.Hashtable<Integer,JLabel> labelTable2 = new java.util.Hashtable<Integer,JLabel>(); 
	    labelTable2.put(new Integer(1000), new JLabel("1.0"));
	    labelTable2.put(new Integer(0), new JLabel("0.0")); 
	    
		java.util.Hashtable<Integer,JLabel> labelTable3 = new java.util.Hashtable<Integer,JLabel>(); 
	    labelTable3.put(new Integer(1000), new JLabel("0.5"));
	    labelTable3.put(new Integer(0), new JLabel("0.0")); 
	    
		panel1.setLayout(new BorderLayout());
		slider1.setMinorTickSpacing(1);
		slider1.setMajorTickSpacing(25);
		slider1.setPaintTicks(true);
		slider1.setLabelTable(labelTable);
		slider1.setPaintLabels(true);
		panel1.add(slider1);
		
		
		
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
		
		panel4.setLayout(new BorderLayout());
		slider4.setMinorTickSpacing(1);
		slider4.setMajorTickSpacing(25);
		slider4.setPaintTicks(true);
		slider4.setLabelTable(labelTable3);
		slider4.setPaintLabels(true);
		panel4.add(slider4);

		panel5.setLayout(new BorderLayout());
		slider5.setMinorTickSpacing(1);
		slider5.setMajorTickSpacing(25);
		slider5.setPaintTicks(true);
		slider5.setLabelTable(labelTable2);
		slider5.setPaintLabels(true);
		panel5.add(slider5);
		
		JFrame frame = new JFrame("Red | Yellow | Blue | Plate | Dots");
		frame.setLayout(new GridLayout(1,3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setBounds(0, 0, 400, 1050);
		frame.add(panel1);
		frame.add(panel2);
		frame.add(panel3);
		frame.add(panel4);
		frame.add(panel5);
		//frame.pack();
		frame.setVisible(true);
		
	}
}
