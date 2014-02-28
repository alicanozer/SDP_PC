package vision;

import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_I32;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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

	private boolean debug;
	private PitchConstants consts;
	static JPanel panel = new JPanel();
	static JSlider slider = new JSlider(JSlider.VERTICAL,0,1000,25);



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
		long thisFrame = System.currentTimeMillis();
		int frameRate = (int) (1000 / (thisFrame - lastFrame));
		lastFrame = thisFrame;
		//KMeans.ClusterHeaps(img, 6, 1, null,15);
		//img = VisionOps.newDisplay(VisionOps.newHSVSegment("blue",img),img.getWidth(), img.getHeight());
		Graphics2D g = (Graphics2D) label.getGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.setColor(Color.white);
		g.drawString("FPS " + frameRate , 10, 10);


		CreateSlider();
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (slider.getValueIsAdjusting()){
					float sliderValue = (float)slider.getValue()/1000; //get slider value and use it from here
					System.out.println(sliderValue);
				}
			}
		});


		try {
			ObjectLocations.updateObjectLocations(img);
		} catch (Exception e) {

		}
		if(debug){
			g.setColor(Color.BLACK);
			g.drawLine(consts.getRegion12X(), 0, consts.getRegion12X(), img.getHeight());
			g.drawLine(consts.getRegion23X(), 0, consts.getRegion23X(), img.getHeight());
			g.drawLine(consts.getRegion34X(), 0, consts.getRegion34X(), img.getHeight());
			try {
				ObjectLocations.drawCrosses(g);
			} catch (Exception e) {
			}
		}
		g.dispose();
		frame.recycle();
	}

	public static void CreateSlider(){
		panel.setLayout(new BorderLayout());
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(25);
		slider.setPaintTicks(true);
		java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();
		labelTable.put(new Integer(1000), new JLabel("1.0"));  
		labelTable.put(new Integer(750), new JLabel("0.75"));  
		labelTable.put(new Integer(500), new JLabel("0.50"));  
		labelTable.put(new Integer(250), new JLabel("0.25"));  
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
