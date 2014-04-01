package strategy.planning;

import georegression.struct.point.Point2D_I32;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Calculations.BallPossession;
import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.VisionRunner;
import world.RobotType;
import lejos.pc.comm.NXTCommException;
import movement.RobotMover;
import comms.Bluetooth;
import comms.BluetoothRobot;

public class StartStrategy extends JFrame {

	//GUI stuff 
	private final JPanel startPanel = new JPanel();
	private final JButton startButton = new JButton("Start");
	private final JButton stopButton = new JButton("Stop");
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
	private String[] connectOption = { "Both", "Attacker", "Defender" };
	private final JComboBox<String> connectOptions = new JComboBox<>(connectOption);
	private static int flag = -1;
	//Bluetooth stuff
    private BluetoothRobot attackRobot;
    private BluetoothRobot defenceRobot;
    private RobotMover attackMover;
    private RobotMover defenceMover;

    //Strategy Thread
	private Thread strategyThread;
	private StrategyInterface strategy;

    public static void main(String[] args) {
    	
		//Start vision
		VisionRunner.startDebugVision(PitchConstants.newPitch, 10, true);
    	
		//Sets up the GUI
		StartStrategy gui = new StartStrategy();
		gui.pack();
		gui.setVisible(true);
		gui.setMinimumSize(gui.getSize());
		
    }
	
    public void runStrategy() {
    	
		assert (strategyThread == null || !strategyThread.isAlive()) : "Strategy is already running";
		System.out.println("Starting Strategy...");
		
		while(!BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getUSDefend()) && ObjectLocations.getBall() != null && ObjectLocations.getUSDefend() != null && ObjectLocations.getUSDefendDot() != null){
			try {
				// TURN
				Point2D_I32 point = new Point2D_I32(ObjectLocations.getUSDefend().x, ObjectLocations.getBall().y);
				double angle = TurnToObject.getAngleToObject(ObjectLocations.getUSDefendDot(), ObjectLocations.getUSDefend(), point);
				System.out.println("Angle to parrallel with goal: " + angle);
				if(Math.abs(angle) < 160 && Math.abs(angle) > 20){
					System.out.println("correcting angle!!!!!!!!!!!!!!!");
					defenceMover.stopRobot("defence");
					defenceMover.rotate("defence", angle);
				}
				
				// move
				System.out.println("distance: " + Math.abs(ObjectLocations.getBall().y - ObjectLocations.getUSDefend().y));
				if(Math.abs(ObjectLocations.getBall().y - ObjectLocations.getUSDefend().y) > 10){
					MoveToPointXY.moveAwayDefence("defence", defenceMover);
					//MoveToPointXY.moveRobotToBlock("defence", defenceMover);
					MoveToPointXY.moveRobotToBlockCont("defence", defenceMover);
					
				}
				else {
					defenceMover.stopRobot("defence");
				}
			} catch (Exception e) {

				e.printStackTrace();
			}		
		}
		
    }
    
    public StartStrategy() {
		
    	this.setTitle("Strategy GUI");
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_startStopQuitPanel = new GridBagConstraints();
		this.getContentPane().add(startPanel, gbc_startStopQuitPanel);
		startPanel.add(startButton);
		startPanel.add(stopButton);
		startPanel.add(quitButton);		
		
		GridBagConstraints gbc_panel = new GridBagConstraints();
		this.getContentPane().add(movePanel, gbc_panel);
		movePanel.add(kickButton);
		movePanel.add(forwardButton);
		movePanel.add(backwardButton);
		movePanel.add(leftButton);
		movePanel.add(rightButton);
		movePanel.add(grabButton);
		movePanel.add(connectOptions);
		movePanel.add(connectBluetooth);
		movePanel.add(disconnectBluetooth);
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Allow restart of strategies after previously killing all
				// strategies
				Strategy.reset();

				try {
					runStrategy();
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
					if (flag == 0) {
						attackMover.kill();
						defenceMover.kill();
					} else if (flag == 1) {
						attackMover.kill();				
					} else if (flag == 2) {
						defenceMover.kill();
					}
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
				
				if (flag == 0) {
					attackMover.stopRobot("attack");
					defenceMover.stopRobot("defence");
				} else if (flag == 1) {
					attackMover.stopRobot("attack");
				} else if (flag == 2) {
					defenceMover.stopRobot("defence");
				}
			}
		});

		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Strategy.alldie = true;
				// Kill the mover and wait for it to stop completely
				try {
					if (flag == 0) {
						attackMover.kill();
						defenceMover.kill();
					} else if (flag == 1) {
						attackMover.kill();				
					} else if (flag == 2) {
						defenceMover.kill();
					}
					// If the mover still hasn't stopped within 3 seconds,
					// assume it's stuck and kill the program the hard way
					if (flag == 0) {
						attackMover.join(3000);
						defenceMover.join(3000);
					} else if (flag == 1) {
						attackMover.join(3000);
					} else if (flag == 2) {
						defenceMover.join(3000);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Quitting the GUI");
				cleanQuit();
			}
		});

		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				if (flag == 0) {
					attackMover.kick("attack");
					defenceMover.kick("defence");
				} else if (flag == 1) {
					attackMover.kick("attack");					
				} else if (flag == 2) {
					defenceMover.kick("defence");
				}
			}
		});

		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flag == 0) {
					attackMover.forward("attack", 5);
					defenceMover.forward("defence", 5);
				} else if (flag == 1) {
					attackMover.forward("attack", 5);
				} else if (flag == 2) {
					defenceMover.forward("defence", 5);
				}
			}
		});

		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flag == 0) {
					attackMover.forward("attack", -5);
					defenceMover.forward("defence", -5);
				} else if (flag == 1) {
					attackMover.forward("attack", -5);
				} else if (flag == 2) {
					defenceMover.forward("defence", -5);
				}
			}
		});

		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flag == 0) {
					attackMover.rotate("attack", 90);
					defenceMover.rotate("defence", 90);
				} else if (flag == 1) {
					attackMover.rotate("attack", 90);
				} else if (flag == 2) {
					defenceMover.rotate("defence", 90);
				}
			}
		});

		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flag == 0) {
					attackMover.rotate("attack", -90);
					defenceMover.rotate("defence", -90);
				} else if (flag == 1) {
					attackMover.rotate("attack", -90);
				} else if (flag == 2) {
					defenceMover.rotate("defence", -90);
				}
			}
		});

		grabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flag == 0) {
					attackMover.grab("attack");
					defenceMover.grab("defence");
				} else if (flag == 1) {
					attackMover.grab("attack");					
				} else if (flag == 2) {
					defenceMover.grab("defence");
				}
			}
		});
		
		connectOptions.addActionListener((ActionListener) this);
		
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
				
				if (connectOptions.getSelectedItem().equals("Both")) {
					defenceRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
					attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);
					attackMover = new RobotMover(attackRobot);
					defenceMover = new RobotMover(defenceRobot);
					attackMover.start();
					defenceMover.start();
					System.out.println("Both Robots Connected!!");
					flag = 0;
				} else if (connectOptions.getSelectedItem().equals("Attacker")) {
					attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);

					attackMover = new RobotMover(attackRobot);
					attackMover.start();
					System.out.println("Attacker Robot Connected!!");			
					flag =1;
				} else if (connectOptions.getSelectedItem().equals("Defender")) {
					try {
						myConnection = new Bluetooth("defence");
					} catch (IOException | NXTCommException e1) {
						e1.printStackTrace();
					}

					defenceRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
					defenceMover = new RobotMover(defenceRobot);
					defenceMover.start();
					System.out.println("Defender Robot Connected!!");		
					flag = 2;
				}
				
				
				
			}
		});

		disconnectBluetooth.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if (attackRobot.isAttackConnected() && defenceRobot.isDefenceConnected()) {
					attackRobot.disconnect("attack");
					defenceRobot.disconnect("defence");
					System.out.println("Both Robots Disconnected!!");
				} else if (flag == 1) {
					attackRobot.disconnect("attack");
					System.out.println("Attacker Robot Disconnected");
				} else if (flag == 2) {
					defenceRobot.disconnect("defence");
					System.out.println("Defender Robot Disconnected!!");					
				}
			}
		});
		
    }
    
	private void cleanQuit() {
		if (flag == 0) {
			attackRobot.disconnect("attack");
			defenceRobot.disconnect("defence");
			System.out.println("Both Robots Disconnected!!");
		} else if (flag == 1) {
			attackRobot.disconnect("attack");
			System.out.println("Attacker Robot Disconnected");
		} else if (flag == 2) {
			defenceRobot.disconnect("defence");
			System.out.println("Defender Robot Disconnected!!");					
		}
		
		System.exit(0);
	}
    
}
