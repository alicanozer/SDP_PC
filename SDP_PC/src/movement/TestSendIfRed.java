// package org.lejos.pcsample.btsend;

package movement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.SwingUtilities;

import vision.*;

import lejos.pc.comm.NXTConnector;

public class TestSendIfRed {	
	
	public static void main(String[] args) {
		
//		ObjectLocations.setYellowDefendingLeft(true);
//		ObjectLocations.setYellowUs(true);
//		try {
//			SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					new SimpleViewer();
//				}
//			});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("i failed miserably and now I must die to repent for my sins... ");
//		}
		
		System.out.println("do we get to here?");
		NXTConnector conn = new NXTConnector();

		// Connect to any NXT over Bluetooth
		boolean connected = conn.connectTo("btspp://0016530970C6"); // The address of the robot
	
		
		if (!connected) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
		} else {
			System.out.println(connected);
		}
		
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		DataInputStream dis = new DataInputStream(conn.getInputStream());
		while (true) {
			ObjectLocations world = new ObjectLocations();
									
			try {
				if (3 > 0) { //If the camera finds the ball 
					System.out.println("Found red ball");
					dos.writeInt(1); // Then add to the stream the integer 1
					dos.flush(); // Then send it to the robot
				} else {
					dos.writeInt(0); // If no ball then add to the stream the integer 0
					dos.flush(); // And send it to the robot
				}
			} catch (IOException ioe) {
				System.out.println("IO Exception writing bytes:");
				System.out.println(ioe.getMessage());
				break;
			}
		}

		/*try {
			dis.close();
			dos.close();
			conn.close();
		} catch (IOException ioe) {
			System.out.println("IOException closing connection:");
			System.out.println(ioe.getMessage());
		}*/
	
	}
}
