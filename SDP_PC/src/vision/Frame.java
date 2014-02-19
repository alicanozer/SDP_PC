package vision;

import java.awt.image.BufferedImage;

public class Frame {
	public BufferedImage getFrame() {
		return frame;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setFrame(BufferedImage frame) {
		this.frame = frame;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public long getNumber(){
		return this.number;
	}
	private BufferedImage frame;
	private long timeStamp;
	private long number;
	private static long frameCount = 0;	
	public Frame(BufferedImage frame, long timeStamp) {
		this.frame = frame;
		this.timeStamp = timeStamp;
		this.number = frameCount;
		frameCount++;
	}
	public String toString(){
		return frame.toString() + "@" + timeStamp;
	}

}
