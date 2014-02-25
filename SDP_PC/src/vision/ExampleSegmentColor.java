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
	static BufferedImage img;
	static ImagePanel gui;
	
	/**
	 * Shows a color image and allows the user to select a pixel, convert it to HSV, print
	 * the HSV values, and calls the function below to display similar pixels.
	 */
	private static void getClickedColor() {
		
		gui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				float[] color = new float[3];
				int rgb = img.getRGB(e.getX(),e.getY());
				ColorHsv.rgbToHsv((rgb >> 16) & 0xFF,(rgb >> 8) & 0xFF , rgb&0xFF,color);
//				System.out.println("h: " + color[0]+ ", s: " + color[1] + ", v:" + color[2]);
				ExampleSegmentColor.colour = color;
				ExampleSegmentColor.flag = true;
				return;
			}
		});

//		float[] colour = new float[3];
//		return colour;
	}
	/**
	 * create the image panel once only
	 * @param img
	 */
	private static void createImagePanel(final BufferedImage img){
		ExampleSegmentColor.img = img;
		ExampleSegmentColor.gui = new ImagePanel(ExampleSegmentColor.img);
		ShowImages.showWindow(gui,"Color Selector");
	}
	
	/**
	 * TODO: implement a closer
	 */
	private static void closeImagePanel(){
		
	}
	private static void looper(){
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
		

	public static PitchColours selectColoursOfPitch(BufferedImage image) {
		//BufferedImage image = UtilImageIO.loadImage("test_images/00000008.jpg");
		// contains h,s,v
		
		float[][] blueRange = new float[2][3];
		float[][] yellowRange = new float[2][3];
		float[][] blackRange = new float[2][3];
		float[][] redRange = new float[2][3];
		float[][] greenPlateRange = new float[2][3];
		float[][] greenPitchRange = new float[2][3];
		float[][] whiteRange = new float[2][3];
		
		
		float[][] blue3 = new float[3][3];
		float[][] yellow3 = new float[3][3];
		float[][] black3 = new float[3][3];
		float[][] red3 = new float[3][3];
		float[][] greenPlate3 = new float[3][3];
		float[][] greenPitch3 = new float[3][3];
		float[][] white3 = new float[3][3];
		
		
		
		createImagePanel(image);
		
		System.out.println("Please click 3 times on a blue object");
		setThreeHSV(blue3);
		setMinMaxHSVRange(blueRange,blue3);
		
		System.out.println("Please click 3 times on a yellow object");
		setThreeHSV(yellow3);
		setMinMaxHSVRange(yellowRange,yellow3);
		
		System.out.println("Please click 3 times on a black dot object");
		setThreeHSV(black3);
		setMinMaxHSVRange(blackRange,black3);
		
		System.out.println("Please click 3 times on the red ball");
		setThreeHSV(red3);
		setMinMaxHSVRange(redRange,red3);
		
		System.out.println("Please click 3 times on a green plate object");
		setThreeHSV(greenPlate3);
		setMinMaxHSVRange(greenPlateRange,greenPlate3);
		
		System.out.println("Please click 3 times on a green pitch object");
		setThreeHSV(greenPitch3);
		setMinMaxHSVRange(greenPitchRange,greenPitch3);
		
		System.out.println("Please click 3 times on a white edge object");
		setThreeHSV(white3);
		setMinMaxHSVRange(whiteRange,white3);
		
		System.out.println("You have selected all objects!");
		
		// Display pre-selected colors
		
		PitchColours colours = new PitchColours(blueRange, yellowRange, blackRange, redRange, greenPlateRange, greenPitchRange, whiteRange);
		return colours;
	}
	private static void setMinMaxHSVRange(float[][] minMaxArray, float[][] floatArray3){
		/*
		 * first row is max
		 * second row is min
		 * first elem is H, second S, third V
		 */
		for(int i = 0 ; i < 3; i ++){
			minMaxArray[0][i] = max3(floatArray3[0][i],floatArray3[1][i],floatArray3[2][i]);
			minMaxArray[1][i] = min3(floatArray3[0][i],floatArray3[1][i],floatArray3[2][i]);
		}
	}
	
	/**
	 * takes a 3 array and returns the mean of the H, S, and V
	 * @param meanFloatArray
	 * @param floatArray3
	 */
	public static void setMeanHSV3(float[] meanFloatArray, float[][] floatArray3) {
		// merge colors
		float h = 0; 
		float s = 0;
		float v = 0;
		for(int i = 0; i < 3; i ++){
			h += floatArray3[i][0];
			s += floatArray3[i][1];
			v += floatArray3[i][2];
		}
		meanFloatArray[0] = h/3f;
		meanFloatArray[1] = s/3f;
		meanFloatArray[2] = v/3f;
	}
	/**
	 * get float array from 3 clicks
	 * @param floatArray3
	 */
	private static void setThreeHSV(float[][] floatArray3) {
		for(int i = 0; i < 3; i ++){
			getClickedColor();
			looper();
			floatArray3[i] = colour;
			flag = false;
		}
	}
	private float minArray(float[] ar){
		float min = Float.POSITIVE_INFINITY;
		for(float a: ar){
			if(a < min) min = a;
		}
		return min;
	}
	private float maxArray(float[] ar){
		float max = Float.NEGATIVE_INFINITY;
		for(float a: ar){
			if(a > max) max = a;
		}
		return max;
		
	}
	public static float max3(float a,float b, float c){
		return Math.max(a, Math.max(b, c));
	}
	public static float min3(float a,float b, float c){
		return Math.min(a, Math.min(b, c));
	}
}