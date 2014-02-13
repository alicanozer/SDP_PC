package comms;

import georegression.struct.point.Point2D_I32;
import java.io.IOException;
import javax.swing.SwingUtilities;

import lejos.nxt.Button;
import strategy.movement.TurnToBall;
import vision.ObjectLocations;
import vision.PointUtils;
import vision.SimpleViewer;
import World.Robot;
import World.RobotType;

public class Test2 {

	static BluetoothRobot bRobot;
	public static final String HERCULES = "0016530D4ED8";
	private static Bluetooth connection;
	
	public static void main(String[] args) throws IOException {
		
		
		
		ObjectLocations.setYellowDefendingLeft(true);
		ObjectLocations.setYellowUs(true);
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new SimpleViewer();
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
		double turn = 0.0;
		double angle = 0.0;
		double distance = 0.0;
				
		
		while(true) {
			if(ObjectLocations.getBall() != null && ObjectLocations.getYellowDEFENDmarker() != null) {
				if (ObjectLocations.getBall().y > ObjectLocations.getYellowDEFENDmarker().y) {
					bRobot.forward();
				} else if (ObjectLocations.getBall().y < ObjectLocations.getYellowDEFENDmarker().y) {
					bRobot.backwards();
				} else {
					bRobot.stop();
				}
			}
		}
		
		/*while(true){
			if(ObjectLocations.getBall() != null && ObjectLocations.getYellowATTACKmarker() != null){
				
//				angle = AngleTurner(ObjectLocations.getBall().x, ObjectLocations.getBall().y);
				System.out.println("Ball Detected");
				System.out.println("Ball Detected");
				System.out.println("Ball Detected");
				System.out.println("Ball Detected");
				System.out.println("Ball Detected");
				System.out.println("Ball Detected");
				System.out.println("Ball Detected");
				System.out.println("Ball Detected");
				System.out.println("Ball Detected");

				int prevX = ObjectLocations.getYellowATTACKmarker().x;
				int prevY = ObjectLocations.getYellowATTACKmarker().y;
				bRobot.setSpeed(5);
				bRobot.forward();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bRobot.stop();
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				int currX = ObjectLocations.getYellowATTACKmarker().x;
				int currY = ObjectLocations.getYellowATTACKmarker().y;
				
				double deltaX = currX - prevX;
				double deltaY = currY - prevY;
				
				double d = Math.sqrt(1.0*deltaY*deltaY + 1.0*deltaX*deltaX);
				
				double c = PointUtils.euclideanDistance(new Point2D_I32(prevX, prevY), ObjectLocations.getBall());
				double beta = Math.acos(Math.abs(prevX-ObjectLocations.getBall().x)/ c);
				
				int dy = currY - prevY;
				int dx = currX - prevX;
				
				double alpha = Math.atan2(dy, dx);		
				
				double e = Math.pow((d*d) + (c*c) - (2*d*Math.cos(beta - alpha)), 0.5);
				double gamma = Math.asin((d*Math.sin(beta-alpha))/e);
				
				turn = beta + gamma - alpha;
				double ballX = ObjectLocations.getBall().x;
				double ballY = ObjectLocations.getBall().y;
				distance = Math.sqrt(Math.pow((ObjectLocations.getYellowATTACKmarker().x-ballX), 2) + Math.pow((ObjectLocations.getYellowATTACKmarker().y-ballY), 2));
				
				System.out.println(ballX + " " + ballY);
				System.out.println("alpha " + alpha);
				System.out.println("beta " + beta);
				System.out.println("gamma " + gamma);
				System.out.println("d " + d);
				System.out.println("c" + c);
				System.out.println("e" + e);
				System.out.println("angle: " + Math.toDegrees(-turn));
				System.out.println("distance: " + distance);
				bRobot.rotateLEFT((int) Math.toDegrees(-turn));
				while (distance != 0) {
					bRobot.forward();
					distance = Math.sqrt(Math.pow((ObjectLocations.getYellowATTACKmarker().x-ballX), 2) + Math.pow((ObjectLocations.getYellowATTACKmarker().y-ballY), 2));
					System.out.println("distance: " + distance);
				}
				Button.waitForAnyPress();

			}
		}*/
	
		//double angle = turnAngle(bearing, pointBearing);
//		System.out.println("angle: " + angle);
//		System.exit(0);
//		bRobot.rotateLEFT((int) angle);
//
//		bRobot.stop();
//		
//		bRobot.disconnect();
		//System.exit(1);
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
		
		
}
