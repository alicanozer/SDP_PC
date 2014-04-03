package movement;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import utility.SafeSleep;

//import world.Robot;
//import world.PixelWorld;

import comms.BluetoothRobot;
import comms.RobotController;

public class RobotMover extends Thread{
	//private world.PixelWorld worldState;
	private RobotController robot;
	//private Robot type;
	private BluetoothRobot bRobot;

	/** A flag to permit grab*/
	private boolean grabFlag = true;
	/** A flag to permit busy-waiting */
	private boolean running = false;
	/** A flag to interrupt any active movements */
	private boolean interruptMove = false;
	/** A flag to tell the RobotMover thread to die */
	private boolean die = false;

	/** A thread-safe queue for movement commands */
	private ConcurrentLinkedQueue<MoverConfig> moveQueue = new ConcurrentLinkedQueue<MoverConfig>();
	/** A reentrant mutex lock for the movement queue */
	private ReentrantLock queueLock = new ReentrantLock(true);
	/** A semaphore used to signal the RobotMover has jobs queued */
	private Semaphore jobSem = new Semaphore(0, true);
	/** A semaphore used to signal the RobotMover has completed its job queue */
	private Semaphore waitSem = new Semaphore(0, true);

	private ReentrantLock coefLock = new ReentrantLock(true);

	private double speedCoef = 1.0;

	public RobotMover(BluetoothRobot bRobot) {
		super("RobotMover");
		this.bRobot = bRobot;
	}

	/** Settings info class to permit queueing of movements */
	private class MoverConfig {
		public double distance = 0;
		public String type = null;
		public int angle = 0;
		public int speed = 0;
		public boolean avoidBall = false;
		public boolean avoidEnemy = false;
		public long milliseconds = 0;
		private int dribblemode = 0;
		//private MovingPoint movPoint = null;
		public Mode mode;
		public  String toString(){
			return this.mode.toString();
		}

	}

	public void setSpeedCoef(double coef) {
		coefLock.lock();
		speedCoef = coef;
		coefLock.unlock();
	}

	private enum Mode {
		FORWARD, BACKWARDSC, STOP, GRAB, KICK, DELAY, ROTATE, SET_SPEED, FORWARDSC, MOVING, SET_ROTATESPEED
	};

	/**
	 * Repeatedly tries to push the movement onto the move queue, giving up
	 * after 10 attempts
	 * 
	 * @param movement
	 *            The movement to push onto the queue
	 * @return true if the movement was successfully pushed, false otherwise
	 */
	private boolean pushMovement(MoverConfig movement) {
		int pushAttempts = 0;
		try {
			queueLock.lockInterruptibly();
		} catch (InterruptedException e) {
			return false;
		}
		// Try to push the movement 10 times before giving up
		while (!moveQueue.offer(movement) && pushAttempts < 10)
			++pushAttempts;
		queueLock.unlock();
		// If we gave up, return false to indicate it
		if (pushAttempts >= 10){
			return false;
		}
		return true;
	}

	/**
	 * Wakes up any threads waiting on a movement queue to complete
	 */
	private void wakeUpWaitingThreads() {
		waitSem.release();
		running = false;
	}

	/**
	 * Processes a single movement
	 * 
	 * @param movement
	 *            The movement to process
	 * @throws Exception
	 *             If an error occurred
	 */
	private void processMovement(MoverConfig movement) throws Exception {
		switch (movement.mode) {
		case FORWARD:
			//System.out.println(movement.type + " - Forward " + movement.distance);
			bRobot.forward(movement.type, movement.distance);
			bRobot.waitForRobotReady(movement.type);
			break;
		case BACKWARDSC:
			//System.out.println(movement.type + " - Backward " + movement.distance);
			bRobot.backwardsC(movement.type);
			bRobot.waitForRobotReady(movement.type);
			break;
		case STOP:
			//System.out.println(movement.type + " - Stop");
			bRobot.stop(movement.type);
			bRobot.waitForRobotReady(movement.type);
			break;
		case GRAB:
			if (grabFlag) {
				//System.out.println(movement.type + " - Grab");
				bRobot.grab(movement.type);
				bRobot.waitForRobotReady(movement.type);
				grabFlag = false;
			} else {
				//System.out.println("Cant grab twice!!");
				grabFlag = false;
			}
			break;
		case KICK:
			if (!grabFlag) {
				//System.out.println(movement.type + " - Kick");
				bRobot.kick(movement.type);
				bRobot.waitForRobotReady(movement.type);
				grabFlag = true;
			} else {
				//System.out.println("Cant kick twice");
				grabFlag = true;
			}
			break;
		case DELAY:
			//System.out.println(movement.type + " - Delay " + movement.milliseconds);
			SafeSleep.sleep(movement.milliseconds);
			bRobot.waitForRobotReady(movement.type);
			break;
		case ROTATE:
			//System.out.println(movement.type + " - Rotate " + movement.angle);
			bRobot.rotateLEFT(movement.type, movement.angle);
			bRobot.waitForRobotReady(movement.type);
			break;
		case SET_SPEED:
			//System.out.println(movement.type + " - Set Speed " + movement.speed);
			bRobot.setTravelSpeed(movement.type, movement.speed);
			bRobot.waitForRobotReady(movement.type);
			break;
		case SET_ROTATESPEED:
			bRobot.setRotateSpeed(movement.type, movement.speed);
			bRobot.waitForRobotReady(movement.type);
			break;			
		case FORWARDSC:
			//System.out.println(movement.type + " - ForwardC");
			bRobot.forwardsC(movement.type);
			bRobot.waitForRobotReady(movement.type);
			break;
		case MOVING:
			bRobot.isMoving(movement.type);
			bRobot.waitForRobotReady(movement.type);
			break;
		default:
			//System.out.println("DERP! Unknown movement mode specified");
			assert (false);
		}
	}

	/**
	 * Main method for the movement thread
	 * 
	 * @see Thread#run()
	 */
	public void run() {
		try {
			while (!die) {

				// Wait for next movement operation
				jobSem.acquire();
				// Clear the movement interrupt flag for the new movement
				interruptMove = false;
				// Set the running flag to true for busy-waiting
				running = true;

				queueLock.lockInterruptibly();
				if (!moveQueue.isEmpty() && !die) {
					MoverConfig movement = moveQueue.poll();
					queueLock.unlock();
					assert (movement != null) : "moveQueue.poll() returned null when non-empty";
					assert (movement.mode != null) : "invalid movement generated";

					processMovement(movement);
				} else {
					queueLock.unlock();
				}

				// If we just did the last move in the queue, wake up the
				// waiting threads
				if (moveQueue.isEmpty())
					wakeUpWaitingThreads();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Try to prevent deadlocks when the mover breaks
			wakeUpWaitingThreads();
		} finally {
			// Stop the robot when the movement thread has been told to exit
			bRobot.stop("attack");
			bRobot.stop("defence");
			// Clear the robot's buffer to potentially allow restart of a
			// RobotMover thread
			//robot.clearBuff();
		}
	}

	/**
	 * Tells the move thread to stop executing and immediately returns. <br/>
	 * Call join() after this if you want to wait for the mover thread to die.
	 * @throws InterruptedException 
	 */
	public void kill() throws InterruptedException {
		die = true;
		interruptMove();
		// Wake up the RobotMover thread if it's waiting for a new job
		jobSem.release();
		interruptMove = true;
		//resetQueue();
		die = false;
	}

	public void interruptMove() {
		interruptMove = true;
	}

	/**
	 * Resets the queue of movements to allow for an immediate change in planned
	 * movements <br/>
	 * NOTE: This does not interrupt an active movement
	 * 
	 * @throws InterruptedException
	 *             if the RobotMover thread was interrupted
	 */
	public void resetQueue(String type) throws InterruptedException {
		// Block changes in the queue until the queue is finished
		// resetting
		queueLock.lockInterruptibly();
		// Reset the job semaphore since there will be no more queued jobs
		jobSem.drainPermits();
		if (moveQueue.isEmpty()) {
			queueLock.unlock();
			return;
		}

		moveQueue.clear();
		this.stopRobot(type);
		queueLock.unlock();
		
	}

	/**
	 * Checks if the mover is running a job
	 * 
	 * @return true if the mover is doing something, false otherwise
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Checks if the mover has queued jobs, not including the one currently
	 * running
	 * 
	 * @return true if there are queued jobs, false otherwise
	 */
	public boolean hasQueuedJobs() {
		try {
			queueLock.lockInterruptibly();
			boolean result = !moveQueue.isEmpty();
			queueLock.unlock();
			return result;
		} catch (InterruptedException e) {
			// InterruptedException can only occur if the thread has been
			// interrupted - therefore there can't be jobs waiting
			return false;
		}
	}

	/**
	 * Determines how many jobs have been queued, not including the one
	 * currently running
	 * 
	 * @return The number of jobs currently queued
	 */
	public int numQueuedJobs() {
		try {
			// Get a lock on the queue to prevent changes while determining how
			// many
			// jobs there are
			queueLock.lockInterruptibly();
			int result = moveQueue.size();
			queueLock.unlock();
			return result;
		} catch (InterruptedException e) {
			// If the thread has been interrupted, there can't be jobs queued.
			return 0;
		}
	}

	/**
	 * Waits for the movement queue to complete before returning
	 */
	public void waitForCompletion() throws InterruptedException {
		//System.out.println("Getting Semaphore");
		waitSem.acquire();
		//.println("Got Semaphore");
	}


	public synchronized boolean setSpeed(String robotType, int speed) {
		MoverConfig movement = new MoverConfig();
		movement.speed = speed;
		movement.mode = Mode.SET_SPEED;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}

	public synchronized boolean setRotateSpeed(String robotType, int speed) {
		MoverConfig movement = new MoverConfig();
		movement.speed = speed;
		movement.mode = Mode.SET_ROTATESPEED;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}
	
	/**
	 * Queues a forward by a distance.
	 * 
	 * @param distance
	 *            Move forward by this value (robot will execute this in cm)
	 * @return true if the forward was successfully queued, false otherwise
	 * 
	 * @see #waitForCompletion()
	 */
	public synchronized boolean forward(String robotType, double distance) {
		MoverConfig movement = new MoverConfig();
		movement.distance = distance;
		movement.mode = Mode.FORWARD;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}

	public synchronized boolean forwardsC(String robotType) {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.FORWARDSC;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}	

	public synchronized boolean backwardsC(String robotType) {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.BACKWARDSC;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}	

	/**
	 * Queues a grab motion.
	 * 
	 * @param robotType
	 *            Which robot's queue the command should be added to.
	 * @return true if the rotate was successfully queued, false otherwise
	 * 
	 * @see #waitForCompletion()
	 */
	public synchronized boolean grab(String robotType) {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.GRAB;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}

	/**
	 * Queues a command to make the robot kick
	 * 
	 * @return true if the kick was successfully queued, false otherwise
	 * 
	 * @see #waitForCompletion()
	 */
	public synchronized boolean kick(String robotType) {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.KICK;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}

	/**
	 * Queues a rotation by an angle.
	 * 
	 * @param angleRad
	 *            clockwise angle to rotate by (in Radians)
	 * @return true if the rotate was successfully queued, false otherwise
	 * 
	 * @see #waitForCompletion()
	 */
	public synchronized boolean rotate(String robotType, double angle) {
		MoverConfig movement = new MoverConfig();
		movement.angle = (int) angle;
		movement.mode = Mode.ROTATE;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}

	/**
	 * Queues a command to stop the robot
	 * 
	 * @return true if the stop was successfully queued, false otherwise
	 * 
	 * @see #waitForCompletion()
	 * @see #interruptMove()
	 * @see #resetQueue()
	 */
	public synchronized boolean stopRobot(String robotType) {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.STOP;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}


	/**
	 * Queues a delay job, where the RobotMover will wait for the specified time <br/>
	 * WARNING: Once a delay job has started, it cannot be interrupted
	 * 
	 * @param milliseconds
	 *            The time in milliseconds to sleep for
	 * 
	 * @return true if the delay was successfully queued, false otherwise
	 */
	public synchronized boolean delay(String robotType, long milliseconds) {
		MoverConfig movement = new MoverConfig();
		movement.milliseconds = milliseconds;
		movement.mode = Mode.DELAY;
		movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}
}
