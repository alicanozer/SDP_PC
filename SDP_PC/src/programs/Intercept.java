package programs;

import java.io.IOException;

import javax.swing.SwingUtilities;

import comms.Bluetooth;
import comms.BluetoothRobot;
import strategy.program.GoalieIntercept;
import vision.SimpleViewer2;
import world.World;

public class Intercept {
	public static void main(String[] args) {
		//Set up the world model
		World world = new World(World.YELLOW, World.LEFT);
		
		//Connect to the bluetooth device
		Bluetooth bluetooth;
		try {
			bluetooth = new Bluetooth(BluetoothRobot.HERCULES);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		BluetoothRobot bluetoothRobot = new BluetoothRobot(bluetooth);
		
		//Set up the vision
		Thread visionThread = new Thread(new SimpleViewer2(world));
		visionThread.start();
		
		Thread strategy = new Thread(new GoalieIntercept(world));
		strategy.start();
	}
}
