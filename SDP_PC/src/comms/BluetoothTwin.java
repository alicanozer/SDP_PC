package comms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTConnector;

public class BluetoothTwin {
	
	/**
	 * This class provides communication between PC and multiple robots
	 * 
	 * @autor Mark Johnston
	 */

	private NXTConnector connector1;
	private NXTConnector connector2;
	private boolean attackReady = false;
	private boolean defenceReady = false;
	private boolean connection1 = false;
	private boolean connection2 = false;
	private InputStream dis1;
	private InputStream dis2;
	private OutputStream dos1;
	private OutputStream dos2;
	private int buffer = 0;
	public final static String ATTACK = "0016530D4ED8";
	public final static String DEFENCE = "0016530970C6";
	
	// Commands
	public final static int NOTHING = 0;
	public final static int FORWARDS = 1;
	public final static int BACKWARDS = 2;
	public final static int STOP = 3;
	public final static int KICK = 4;
	public final static int SPEED = 5;
	public final static int ROTATELEFT = 6;
	public final static int ROTATERIGHT = 7;
	public final static int MOVING = 8;
	public final static int QUIT = 9;
	
	/**
	 * Creates a new bluetooth object and opens the relevant bluetooth connection.
	 * It needs to be passed the robot type: attack or defence. This method will check
	 * that a valid robot type has been given, and if a connection has already been opened 
	 * to that robot, before calling the openBluetoothConnection() method.
	 * 
	 * @param robotType - the type of robot to connect to: attack or defence
	 * @throws IOException - when trying to connect to a robot that already has an open
	 *                       connection or when this method is called using an invalid
	 *                       robot type.
	 */
	public BluetoothTwin(String robotType) throws IOException {
		String theType = robotType.toLowerCase();
		if (theType.equals("attack")) {
			if (isAttackConnected()) {
				throw new IOException("A connection to the attack robot has already been opened.");
			} else {
				openBluetoothConnection(connector1, "attack");
			}
		} else if (theType.equals("defence")) {
			if (isAttackConnected()) {
				throw new IOException("A connection to the defence robot has already been opened.");
			} else {
				openBluetoothConnection(connector2, "defence");
			}
		} else {
			throw new IOException("Invalid robot type. You must choose attack or defence");
		}
	}

	/**
	 * Opens the bluetooth connection to either the attack or defence robot.
	 * Once connected it will open data input and output streams to that robot.
	 * Finally it will check that the robot is ready by waiting for a message from the robot
	 * signalling it is ready.
	 * 
	 * @param connector - the NXT connection to use.
	 * @param robotType - the type of robot to connect to: attack or defence.
	 * @throws IOException - when rippled from checkReady()
	 */
	private void openBluetoothConnection(NXTConnector connector, String robotType) throws IOException {
		System.out.println("Trying to connect to robot..");
		if (robotType.equals("attack")) {
			//Open connection
			connection1 = connector.connectTo("btspp://"+ATTACK);
			//Initialise input and output streams
			dis1 = connector.getInputStream();
			dos1 = connector.getOutputStream();
			checkReady("attack");
			attackReady = true;
			System.out.println("Connected to attack robot");
		} else {
			//Open connection
			connection2 = connector.connectTo("btspp://"+DEFENCE);
			//Initialise input and output streams
			dis2 = connector.getInputStream();
			dos2 = connector.getOutputStream();
			checkReady("defence");
			defenceReady = true;
			System.out.println("Connected to defence robot");
		}
	}
	
	/**
	 * Waits for a message from the robot that signals it is in a ready state.
	 * 
	 * @param robotType - the type of robot to connect to: attack or defence.
	 * @throws IOException -  when connection is lost to the robot
	 */
	private void checkReady(String robotType) throws IOException {
		final int[] READY_STATE = { 0, 0, 0, 0 };
		//Check that robot is ready
		while (true) {
			int[] received = receiveData(robotType);
			boolean equals = true;
			//Check that each integer sent from robot equals expected value
			for (int i = 0; i < 4; i++) {
				if (received[i] != READY_STATE[i]) {
					equals = false;
					break;
				}
			}
			//If not ready try again in 10 ms
			if (equals) {
				break;
			} else {
				try {
					Thread.sleep(10);  // Prevent 100% cpu usage
				} catch (InterruptedException e) {
					throw new IOException("Failed to connect: "+ e.toString());
				}
			}
		}
	}

	/**
	 * Receive a byte message from the robot.
	 * 
	 * @param robotType - the type of robot to connect to: attack or defence.
	 * @return - an integer array containing individual bits of robot message byte. 
	 * @throws IOException - when failing to read from data input stream.
	 */
	private int[] receiveData(String robotType) throws IOException {
		byte[] res = new byte[4];
		if(robotType.equals("attack")) {
			dis1.read(res);
		} else {
			dis2.read(res);
		}
		int[] ret = { (int) (res[0]), (int) (res[1]), (int) (res[2]), (int) (res[3]) };
		return ret;
	}
	
	/**
	 * Send a byte message to the required robot
	 * 
	 * TODO - add buffer to keep track of commands not executed
	 * 
	 * @param comm - integer array that will make up command.
	 * @param robotType - the type of robot to connect to: attack or defence.
	 * @throws IOException - if sending of message cannot be completed.
	 */
	public void sendCommand(int[] comm, String robotType) throws IOException {
		String theType = robotType.toLowerCase();
		byte[] command = { (byte) comm[0], (byte) comm[1], (byte) comm[2], (byte) comm[3] };
		if (theType.equals("attack")) {
			if (isAttackConnected()) {
				dos1.write(command);
				dos1.flush();
			} else {
				throw new IOException("Cannot send command to attack robot as there is no open connection.");
			}
		} else if (theType.equals("defence")) {
			if (isDefenceConnected()) {
				dos2.write(command);
				dos2.flush();
			} else {
				throw new IOException("Cannot send command to defence robot as there is no open connection.");
			}
		} else {
			throw new IOException("Invalid robot type. Message cannot be sent.");
		}
	}

	/**
	 * @return - true if PC has established a connection to attack robot
	 */
	public boolean isAttackConnected() {
		return attackReady;
	}
	
	/**
	 * @return - true if PC has established a connection to defence robot
	 */
	public boolean isDefenceConnected() {
		return defenceReady;
	}
}
