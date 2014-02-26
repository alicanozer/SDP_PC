package comms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Bluetooth {
	
	/**
	 * This class provides communication between PC and multiple robots
	 * 
	 * @autor Mark Johnston
	 */

	private NXTComm nxtComm1;
	private NXTComm nxtComm2;
	private NXTInfo nxtInfo1;
	private NXTInfo nxtInfo2;
	private boolean attackReady = false;
	private boolean defenceReady = false;
	private InputStream dis1;
	private InputStream dis2;
	private OutputStream dos1;
	private OutputStream dos2;
	private int buffer = 0;
	public final static String ATTACK = "0016530D4ED8";  //Hercules
	public final static String DEFENCE = "0016530970C6"; //team trinity
	
	// Commands
	public final static int NOTHING = 0;
	public final static int FORWARDS = 1;
	public final static int BACKWARDS = 2;
	public final static int STOP = 3;
	public final static int GRAB = 4;
	public final static int KICK = 5;
	public final static int SPEED = 6;
	//public final static int ROTATESPEED = 7;
	public final static int ROTATELEFT = 7;
	public final static int ROTATERIGHT = 8;
	public final static int MOVING = 9;
	public final static int QUIT = 10;
	
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
	public Bluetooth(String robotType) throws IOException, NXTCommException {
		String theType = robotType.toLowerCase();
		if (theType.equals("attack")) {
			if (isAttackConnected()) {
				throw new IOException("A connection to the attack robot has already been opened.");
			} else {
				openBluetoothConnection("attack");
			}
		} else if (theType.equals("defence")) {
			if (isAttackConnected()) {
				throw new IOException("A connection to the defence robot has already been opened.");
			} else {
				openBluetoothConnection("defence");
			}
		} else if (theType.equals("both")){
			if (!isAttackConnected() && !isDefenceConnected()) {
				openBluetoothConnection("attack");
				openBluetoothConnection("defence");
			} else {
				throw new IOException("A connection to one of the robots has already been opened.");
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
	 * @throws NXTCommException 
	 */
	public void openBluetoothConnection(String robotType) throws IOException, NXTCommException {
		System.out.println("Trying to connect to robot..");
		if (robotType.equals("attack")) {
			//Open connection
			nxtComm1 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			nxtInfo1 = new NXTInfo(NXTCommFactory.BLUETOOTH,"Hercules",ATTACK);
			nxtComm1.open(nxtInfo1);
			//Initialise input and output streams
			dis1 = nxtComm1.getInputStream();
			dos1 = nxtComm1.getOutputStream();
			checkReady("attack");
			attackReady = true;
			System.out.println("Connected to attack robot");
		} else {
			//Open connection
			nxtComm2 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			nxtInfo2 = new NXTInfo(NXTCommFactory.BLUETOOTH,"team trinity",DEFENCE);
			nxtComm2.open(nxtInfo2);
			//Initialise input and output streams
			dis2 = nxtComm2.getInputStream();
			dos2 = nxtComm2.getOutputStream();
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