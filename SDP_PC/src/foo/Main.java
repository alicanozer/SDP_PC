package foo;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		//I have a series of 1000 pictures that I use instead of a live video feed. The image sink allows me to access them one at a time.
		ImageSink imageSink = new ImageSink("/Users/apljungquist/Vid/series1/");
		
		//Create the window that will contain everything
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//The mainPane is just a container for everything else
		JPanel mainPane = new JPanel(new BorderLayout());
		
		//Add an image component to the window. This one will be continously updated.
		ImageComponent liveImage = new ImageComponent();
		mainPane.add(liveImage);
		
		// Add a color picker component to the window. This one will not be
		// updated from here but from user interaction. We will however query it
		// to learn what colors have been picked.
		ColorPicker colorPicker = new ColorPicker(imageSink);
		mainPane.add(colorPicker);
		
		//Set the layout to make all the components display correctly.
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		
		//Add our container to the window.
		frame.getContentPane().add(mainPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);

		
		Filters filters = new Filters();
		BufferedImage img;
		while ((img = imageSink.getNext()) != null) {
			//For every image in the image series, display the processed one using the configuration provided by the colorpicker
			liveImage.setImage(filters.nearestNeightbour(img, colorPicker.getColors()));
			liveImage.repaint();
			Thread.sleep(40);
		}
	}

}
