package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
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

	private static Point2D_I32 yellowATTACKdot = null;
	private static Point2D_I32 yellowDEFENDdot = null;
	private static Point2D_I32 blueATTACKdot = null;
	private static Point2D_I32 blueDEFENDdot = null;	
	

	public static double getBallDirectionAngle() {
		while(!lock);
		return ballDirectionAngle;
	}
	public static double getYellowATTACKmarkerDirectionAngle() {
		while(!lock);
		return yellowATTACKmarkerDirectionAngle;
	}
	public static double getYellowDEFENDmarkerDicrectionAngle() {
		while(!lock);
		return yellowDEFENDmarkerDicrectionAngle;
	}
	public static double getBlueATTACKmarkerDirectionAngle() {
		while(!lock);
		return blueATTACKmarkerDirectionAngle;
	}
	public static double getBlueDEFENDmarkerDirectionAngle() {
		while(!lock);
		return blueDEFENDmarkerDirectionAngle;
	}

	private static double ballDirectionAngle;
	private static double yellowATTACKmarkerDirectionAngle;
	private static double yellowDEFENDmarkerDicrectionAngle;
	private static double blueATTACKmarkerDirectionAngle;
	private static double blueDEFENDmarkerDirectionAngle;

	public static double getYellowATTACKmarkerOrientationAngle() {
		while(!lock);
		return yellowATTACKmarkerOrientationAngle;
	}
	public static double getYellowDEFENDmarkerOrientationAngle() {
		while(!lock);
		return yellowDEFENDmarkerOrientationAngle;
	}
	public static double getBlueATTACKmarkerOrientationAngle() {
		while(!lock);
		return blueATTACKmarkerOrientationAngle;
	}
	public static double getBlueDEFENDmarkerOrientationAngle() {
		while(!lock);
		return blueDEFENDmarkerOrientationAngle;
	}

	private static double yellowATTACKmarkerOrientationAngle = 0.0;
	private static double yellowDEFENDmarkerOrientationAngle = 0.0;
	private static double blueATTACKmarkerOrientationAngle = 0.0;
	private static double blueDEFENDmarkerOrientationAngle = 0.0;



	private static ArrayList<Point2D_I32> dots = null;
	// we assume the leftmost region of the pitch is region 1
	private static boolean yellowLeft; // flag whether the yellow team is defending the left goal
	private static boolean yellowUs;   // flag whether we are the yellow team


	//new pitch
	//	private static int region12X = 130;
	//	private static int region23X = 280;
	//	private static int region34X = 430;
	//old pitch
	private static int region12X = 115;
	private static int region23X = 260;
	private static int region34X = 410;

	// lock, dunno if needed
	private static boolean lock = true;



	public static void setYellowDefendingLeft(boolean flag){
		while(!lock);

		//entering critical section
		lock = false;
		yellowLeft = flag;
		lock = true;
		//leaving critical section
	}
	public static boolean getYellowDefendingLeft() {
		return yellowLeft;
	}
	
	public static void setYellowUs(boolean flag){
		while(!lock);

		//entering critical section
		lock = false;
		yellowUs = flag;
		lock = true;
		//leaving critical section

	}
	public static boolean getYellowUs() {
		return yellowUs;
	}
	/**
	 * main method, only call this one on the new grabbed frame! do not attempt to set objects yourselves...
	 * @param img
	 * @param colors 
	 * @param whitePoints 
	 */
	public static void updateObjectLocations(BufferedImage img, PitchColours colors){
		Point2D_I32 ballLocal = VisionOps.findBall(img);
		ArrayList<Point2D_I32> yellowMarkers = VisionOps.findYellowMarkers(img);
		ArrayList<Point2D_I32> blueMarkers = VisionOps.findBlueMarkers(img);


		ArrayList<Point2D_I32> dotsLocal = new ArrayList<Point2D_I32>();

		if(yellowMarkers != null){
			for(Point2D_I32 p: yellowMarkers){
				Point2D_I32 newCentre = VisionOps.getMeanDotNearMarker(img,p,30);
//				Point2D_I32 newCentre = VisionOps.getDotPosition(
//						img, 
//						p, 
//						30, 
//						2, 
//						1, 
//						colors.getBlackValue(), 
//						colors.getYellowValue(), 
//						colors.getGreenPlateValue());   //.getMeanDotNearMarker(img,p,40,colors.getBlackValue());
				if(newCentre != null)
					dotsLocal.add(newCentre); // window 44 works well
			}
		}

		if(blueMarkers != null){
			for(Point2D_I32 p: blueMarkers){
				Point2D_I32 newCentre = VisionOps.getMeanDotNearMarker(img,p,30);
//				Point2D_I32 newCentre = VisionOps.getDotPosition(
//						img, 
//						p, 
//						30, 
//						2, 
//						1, 
//						colors.getBlackValue(), 
//						colors.getBlueValue(), 
//						colors.getGreenPlateValue()); 
				if(newCentre != null)
					dotsLocal.add(newCentre); // window 44 works well
			}
		}

		//setting the ball and its angle
		try {
			ballDirectionAngle = VisionOps.getDirection(ball,ballLocal);
		} catch (Exception e) {

		}
		setBall(ballLocal);

		if(yellowLeft){
			//set yellow
			if(yellowMarkers != null){
				for(Point2D_I32 p: yellowMarkers){
					if(p.x < region12X) {
						try {
							//if(PointUtils.euclideanDistance(yellowDEFENDmarker, p) > 3)
							yellowDEFENDmarkerDicrectionAngle = VisionOps.getDirection(yellowDEFENDmarker, p);
						} catch (Exception e) {

						}
						setYellowDEFENDmarker(p);
					}

					if(region23X < p.x && p.x < region34X) {
						try {
							//if(PointUtils.euclideanDistance(yellowATTACKmarker, p) > 3 )
							yellowATTACKmarkerDirectionAngle = VisionOps.getDirection(yellowATTACKmarker, p);
						} catch (Exception e) {

						}
						setYellowATTACKmarker(p);
					}
				}
			}
			//set blue
			if(blueMarkers != null){
				for(Point2D_I32 p: blueMarkers){
					if(region12X < p.x && p.x < region23X) {
						try {
							blueATTACKmarkerDirectionAngle = VisionOps.getDirection(blueATTACKmarker, p);
						} catch (Exception e) {

						}
						setBlueATTACKmarker(p);
					}
					if(p.x > region34X) {
						try {
							blueDEFENDmarkerDirectionAngle = VisionOps.getDirection(blueDEFENDmarker, p);
						} catch (Exception e) {

						}
						setBlueDEFENDmarker(p);
					}
				}
			}
		}
		else{ // blueLeft
			//set yellow
			if(yellowMarkers != null){
				for(Point2D_I32 p: yellowMarkers){
					if(region12X < p.x && p.x < region23X) {
						try {
							//if(PointUtils.euclideanDistance(yellowATTACKmarker, p) > 7 )
							yellowATTACKmarkerDirectionAngle = VisionOps.getDirection(yellowATTACKmarker, p);
						} catch (Exception e) {

						}
						setYellowATTACKmarker(p);
					}
					if(p.x > region34X) {
						try {
							//if(PointUtils.euclideanDistance(yellowDEFENDmarker, p) > 3)
							yellowDEFENDmarkerDicrectionAngle = VisionOps.getDirection(yellowDEFENDmarker, p);
						} catch (Exception e) {

						}
						setYellowDEFENDmarker(p);
					}
				}
			}
			//set blue
			if(blueMarkers != null){
				for(Point2D_I32 p: blueMarkers){
					if(p.x < region12X) {
						try {
							blueDEFENDmarkerDirectionAngle = VisionOps.getDirection(blueDEFENDmarker, p);
						} catch (Exception e) {

						}
						setBlueDEFENDmarker(p);
					}
					if(region23X < p.x && p.x < region34X) {
						try {
							blueATTACKmarkerDirectionAngle = VisionOps.getDirection(blueATTACKmarker, p);
						} catch (Exception e) {

						}
						setBlueATTACKmarker(p);
					}
				}
			}
		}

		//setting orientation angles

		dots = dotsLocal;
		double angleTolerance = 0.17; // 0.17 radians is 10 degrees
		if(yellowLeft){
			for(Point2D_I32 dot : dots){
				if(dot != null){
					if (dot.x < region12X)
						try {
							double newAngle = VisionOps.getDirection(yellowDEFENDmarker, dot);
							if(Math.abs(newAngle - yellowDEFENDmarkerOrientationAngle) < angleTolerance); 
							yellowDEFENDmarkerOrientationAngle = VisionOps.getDirection(yellowDEFENDmarker, dot);

							setYellowDEFENDdot(dot);
							} catch (Exception e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();

						}
					else if(region12X < dot.x && dot.x < region23X)
						try {
							double newAngle = VisionOps.getDirection(blueATTACKmarker, dot);
							if(Math.abs(newAngle - blueATTACKmarkerOrientationAngle) < angleTolerance);
							blueATTACKmarkerOrientationAngle = VisionOps.getDirection(blueATTACKmarker, dot);
							setBlueATTACKdot(dot);
						} catch (Exception e) {

						}
					else if (region23X < dot.x && dot.x < region34X){
						try {
							double newAngle = VisionOps.getDirection(yellowATTACKmarker, dot);
							if(Math.abs(newAngle - yellowATTACKmarkerOrientationAngle) < angleTolerance); 
							yellowATTACKmarkerOrientationAngle = VisionOps.getDirection(yellowATTACKmarker, dot);
							setYellowATTACKdot(dot);
						} catch (Exception e) {

						}
					}
					else if(dot.x > region34X){
						try {
							double newAngle = VisionOps.getDirection(blueDEFENDmarker, dot);
							if(Math.abs(newAngle - blueDEFENDmarkerOrientationAngle) < angleTolerance); // 10 degrees is 0.17 radians
							blueDEFENDmarkerOrientationAngle = VisionOps.getDirection(blueDEFENDmarker, dot);
							setYellowDEFENDdot(dot);
						} catch (Exception e) {

						}
					}
				}
			}
		}
		else{ // blue if left
			for(Point2D_I32 dot : dots){
				if(dot != null){
					if (dot.x < region12X)
						try {
							double newAngle = VisionOps.getDirection(blueDEFENDmarker, dot);
							if(Math.abs(newAngle - blueDEFENDmarkerOrientationAngle) < angleTolerance); 
							blueDEFENDmarkerOrientationAngle = VisionOps.getDirection(blueDEFENDmarker, dot);
						} catch (Exception e) {

						}
					else if(region12X < dot.x && dot.x < region23X)
						try {
							double newAngle = VisionOps.getDirection(yellowATTACKmarker, dot);
							if(Math.abs(newAngle - yellowATTACKmarkerOrientationAngle) < angleTolerance);
							yellowATTACKmarkerOrientationAngle = VisionOps.getDirection(yellowATTACKmarker, dot);
						} catch (Exception e) {

						}
					else if (region23X < dot.x && dot.x < region34X){
						try {
							double newAngle = VisionOps.getDirection(blueATTACKmarker, dot);
							if(Math.abs(newAngle - blueATTACKmarkerOrientationAngle) < angleTolerance); 
							blueATTACKmarkerOrientationAngle = VisionOps.getDirection(blueATTACKmarker, dot);
						} catch (Exception e) {

						}
					}
					else if(dot.x > region34X){
						try {
							double newAngle = VisionOps.getDirection(yellowDEFENDmarker, dot);
							if(Math.abs(newAngle - yellowDEFENDmarkerOrientationAngle) < angleTolerance); 
							yellowDEFENDmarkerOrientationAngle = VisionOps.getDirection(yellowDEFENDmarker, dot);
						} catch (Exception e) {

						}
					}
				}
			}
		}




	}

	public static Point2D_I32 getYellowATTACKdot() {
		return yellowATTACKdot;
	}
	public static void setYellowATTACKdot(Point2D_I32 yellowATTACKdot) {
		ObjectLocations.yellowATTACKdot = yellowATTACKdot;
	}
	public static Point2D_I32 getYellowDEFENDdot() {
		return yellowDEFENDdot;
	}
	public static void setYellowDEFENDdot(Point2D_I32 yellowDEFENDdot) {
		ObjectLocations.yellowDEFENDdot = yellowDEFENDdot;
	}
	public static Point2D_I32 getBlueATTACKdot() {
		return blueATTACKdot;
	}
	public static void setBlueATTACKdot(Point2D_I32 blueATTACKdot) {
		ObjectLocations.blueATTACKdot = blueATTACKdot;
	}
	public static Point2D_I32 getBlueDEFENDdot() {
		return blueDEFENDdot;
	}
	public static void setBlueDEFENDdot(Point2D_I32 blueDEFENDdot) {
		ObjectLocations.blueDEFENDdot = blueDEFENDdot;
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

	public static Point2D_I32 getBall(){
		while(!lock);
		return ball;
	}

	public static Point2D_I32 getYellowDEFENDmarker(){
		while(!lock);
		return yellowDEFENDmarker;
	}

	public static Point2D_I32 getYellowATTACKmarker(){
		while(!lock);
		return yellowATTACKmarker;
	}
	public static Point2D_I32 getBlueDEFENDmarker(){
		while(!lock);
		return blueDEFENDmarker;
	}

	public static Point2D_I32 getBlueATTACKmarker(){
		while(!lock);
		return blueATTACKmarker;
	}

	/**
	 * draws lines indicating direction of movement over an object
	 * @param g
	 * @throws Exception 
	 */
	//	public static void drawDirection(Graphics2D g, String object) throws Exception{
	//		Color c = g.getColor();
	//		g.setColor(Color.MAGENTA);
	//		double angle = 0;
	//		if (object == "ball" && ObjectLocations.ball != null) {
	//			angle = VisionOps.getDirection(SimpleViewer.ballPrvPos, SimpleViewer.ballCurPos);
	//			if (angle != 0.0){
	//				g.draw(new Line2D.Double(SimpleViewer.ballCurPos.x, SimpleViewer.ballCurPos.y, (SimpleViewer.ballCurPos.x + Math.sin(angle)*100), (SimpleViewer.ballCurPos.y + Math.cos(angle)*100)));
	//				//g.drawLine(SimpleViewer.ballCurPos.x, SimpleViewer.ballCurPos.y, (SimpleViewer.ballCurPos.x + (int) Math.sin(angle)*10), (SimpleViewer.ballCurPos.y + (int) Math.cos(angle)*10));
	//				/*System.out.println((SimpleViewer.ballCurPos.y + Math.cos(angle)*100));
	//				System.out.println((SimpleViewer.ballCurPos.x + Math.sin(angle)*100));
	//				System.out.println("drawing from " + SimpleViewer.ballCurPos.x + " , " + SimpleViewer.ballCurPos.y + "->"+" , " + " at angle " + angle);*/
	//				}
	//			}
	//		else if (object == "yellowAtk"){
	//			angle = VisionOps.getDirection(SimpleViewer.yellowAttackPrvPos, SimpleViewer.yellowAttackCurPos);
	//			if (angle != 0.0){
	//				g.draw(new Line2D.Double(SimpleViewer.yellowAttackCurPos.x, SimpleViewer.yellowAttackCurPos.y, (SimpleViewer.yellowAttackCurPos.x + Math.sin(angle)*100), (SimpleViewer.yellowAttackCurPos.y + Math.cos(angle)*100)));
	//				}
	//			}
	//		else if (object == "yellowDef"){
	//			angle = VisionOps.getDirection(SimpleViewer.yellowDefendPrvPos, SimpleViewer.yellowDefendCurPos);
	//			if (angle != 0.0){
	//				g.draw(new Line2D.Double(SimpleViewer.yellowDefendCurPos.x, SimpleViewer.yellowDefendCurPos.y, (SimpleViewer.yellowDefendCurPos.x + Math.sin(angle)*100), (SimpleViewer.yellowDefendCurPos.y + Math.cos(angle)*100)));
	//				}
	//			}
	//		else if (object == "blueAtk"){
	//			angle = VisionOps.getDirection(SimpleViewer.blueAttackPrvPos, SimpleViewer.blueAttackCurPos);
	//			if (angle != 0.0){
	//				g.draw(new Line2D.Double(SimpleViewer.blueAttackCurPos.x, SimpleViewer.blueAttackCurPos.y, (SimpleViewer.blueAttackCurPos.x + Math.sin(angle)*100), (SimpleViewer.blueAttackCurPos.y + Math.cos(angle)*100)));
	//				}
	//			}
	//		else if (object == "blueDef"){
	//			angle = VisionOps.getDirection(SimpleViewer.blueDefendPrvPos, SimpleViewer.blueDefendCurPos);
	//			if (angle != 0.0){
	//				g.draw(new Line2D.Double(SimpleViewer.blueDefendCurPos.x, SimpleViewer.blueDefendCurPos.y, (SimpleViewer.blueDefendCurPos.x + Math.sin(angle)*100), (SimpleViewer.blueDefendCurPos.y + Math.cos(angle)*100)));
	//				}
	//			}
	//		else{
	////			/throw new Exception("what the hell is a "+ object +" ?");
	//		}
	//	}

	//	//draws directions over a list of objects you want 
	//	public static void drawAllDirections(Graphics2D g, String[] objects) throws Exception{
	//		for (String obj:objects){
	//			drawDirection(g, obj);
	//		}
	//		
	//	}
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
					g.drawLine(p.x - 10, p.y, p.x + 10, p.y);
					g.drawLine(p.x, p.y - 10, p.x, p.y + 10);
				}
			}
		}

		g.setColor(Color.MAGENTA);

		try {
			g.draw(new Line2D.Double(ball.x, ball.y, (ball.x + Math.sin(ballDirectionAngle)*100), (ball.y + Math.cos(ballDirectionAngle)*100)));
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		
		try {
			g.draw(new Line2D.Double(yellowATTACKmarker.x, yellowATTACKmarker.y, (yellowATTACKmarker.x + Math.sin(yellowATTACKmarkerDirectionAngle)*100), (yellowATTACKmarker.y + Math.cos(yellowATTACKmarkerDirectionAngle)*100)));
			g.draw(new Line2D.Double(yellowDEFENDmarker.x, yellowDEFENDmarker.y, (yellowDEFENDmarker.x + Math.sin(yellowDEFENDmarkerDicrectionAngle)*100), (yellowDEFENDmarker.y + Math.cos(yellowDEFENDmarkerDicrectionAngle)*100)));
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		
		try {
			g.draw(new Line2D.Double(blueATTACKmarker.x, blueATTACKmarker.y, (blueATTACKmarker.x + Math.sin(blueATTACKmarkerDirectionAngle)*100), (blueATTACKmarker.y + Math.cos(blueATTACKmarkerDirectionAngle)*100)));
			g.draw(new Line2D.Double(blueDEFENDmarker.x, blueDEFENDmarker.y, (blueDEFENDmarker.x + Math.sin(blueDEFENDmarkerDirectionAngle)*100), (blueDEFENDmarker.y + Math.cos(blueDEFENDmarkerDirectionAngle)*100)));
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		
		g.setColor(Color.YELLOW);
		try {
			g.draw(new Line2D.Double(yellowDEFENDmarker.x, yellowDEFENDmarker.y, (yellowDEFENDmarker.x + Math.sin(yellowDEFENDmarkerOrientationAngle)*100), (yellowDEFENDmarker.y + Math.cos(yellowDEFENDmarkerOrientationAngle)*100)));
			g.draw(new Line2D.Double(yellowATTACKmarker.x, yellowATTACKmarker.y, (yellowATTACKmarker.x + Math.sin(yellowATTACKmarkerOrientationAngle)*100), (yellowATTACKmarker.y + Math.cos(yellowATTACKmarkerOrientationAngle)*100)));
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		
		try {
			g.draw(new Line2D.Double(blueDEFENDmarker.x, blueDEFENDmarker.y, (blueDEFENDmarker.x + Math.sin(blueDEFENDmarkerOrientationAngle)*100), (blueDEFENDmarker.y + Math.cos(blueDEFENDmarkerOrientationAngle)*100)));
			g.draw(new Line2D.Double(blueATTACKmarker.x, blueATTACKmarker.y, (blueATTACKmarker.x + Math.sin(blueATTACKmarkerOrientationAngle)*100), (blueATTACKmarker.y + Math.cos(blueATTACKmarkerOrientationAngle)*100)));
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		
		

		g.setColor(c);
	}

}
