package comms;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

public class BluetoothOld {
	/**
	 * These variables will need to change to include ready states for both robots
	 * 
	 * e.g. attackReady, defenceReady, attackConnected and defenceConnceted
	 * 
	 * There will also need to data streams for both robots
	 * 
	 * @author Mark Johnston	
	*/
	private boolean robotReady = false;
	private boolean connected = false;
	private InputStream dis1;
	private OutputStream dos1;
	private int buffer = 0;
	

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
	

	public BluetoothOld(String nxtAddress) throws IOException {
		NXTConnector conn1 = new NXTConnector();
		openBluetoothConnection(conn1, nxtAddress);
	}
	
	public void openBluetoothConnection(NXTConnector connector, String nxtAddress) throws IOException {
		final int[] READY_STATE = { 0, 0, 0, 0 };
		System.out.println("Trying to connect to robot..");
		
		boolean connection = connector.connectTo("btspp://"+nxtAddress);
		
		//Initialise input and output streams
		dis1 = connector.getInputStream();
		dos1 = connector.getOutputStream();
		
		//Check that robot is ready
		while (true) {
			int[] received = receiveData();
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
		
		//These will need changed when using multiple robots
		connected = true;
		robotReady = true;
		System.out.println("Connected to robot");
	}
	
	//Receive byte data from robot and convert to integer array
	public int[] receiveData() throws IOException {
		byte[] res = new byte[4];
		dis1.read(res);
		int[] ret = { (int) (res[0]), (int) (res[1]), (int) (res[2]), (int) (res[3]) };
		return ret;
	}
	
	//Send byte data to robot
	public void sendCommand(int[] comm) throws IOException {

		if (!connected)
			return;
		byte[] command = { (byte) comm[0], (byte) comm[1], (byte) comm[2], (byte) comm[3] };
		dos1.write(command);
		dos1.flush();	
		//Need to add buffer to keep track of data sent to robot
	}
	
	public void closeBluetoothConnection(NXTConnector connector) {
		try {
			connected = false;
			dis1.close();
			dos1.close();
			connector.close();
		} catch (IOException e) {
			System.err.println("Couldn't close Bluetooth connection: " + e.toString());
		}
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public boolean isRobotReady() {
		return robotReady;
	}


	
}
