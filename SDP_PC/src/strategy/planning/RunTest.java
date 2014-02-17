package strategy.planning;

import javax.swing.SwingUtilities;


import Calculations.GoalInfo;
import World.RobotType;

import comms.Bluetooth;
import comms.BluetoothRobot;

import vision.ObjectLocations;
import vision.SimpleViewer;
import World.WorldState;

public class RunTest {

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
					new SimpleViewer();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("i failed miserably and now I must die to repent for my sins... ");
		}

//		ObjectLocations obj = new ObjectLocations();
//		GoalInfo goalInfo = new GoalInfo(obj);
//		WorldState worldstate = new WorldState(goalInfo);



		bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
		bRobot.connect();

		while (!bRobot.isConnected()) {
			// Reduce CPU cost
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		System.out.println("Robot ready!");

//		while (true) {
//			Thread strategy = new Thread(new TestStrategy(worldstate, bRobot));
//			strategy.start();
//		}

	}

}

