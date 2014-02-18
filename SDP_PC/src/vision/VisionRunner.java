package vision;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.SwingUtilities;

public class VisionRunner {
	/**
	 * blocking array that holds the N most recent frames.
	 */
	public static ArrayBlockingQueue<Frame> frameQueue;
	public static ArrayList<Boolean> set;
	public static int histLen;
	private VisionRunner(){};
	/**
	 * Starts of the vision in a separate thread
	 * @param debug boolean flag, true if we want to display debugging info
	 * @param consts PitchConstants object, use predefined static fields in the class itself
	 */
	public static void start(final boolean debug, final PitchConstants consts, int histLen){
		VisionRunner.histLen = histLen;
		/*
		 * the true in the constructor ensures that the most recent frame is at the top of the queue
		 */
		frameQueue = new ArrayBlockingQueue<Frame>(histLen,true);
		
		ObjectLocations.setYellowDefendingLeft(true);
		ObjectLocations.setYellowUs(true);
		
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new FrameHandler(debug,consts);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("i failed miserably and now I must die to repent for my sins... ");
		}
		System.out.println("Vision started successfully!");
	}
	/**
	 * Send a frame to VisionRunner. The frame is sent and set asynchronously
	 * @param frame
	 */
	public  static void  sendFrame(Frame frame){
		if(!VisionRunner.frameQueue.offer(frame)){
			if(frameQueue.poll() != null){
				VisionRunner.frameQueue.offer(frame);
			}
		}
	}
	
}
