package vision;

import java.awt.Polygon;
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
	static int[] point = new int[2];
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
				synchronized(ExampleSegmentColor.colour){
					float[] color = new float[3];
					int rgb = img.getRGB(e.getX(),e.getY());
					ColorHsv.rgbToHsv((rgb >> 16) & 0xFF,(rgb >> 8) & 0xFF , rgb&0xFF,color);
					ExampleSegmentColor.colour = color;
					ExampleSegmentColor.flag = true;
					return;
				}
			}
		});
	}
	
	private static void getClickedCoords() {
		gui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int[] point = new int[2];
				point[0] = e.getX();
				point[1] = e.getY();
				ExampleSegmentColor.point = point;
				ExampleSegmentColor.flag = true;
				return;
			}
		});
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
		        Thread.sleep(100);
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
		
		float[] blue = new float[3];
		float[] yellow= new float[3];
		float[] black= new float[3];
		float[] red= new float[3];
		float[] greenPlate= new float[3];
		float[] greenPitch= new float[3];
		float[] white = new float[3];
		
		
		float[][] blue3 = new float[3][3];
		float[][] yellow3 = new float[3][3];
		float[][] black3 = new float[3][3];
		float[][] red3 = new float[3][3];
		float[][] greenPlate3 = new float[3][3];
		float[][] greenPitch3 = new float[3][3];
		float[][] white3 = new float[3][3];
		
		// pitch stuff
		int[] xsD = new int[6];
		int[] ysD = new int[6];

		int[] xsA = new int[4];
		int[] ysA = new int[4];
		createImagePanel(image);
//		System.out.println("Please click on the 8 corners of the inside of the pitch");
//		setPitchPolygon(xs, ys);
//		Polygon p = new Polygon(xs,ys,8);
//		PitchConstants.pitchPolygon = p;
		System.out.println("Please click on the 6 corners(inside i.e. on the green) of the leftmost defending region");
		setDefendPolygon(xsD, ysD);
		Polygon r1 = new Polygon(xsD,ysD,6);
		PitchConstants.region1 = r1;
		
		System.out.println("Please click on the 4 corners(inside i.e. on the green) of the leftmost attacking region");
		setAttackPolygon(xsA, ysA);
		Polygon r2 = new Polygon(xsA,ysA,4);
		PitchConstants.region2 = r2;
		
		System.out.println("Please click on the 4 corners(inside i.e. on the green) of the rightmost attacking region");
		setAttackPolygon(xsA, ysA);
		Polygon r3 = new Polygon(xsA,ysA,4);
		PitchConstants.region3 = r3;
		
		System.out.println("Please click on the 6 corners(inside i.e. on the green) of the rightmost defending region");
		setDefendPolygon(xsD, ysD);
		Polygon r4 = new Polygon(xsD,ysD,6);
		PitchConstants.region4 = r4;
		
		System.out.println("Please click 3 times on a blue object");
		setThreeHSV(blue3);
		setMeanHSV3(blue,blue3);
		
		System.out.println("Please click 3 times on a yellow object");
		setThreeHSV(yellow3);
		setMeanHSV3(yellow,yellow3);
		
		System.out.println("Please click 3 times on a black dot object");
		setThreeHSV(black3);
		setMeanHSV3(black,black3);
		
		System.out.println("Please click 3 times on the red ball");
		setThreeHSV(red3);
		setMeanHSV3(red,red3);
		
		System.out.println("Please click 3 times on a green plate object");
		setThreeHSV(greenPlate3);
		setMeanHSV3(greenPlate,greenPlate3);
		
		System.out.println("Please click 3 times on a green pitch object");
		setThreeHSV(greenPitch3);
		setMeanHSV3(greenPitch,greenPitch3);
		
		System.out.println("Please click 3 times on a white edge object");
		setThreeHSV(white3);
		setMeanHSV3(white,white3);
		
		System.out.println("You have selected all objects!");
		
		// Display pre-selected colors
		
		PitchColours colours = new PitchColours(blue, yellow, black, red, greenPlate, greenPitch, white);
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
		float h = 0f; 
		float s = 0f;
		float v = 0f;
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
	
	private static void setPitchPolygon(int[] xs, int ys[]) {
		for(int i = 0; i < 8; i ++){
			getClickedCoords();
			looper();
			xs[i] = point[0];
			ys[i] = point[1];
			flag = false;
		}
	}
	/**
	 * puts polygon coords in xs and ys
	 * @param xs
	 * @param ys
	 */
	private static void setDefendPolygon(int[] xs, int ys[]) {
		for(int i = 0; i < 6; i ++){
			getClickedCoords();
			looper();
			xs[i] = point[0];
			ys[i] = point[1];
			flag = false;
		}
	}
	/**
	 * set attack polygon coords in xs and ys
	 * @param xs
	 * @param ys
	 */
	private static void setAttackPolygon(int[] xs, int ys[]) {
		for(int i = 0; i < 4; i ++){
			getClickedCoords();
			looper();
			xs[i] = point[0];
			ys[i] = point[1];
			flag = false;
		}
	}
	

	public static float max3(float a,float b, float c){
		return Math.max(a, Math.max(b, c));
	}
	public static float min3(float a,float b, float c){
		return Math.min(a, Math.min(b, c));
	}
}