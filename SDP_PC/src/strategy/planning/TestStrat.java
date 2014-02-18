package strategy.planning;

import javax.swing.JButton;

import comms.Bluetooth;
import comms.BluetoothRobot;
import comms.RobotController;

import Calculations.GoalInfo;
import World.RobotType;

import vision.PitchConstants;
import vision.VisionRunner;
import World.WorldState;

public class TestStrat {

	static BluetoothRobot bRobot;
	public static final String HERCULES = "0016530D4ED8";
	private static Bluetooth connection;

	private Thread strategyThread;
	private StrategyInterface strategy;
	private WorldState worldstate;

	private final JButton stratStartButton = new JButton("Strat Start");

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

		TestStrat teststrat = new TestStrat(worldstate, bRobot);

	}

	private void startStrategy() {
		assert (strategyThread == null || !strategyThread.isAlive()) : "Strategy is already running";
		strategy = new TestStrategy(worldstate, bRobot);
		strategyThread = new Thread(strategy);
		strategyThread.start();
	}

	public TestStrat (final WorldState worldstate, final BluetoothRobot robot){
		this.worldstate = worldstate;
		this.bRobot = robot;

		Strategy.reset();
		startStrategy();
	}
}

