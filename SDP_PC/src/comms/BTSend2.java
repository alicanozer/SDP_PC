package comms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
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
		NXTComm link1 = null;
		try {
			link1 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {
			System.out.println("Error loading bluetooth driver:");
			System.out.println(e.getMessage());
		}
		//NXTComm link2 = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		
		//Create connections to each robot
		NXTInfo robot1 = new NXTInfo(NXTCommFactory.BLUETOOTH, "team trinity", "00:16:53:09:70:C6");
		//NXTInfo robot2 = new NXTInfo(NXTCommFactory.BLUETOOTH, "hercules", "00:16:53:0D:4E:D8");
		
		//Open connection to each robot
		boolean connected1 = false;
		try {
			connected1 = link1.open(robot1);
		} catch (NXTCommException e) {
			System.out.println("Error opening connection:");
			System.out.println(e.getMessage());
		}
		//boolean connected2 = link2.open(robot2);
		
		if (!connected1) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
		}
		
		//Open input and output streams to each robot
		DataOutputStream dos1 = new DataOutputStream(link1.getOutputStream());
		DataInputStream dis1 = new DataInputStream(link1.getInputStream());
		//DataOutputStream dos2 = new DataOutputStream(link2.getOutputStream());
		//DataInputStream dis2 = new DataInputStream(link2.getInputStream());
		
		//Try sending integer to robot 1
		try {
			System.out.println("Sending test message");
			dos1.writeInt((12345));
			dos1.flush();
		} catch (IOException e) {
			System.out.println("IO Exception writing bytes:");
			System.out.println(e.getMessage());
		}
		
		//Try receiving integer from robot 1
		
		//Close open connections and streams
		try {
			dis1.close();
			dos1.close();
			link1.close();
		} catch (IOException e) {
			System.out.println("IOException closing connection:");
			System.out.println(e.getMessage());
		}
	}
	
}
