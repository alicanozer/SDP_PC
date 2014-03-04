package strategy.planning;

import comms.BluetoothRobot;
import comms.BluetoothRobotOld;
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

	RobotMover attackMover;
	RobotMover defenceMover;

	public StrategyInterface(RobotMover attackMover, RobotMover defenceMover) {
		this.shouldidie = false;
		this.attackMover = attackMover;
		this.defenceMover = defenceMover;
	}

	
	public void kill() {
		shouldidie = true;
		// Terminate any active movements
		// NOTE: does NOT tell the robot to stop, it only breaks any loops in
		// the mover
		try {
			attackMover.resetQueue();
			defenceMover.resetQueue();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		attackMover.interruptMove();
		defenceMover.interruptMove();
		try { // Sleep for a bit, because we want movement to die.
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
}



