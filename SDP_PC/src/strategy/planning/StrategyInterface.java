package strategy.planning;

import comms.BluetoothRobot;
import movement.RobotMover;
import vision.ObjectLocations;
/**
 * Interface for Strategy. Extend for your own strategy. See README for details of how to.
 * 
 * @author s0925284
 *
 */

public abstract class StrategyInterface implements Runnable {
	/**
	 * To specify if we want the thread to die. The shouldidie is modified when the kill() method is run.
	 */
	protected boolean shouldidie;

	ObjectLocations obj;
	BluetoothRobot bRobot;

	public StrategyInterface(BluetoothRobot bRobot) {
		this.shouldidie = false;
		this.bRobot = bRobot;
	}
	
	//TO DO: kill() doesn't work. Have no way of currently breaking loops in mover.
	public void kill() {
		shouldidie = true;
		// Terminate any active movements
		// NOTE: does NOT tell the robot to stop, it only breaks any loops in
		// the mover
		try {
			bRobot.wait(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try { // Sleep for a bit, because we want movement to die.
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


