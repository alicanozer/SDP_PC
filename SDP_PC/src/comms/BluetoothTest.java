package comms;

import java.io.IOException;

import javax.swing.SwingUtilities;

import strategy.movement.TurnToBall;
import vision.ObjectLocations;
import vision.FrameHandler;

import World.Robot;
import World.RobotType;

public class BluetoothTest {

	static BluetoothRobot bRobot;
	public static final String HERCULES = "0016530D4ED8";
	private static Bluetooth connection;

	
	public static void main(String[] args) throws IOException {
		
		
//		ObjectLocations.setYellowDefendingLeft(true);
//		ObjectLocations.setYellowUs(true);
//		try {
//			SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					new SimpleViewer();
//				}
//			});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("i failed miserably and now I must die to repent for my sins... ");
//		}
		
		connection = new Bluetooth(HERCULES);
		bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
//		bRobot.connect();*/
		
		Robot us = new Robot(RobotType.AttackUs); 
		double angle = TurnToBall.AngleTurner(us, ObjectLocations.getBall().x, ObjectLocations.getBall().y); 
		int angle2 = (int) Math.round(angle); 
		if(connection.isRobotReady()){
			bRobot.rotateLEFT(angle2); 
		}
	}
	
}
