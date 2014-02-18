package strategy.planning;

import comms.BluetoothRobot;

import World.WorldState;
import movement.RobotMover;
import vision.ObjectLocations;

public abstract class StrategyInterface implements Runnable {
	protected boolean shouldidie;

	ObjectLocations obj;
	BluetoothRobot bRobot;
	WorldState world;

	public StrategyInterface(WorldState world, BluetoothRobot bRobot) {
		this.world = world;
		this.shouldidie = false;
		this.bRobot = bRobot;
	}
	
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


