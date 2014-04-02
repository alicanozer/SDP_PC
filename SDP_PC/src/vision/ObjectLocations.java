package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

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
	private static ArrayList<Polygon> platePolygons = null;
	public static double frameRate;
	private static double ballSpeed = 0;

	private static Object lock1 = new Object();
	public static double getBallSpeed(){
		synchronized(lock1){
			return ballSpeed;
		}
	}
	public static double getBallDirectionAngle() {
		synchronized(lock1){
			return ballDirectionAngle;
		}
	}
	public static double getYellowATTACKmarkerDirectionAngle() {
		synchronized(lock1){
			return yellowATTACKmarkerDirectionAngle;
		}
	}
	public static double getYellowDEFENDmarkerDicrectionAngle() {
		synchronized(lock1){
			return yellowDEFENDmarkerDicrectionAngle;}
	}
	public static double getBlueATTACKmarkerDirectionAngle() {
		synchronized(lock1){
			return blueATTACKmarkerDirectionAngle;}
	}
	public static double getBlueDEFENDmarkerDirectionAngle() {
		synchronized(lock1){
			return blueDEFENDmarkerDirectionAngle;}
	}

	private static double ballDirectionAngle;
	private static double yellowATTACKmarkerDirectionAngle;
	private static double yellowDEFENDmarkerDicrectionAngle;
	private static double blueATTACKmarkerDirectionAngle;
	private static double blueDEFENDmarkerDirectionAngle;

	public static double getYellowATTACKmarkerOrientationAngle() {
		synchronized(lock1){
			return yellowATTACKmarkerOrientationAngle;
		}
	}
	public static double getYellowDEFENDmarkerOrientationAngle() {
		synchronized(lock1){
			return yellowDEFENDmarkerOrientationAngle;
		}
	}
	public static double getBlueATTACKmarkerOrientationAngle() {
		synchronized(lock1){
			return blueATTACKmarkerOrientationAngle;
		}
	}
	public static double getBlueDEFENDmarkerOrientationAngle() {
		synchronized(lock1){
			return blueDEFENDmarkerOrientationAngle;}
	}

	private static double yellowATTACKmarkerOrientationAngle = 0.0;
	private static double yellowDEFENDmarkerOrientationAngle = 0.0;
	private static double blueATTACKmarkerOrientationAngle = 0.0;
	private static double blueDEFENDmarkerOrientationAngle = 0.0;

	private static ArrayList<Point2D_I32> dots = null;
	// we assume the leftmost region of the pitch is region 1
	private static boolean yellowLeft; // flag whether the yellow team is defending the left goal
	private static boolean yellowUs;   // flag whether we are the yellow team

	private static PitchConstants consts;

	public static PitchConstants getConsts() {
		return consts;
	}
	public static void setConsts(PitchConstants consts) {
		ObjectLocations.consts = consts;
	}
	private static boolean lock = true;

	public static void setYellowDefendingLeft(boolean flag){
		synchronized(lock1){

			//entering critical section
			lock = false;
			yellowLeft = flag;
			lock = true;}
		//leaving critical section
	}
	public static boolean getYellowDefendingLeft() {
		return yellowLeft;
	}

	public static void setYellowUs(boolean flag){
		synchronized(lock1){

			//entering critical section
			lock = false;
			yellowUs = flag;
			lock = true;}
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
	public static void updateObjectLocations(
			BufferedImage img, 
			float[][] colors,
			float[] distanceThresholds,
			int radius){
		HashMap<Integer,ArrayList<Point2D_I32>> objectsToLocations = VisionOps.getMultipleObjects(img, colors, distanceThresholds,false,radius);
		Point2D_I32 ballLocal = VisionOps.findBallFromMapping(objectsToLocations);
		platePolygons = VisionOps.findGreenPlates(objectsToLocations,PitchConstants.region1,PitchConstants.region2, PitchConstants.region3, PitchConstants.region4);
		ArrayList<Point2D_I32> yellowMarkers = VisionOps.findYellowMarkersFromMapping(objectsToLocations, consts.getMiddleLine(),platePolygons);
		ArrayList<Point2D_I32> blueMarkers = VisionOps.findBlueMarkersFromMapping(objectsToLocations, consts.getMiddleLine(),platePolygons);



		ArrayList<Point2D_I32> dotsLocal = new ArrayList<Point2D_I32>();


		if(platePolygons != null){
			for(Polygon p : platePolygons){
				if(p != null){
					Point2D_I32 newCentre = VisionOps.getDotFromPlate(p,objectsToLocations.get(4));
					if(newCentre != null) dotsLocal.add(newCentre);
				}
			}
		}

		//		if(yellowMarkers != null){
		//			for(Point2D_I32 p: yellowMarkers){
		//				Point2D_I32 newCentre = VisionOps.getMeanDotNearMarker(img,p,30);
		////				Point2D_I32 newCentre = VisionOps.getDotPosition(
		////						img, 
		////						p, 
		////						30, 
		////						2, 
		////						1, 
		////						colors.getBlackValue(), 
		////						colors.getYellowValue(), 
		////						colors.getGreenPlateValue());   //.getMeanDotNearMarker(img,p,40,colors.getBlackValue());
		//				if(newCentre != null)
		//					dotsLocal.add(newCentre); // window 44 works well
		//			}
		//		}
		//
		//		if(blueMarkers != null){
		//			for(Point2D_I32 p: blueMarkers){
		//				Point2D_I32 newCentre = VisionOps.getMeanDotNearMarker(img,p,30);
		////				Point2D_I32 newCentre = VisionOps.getDotPosition(
		////						img, 
		////						p, 
		////						30, 
		////						2, 
		////						1, 
		////						colors.getBlackValue(), 
		////						colors.getBlueValue(), 
		////						colors.getGreenPlateValue()); 
		//				if(newCentre != null)
		//					dotsLocal.add(newCentre); // window 44 works well
		//			}
		//		}

		//setting the ball and its angle
		try {
			ballDirectionAngle = VisionOps.getDirection(ball,ballLocal);
		} catch (Exception e) {

		}
		// before we set the ball
		synchronized(lock1){
			try {
				double dist = 0.0;
				if(ballLocal != null & ObjectLocations.getBall()!= null){
					dist= PointUtils.euclideanDistance(ballLocal, ObjectLocations.getBall());
					double v = dist*frameRate;
					//set ball speed
					ballSpeed = v;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// set the ball
		setBall(ballLocal);

		if(yellowLeft){
			//set yellow
			if(yellowMarkers != null){
				for(Point2D_I32 p: yellowMarkers){
					if(p != null){
						if(p.x < consts.getRegion12X()) {
							try {
								//if(PointUtils.euclideanDistance(yellowDEFENDmarker, p) > 3)
								yellowDEFENDmarkerDicrectionAngle = VisionOps.getDirection(yellowDEFENDmarker, p);
							} catch (Exception e) {

							}
							setYellowDEFENDmarker(p);
						}

						if(consts.getRegion23X() < p.x && p.x < consts.getRegion34X()) {
							try {
								//if(PointUtils.euclideanDistance(yellowATTACKmarker, p) > 3 )
								yellowATTACKmarkerDirectionAngle = VisionOps.getDirection(yellowATTACKmarker, p);
							} catch (Exception e) {

							}
							setYellowATTACKmarker(p);
						}
					}
				}
			}
			//set blue
			if(blueMarkers != null){
				for(Point2D_I32 p: blueMarkers){
					if(p!= null){
						if(consts.getRegion12X() < p.x && p.x < consts.getRegion23X()) {
							try {
								blueATTACKmarkerDirectionAngle = VisionOps.getDirection(blueATTACKmarker, p);
							} catch (Exception e) {

							}
							setBlueATTACKmarker(p);
						}
						if(p.x > consts.getRegion34X()) {
							try {
								blueDEFENDmarkerDirectionAngle = VisionOps.getDirection(blueDEFENDmarker, p);
							} catch (Exception e) {

							}
							setBlueDEFENDmarker(p);
						}
					}
				}
			}
		}
		else{ // blueLeft
			//set yellow
			if(yellowMarkers != null){
				for(Point2D_I32 p: yellowMarkers){
					if(p != null){
						if(consts.getRegion12X() < p.x && p.x < consts.getRegion23X()) {
							try {
								//if(PointUtils.euclideanDistance(yellowATTACKmarker, p) > 7 )
								yellowATTACKmarkerDirectionAngle = VisionOps.getDirection(yellowATTACKmarker, p);
							} catch (Exception e) {

							}
							setYellowATTACKmarker(p);
						}
						if(p.x > consts.getRegion34X()) {
							try {
								//if(PointUtils.euclideanDistance(yellowDEFENDmarker, p) > 3)
								yellowDEFENDmarkerDicrectionAngle = VisionOps.getDirection(yellowDEFENDmarker, p);
							} catch (Exception e) {

							}
							setYellowDEFENDmarker(p);
						}
					}
				}
			}
			//set blue
			if(blueMarkers != null){
				for(Point2D_I32 p: blueMarkers){
					if(p != null){
						if(p.x < consts.getRegion12X()) {
							try {
								blueDEFENDmarkerDirectionAngle = VisionOps.getDirection(blueDEFENDmarker, p);
							} catch (Exception e) {

							}
							setBlueDEFENDmarker(p);
						}
						if(consts.getRegion23X() < p.x && p.x < consts.getRegion34X()) {
							try {
								blueATTACKmarkerDirectionAngle = VisionOps.getDirection(blueATTACKmarker, p);
							} catch (Exception e) {

							}
							setBlueATTACKmarker(p);
						}
					}
				}
			}
		}

		//setting orientation angles

		dots = dotsLocal;
		double angleTolerance = 6.0; // 0.17 radians is 10 degrees
		double distanceTolerance = 70.0;
		if(yellowLeft){
			for(Point2D_I32 dot : dots){
				if(dot != null){
					if (dot.x < consts.getRegion12X())
						try {
							double newAngle = VisionOps.getDirection(yellowDEFENDmarker, dot);
							if(Math.abs(newAngle - yellowDEFENDmarkerOrientationAngle) < angleTolerance)
								yellowDEFENDmarkerOrientationAngle = VisionOps.getDirection(yellowDEFENDmarker, dot);
							//
							//if(PointUtils.euclideanDistance(dot, yellowDEFENDdot) < distanceTolerance) 
							setYellowDEFENDdot(dot);
							//
						} catch (Exception e) {
						}
					else if(consts.getRegion12X() < dot.x && dot.x < consts.getRegion23X())
						try {
							double newAngle = VisionOps.getDirection(blueATTACKmarker, dot);
							if(Math.abs(newAngle - blueATTACKmarkerOrientationAngle) < angleTolerance)
								blueATTACKmarkerOrientationAngle = VisionOps.getDirection(blueATTACKmarker, dot);
							//
							//if(PointUtils.euclideanDistance(dot, blueATTACKdot) < distanceTolerance) 
							setBlueATTACKdot(dot);
							//
						} catch (Exception e) {
						}
					else if (consts.getRegion23X() < dot.x && dot.x < consts.getRegion34X()){
						try {
							double newAngle = VisionOps.getDirection(yellowATTACKmarker, dot);
							if(Math.abs(newAngle - yellowATTACKmarkerOrientationAngle) < angleTolerance)
								yellowATTACKmarkerOrientationAngle = VisionOps.getDirection(yellowATTACKmarker, dot);
							//
							//if(PointUtils.euclideanDistance(dot, yellowATTACKdot) < distanceTolerance) 
							setYellowATTACKdot(dot);
							//
						} catch (Exception e) {
						}
					}
					else if(dot.x > consts.getRegion34X()){
						try {
							double newAngle = VisionOps.getDirection(blueDEFENDmarker, dot);
							if(Math.abs(newAngle - blueDEFENDmarkerOrientationAngle) < angleTolerance) // 10 degrees is 0.17 radians
								blueDEFENDmarkerOrientationAngle = VisionOps.getDirection(blueDEFENDmarker, dot);
							//
							//if(PointUtils.euclideanDistance(dot, blueDEFENDdot) < distanceTolerance) 
							setBlueDEFENDdot(dot);
							//
						} catch (Exception e) {
						}
					}
				}
			}
		}
		else{ // blue if left
			for(Point2D_I32 dot : dots){
				if(dot != null){
					if (dot.x < consts.getRegion12X())
						try {
							double newAngle = VisionOps.getDirection(blueDEFENDmarker, dot);
							if(Math.abs(newAngle - blueDEFENDmarkerOrientationAngle) < angleTolerance)
								blueDEFENDmarkerOrientationAngle = VisionOps.getDirection(blueDEFENDmarker, dot);
							//
							//if(PointUtils.euclideanDistance(dot, blueDEFENDdot) < distanceTolerance) 
							setBlueDEFENDdot(dot);
							//
						} catch (Exception e) {

						}
					else if(consts.getRegion12X() < dot.x && dot.x < consts.getRegion23X())
						try {
							double newAngle = VisionOps.getDirection(yellowATTACKmarker, dot);
							if(Math.abs(newAngle - yellowATTACKmarkerOrientationAngle) < angleTolerance)
								yellowATTACKmarkerOrientationAngle = VisionOps.getDirection(yellowATTACKmarker, dot);
							//
							//if(PointUtils.euclideanDistance(dot, yellowATTACKdot) < distanceTolerance) 
							setYellowATTACKdot(dot);
							//
						} catch (Exception e) {

						}
					else if (consts.getRegion23X() < dot.x && dot.x < consts.getRegion34X()){
						try {
							double newAngle = VisionOps.getDirection(blueATTACKmarker, dot);
							if(Math.abs(newAngle - blueATTACKmarkerOrientationAngle) < angleTolerance) 
								blueATTACKmarkerOrientationAngle = VisionOps.getDirection(blueATTACKmarker, dot);
							//
							//if(PointUtils.euclideanDistance(dot, blueATTACKdot) < distanceTolerance) 
							setBlueATTACKdot(dot);
							//
						} catch (Exception e) {

						}
					}
					else if(dot.x > consts.getRegion34X()){
						try {
							double newAngle = VisionOps.getDirection(yellowDEFENDmarker, dot);
							if(Math.abs(newAngle - yellowDEFENDmarkerOrientationAngle) < angleTolerance) 
								yellowDEFENDmarkerOrientationAngle = VisionOps.getDirection(yellowDEFENDmarker, dot);
							//
							//if(PointUtils.euclideanDistance(dot, yellowDEFENDdot) < distanceTolerance) 
							setYellowDEFENDdot(dot);
							//
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
		synchronized(lock1){
			//entering critical section
			lock = false;
			ball = pos;
			lock = true;}
		//leaving critical section
	}

	private static void setYellowDEFENDmarker(Point2D_I32 pos) {
		synchronized(lock1){
			//entering critical section
			lock = false;
			yellowDEFENDmarker = pos;
			lock = true;}
		//leaving critical section
	}

	private static void setYellowATTACKmarker(Point2D_I32 pos) {
		synchronized(lock1){
			//entering critical section
			lock = false;
			yellowATTACKmarker = pos;
			lock = true;}
		//leaving critical section
	}

	private static void setBlueDEFENDmarker(Point2D_I32 pos) {
		synchronized(lock1){
			//entering critical section
			lock = false;
			blueDEFENDmarker = pos;
			lock = true;}
		//leaving critical section
	}

	private static void setBlueATTACKmarker(Point2D_I32 pos) {
		synchronized(lock1){
			//entering critical section
			lock = false;
			blueATTACKmarker = pos;
			lock = true;}
		//leaving critical section
	}

	public static Point2D_I32 getBall(){
		synchronized(lock1){
			return ball;}
	}

	public static Point2D_I32 getYellowDEFENDmarker(){
		synchronized(lock1){
			return yellowDEFENDmarker;}
	}

	public static Point2D_I32 getYellowATTACKmarker(){
		synchronized(lock1){
			return yellowATTACKmarker;}
	}
	public static Point2D_I32 getBlueDEFENDmarker(){
		synchronized(lock1){
			return blueDEFENDmarker;}
	}

	public static Point2D_I32 getBlueATTACKmarker(){
		synchronized(lock1){
			return blueATTACKmarker;}
	}

	public static Point2D_I32 getUSAttack(){
		synchronized(lock1){
			if(yellowUs) return getYellowATTACKmarker();
			else return getBlueATTACKmarker();}
	}

	public static Point2D_I32 getUSAttackDot() {
		synchronized(lock1){
			if(yellowUs) return getYellowATTACKdot();
			else return getBlueATTACKdot();}
	}

	public static Point2D_I32 getUSDefend(){
		synchronized(lock1){
			if(yellowUs) return getYellowDEFENDmarker();
			else return getBlueDEFENDmarker();}
	}

	public static Point2D_I32 getUSDefendDot() {
		synchronized(lock1){
			if(yellowUs) return getYellowDEFENDdot();
			else return getBlueDEFENDdot();}
	}

	public static Point2D_I32 getTHEMAttack() {
		synchronized(lock1){
			if(yellowUs) return getBlueATTACKmarker();
			else return getYellowATTACKmarker();}
	}

	public static Point2D_I32 getTHEMAttackDot() {
		synchronized(lock1){
			if(yellowUs) return getBlueATTACKdot();
			else return getYellowATTACKdot();}
	}


	public static Point2D_I32 getTHEMDefend() {
		synchronized(lock1){
			if (yellowUs) return getBlueDEFENDmarker();
			else return getYellowDEFENDmarker();}
	}

	public static Point2D_I32 getTHEMDefendDot() {
		synchronized(lock1){
			if(yellowUs) return getBlueDEFENDdot();
			else return getYellowDEFENDdot();}
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
		g.setColor(Color.RED);
		if (ObjectLocations.ball != null) {
			// drawing X over ball			//	
			g.drawLine(ObjectLocations.ball.x - 5, ObjectLocations.ball.y, ObjectLocations.ball.x + 5, ObjectLocations.ball.y);
			g.drawLine(ObjectLocations.ball.x, ObjectLocations.ball.y - 5, ObjectLocations.ball.x, ObjectLocations.ball.y + 5);
		}
		// WRONG position of yellowDEFENDmarker
		g.setColor(Color.YELLOW);
		if(yellowDEFENDmarker != null){
			g.drawLine(yellowDEFENDmarker.x - 5, yellowDEFENDmarker.y, yellowDEFENDmarker.x + 5, yellowDEFENDmarker.y );
			g.drawLine(yellowDEFENDmarker.x, yellowDEFENDmarker.y - 5, yellowDEFENDmarker.x, yellowDEFENDmarker.y + 5);
			g.drawOval(yellowDEFENDmarker.x - 22, yellowDEFENDmarker.y - 22, 44, 44);
		}

		if(yellowATTACKmarker != null){
			g.drawLine(yellowATTACKmarker.x - 5, yellowATTACKmarker.y, yellowATTACKmarker.x + 5, yellowATTACKmarker.y );
			g.drawLine(yellowATTACKmarker.x, yellowATTACKmarker.y - 5, yellowATTACKmarker.x, yellowATTACKmarker.y + 5);
			g.drawOval(yellowATTACKmarker.x - 22, yellowATTACKmarker.y - 22, 44, 44);
		}
		g.setColor(Color.BLUE);
		if(blueDEFENDmarker != null){
			g.drawLine(blueDEFENDmarker.x - 5, blueDEFENDmarker.y, blueDEFENDmarker.x + 5, blueDEFENDmarker.y );
			g.drawLine(blueDEFENDmarker.x, blueDEFENDmarker.y - 5, blueDEFENDmarker.x, blueDEFENDmarker.y + 5);
			g.drawOval(blueDEFENDmarker.x - 22, blueDEFENDmarker.y - 22, 44, 44);
		}
		g.setColor(Color.BLUE);
		if(blueATTACKmarker != null){
			g.drawLine(blueATTACKmarker.x - 5, blueATTACKmarker.y, blueATTACKmarker.x + 5, blueATTACKmarker.y );
			g.drawLine(blueATTACKmarker.x, blueATTACKmarker.y - 5, blueATTACKmarker.x, blueATTACKmarker.y + 5);
			g.drawOval(blueATTACKmarker.x - 22, blueATTACKmarker.y - 22, 44, 44);
		}

		g.setColor(Color.BLACK);
		if(dots != null){
			for(Point2D_I32 p: dots){
				if(p != null){
					g.drawLine(p.x - 5, p.y, p.x + 5, p.y);
					g.drawLine(p.x, p.y - 5, p.x, p.y + 5);
				}
			}
		}

		g.setColor(Color.MAGENTA);

		try {
			g.draw(new Line2D.Double(ball.x, ball.y, (ball.x + Math.sin(ballDirectionAngle)*100), (ball.y + Math.cos(ballDirectionAngle)*100)));
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

		//		try {
		//			g.draw(new Line2D.Double(yellowATTACKmarker.x, yellowATTACKmarker.y, (yellowATTACKmarker.x + Math.sin(yellowATTACKmarkerDirectionAngle)*100), (yellowATTACKmarker.y + Math.cos(yellowATTACKmarkerDirectionAngle)*100)));
		//			g.draw(new Line2D.Double(yellowDEFENDmarker.x, yellowDEFENDmarker.y, (yellowDEFENDmarker.x + Math.sin(yellowDEFENDmarkerDicrectionAngle)*100), (yellowDEFENDmarker.y + Math.cos(yellowDEFENDmarkerDicrectionAngle)*100)));
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//
		//		}
		//		
		//		try {
		//			g.draw(new Line2D.Double(blueATTACKmarker.x, blueATTACKmarker.y, (blueATTACKmarker.x + Math.sin(blueATTACKmarkerDirectionAngle)*100), (blueATTACKmarker.y + Math.cos(blueATTACKmarkerDirectionAngle)*100)));
		//			g.draw(new Line2D.Double(blueDEFENDmarker.x, blueDEFENDmarker.y, (blueDEFENDmarker.x + Math.sin(blueDEFENDmarkerDirectionAngle)*100), (blueDEFENDmarker.y + Math.cos(blueDEFENDmarkerDirectionAngle)*100)));
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//
		//		}

		g.setColor(Color.WHITE);
		try {
			g.draw(new Line2D.Double(yellowDEFENDmarker.x,yellowDEFENDmarker.y, yellowDEFENDdot.x, yellowDEFENDdot.y));
			g.draw(new Line2D.Double(yellowATTACKmarker.x,yellowATTACKmarker.y, yellowATTACKdot.x, yellowATTACKdot.y));
			//System.out.println("asd1");
		} catch (Exception e) {
			//e.printStackTrace();
		}

		try {

			g.draw(new Line2D.Double(blueDEFENDmarker.x,blueDEFENDmarker.y, blueDEFENDdot.x, blueDEFENDdot.y));
			g.draw(new Line2D.Double(blueATTACKmarker.x,blueATTACKmarker.y, blueATTACKdot.x, blueATTACKdot.y));
			//System.out.println("asd2");
		} catch (Exception e) {
			//e.printStackTrace();
		}

		g.setColor(Color.GREEN);
		try{
			for(Polygon p: ObjectLocations.platePolygons){
				g.draw(p);
			}
		} catch(Exception e){

		}

		g.setColor(c);
	}

}
