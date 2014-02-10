package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
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
	private static Point2D_I32 ball = null;
	private static Point2D_I32 yellowATTACKmarker = null;
	private static Point2D_I32 yellowDEFENDmarker = null;
	private static Point2D_I32 blueATTACKmarker = null;
	private static Point2D_I32 blueDEFENDmarker = null;
	
	private static ArrayList<Point2D_I32> dots = null;
	//TODO : add orientations
	// we assume the leftmost region of the pitch is region 1
	private static boolean yellowLeft; // flag whether the yellow team is defending the left goal
	private static boolean yellowUs;   // flag whether we are the yellow team
	private static boolean regionsSet = false; // flag whether regions were set

	//new pitch
//	private static int region12X = 130;
//	private static int region23X = 280;
//	private static int region34X = 430;
	//old pitch
	private static int region12X = 120;
	private static int region23X = 270;
	private static int region34X = 420;

	// lock
	private static boolean lock = true;

	
	/**
	 * attempts to set the regions
	 * @param img
	 * @return TRUE if the regions are or were already set, FALSE otherwise
	 * @throws Exception
	 */
//	public static boolean setRegions(BufferedImage img) throws Exception{
//		boolean done = false;
//		if(!regionsSet){
//			System.out.println("Attempting to construct the internal representation of the field...");
//			float[] hues = {0.5f};
//			float[] saturations = {0.4f};
//			ArrayList<Polygon> regions = null;
//			try {
//				regions = VisionOps.getRegions(VisionOps.segmentMultiHSV(img, hues, saturations)[0]);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(regions != null){
//				System.out.println("Successfully constructed the internal representation of the field.");
//			}
//			else{
//				System.out.println(
//						"WARNING: Unable to construct the internal representation of the field, will attempty at next frame.\n " +
//						"Is there clutter on the pitch?");
//				return false;
//			}
//			for(Polygon p: regions) {
//				System.out.println(p);
//				System.out.println(p.npoints);
//				int x = 0;
//				int y = 0;
//				int npoints = p.npoints;
//				for(int i=0;i<npoints;i++){
//					 x += p.xpoints[i];
//					 y += p.ypoints[i];
//				}
//				System.out.println("Polygon coords : " + x/npoints + " " + y/npoints);
//			}
//			//busy wait for others to release lock
//			while(!lock);
//
//			//entering critical section
//			lock = false;
//
//			Point2D_I32[] polCentroid = new Point2D_I32[4];
//			for(int z = 0 ; z < 4; z++){
//				polCentroid[z] = ContourUtils.getPolygonCentroid(regions.get(z));
//			}
//
//			int i = polCentroid[0].x;
//			int j = polCentroid[1].x;
//			int k = polCentroid[2].x;
//			int l = polCentroid[3].x;
//
//			int[] order = sort(i,j,k,l);
//			
//			region1 = regions.get(order[0] - 1);
//			region1Centre = polCentroid[order[0] - 1];
//			
//			region2 = regions.get(order[1] - 1);
//			region2Centre = polCentroid[order[1] - 1];
//			
//			region3 = regions.get(order[2] - 1);
//			region3Centre = polCentroid[order[2] - 1];
//			
//			region4 = regions.get(order[3] - 1);
//			region4Centre = polCentroid[order[3] - 1];
//
//			lock = true;
//			regionsSet = true;
//			// leaving critical section
//			return true;
//		}
//		else{
//			return true;
//			/**
//			 * do NOTHING
//			 */
//		}
//
//	}
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
	// i j k l
	// 1 2 3 4
	private static int[] sort(int i,int j, int k, int l){
		if (i < j) {
			if (j < k) {
				if (k < l) return new int[] {1,2,3,4}; // [i,j,k,l];
				if (j < l) return new int[] {1,2,4,3}; // [i,j,l,k];
				if (i < l) return new int[] {1,4,2,3}; // [i,l,j,k];
				return new int[] {4,1,2,3};  //[l,i,j,k];
			} else if (i < k) {
				if (j < l) return new int[] {1,3,2,4} ;// [i,k,j,l];
				if (k < l) return new int[] {1,3,4,2} ;//[i,k,l,j];
				if (i < l) return new int[] {1,4,3,2} ;//[i,l,k,j];
				return new int[] { 4,1,3,2};//[l,i,k,j];
			} else {
				if (j < l) return new int[] {3,1,2,4};//[k,i,j,l];
				if (i < l) return new int[] {3,1,4,2};//[k,i,l,j];
				if (k < l) return new int[] {3,4,1,2};//[k,l,i,j];
				return new int[] {4,3,1,2};//[l,k,i,j];
			}
			// i j k l
			// 1 2 3 4
		} else {
			if (i < k) {
				if (k < l) return new int[] {2,1,3,4};//[j,i,k,l];
				if (i < l) return new int[] {2,1,4,3};//[j,i,l,k];
				if (j < l) return new int[] {2,4,1,3};//[j,l,i,k];
				return new int[] {4,2,1,3};//[l,j,i,k];
			} else if (j < k) {
				if (i < l) return new int[] {2,3,1,4};//[j,k,i,l];
				if (k < l) return new int[] {2,3,4,1};  //[j,k,l,i];
				if (j < l) return new int[] {2,4,3,1};     //[j,l,k,i];
				return new int[] {4,2,3,1};//[l,j,k,i];
			} else {
				if (i < l) return new int[] {3,2,1,4}; // [k,j,i,l];
				if (j < l) return new int[] {3,2,4,1}; // [k,j,l,i];
				if (k < l) return new int[] {3,4,2,1};// [k,l,j,i];
				return new int[]  {4,3,2,1}; //[l,k,j,i];
			}
		}

	}
	public static void updateObjectLocations(BufferedImage img){
		Point2D_I32 ballLocal = VisionOps.findBall(img);
		ArrayList<Point2D_I32> yellowMarkers = VisionOps.findYellowMarkers(img);
		ArrayList<Point2D_I32> blueMarkers = VisionOps.findBlueMarkers(img);

		ArrayList<Point2D_I32> dotsLocal = new ArrayList<Point2D_I32>();

		if(yellowMarkers != null){
			for(Point2D_I32 p: yellowMarkers){
				dotsLocal.add(VisionOps.getMeanDotNearMarker(img,p,44)); // window 44 works well
			}
		}

		if(blueMarkers != null){
			for(Point2D_I32 p: blueMarkers){
				dotsLocal.add(VisionOps.getMeanDotNearMarker(img,p,44));
			}
		}
		
		//setting the ball
		setBall(ballLocal);
		
		if(yellowLeft){
			//set yellow
			if(yellowMarkers != null){
				for(Point2D_I32 p: yellowMarkers){
					if(p.x < region12X) setYellowDEFENDmarker(p);
					if(region23X < p.x && p.x < region34X) setYellowATTACKmarker(p);
				}
			}
			//set blue
			if(blueMarkers != null){
				for(Point2D_I32 p: blueMarkers){
					if(region12X < p.x && p.x < region23X) setBlueATTACKmarker(p);
					if(p.x > region34X) setBlueDEFENDmarker(p);
				}
			}
		}
		else{ // blueLeft
			//set yellow
			if(yellowMarkers != null){
				for(Point2D_I32 p: yellowMarkers){
					if(region12X < p.x && p.x < region23X) setYellowATTACKmarker(p);
					if(p.x > region34X) setYellowDEFENDmarker(p);
				}
			}
			//set blue
			if(blueMarkers != null){
				for(Point2D_I32 p: blueMarkers){
					if(p.x < region12X) setBlueDEFENDmarker(p);
					if(region23X < p.x && p.x < region34X) setBlueATTACKmarker(p);
				}
			}
		}
		
		dots = dotsLocal;
		

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
	 * draws lines indicating direction of movement over an object
	 * @param g
	 * @throws Exception 
	 */
	public static void drawDirection(Graphics2D g, String object) throws Exception{
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		double angle = 0;
		if (object == "ball" && ObjectLocations.ball != null) {
			angle = VisionOps.getDirection(SimpleViewer.ballPrvPos, SimpleViewer.ballCurPos);
			if (angle != 0.0){
				g.draw(new Line2D.Double(SimpleViewer.ballCurPos.x, SimpleViewer.ballCurPos.y, (SimpleViewer.ballCurPos.x + Math.sin(angle)*100), (SimpleViewer.ballCurPos.y + Math.cos(angle)*100)));
				//g.drawLine(SimpleViewer.ballCurPos.x, SimpleViewer.ballCurPos.y, (SimpleViewer.ballCurPos.x + (int) Math.sin(angle)*10), (SimpleViewer.ballCurPos.y + (int) Math.cos(angle)*10));
				/*System.out.println((SimpleViewer.ballCurPos.y + Math.cos(angle)*100));
				System.out.println((SimpleViewer.ballCurPos.x + Math.sin(angle)*100));
				System.out.println("drawing from " + SimpleViewer.ballCurPos.x + " , " + SimpleViewer.ballCurPos.y + "->"+" , " + " at angle " + angle);*/
				}
			}
		else if (object == "yellowAtk"){
			angle = VisionOps.getDirection(SimpleViewer.yellowAttackPrvPos, SimpleViewer.yellowAttackCurPos);
			if (angle != 0.0){
				g.draw(new Line2D.Double(SimpleViewer.yellowAttackCurPos.x, SimpleViewer.yellowAttackCurPos.y, (SimpleViewer.yellowAttackCurPos.x + Math.sin(angle)*100), (SimpleViewer.yellowAttackCurPos.y + Math.cos(angle)*100)));
				}
			}
		else if (object == "yellowDef"){
			angle = VisionOps.getDirection(SimpleViewer.yellowDefendPrvPos, SimpleViewer.yellowDefendCurPos);
			if (angle != 0.0){
				g.draw(new Line2D.Double(SimpleViewer.yellowDefendCurPos.x, SimpleViewer.yellowDefendCurPos.y, (SimpleViewer.yellowDefendCurPos.x + Math.sin(angle)*100), (SimpleViewer.yellowDefendCurPos.y + Math.cos(angle)*100)));
				}
			}
		else if (object == "blueAtk"){
			angle = VisionOps.getDirection(SimpleViewer.blueAttackPrvPos, SimpleViewer.blueAttackCurPos);
			if (angle != 0.0){
				g.draw(new Line2D.Double(SimpleViewer.blueAttackCurPos.x, SimpleViewer.blueAttackCurPos.y, (SimpleViewer.blueAttackCurPos.x + Math.sin(angle)*100), (SimpleViewer.blueAttackCurPos.y + Math.cos(angle)*100)));
				}
			}
		else if (object == "blueDef"){
			angle = VisionOps.getDirection(SimpleViewer.blueDefendPrvPos, SimpleViewer.blueDefendCurPos);
			if (angle != 0.0){
				g.draw(new Line2D.Double(SimpleViewer.blueDefendCurPos.x, SimpleViewer.blueDefendCurPos.y, (SimpleViewer.blueDefendCurPos.x + Math.sin(angle)*100), (SimpleViewer.blueDefendCurPos.y + Math.cos(angle)*100)));
				}
			}
		else{
//			/throw new Exception("what the hell is a "+ object +" ?");
		}
	}
	
	//draws directions over a list of objects you want 
	public static void drawAllDirections(Graphics2D g, String[] objects) throws Exception{
		for (String obj:objects){
			drawDirection(g, obj);
		}
		
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
		// WRONG position of yellowDEFENDmarker
		if(yellowDEFENDmarker != null){
			g.drawLine(yellowDEFENDmarker.x - 10, yellowDEFENDmarker.y, yellowDEFENDmarker.x + 10, yellowDEFENDmarker.y );
			g.drawLine(yellowDEFENDmarker.x, yellowDEFENDmarker.y - 10, yellowDEFENDmarker.x, yellowDEFENDmarker.y + 10);
			//g.drawOval(yellowDEFENDmarker.x - 22, yellowDEFENDmarker.y - 22, 44, 44);
		}

		if(yellowATTACKmarker != null){
			g.drawLine(yellowATTACKmarker.x - 10, yellowATTACKmarker.y, yellowATTACKmarker.x + 10, yellowATTACKmarker.y );
			g.drawLine(yellowATTACKmarker.x, yellowATTACKmarker.y - 10, yellowATTACKmarker.x, yellowATTACKmarker.y + 10);
			//g.drawOval(yellowATTACKmarker.x - 22, yellowATTACKmarker.y - 22, 44, 44);
		}
		if(blueDEFENDmarker != null){
			g.drawLine(blueDEFENDmarker.x - 10, blueDEFENDmarker.y, blueDEFENDmarker.x + 10, blueDEFENDmarker.y );
			g.drawLine(blueDEFENDmarker.x, blueDEFENDmarker.y - 10, blueDEFENDmarker.x, blueDEFENDmarker.y + 10);
			//g.drawOval(blueDEFENDmarker.x - 22, blueDEFENDmarker.y - 22, 44, 44);
		}

		if(blueATTACKmarker != null){
			g.drawLine(blueATTACKmarker.x - 10, blueATTACKmarker.y, blueATTACKmarker.x + 10, blueATTACKmarker.y );
			g.drawLine(blueATTACKmarker.x, blueATTACKmarker.y - 10, blueATTACKmarker.x, blueATTACKmarker.y + 10);
			//g.drawOval(blueATTACKmarker.x - 22, blueATTACKmarker.y - 22, 44, 44);
		}
		
		g.setColor(Color.RED);
		if(dots != null){
			for(Point2D_I32 p: dots){
				if(p != null){
					g.drawLine(p.x - 10, p.y, p.x + 10, p.y );
					g.drawLine(p.x, p.y - 10, p.x, p.y + 10);
				}
			}
		}
		g.setColor(c);
	}

}
