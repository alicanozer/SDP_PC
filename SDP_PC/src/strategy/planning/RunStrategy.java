package strategy.planning;

import georegression.struct.point.Point2D_I32;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import lejos.pc.comm.NXTCommException;
import movement.RobotMover;
import comms.Bluetooth;
import comms.BluetoothRobot;
import comms.BluetoothRobotOld;
import Calculations.BallPossession;
import Calculations.DistanceCalculator;
import Calculations.GoalInfo;
import Calculations.IntersectionLines;
import strategy.movement.MoveToPoint;
import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.PointUtils;
import vision.VisionRunner;
import world.RobotType;

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
	private final JButton connectBluetooth = new JButton("Connect");
	private final JButton disconnectBluetooth = new JButton("Disconnect");
	
	//Bluetooth stuff
    private final BluetoothRobot attackRobot;
    private final BluetoothRobot defenseRobot;
    private RobotMover attackMover;
    private RobotMover defenceMover;

    //Strategy stuff
	private Thread strategyThread;
	private StrategyInterface strategy;
	
	public static void main(String[] args) throws Exception {

		//Start vision
		VisionRunner.startDebugVision(PitchConstants.newPitch, 10, true);
		
		//Create Bluetooth connections
		Bluetooth myConnection = new Bluetooth("both"); //should be "both"
		BluetoothRobot defenseRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
		BluetoothRobot attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);

		//Check if Bluetooth connection was successful
		while (!defenseRobot.isDefenceConnected() && !attackRobot.isAttackConnected()) { // include && !attackRobot.isAttackConnected() for both
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
	 * @throws Exception 
	 */
	private void startStrategy() throws Exception {
		assert (strategyThread == null || !strategyThread.isAlive()) : "Strategy is already running";
		System.out.println("Starting Strategy...");
//		strategy = new Friendly(attackMover, defenseMover); //Put your strategy class here.
//		strategyThread = new Thread(strategy);
//		strategyThread.start();		
		

// ********** TESTING ATTACK STRATEGY
		
		//grab the ball
//		MoveToPointXY.moveRobotToBall("defence", defenceMover);
//		System.out.println("Grabbing the Ball");
//		attackMover.grab("attack");
//		
//		attackMover.kick("attack");
		
//		//turn to shoot
//		double angle = TurnToObject.shootAngle();
//		System.out.println("Angle to Shoot:" + angle);	
//		attackMover.rotate("attack", (int) angle+5);	
//		
//		//kick the ball
//		System.out.println("Kicking the Ball");
//		attackMover.kick("attack");

// *********** TESTING DEFENCE STRATEGY		
		while(ObjectLocations.getBall() == null || ObjectLocations.getUSDefend() == null || ObjectLocations.getUSDefendDot() == null)
		{
			
		}

//		Point2D_I32 point = new Point2D_I32(ObjectLocations.getUSDefend().x, ObjectLocations.getBall().y);
//		double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);
//		System.out.println("Angle to parrallel with goal: " + angle);
//		if(Math.abs(angle) < 160 && Math.abs(angle) > 20){
//			defenceMover.rotate("defence", angle);
//		}
		
		
		while(!BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend()) && ObjectLocations.getBall() != null && ObjectLocations.getUSDefend() != null && ObjectLocations.getUSDefendDot() != null){
			// TURN
			Point2D_I32 point = new Point2D_I32(ObjectLocations.getUSDefend().x, ObjectLocations.getBall().y);
			double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);
			System.out.println("Angle to parrallel with goal: " + angle);
			if(Math.abs(angle) < 160 && Math.abs(angle) > 20){
				defenceMover.rotate("defence", angle);
			}
			
			// move
			MoveToPointXY.moveRobotToBlock("defence", defenceMover);
			
			MoveToPointXY.moveAwayDefence("defence", defenceMover);
		}
//		
//		//Test Defence Strategy 1		
//		while(!BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend()) && ObjectLocations.getBall() != null && ObjectLocations.getUSDefend() != null && ObjectLocations.getUSDefendDot() != null){			defenceMover.kill();
//			defenceMover.kill();	
//			MoveToPointXY.moveRobotToBlock("defence", defenceMover);
//		}
		
		//Test Defence Strategy 2
//		Point2D_I32 intersect = IntersectionLines.intersectLines(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), ObjectLocations.getTHEMAttackDot(), ObjectLocations.getTHEMAttack());
//		System.out.println("Point( " + intersect.x + ", " + intersect.y + ")");
//		MoveToPointXY.moveRobotToPoint("defence", defenceMover, intersect);
		
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
		this.defenceMover = new RobotMover(defenseRobot);
		this.attackMover.start();
		this.defenceMover.start();
		
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
		movePanel.add(connectBluetooth);
		movePanel.add(disconnectBluetooth);
				
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Allow restart of strategies after previously killing all
				// strategies
				Strategy.reset();

				try {
					startStrategy();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Halt and clear active movements
				try {
					attackMover.interruptMove();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					defenceMover.interruptMove();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					attackMover.resetQueue();
					defenceMover.resetQueue();
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
				defenceMover.stopRobot("defence");
			}
		});
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Disconnecting...");
				Strategy.alldie = true;
				// Kill the mover and wait for it to stop completely
				if (attackMover.isAlive() && defenceMover.isAlive()) {
					try {
						attackMover.kill();
						defenceMover.kill();
						attackMover.join(3000);
						defenceMover.join(3000);
						// If the mover still hasn't stopped within 3
						// seconds,
						// assume it's stuck and kill the program
						if (attackMover.isAlive()||defenceMover.isAlive()) {
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
					defenceMover = new RobotMover(defenseRobot);
					attackMover.start();
					defenceMover.start();
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
					defenceMover.kill();
					// If the mover still hasn't stopped within 3 seconds,
					// assume it's stuck and kill the program the hard way
					attackMover.join(3000);
					defenceMover.join(3000);
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
				defenceMover.kick("defence");
			
			}
		});
		
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				attackMover.forward("attack", 5);
				defenceMover.forward("defence", 5);
			}
		});

		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defenceMover.forward("defence", -5);
				attackMover.forward("attack", -5);
			}
		});

		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defenceMover.rotate("defence", 90);
				attackMover.rotate("attack", 90);
			}
		});

		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defenceMover.rotate("defence", -90);
				attackMover.rotate("attack", -90);
			}
		});
		
		grabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defenceMover.grab("defence");
				attackMover.grab("attack");
			}
		});
		
		connectBluetooth.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Create Bluetooth connections
				Bluetooth myConnection = null;
				try {
					myConnection = new Bluetooth("both");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NXTCommException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} //should be "both"
				BluetoothRobot defenseRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
				BluetoothRobot attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);
			}
		});
		
		disconnectBluetooth.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				attackRobot.disconnect("attack");
				defenseRobot.disconnect("attack");				
			}
		});

	}
	
}

