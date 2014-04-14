package strategy.planning;

import java.awt.Color;
import java.awt.Graphics2D;
import comms.Bluetooth;
import comms.BluetoothRobot;
import Calculations.GoalInfo;
import World.RobotType;
import vision.PitchConstants;
import vision.VisionRunner;

/**
 * Runs the vision, starts a bluetooth connection with HERCULES and starts the
 * strategy. 
 * 
 * To test with your own strategy replace 'TestStrategy' with the class name of your strategy.
 * @author s0925284
 *
 */
public class RunStrategy {

	static BluetoothRobot bRobot;
	private static Bluetooth connection;

	private Thread strategyThread;
	private StrategyInterface strategy;

	public static void main(String[] args) throws Exception {

		VisionRunner.start(true,PitchConstants.newPitch,10);
		

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

		PitchConstants pitchconstants = PitchConstants.newPitch;

		RunStrategy teststrat = new RunStrategy(bRobot);

	}

	/**
	 * Starts the strategy thread. Checks if a strategy is already running first.
	 */
	private void startStrategy() {
		assert (strategyThread == null || !strategyThread.isAlive()) : "Strategy is already running";
		strategy = new InterceptBall(bRobot); //Put your strategy class here.
		strategyThread = new Thread(strategy);
		strategyThread.start();
	}

	public RunStrategy (final BluetoothRobot robot){
		this.bRobot = robot;

		Strategy.reset();
		startStrategy();
	}
}

