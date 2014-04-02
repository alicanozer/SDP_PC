package strategy.planning;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import vision.PitchConstants;
import vision.VisionRunner;
import world.RobotType;
import lejos.pc.comm.NXTCommException;
import movement.RobotMover;
import comms.Bluetooth;
import comms.BluetoothRobot;

public class StartStrategy extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private static BluetoothRobot attackRobot;
	private static BluetoothRobot defenceRobot;
	private RobotMover attackMover;
	private RobotMover defenceMover;
	private static Thread dthread = null;
	private static Thread athread = null;
	
	public static void main(String[] args) {

		//Start vision
		VisionRunner.startDebugVision(PitchConstants.newPitch, 10, true);

		//Sets up the GUI
		StartStrategy guiStrat = new StartStrategy();
		System.out.println("GOT");
		guiStrat.pack();
		System.out.println("GOT");
		guiStrat.setVisible(true);
		guiStrat.setMinimumSize(guiStrat.getSize());
		
	}

	public void runStrategy() {
		
		System.out.println("Starting Strategy...");
		
		//AttackThread arun = new AttackThread("attack",attackMover);
		DefenceThread drun = new DefenceThread("defence",defenceMover);
		dthread = new Thread(drun);
		dthread.start();
		//athread = new Thread(arun);
		//athread.start();
		System.out.println("Started Threads");

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
				try {
					runStrategy();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flag == 0) {
					attackMover.stopRobot("attack");
					defenceMover.stopRobot("defence");
					AttackThread.kill();
					DefenceThread.kill();						
				} else if (flag == 1) {
					attackMover.stopRobot("attack");
					AttackThread.kill();
				} else if (flag == 2) {
					defenceMover.stopRobot("defence");
					DefenceThread.kill();
				}

				System.out.println("Strategy Stopped");
				
			}
		});

		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Kill the mover and wait for it to stop completely
				try {
					if (flag == 0) {
						attackMover.kill();
						defenceMover.kill();
						//Attacker Thread kill
						AttackThread.kill();
						DefenceThread.kill();						
					} else if (flag == 1) {
						attackMover.kill();
						//Attacker Thread kill
						AttackThread.kill();
					} else if (flag == 2) {
						defenceMover.kill();
						DefenceThread.kill();
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

		connectOptions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Do nothing
			}
		});

		connectBluetooth.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Create Bluetooth connections
				Bluetooth myConnection = null;

				if (connectOptions.getSelectedItem().equals("Both")) {
					try {
						myConnection = new Bluetooth("both");
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (NXTCommException e1) {
						e1.printStackTrace();
					}
					defenceRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
					attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);
					attackMover = new RobotMover(attackRobot);
					defenceMover = new RobotMover(defenceRobot);
					attackMover.start();
					defenceMover.start();
					System.out.println("Both Robots Connected!!");
					flag = 0;
				} else if (connectOptions.getSelectedItem().equals("Attacker")) {
					try {
						myConnection = new Bluetooth("attack");
					} catch (IOException | NXTCommException e1) {
						e1.printStackTrace();
					}
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
