package strategy.planning;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import comms.Bluetooth;
import comms.BluetoothRobot;
import comms.BluetoothRobotOld;
import Calculations.GoalInfo;
import World.RobotType;
import vision.PitchConstants;
import vision.VisionRunner;

/**
 * The main runner for matches. Starts the vision, creates Bluetooth connections and starts the strategy GUI.
 * 
 * To test with your own strategy replace 'TestStrategy' with the class name of your strategy.
 * @author s0925284
 *
 */
public class RunStrategy extends JFrame {

	//GUI stuff 
	private final JPanel startPanel = new JPanel();
	private final JButton startButton = new JButton("Start");
	private final JPanel movePanel = new JPanel();
	private final JButton kickButton = new JButton("Kick");
	
	//Bluetooth stuff
    private final BluetoothRobot attackRobot;
    private final BluetoothRobot defenseRobot;

    //Strategy stuff
	private Thread strategyThread;
	private StrategyInterface strategy;

	public static void main(String[] args) throws Exception {

		//Start vision
		VisionRunner.startDebugVision(PitchConstants.newPitch, 10, true);
		
		//Create Bluetooth connections
		Bluetooth myConnection = new Bluetooth("attack"); //should be "both"
		BluetoothRobot defenseRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
		BluetoothRobot attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);

		//Check if Bluetooth connection was successful
		while (!defenseRobot.isAttackConnected()) { // include && !attackRobot.isAttackConnected() for both
			// Reduce CPU cost
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		System.out.println("Robots ready!");

		//Sets up the GUI
		RunStrategy gui = new RunStrategy(attackRobot, defenseRobot);
		gui.pack();
		gui.setVisible(true);
		gui.setMinimumSize(gui.getSize());

	}

	/**
	 * Starts the strategy thread. Checks if a strategy is already running first.
	 */
	private void startStrategy() {
		assert (strategyThread == null || !strategyThread.isAlive()) : "Strategy is already running";
		System.out.println("Starting Strategy...");
		strategy = new Friendly(attackRobot, defenseRobot); //Put your strategy class here.
		strategyThread = new Thread(strategy);
		strategyThread.start();
	}
	/**
	 * Creating GUI
	 * @param attackRobot
	 * @param defenseRobot
	 */
	public RunStrategy (final BluetoothRobot attackRobot, final BluetoothRobot defenseRobot){
		this.attackRobot = attackRobot;
		this.defenseRobot = defenseRobot;
			
		this.setTitle("Strategy GUI");
	
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_startStopQuitPanel = new GridBagConstraints();
		this.getContentPane().add(startPanel, gbc_startStopQuitPanel);
		startPanel.add(startButton);
		
		GridBagConstraints gbc_panel = new GridBagConstraints();
		this.getContentPane().add(movePanel, gbc_panel);
		movePanel.add(kickButton);
				
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Allow restart of strategies after previously killing all
				// strategies
				Strategy.reset();

				startStrategy();
			}
		});
		
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attackRobot.kick("attack");
			}
		});

	}
	
}

