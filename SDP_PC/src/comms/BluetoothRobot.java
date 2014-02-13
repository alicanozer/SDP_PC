package comms;

import java.io.IOException;

import lejos.pc.comm.NXTConnector;

public class BluetoothRobot implements RobotController {

	private Bluetooth bluetooth;
	public static final String TEAM_TRINITY = "0016530970C6";
	public static final String HERCULES = "0016530D4ED8";
	NXTConnector connector = new NXTConnector();
	
	public BluetoothRobot(Bluetooth bluetooth) {
		this.bluetooth = bluetooth;
	}
	
	@Override
	public void connect() {
		try {
			bluetooth = new Bluetooth(HERCULES);
		}catch (Exception e) {
			System.err.println();
		}
	}
	
	@Override
	public boolean isConnected() {
		return bluetooth.isConnected();
	}
	
	@Override
	public boolean isReady() {
		return bluetooth.isRobotReady();
	}
	
	@Override
	public void disconnect() {
		int[] commands = { bluetooth.QUIT, 0, 0, 0 };
		
		try {
			bluetooth.sendCommand(commands);
			Thread.sleep(100);
		}catch (IOException e){
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}catch (InterruptedException e1) {
			System.out.println("Thread Interrupted");
			e1.printStackTrace();
		}
		
		bluetooth.closeBluetoothConnection(connector);
		System.out.println("Connection Disconnected");
	}
	
	@Override
	public void stop() {
		int[] commands = { bluetooth.STOP, 0, 0, 0 };
		
		try {
			bluetooth.sendCommand(commands);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void kick() {
		int[] commands = { bluetooth.KICK, 0, 0, 0 };
		
		try {
			bluetooth.sendCommand(commands);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void move(int speedX, int speedY) {
		
	}

	@Override
	public void rotateLEFT(int angle) {
		int[] commands = { bluetooth.ROTATELEFT, angle,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void rotateRIGHT(int angle) {
		
		int[] commands = { bluetooth.ROTATERIGHT, 0,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void forward() {
	int[] commands = { bluetooth.FORWARDS, 0,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}	
		
	}

	@Override
	public void backwards() {
		
	int[] commands = { bluetooth.BACKWARDS, 0,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}		
	}
	
	@Override
	public void setSpeed(int speed) {
		int[] commands = { bluetooth.SPEED, speed,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}
	
	
}
