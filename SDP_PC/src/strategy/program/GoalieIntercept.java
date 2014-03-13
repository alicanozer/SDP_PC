package strategy.program;

import comms.RobotController;
import geometry.Vector;
import world.RealWorld;

public class GoalieIntercept implements Runnable {

	//Keep instances of the world model and the robot controller
	protected RealWorld world;
	protected RobotController robot;
	
	protected boolean interrupt = false;
	
	/**
	 * Create a new GoalieIntercept strategy. This continuously estimaes where
	 * the ball will cross its path and tries to move the robot there.
	 * 
	 * @param world
	 *            The world model which the strategy will rely on for
	 *            information about positions etc.
	 * 
	 * @param robot
	 *            The robot connection to the Goalie.
	 */
	public GoalieIntercept(RealWorld world, RobotController robot) {
		this.world = world;
		this.robot = robot;
	}

	@Override
	public void run() {
		//Wait for a ball to appear in the world.
		while(world.getMobileObject(RealWorld.BALL) == null) { 
//			System.out.println("No ball");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		
		//Wait for the ball to start moving.
		while(world.getMobileObject(RealWorld.BALL).getRealVelocity().getMagnitude() < 1) {
//			System.out.println("WAIT FOR IT..." + world.getMobileObject(RealWorld.BALL).getRealVelocity().getMagnitude());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//At this point the ball should be moving.
		//Set up variables to handle vector and cartesian equations of lines (the path of the ball)
		Vector positionVector;
		Vector directionVector;
		double[] cartesian;
		double y;
		
		//Run this loop for as long as the strategy is running.
		while (!interrupt) {
			// Find the y at which the ball is expectod to cross the line the
			// robot move on. (By finding the intersection of that line and the
			// predicted path of the ball. Since the robot only moves in y this
			// is the same as solving the cartesian equation of the path of the
			// ball for the x-coordinate of the ball.)  
			positionVector = world.getMobileObject(RealWorld.BALL).getRealPosition();
			directionVector = world.getMobileObject(RealWorld.BALL).getRealVelocity();
			cartesian = getCartesian(positionVector, directionVector);
			y = cartesian[1]+cartesian[0]*world.getMobileObject(RealWorld.HERO_DEFENDER).getRealPosition().getX();
			
			if (Math.abs(world.getMobileObject(RealWorld.HERO_DEFENDER).getRealPosition().getY() - y) < 5) {
				//If the robot is within 5cm of where it is supposed to be, stop it.
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
			
			// Sleep the thread to wait for the world to update (if the world
			// doesn't change we will just do the same thing again). Also check
			// if we are being interupted and need to exit.
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				interrupt = true;
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Calculate the coefficients of the cartesian equation of a line given in
	 * its vector form.
	 * 
	 * @param positionVector
	 *            The position vector of the equation.
	 * @param directionVector
	 *            The direction vector of the equation.
	 * @return
	 */
	private double[] getCartesian(Vector positionVector, Vector directionVector) {
		Vector p = positionVector;
		Vector q = positionVector.add(directionVector);
		double a = (p.getY()-q.getY())/(p.getX()-q.getX());
		double c = p.getY()-a*p.getX();
		double[] result = {a,c};
		return result;
	}

}
