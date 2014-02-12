package sample;

import comms.BluetoothRobot;

import geometry.Vector;
import world.World;

public class EntryPoint {

	/*
	 * In this example it is assumed that the Vision, the RobotLinks and the Strategy are all descendants of thread. With minor modifications we
	 * can also use the implements runnable approach.
	 * I would suggest this layout as it allows us to easily try strategies and even restart our strategies without restarting vision and bluetooth.
	 */
	public static void main(String[] args) {
		//Creates a new world model where the camera is 200cm above the table and is positioned over pixel 320,240 (the case before the image is cropped)
		MixedWorld world = new MixedWorld(200, new Vector(320, 240), world.YELLOW, world.LEFT);
		
		//Only set height when different from default 18cm...
		world.setMobileRealObject(World.HERO_DEFENDER, new Robot(10)); 
		
		//Create a new vision system and pass it the world to put its output in.
		//Depending on implementation it will have to either be run like the strategy below or the way it currently is in
		//comms.Test2 
		Vision vision = new Vision(world);
		
		//Set up bluetooth connections to the to robots.
		BluetoothRobot attacker = new BluetoothRobot(BluetoothRobot.TRINITY);
		BluetoothRobot defender = new BluetoothRobot(BluetoothRobot.HERCULES);
		
		//Finally, pass the world representation and the two robot connections to some strategy
		Thread strategy = new Thread(new Strategy(world, defender, attacker));
		strategy.start();
	}

}
