package strategy.program;

import comms.BluetoothRobot;

import geometry.Vector;
import world.RealWorld;

public class GoalieIntercept implements Runnable {

	protected RealWorld world;
	protected BluetoothRobot robot;
	
	protected boolean interrupt = false;
	
	public GoalieIntercept(RealWorld world, BluetoothRobot robot) {
		this.world = world;
		this.robot = robot;
	}

	@Override
	public void run() {
		//While the ball is still stationary, do nothing.
		while(world.getMobileRealObject(RealWorld.BALL).getRealVelocity().getMagnitude() < 1 && !interrupt) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				interrupt = true;
				e.printStackTrace();
			}
		}
		//Now that the ball is moving, start the program.
		Vector positionVector;
		Vector directionVector;
		double[] cartesian;
		double y;
		
		while (!interrupt) {
			//Find the y at which the ball is expectod to hit the goal.
			positionVector = world.getMobileRealObject(RealWorld.BALL).getRealPosition();
			directionVector = world.getMobileRealObject(RealWorld.BALL).getRealVelocity();
			cartesian = getCartesian(positionVector, directionVector);
			y = cartesian[1]+cartesian[0]*positionVector.getX();
			
			if (Math.abs(world.getMobileRealObject(RealWorld.HERO_DEFENDER).getRealPosition().getY() - y) < 5) {
				//If the robot is already in the way of the ball, stop.
				robot.stop();
			}else if (world.getMobileRealObject(RealWorld.HERO_DEFENDER).getRealPosition().getY() < y) {
				//If the robot is above the ball, move forward (downwards)
				robot.forward();
			}else if (world.getMobileRealObject(RealWorld.HERO_DEFENDER).getRealPosition().getY() - y > 0) {
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
