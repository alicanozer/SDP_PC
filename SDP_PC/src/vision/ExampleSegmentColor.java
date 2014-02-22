package vision;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import boofcv.alg.color.ColorHsv;
import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;

public class ExampleSegmentColor {
	static boolean flag = false;
	static float[] colour = new float[3];
	
	/**
	 * Shows a color image and allows the user to select a pixel, convert it to HSV, print
	 * the HSV values, and calls the function below to display similar pixels.
	 */
	public static void getClickedColor( final BufferedImage image ) {
		ImagePanel gui = new ImagePanel(image);
		gui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				float[] color = new float[3];
				int rgb = image.getRGB(e.getX(),e.getY());
				ColorHsv.rgbToHsv((rgb >> 16) & 0xFF,(rgb >> 8) & 0xFF , rgb&0xFF,color);
				ExampleSegmentColor.colour = color;
				ExampleSegmentColor.flag = true;
				return;
			}
		});

		ShowImages.showWindow(gui,"Color Selector");
//		float[] colour = new float[3];
//		return colour;
	}
	
	

	public static void looper(){
		while(flag == false){
			try {
		        Thread.sleep(1);
		    } catch (InterruptedException e) {
		        // We've been interrupted: no more messages.
		        return;
		    }
		}
		return;
	}
		

	public PitchColours selectColoursOfPitch(BufferedImage image) {
		//BufferedImage image = UtilImageIO.loadImage("test_images/00000008.jpg");
		float[] blue = new float[3];
		float[] yellow = new float[3];
		float[] black = new float[3];
		float[] red = new float[3];
		float[] greenPlate = new float[3];
		float[] greenPitch = new float[3];
		float[] white = new float[3];
		
		
		System.out.println("Please click a blue object");
		getClickedColor(image);
		looper();
		blue = colour;
		flag = false;
		
		System.out.println("Please click a yellow object");
		getClickedColor(image);
		looper();
		yellow = colour;
		flag = false;
		
		System.out.println("Please click a black dot object");
		getClickedColor(image);
		looper();
		black = colour;
		flag = false;
		
		System.out.println("Please click the red ball");
		getClickedColor(image);
		looper();
		red = colour;
		flag = false;
		
		System.out.println("Please click a green plate object");
		getClickedColor(image);
		looper();
		greenPlate = colour;
		flag = false;
		
		System.out.println("Please click a green pitch object");
		getClickedColor(image);
		looper();
		greenPitch = colour;
		flag = false;
		
		System.out.println("Please click a white edge object");
		getClickedColor(image);
		looper();
		white = colour;
		flag = false;
		
		System.out.println("You have selected all objects");
//		System.out.println("h = " + blue[0] + " s = " + blue[1] + " v = " + blue[2]);
//		System.out.println("h = " + yellow[0] + " s = " + yellow[1] + " v = " + yellow[2]);
		
		
		
		// Display pre-selected colors
		
		PitchColours colours = new PitchColours(blue, yellow, black, red, greenPlate, greenPitch, white);
		return colours;
		//System.out.println(colours.getWhiteValue()[0]);
	}
}