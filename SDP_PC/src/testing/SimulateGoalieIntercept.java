package testing;

import strategy.program.GoalieIntercept;
import geometry.Vector;
import world.MobilePixelObject;
import world.PixelWorld;
import world.RealWorld;
import world.World;
import comms.RobotController;

public class SimulateGoalieIntercept {
	/*
	 * Run the GoalieIntercept strategy in the absence of video feed and robots.
	 * This simulation is very crude but it can help spot some silly mistakes.
	 */

	public static void main(String[] args) {
		try {
			//Create a new world
			World world = new World(World.YELLOW, World.LEFT);
			
			//Create a virtual robot
			RobotController btRobot = new BluetoothSimulator(world, World.YELLOW_DEFENDER);
			
			//Create shortcuts to access important objects in the world.
			MobilePixelObject robot = world.getMobileObject(PixelWorld.YELLOW_DEFENDER);
			MobilePixelObject ball = world.getMobileObject(PixelWorld.BALL);
			
			//Place the ball and ensure it has 0 velocity
			ball.setPixelPosition(new Vector(400,0));
			Thread.sleep(100);
			ball.setPixelPosition(new Vector(400,0));
			
			//Make the robot face the right way.
			robot.setPixelOrientation(new Vector(0, 1));
			//And give it a position mostly for fun.
			robot.setPixelPosition(new Vector(10, 10));
			
			//Now create the strategy and start it.
			Thread strategy = new Thread(new GoalieIntercept(world, btRobot));
			strategy.start();
			
			//At the same time as the strategy is running we will take us the freedom of moving the ball around a bit.
			//We stop modifying the world when the ball has left the field or when the time runs up
			for (int i = 0; i < 1000 && ball.getPixelPosition().getX() < 0; i++) {
				//For the first 25 frames we don't move the ball
				if (i > 25) {
					if (i < 50) {
//							Let the ball move in this direction for a while
						ball.setPixelPosition(ball.getPixelPosition().add(new Vector(-10, 5)));
					}else {
//							Then change the direction (and speed - it's a strange ball).
						ball.setPixelPosition(ball.getPixelPosition().add(new Vector(-20, 0)));
					}
				}
				//Now sleep to simulate waiting for the next frame;
				Thread.sleep(40);
				//Lastly let us know what the world looks like
				System.out.println();
				System.out.println("robot:\n" + robot);
				System.out.println("ball:\n" + ball);
			}
		} catch (InterruptedException e) {
		}
	}
}
