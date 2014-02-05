package comms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import lejos.pc.comm.NXTCommFactory;

/**
 * This PC class attempts to create a Bluetooth connection
 * to both our robots.
 * 
 * Once a connection is created it will send an integer to each
 * robot which will then be printed on the robots LCD.
 * 
 * The robot must already be running a program to complete the
 * connection and receive the integer. Such as BTReceive.java 
 * 
 * @author Mark Johnston
 */

public class BTSend2 {

	public static void main(String[] args) {
		
		//Create link objects
		/*NXTComm link1 = null;
		try {
			link1 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {
			System.out.println("Error loading bluetooth driver:");
			System.out.println(e.getMessage());
		}*/
		//NXTComm link2 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		
		//Create connections to each robot
		//NXTInfo robot1 = new NXTInfo(NXTCommFactory.BLUETOOTH, "team trinity", "00:16:53:09:70:C6");
		//NXTInfo robot2 = new NXTInfo(NXTCommFactory.BLUETOOTH, "hercules", "00:16:53:0D:4E:D8");
		
		//Open connection to each robot
		/*boolean connected1 = false;
		try {
			connected1 = link1.open(robot1);
		} catch (NXTCommException e) {
			System.out.println("Error opening connection:");
			System.out.println(e.getMessage());
		}*/
		//boolean connected2 = link2.open(robot2);
		
		NXTConnector conn1 = new NXTConnector();
		boolean connected1 = conn1.connectTo("btspp://0016530970C6");
		NXTConnector conn2 = new NXTConnector();
		boolean connected2 = conn2.connectTo("btspp://0016530D4ED8");
		
		if (!connected1 || !connected2) {
			System.err.println("Failed to connect to a NXT");
			System.exit(1);
		}
		
		DataOutputStream dos1 = new DataOutputStream(conn1.getOutputStream());
		DataInputStream dis1 = new DataInputStream(conn1.getInputStream());
		DataOutputStream dos2 = new DataOutputStream(conn2.getOutputStream());
		DataInputStream dis2 = new DataInputStream(conn2.getInputStream());
		
		//If modulus of i is 0 then send integer to brock 1 else brick 2
		for(int i=0;i<100;i++) {
			try {
				int j = i%2;
				if(j==0) {
					dos1.writeInt((i*30000));
					dos1.flush();
				} else {
					dos2.writeInt((i*30000));
					dos2.flush();
				}				
			} catch (IOException ioe) {
				System.out.println("IO Exception writing bytes:");
				System.out.println(ioe.getMessage());
				break;
			}
		}
		
		//Close open connections and streams
		try {
			dis1.close();
			dos1.close();
			dis2.close();
			dos2.close();
			conn1.close();
			conn2.close();
		} catch (IOException e) {
			System.out.println("IOException closing connection:");
			System.out.println(e.getMessage());
		}
	}
	
}
