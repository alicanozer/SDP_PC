package strategy.planning;

import comms.RobotController;

import vision.ObjectLocations;
import World.RealWorld;

public abstract class StrategyInterface implements Runnable {
	protected boolean shouldidie;

	RealWorld world;
	RobotController robot;

	public StrategyInterface(RealWorld world, RobotController robot) {
		this.shouldidie = false;
		this.world = world;
		this.robot = robot;
	}

	public void kill() {
		shouldidie = true;
		// Terminate any active movements
		// NOTE: does NOT tell the robot to stop, it only breaks any loops in
		// the mover
		try {
			robot.resetQueue();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		robot.interruptMove();
		try { // Sleep for a bit, because we want movement to die.
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
