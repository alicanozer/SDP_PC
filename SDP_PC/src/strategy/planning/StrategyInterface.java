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

	BluetoothRobot attackRobot;
	BluetoothRobot defenceRobot;

	public StrategyInterface(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		this.shouldidie = false;
		this.attackRobot = attackRobot;
		this.defenceRobot = defenceRobot;
	}
	
	//TO DO: kill() doesn't work. Have no way of currently breaking loops in mover.
	public void kill() {
		shouldidie = true;
		
	}
}


