package foo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageSink {

	private String path;
	private int counter;
	
	public ImageSink(String path) {
		this.path = path;
		this.counter = 1;
	}
	
	public ImageSink() {
		this("/Users/apljungquist/Vid/series1/");
	}
	
	public BufferedImage getNext() throws IOException {
		String filename = path+String.format("%08d", ++counter)+".jpg";
		System.out.println(filename);
		return ImageIO.read(new File(filename));
	}
	
	public BufferedImage getLatest() throws IOException {
		String filename = path+String.format("%08d", counter)+".jpg";
		System.out.println(filename);
		return ImageIO.read(new File(filename));
	}

}
