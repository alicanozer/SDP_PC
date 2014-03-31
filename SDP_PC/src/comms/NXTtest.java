package comms;

import lejos.pc.comm.NXTCommException;

import java.io.IOException;

import world.RobotType;

public class NXTtest {
	
	static BluetoothRobot attackRobot;
	static BluetoothRobot defenceRobot;
	
	public static void main(String[] args) throws IOException, NXTCommException, InterruptedException {
		
		Bluetooth myConnection = new Bluetooth("attack");
		attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);
		defenceRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
		
		attackRobot.stop("attack");
		//defenceRobot.stop("defence");
		Thread.sleep(3000);
		attackRobot.kick("attack");
		//defenceRobot.kick("defence");
		Thread.sleep(3000);
		attackRobot.disconnect("attack");
		//defenceRobot.disconnect("defence");
		//Give program some time to allow the robots to disconnect from their end
		Thread.sleep(100);
	
	}
	
}