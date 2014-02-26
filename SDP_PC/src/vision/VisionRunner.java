package vision;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.SwingUtilities;

public class VisionRunner {
	/**
	 * blocking array that holds the N most recent frames.
	 */
	public static LinkedBlockingDeque<Frame> frameQueue;
	public static ArrayList<Boolean> set;
	public static int histLen;

	private VisionRunner(){};
	/**
	 * Starts of the vision in a separate thread
	 * @param debug boolean flag, true if we want to display debugging info
	 * @param consts PitchConstants object, use predefined static fields in the class itself
	 */
	public static void startDebugVision(final PitchConstants consts, int histLen){
		
		VisionRunner.histLen = histLen;
		frameQueue = new LinkedBlockingDeque<Frame>(histLen);
		
		ObjectLocations.setYellowDefendingLeft(true);
		ObjectLocations.setYellowUs(true);
		ObjectLocations.setConsts(consts);
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new FrameHandler(true,consts);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("i failed miserably and now I must die to repent for my sins... ");
		}
		System.err.println("Vision started successfully!");
	}
	public static void startNormalVision(int histLen){
		
	}
	public static void startStaticVideoVision(int histLen){
		
	}
	/**
	 * Send a frame to VisionRunner. The frame is sent and set asynchronously
	 * @param frame - the frame sent
	 */
	public  static void  sendFrame(Frame frame){
		if(!VisionRunner.frameQueue.offerLast(frame)){
			if(frameQueue.pollFirst() != null){
				VisionRunner.frameQueue.offerLast(frame);
			}
		}
	}
	public static boolean checkFrames(){
		Frame[] frames = new Frame[frameQueue.size()];
		frames = frameQueue.toArray(frames);
		for(int i = 0; i < frames.length -1; i++){
			if(frames[i].getNumber() > frames[i+1].getNumber()) return false;
		}
		return true;
	}
}
