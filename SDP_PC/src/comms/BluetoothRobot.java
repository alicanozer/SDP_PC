package comms;

import java.io.IOException;

import world.Robot;
import world.RobotType;
import lejos.pc.comm.NXTConnector;

public class BluetoothRobot extends Robot implements RobotController {

	private Bluetooth bluetooth;
	public static final String TEAM_TRINITY = "0016530970C6";
	public static final String HERCULES = "0016530D4ED8";
	NXTConnector connector = new NXTConnector();
	
	public BluetoothRobot(RobotType type, Bluetooth bluetooth) {
		super(type);
		this.bluetooth = bluetooth;
	}
	
	public void connect() {
		try {
			bluetooth = new Bluetooth(HERCULES);
		}catch (Exception e) {
			System.err.println();
		}
	}
	
	@Override
	public boolean isAttackConnected() {
		return bluetooth.isAttackConnected();
	}
	
	@Override
	public boolean isDefenceConnected() {
		return bluetooth.isDefenceConnected();
	}
	
	@Override
	public void disconnect(String robotType) {
		int[] commands = { bluetooth.QUIT, 0, 0, 0 };
		try {
			bluetooth.sendCommand(commands, robotType);
			Thread.sleep(100);
		}catch (IOException e){
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}catch (InterruptedException e1) {
			System.out.println("Thread Interrupted");
			e1.printStackTrace();
		}
		
		//bluetooth.closeBluetoothConnection(connector);
		System.out.println("Connection Disconnected");
	}
	
	@Override
	public void stop(String robotType) {
		int[] commands = { bluetooth.STOP, 0, 0, 0 };
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}
	
	@Override
	public void grab(String robotType) {
		int[] commands = { bluetooth.GRAB, 0, 0, 0 };
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void kick(String robotType) {
		int[] commands = { bluetooth.KICK, 0, 0, 0 };
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void rotateLEFT(String robotType, int turn) {

		int angleMax;
		int angle;
		
		if (turn >= 127) {
			angleMax = 127;
			angle = turn - 127;
		} else {
			angleMax = turn;
			angle = 0;
		}
		
		int[] commands = { bluetooth.ROTATELEFT, angleMax, angle, 0}; 
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void rotateRIGHT(String robotType, int angle) {
		
		int[] commands = { bluetooth.ROTATERIGHT, 0,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void forward(String robotType, double distance) {
	int half = (int) (distance/2);	
	int[] commands = { bluetooth.FORWARDS, half, half,0 }; 
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}	
		
	}

	@Override
	public void backwardsC(String robotType) {
		
	int[] commands = { bluetooth.BACKWARDSC, 0,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}		
	}
	
	@Override
	public void setSpeed(String robotType, int speed) {
		int[] commands = { bluetooth.SPEED, speed,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}
	
	@Override
	public void isMoving(String robotType) {
		int[] commands = { bluetooth.MOVING, 0,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands, robotType);
			
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}
	}

	@Override
	public void forwardsC(String robotType) {
		// TODO Auto-generated method stub
	int[] commands = { bluetooth.FORWARDSC, 0,0,0 }; 
		
		try {
			bluetooth.sendCommand(commands, robotType);
			System.out.println("Robot Stopped");
		}catch (IOException e) {
			System.out.println("Command could not be sent");
			e.printStackTrace();
		}	

	}
	
	@Override
	public void waitForRobotReady(String robotType) {
		try {
			System.out.println("Waiting for command completion");
			bluetooth.waitForReadyCommand(robotType);
		} catch (IOException e) {
			System.out.println("Failed while waiting for ready state");
			e.printStackTrace();
		}
	}

}