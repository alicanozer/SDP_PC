package comms;

import georegression.struct.point.Point2D_I32;
import java.io.IOException;
import javax.swing.SwingUtilities;

import lejos.nxt.Button;
import strategy.movement.TurnToBall;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.PointUtils;
import vision.FrameHandler;
import World.Robot;
import World.RobotType;

public class Defend {

	static BluetoothRobot bRobot;
	public static final String TEAM_TRINITY = "0016530970C6";
	private static Bluetooth connection;
	
	public static void main(String[] args) throws IOException {
		
		
		
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

		bRobot = new BluetoothRobot(RobotType.DefendUs, connection);
		bRobot.connect();
		
		while(true) {
			if(ObjectLocations.getBall() != null && ObjectLocations.getYellowATTACKmarker() != null) {
				if (ObjectLocations.getBall().y > ObjectLocations.getYellowDEFENDmarker().y) {
					bRobot.forward();
				} else if (ObjectLocations.getBall().y < ObjectLocations.getYellowDEFENDmarker().y) {
					bRobot.backwards();
				} else {
					bRobot.stop();
				}	
			}

		}
		
	}
}
