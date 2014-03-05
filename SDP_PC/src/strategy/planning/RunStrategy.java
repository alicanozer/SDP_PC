package strategy.planning;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import movement.RobotMover;
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
	private final JButton stopButton = new JButton("Stop");
	private final JButton resetButton = new JButton("Reset");
	private final JButton quitButton = new JButton("Quit");
	private final JPanel movePanel = new JPanel();
	private final JButton kickButton = new JButton("Kick");
	private final JButton grabButton = new JButton("Grab");
	private final JButton forwardButton = new JButton("Forward");
	private final JButton backwardButton = new JButton("Backward");
	private final JButton leftButton = new JButton("Left");
	private final JButton rightButton = new JButton("Right");
	
	//Bluetooth stuff
    private final BluetoothRobot attackRobot;
    private final BluetoothRobot defenseRobot;
    private RobotMover attackMover;
    private RobotMover defenseMover;

    //Strategy stuff
	private Thread strategyThread;
	private StrategyInterface strategy;

	public static void main(String[] args) throws Exception {

		//Start vision
		VisionRunner.startDebugVision(PitchConstants.oldPitch, 10, true);
		
		//Create Bluetooth connections
		Bluetooth myConnection = new Bluetooth("both"); //should be "both"
		BluetoothRobot defenseRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
		BluetoothRobot attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);

		//Check if Bluetooth connection was successful
		while (!defenseRobot.isDefenceConnected()&& !attackRobot.isAttackConnected()) { // include && !attackRobot.isAttackConnected() for both
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
		strategy = new Friendly(attackMover, defenseMover); //Put your strategy class here.
		strategyThread = new Thread(strategy);
		strategyThread.start();
	}
	private void cleanQuit() {
		if (attackRobot.isAttackConnected())
			attackRobot.disconnect("attack");
		if (defenseRobot.isDefenceConnected())
			defenseRobot.disconnect("defence");
		System.exit(0);
	}
	/**
	 * Creating GUI
	 * @param attackRobot
	 * @param defenseRobot
	 */
	public RunStrategy (final BluetoothRobot attackRobot, final BluetoothRobot defenseRobot){
		this.attackRobot = attackRobot;
		this.defenseRobot = defenseRobot;
		this.attackMover = new RobotMover(attackRobot);
		this.defenseMover = new RobotMover(defenseRobot);
		this.attackMover.start();
		this.defenseMover.start();
		
		this.setTitle("Strategy GUI");
	
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_startStopQuitPanel = new GridBagConstraints();
		this.getContentPane().add(startPanel, gbc_startStopQuitPanel);
		startPanel.add(startButton);
		startPanel.add(stopButton);
		startPanel.add(resetButton);
		startPanel.add(quitButton);
		
		GridBagConstraints gbc_panel = new GridBagConstraints();
		this.getContentPane().add(movePanel, gbc_panel);
		movePanel.add(kickButton);
		movePanel.add(forwardButton);
		movePanel.add(backwardButton);
		movePanel.add(leftButton);
		movePanel.add(rightButton);
		movePanel.add(grabButton);
				
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Allow restart of strategies after previously killing all
				// strategies
				Strategy.reset();

				startStrategy();
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Halt and clear active movements
				attackMover.interruptMove();
				defenseMover.interruptMove();
				try {
					attackMover.resetQueue();
					defenseMover.resetQueue();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Stop strategy if it's running
				if (strategyThread != null && strategyThread.isAlive()) {
					System.out.println("Killing strategy thread");
					Strategy.stop();
					strategy.kill();
					try {
						strategyThread.join(3000);
						if (strategyThread.isAlive()) {
							System.out.println("Strategy failed to stop");
							cleanQuit();
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				System.out.println("Stopping the robot");
				// Stop the robot.
				attackMover.stopRobot("attack");
				defenseMover.stopRobot("defence");
			}
		});
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Disconnecting...");
				Strategy.alldie = true;
				// Kill the mover and wait for it to stop completely
				if (attackMover.isAlive() && defenseMover.isAlive()) {
					try {
						attackMover.kill();
						defenseMover.kill();
						attackMover.join(3000);
						defenseMover.join(3000);
						// If the mover still hasn't stopped within 3
						// seconds,
						// assume it's stuck and kill the program
						if (attackMover.isAlive()||defenseMover.isAlive()) {
							System.out.println("Could not kill mover! Shutting down GUI...");
							cleanQuit();
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				attackRobot.disconnect("attack");
				defenseRobot.disconnect("defence");
				System.out.println("Disconnected succesfully");
				System.out.println("Reconnecting...");
				try {
					Thread.sleep(400);
					attackRobot.connect();
					defenseRobot.connect();
					attackMover = new RobotMover(attackRobot);
					defenseMover = new RobotMover(defenseRobot);
					attackMover.start();
					defenseMover.start();
					System.out.println("Reconnected successfully!");
				} catch (Exception e1) {
					System.out.println("Failed to reconnect! Shutting down GUI...");
					cleanQuit();
				}
			}
		});
		
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Strategy.alldie = true;
				// Kill the mover and wait for it to stop completely
				try {
					attackMover.kill();
					defenseMover.kill();
					// If the mover still hasn't stopped within 3 seconds,
					// assume it's stuck and kill the program the hard way
					attackMover.join(3000);
					defenseMover.join(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Quitting the GUI");
				cleanQuit();
			}
		});
		
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				attackMover.kick("attack");
				defenseMover.kick("defence");
			
			}
		});
		
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				attackMover.forward("attack", 5);
				defenseMover.forward("defence", 5);
			}
		});

		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defenseMover.forward("defence", -5);
				attackMover.forward("attack", -5);
			}
		});

		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defenseMover.rotate("defence", 90);
				attackMover.rotate("attack", 90);
			}
		});

		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defenseMover.rotate("defence", -90);
				attackMover.rotate("attack", -90);
			}
		});
		
		grabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defenseMover.grab("defence");
				attackMover.grab("attack");
			}
		});


	}
	
}

