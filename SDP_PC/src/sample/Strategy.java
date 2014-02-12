package sample;

import comms.BluetoothRobot;

import world.RealWorld;

public class Strategy implements Runnable{

	public Strategy(RealWorld world, BluetoothRobot defender, BluetoothRobot attacker) {
		
	}

	@Override
	public void run() {
		boolean interupt = false;
		while (!interupt) {
//			Check what the world looks like
//			Decide what to do about it
//			Tell the robots to do it.
//			Maybe sleep a little while waiting for the vision to process the next frame
		}
	}



}
