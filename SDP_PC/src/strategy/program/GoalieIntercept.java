package strategy.program;

import comms.BluetoothRobot;
import comms.RobotController;
import geometry.Vector;
import world.RealWorld;

public class GoalieIntercept implements Runnable {

	protected RealWorld world;
	protected RobotController robot;
	
	protected boolean interrupt = false;
	
	public GoalieIntercept(RealWorld world, RobotController robot) {
		this.world = world;
		this.robot = robot;
	}

	@Override
	public void run() {
		//While the ball is still stationary, do nothing.
		while(world.getMobileObject(RealWorld.BALL) == null) {
			System.out.println("No ball");
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(world.getMobileObject(RealWorld.BALL).getRealVelocity().getMagnitude() < 1 && !interrupt) {
			System.out.println("WAIT FOR IT..." + world.getMobileObject(RealWorld.BALL).getRealVelocity().getMagnitude());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				interrupt = true;
				e.printStackTrace();
			}
		}
		System.out.println("BALL IS ON THE MOVE!");
		//Now that the ball is moving, start the program.
		Vector positionVector;
		Vector directionVector;
		double[] cartesian;
		double y;
		
		while (!interrupt) {
			//Find the y at which the ball is expectod to hit the goal.
			positionVector = world.getMobileObject(RealWorld.BALL).getRealPosition();
			directionVector = world.getMobileObject(RealWorld.BALL).getRealVelocity();
			cartesian = getCartesian(positionVector, directionVector);
			y = cartesian[1]+cartesian[0]*world.getMobileObject(RealWorld.HERO_DEFENDER).getRealPosition().getX();
			
			if (Math.abs(world.getMobileObject(RealWorld.HERO_DEFENDER).getRealPosition().getY() - y) < 5) {
				//If the robot is already in the way of the ball, stop.
				if (robot.isMoving()) {
					robot.stop();
				}
			}else if (world.getMobileObject(RealWorld.HERO_DEFENDER).getRealPosition().getY() < y) {
				//If the robot is above the ball, move forward (downwards)
				robot.forward();
			}else if (world.getMobileObject(RealWorld.HERO_DEFENDER).getRealPosition().getY() - y > 0) {
				//If the robot is below the ball, move backwards (upwards)
				robot.backwards();
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				interrupt = true;
				e.printStackTrace();
			}
		}
		
	}
	
	private double[] getCartesian(Vector positionVector, Vector directionVector) {
		Vector p = positionVector;
		Vector q = positionVector.add(directionVector);
		double a = (p.getY()-q.getY())/(p.getX()-q.getX());
		double c = p.getY()-a*p.getX();
		double[] result = {a,c};
		return result;
	}

}
