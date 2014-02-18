package comms;

import georegression.struct.point.Point2D_I32;
import java.io.IOException;
import javax.swing.SwingUtilities;

import lejos.nxt.Button;
import strategy.movement.TurnToBall;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.PointUtils;
import vision.FrameHandler;
import Calculations.DistanceCalculator;
import World.Robot;
import World.RobotType;

public class Test2 {

	static BluetoothRobot bRobot;
	public static final String HERCULES = "0016530D4ED8";
	private static Bluetooth connection;
	
	public static void main(String[] args) throws Exception {
		
		ObjectLocations.setYellowDefendingLeft(true);
		ObjectLocations.setYellowUs(true);
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new FrameHandler(true, PitchConstants.newPitch);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("i failed miserably and now I must die to repent for my sins... ");
		}
//			
		bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
		bRobot.connect();
		bRobot.setSpeed(20);
		
		TurnToObject angle = new TurnToObject();
		
		while(true) {
			if(ObjectLocations.getBall() != null && ObjectLocations.getYellowATTACKmarker() != null) {
				
				double turnAngle = angle.Ball(ObjectLocations.getYellowATTACKmarker());
								
				int correct = (int) turnAngle;

				System.out.println(correct);

				if (correct > 0 && correct < 180) {
					bRobot.rotateLEFT(correct);
					Button.waitForAnyPress();
				} else {
					bRobot.rotateLEFT(-(360-correct));
					Button.waitForAnyPress();
				}
				
//				CODE TO MOVE TO THE BALL				
//				while (bRobot.isMoving()) {
//					//DO NOTHING
//				}
				
//				bRobot.stop();
//				int x1 = ObjectLocations.getYellowATTACKmarker().x;
//				int y1 = ObjectLocations.getYellowATTACKmarker().y;
//				
//				int x2 = ObjectLocations.getBall().x;
//				int y2 = ObjectLocations.getBall().y;
//				
//				double threshold = DistanceCalculator.Distance(x1, y1, x2, y2);
//				int distance = 0;
//				
//				while (distance < threshold) {
//					bRobot.forward();
//					int[] data = connection.receiveData();
//					int convertToDistance = data[0]*256*256*256 + data[1]*256*256 + data[2]*256 + data[3]*1;
//					System.out.println(convertToDistance);
//					distance = convertToDistance;
//				}
//				
//				bRobot.stop();
			}
		
		}
		
	}
	
	
	public static double turnAngle(double botBearing, double toBallBearing) {
		botBearing = Math.toDegrees(botBearing);
		double turnAngle = toBallBearing - botBearing;
		if (turnAngle > 180.0)
			turnAngle -= 360.0;
		return turnAngle;
	}
	
	public static double findPointBearing(double x, double y) {
		// calculates the bearing of a point (x,y) relative to the robot (using
		// the robot as the origin and "north"
		// as being up on the camera feed)
		// calculates the angle depending on which side of the robot the ball is
		// on
		double bearing = 0;
		double xDiff = x - ObjectLocations.getYellowATTACKmarker().x;
		double yDiff = y - ObjectLocations.getYellowATTACKmarker().y;

		// Use the dot product formula to determine the angle between the vector
		// (0, -1) (up to the camera) and the vector (xDiff, yDiff)
		bearing = Math.acos(-yDiff / Math.sqrt(xDiff * xDiff + yDiff * yDiff));

		// Correct for the case when the angle calculated above is the
		// counterclockwise bearing instead of clockwise
		if (xDiff < 0)
		bearing = 2.0 * Math.PI - bearing;

		return Math.toDegrees(bearing);
		}
	
		public static double findBearing() {
			return findPointBearing(ObjectLocations.getBall().x , ObjectLocations.getBall().y);
		}
		
		public static double Turner() {
			double ballBearing = findBearing();
			double bearing = findRawBearing(ObjectLocations.getBall().x , ObjectLocations.getBall().y);
			double angle = turnAngle(bearing, ballBearing);
			return angle;
		}
		
		public static double findRawBearing(int x, int y) {
			double bearing = 0;
			double xDiff = x - ObjectLocations.getYellowATTACKmarker().x;
			double yDiff = y - ObjectLocations.getYellowATTACKmarker().y;

			// Use the dot product formula to determine the angle between the vector
			// (0, -1) (up to the camera) and the vector (xDiff, yDiff)
			bearing = Math.acos(-yDiff / Math.sqrt(xDiff * xDiff + yDiff * yDiff));
			
			return bearing; 
		}
		
		public static double AngleTurner(double x, double y) {
			double pointBearing = findPointBearing(x, y);
			double test = findRawBearing(ObjectLocations.getYellowATTACKmarker().x, ObjectLocations.getYellowATTACKmarker().y);
			double angle = turnAngle(test, pointBearing);
			return angle;
		}
		
		public static byte[] intToByteArray(int a)
		{
		    byte[] ret = new byte[4];
		    ret[3] = (byte) (a & 0xFF);   
		    ret[2] = (byte) ((a >> 8) & 0xFF);   
		    ret[1] = (byte) ((a >> 16) & 0xFF);   
		    ret[0] = (byte) ((a >> 24) & 0xFF);
		    return ret;
		}		
		
}
