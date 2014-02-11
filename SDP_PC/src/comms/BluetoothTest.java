package comms;

import java.io.IOException;

public class BluetoothTest {
	
	public static final String TEAM_TRINITY = "0016530970C6";
	public static final String HERCULES = "0016530D4ED8";
	private static Bluetooth connection;

	public static void main(String[] args) throws IOException {
		
		//Create and open new Bluetooth connection to robot of choice
		connection = new Bluetooth(HERCULES);
		
		//If connected send some test commands
		if(connection.isRobotReady()) {
			/**
			 * A list of available commands are available in the Bluetooth class.
			 * Send commands to robot by first defining a test command:
			 * 
			 * int[] testCommand = new int[] {Bluetooth.COMMAND,option1,option2,option3};
			 * 
			 * then send the test command using:
			 * 
			 * connection.sendCommand(testCommand);
			 * 
			 * If you want to leave a delay between commands then include Thread.sleep(int)
			 */
			
			int [] testFORWARDS = new int [] {Bluetooth.FORWARDS,0,0,0};
			int [] testBACKWARDS = new int [] {Bluetooth.BACKWARDS,0,0,0};
			int [] testKICK = new int [] {Bluetooth.KICK,0,0,0};
			int [] testSPEED = new int [] {Bluetooth.SPEED,10,0,0};
			int [] testSPEED2 = new int [] {Bluetooth.SPEED,5,0,0};
			int [] testROTATELEFT = new int [] {Bluetooth.ROTATELEFT,10,0,0};
			int [] testROTATERIGHT = new int [] {Bluetooth.ROTATERIGHT,10,0,0};
			int [] testSTOP = new int [] {Bluetooth.STOP,10,0,0};
			int [] testQUIT = new int [] {Bluetooth.QUIT,10,0,0};
			
			try {
				connection.sendCommand(testSPEED);
				connection.sendCommand(testFORWARDS);
				Thread.sleep(3000);
				connection.sendCommand(testSPEED2);
				connection.sendCommand(testBACKWARDS);
				Thread.sleep(3000);
				connection.sendCommand(testROTATELEFT);
				Thread.sleep(3000);
				connection.sendCommand(testROTATERIGHT);
				Thread.sleep(3000);
				connection.sendCommand(testSTOP);
				Thread.sleep(3000);
				connection.sendCommand(testQUIT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Close connection
		//connection.closeBluetoothConnection(conn1);
		
	}
	
}