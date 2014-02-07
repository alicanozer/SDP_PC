package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.MultiSpectral;
/**
 * stub wrapper around object positions
 * @author bilyan
 *
 */
public class ObjectLocations {
	public static Point2D_I32 ball = null;
	public static Point2D_I32 yellowATTACKmarker = null;
	public static Point2D_I32 yellowDEFENDmarker = null;
	public static Point2D_I32 blueATTACKmarker = null;
	public static Point2D_I32 blueDEFENDmarker = null;
	
	public static ArrayList<Point2D_I32> dots = null;
	//TODO : add orientations
	// we assume the leftmost region of the pitch is region 1
	private static boolean yellowLeft; // flag whether the yellow team is defending the left goal
	private static boolean yellowUs;   // flag whether we are the yellow team
	private static boolean regionsSet = false; // flag whether regions were set

	private static Polygon region1;
	private static Polygon region2;
	private static Polygon region3;
	private static Polygon region4;

	// lock
	private static boolean lock = true;

	

	public static void setRegions(BufferedImage img) throws Exception{
		if(!regionsSet){
			System.out.println("Attempting to construct the internal representation of the field...");
			float[] hues = {0.5f};
			float[] saturations = {0.4f};
			ArrayList<Polygon> regions = null;
			try {
				regions = VisionOps.getRegions(VisionOps.segmentMultiHSV(img, hues, saturations)[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(regions != null){
				System.out.println("Successfully constructed the internal representation of the field.");
			}
			else{
				System.out.println(
						"WARNING: Unable to construct the internal representation of the field, will attempty at next frame.\n " +
						"Is there clutter on the pitch?");
				return;
			}
			for(Polygon p: regions) System.out.println(p);
			//busy wait for others to release lock
			while(!lock);

			//entering critical section
			lock = false;

			region1 = regions.get(0);
			region2 = regions.get(1);
			region3 = regions.get(2);
			region4 = regions.get(3);

			lock = true;
			regionsSet = true;
			// leaving critical section
			
		}
		else{
			/**
			 * do NOTHING
			 */
		}

	}
	public static void setYellowDefendingLeft(boolean flag){
		while(!lock);

		//entering critical section
		lock = false;
		yellowLeft = flag;
		lock = true;
		//leaving critical section
	}
	public static void setYellowUs(boolean flag){
		while(!lock);

		//entering critical section
		lock = false;
		yellowUs = flag;
		lock = true;
		//leaving critical section
		
	}
	public static void updateObjectLocations(BufferedImage img){

		float[] hues = {6.21f,0.7f,3.14f}; 
		float[] saturations = {0.88f,0.95f,0.605f}; 
		MultiSpectral<ImageFloat32>[] segmented = VisionOps.segmentMultiHSV(img,hues,saturations);

		Point2D_I32 ballLocal = VisionOps.findBall(segmented[0]);
		ArrayList<Point2D_I32> yellowMarkers = VisionOps.findYellowMarkers(segmented[1]);
		ArrayList<Point2D_I32> blueMarkers = VisionOps.findBlueMarkers(segmented[2]);

		ArrayList<Point2D_I32> dots = new ArrayList<Point2D_I32>();

		if(yellowMarkers != null){
			for(Point2D_I32 p: yellowMarkers){
				dots.add(VisionOps.getMeanDotNearMarker(img,p,40));
			}
		}

		if(blueMarkers != null){
			for(Point2D_I32 p: blueMarkers){
				dots.add(VisionOps.getMeanDotNearMarker(img,p,40));
			}
		}
		
		//setting the ball
		setBall(ballLocal);

		//setting the yellow robots, by cases
		if(ContourUtils.isInside(yellowMarkers.get(0), region1) && ContourUtils.isInside(yellowMarkers.get(1), region3) && yellowLeft) {
			setYellowDEFENDmarker(yellowMarkers.get(0));
			setYellowATTACKmarker(yellowMarkers.get(1));
			
		}
		else if(ContourUtils.isInside(yellowMarkers.get(1), region1) && ContourUtils.isInside(yellowMarkers.get(0), region3) && yellowLeft) {
			setYellowDEFENDmarker(yellowMarkers.get(1));
			setYellowATTACKmarker(yellowMarkers.get(0));
		}
		else if(ContourUtils.isInside(yellowMarkers.get(0), region2) && ContourUtils.isInside(yellowMarkers.get(1), region4) && !yellowLeft) {
			setYellowDEFENDmarker(yellowMarkers.get(0));
			setYellowATTACKmarker(yellowMarkers.get(1));
		}
		else if(ContourUtils.isInside(yellowMarkers.get(1), region2) && ContourUtils.isInside(yellowMarkers.get(0), region4) && !yellowLeft) {
			setYellowDEFENDmarker(yellowMarkers.get(1));
			setYellowATTACKmarker(yellowMarkers.get(0));
		}
		else{
			System.err.println("Unable to locate yellow robots");
		}
		//setting the blue robots by cases
		if(ContourUtils.isInside(blueMarkers.get(0), region1) && ContourUtils.isInside(blueMarkers.get(1), region3) && !yellowLeft) {
			setBlueDEFENDmarker(blueMarkers.get(0));
			setBlueATTACKmarker(blueMarkers.get(1));
		}
		else if(ContourUtils.isInside(blueMarkers.get(1), region1) && ContourUtils.isInside(blueMarkers.get(0), region3) && !yellowLeft) {
			setBlueDEFENDmarker(blueMarkers.get(1));
			setBlueATTACKmarker(blueMarkers.get(0));
		}
		else if(ContourUtils.isInside(blueMarkers.get(0), region2) && ContourUtils.isInside(blueMarkers.get(1), region4) && yellowLeft) {
			setBlueDEFENDmarker(blueMarkers.get(0));
			setBlueATTACKmarker(blueMarkers.get(1));
		}
		else if(ContourUtils.isInside(blueMarkers.get(1), region2) && ContourUtils.isInside(blueMarkers.get(0), region4) && yellowLeft) {
			setBlueDEFENDmarker(blueMarkers.get(1));
			setBlueATTACKmarker(blueMarkers.get(0));
		}
		else{
			System.err.println("Unable to locate blue robots");
		}
		
		dots = null;
		

	}

	private static void setBall(Point2D_I32 pos) {
		while(!lock);
		//entering critical section
		lock = false;
		ball = pos;
		lock = true;
		//leaving critical section
	}
	
	private static void setYellowDEFENDmarker(Point2D_I32 pos) {
		while(!lock);
		//entering critical section
		lock = false;
		yellowDEFENDmarker = pos;
		lock = true;
		//leaving critical section
	}
	
	private static void setYellowATTACKmarker(Point2D_I32 pos) {
		while(!lock);
		//entering critical section
		lock = false;
		yellowATTACKmarker = pos;
		lock = true;
		//leaving critical section
	}
	
	private static void setBlueDEFENDmarker(Point2D_I32 pos) {
		while(!lock);
		//entering critical section
		lock = false;
		blueDEFENDmarker = pos;
		lock = true;
		//leaving critical section
	}
	
	private static void setBlueATTACKmarker(Point2D_I32 pos) {
		while(!lock);
		//entering critical section
		lock = false;
		blueATTACKmarker = pos;
		lock = true;
		//leaving critical section
	}
	
	public Point2D_I32 getBall(){
		while(!lock);
		return ball;
	}
	
	public Point2D_I32 getYellowDEFENDmarker(){
		while(!lock);
		return yellowDEFENDmarker;
	}
	
	public Point2D_I32 getYellowATTACKmarker(){
		while(!lock);
		return yellowATTACKmarker;
	}
	public Point2D_I32 getBlueDEFENDmarker(){
		while(!lock);
		return blueDEFENDmarker;
	}
	
	public Point2D_I32 getBlueATTACKmarker(){
		while(!lock);
		return blueATTACKmarker;
	}
	
	/**
	 * draws crosses over all objects of interest
	 * @param g
	 */
	public static  void drawCrosses(Graphics2D g){
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		if (ObjectLocations.ball != null) {
			// drawing X over ball			//		
			g.drawLine(ObjectLocations.ball.x - 10, ObjectLocations.ball.y, ObjectLocations.ball.x + 10, ObjectLocations.ball.y);
			g.drawLine(ObjectLocations.ball.x, ObjectLocations.ball.y - 10, ObjectLocations.ball.x, ObjectLocations.ball.y + 10);
		}
		if(yellowDEFENDmarker != null){
			g.drawLine(yellowDEFENDmarker.x - 10, yellowDEFENDmarker.y, yellowDEFENDmarker.x + 10, yellowDEFENDmarker.y );
			g.drawLine(yellowDEFENDmarker.x, yellowDEFENDmarker.y - 10, yellowDEFENDmarker.x, yellowDEFENDmarker.y + 10);
		}

		if(yellowATTACKmarker != null){
			g.drawLine(yellowATTACKmarker.x - 10, yellowATTACKmarker.y, yellowATTACKmarker.x + 10, yellowATTACKmarker.y );
			g.drawLine(yellowATTACKmarker.x, yellowATTACKmarker.y - 10, yellowATTACKmarker.x, yellowATTACKmarker.y + 10);
		}
		if(blueDEFENDmarker != null){
			g.drawLine(blueDEFENDmarker.x - 10, blueDEFENDmarker.y, blueDEFENDmarker.x + 10, blueDEFENDmarker.y );
			g.drawLine(blueDEFENDmarker.x, blueDEFENDmarker.y - 10, blueDEFENDmarker.x, blueDEFENDmarker.y + 10);
		}

		if(blueATTACKmarker != null){
			g.drawLine(blueATTACKmarker.x - 10, blueATTACKmarker.y, blueATTACKmarker.x + 10, blueATTACKmarker.y );
			g.drawLine(blueATTACKmarker.x, blueATTACKmarker.y - 10, blueATTACKmarker.x, blueATTACKmarker.y + 10);
		}


		if(dots != null){
			for(Point2D_I32 p: dots){
				g.drawLine(p.x - 10, p.y, p.x + 10, p.y );
				g.drawLine(p.x, p.y - 10, p.x, p.y + 10);
			}
		}
		g.setColor(c);
	}

}
