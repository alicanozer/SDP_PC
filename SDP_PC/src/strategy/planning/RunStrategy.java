package strategy.planning;

import comms.Bluetooth;
import comms.BluetoothRobot;

import Calculations.GoalInfo;
import World.RobotType;

import vision.PitchConstants;
import vision.VisionRunner;
import World.WorldState;
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
	private WorldState worldstate;

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
		GoalInfo goalInfo = new GoalInfo(pitchconstants);
		WorldState worldstate = new WorldState(goalInfo);

		RunStrategy teststrat = new RunStrategy(worldstate, bRobot);

	}

	/**
	 * Starts the strategy thread. Checks if a strategy is already running first.
	 */
	private void startStrategy() {
		assert (strategyThread == null || !strategyThread.isAlive()) : "Strategy is already running";
		strategy = new TestStrategy(bRobot); //Put your strategy class here.
		strategyThread = new Thread(strategy);
		strategyThread.start();
	}

	public RunStrategy (final WorldState worldstate, final BluetoothRobot robot){
		this.worldstate = worldstate;
		this.bRobot = robot;

		Strategy.reset();
		startStrategy();
	}
}

