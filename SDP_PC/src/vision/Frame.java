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
	private BufferedImage frame;
	private long timeStamp;
	public Frame(BufferedImage frame, long timeStamp) {
		this.frame = frame;
		this.timeStamp = timeStamp;
	}
	

}
