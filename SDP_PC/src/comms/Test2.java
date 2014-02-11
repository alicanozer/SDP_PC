package comms;

import java.io.IOException;

import World.RobotType;

public class Test2 {

	static BluetoothRobot bRobot;
	static Bluetooth bluetooth;
	
	public static void main(String[] args) throws IOException {
		bRobot = new BluetoothRobot(RobotType.AttackUs, bluetooth);
		bRobot.connect();
		
		bRobot.kick();
	}
	
}
