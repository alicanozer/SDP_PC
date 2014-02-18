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
}


