package foo;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		ImageSink imageSink = new ImageSink("/Users/apljungquist/Vid/series1/");

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add contents to the window.
		final Filters filters = new Filters();
		
		JPanel mainPane = new JPanel(new BorderLayout());
		
		final ImageComponent liveImage = new ImageComponent();
		mainPane.add(liveImage);
		
		ColorPicker colorPicker = new ColorPicker(imageSink);
		colorPicker.setVisible(true);
		mainPane.add(colorPicker);
		
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		
		frame.getContentPane().add(mainPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);

		BufferedImage img;
		while ((img = imageSink.getNext()) != null) {
			liveImage.setImage(filters.nearestNeightbour(img, colorPicker.colors));
			liveImage.repaint();
			Thread.sleep(40);
		}
	}

}
