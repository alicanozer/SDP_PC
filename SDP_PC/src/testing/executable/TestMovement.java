package testing.executable;

import java.io.IOException;

import world.RobotType;
import lejos.pc.comm.NXTCommException;
import movement.RobotMover;

import comms.Bluetooth;
import comms.BluetoothRobot;

public class TestMovement {

	//Bluetooth stuff

    private static RobotMover attackMover;

    public static void main(String[] args) throws IOException, NXTCommException, InterruptedException {
    	
		//Create Bluetooth connections
		Bluetooth myConnection = new Bluetooth("attack"); //should be "both"
//		BluetoothRobot defenseRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
		BluetoothRobot attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);

		//Check if Bluetooth connection was successful
		while (!attackRobot.isAttackConnected()) { // include && !attackRobot.isAttackConnected() for both
			// Reduce CPU cost
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		System.out.println("Robots ready!");
		
		attackMover = new RobotMover(attackRobot);
//		defenseMover = new RobotMover(defenseRobot);
		attackMover.setSpeedCoef(10);
		attackMover.forward("attack", 30);
		System.out.println("Robots forwards!");
		attackMover.rotate("attack", 90);
		System.out.println("Robots rotate!");
		System.out.println(attackMover.hasQueuedJobs());
		System.out.println(attackMover.numQueuedJobs());
		attackMover.run();
		attackMover.waitForCompletion();
    	
    }
	
}
