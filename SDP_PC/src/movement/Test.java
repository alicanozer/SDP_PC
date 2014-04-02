package movement;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

//import world.Robot;
//import world.PixelWorld;

import comms.BluetoothRobot;
import comms.RobotController;

public class Test extends Thread{
	
	/** A flag to tell the RobotMover thread to die */
	private static boolean die = false;

	/** A thread-safe queue for movement commands */
	private ConcurrentLinkedQueue<MoverConfig> moveQueue = new ConcurrentLinkedQueue<MoverConfig>();
	/** A reentrant mutex lock for the movement queue */
	private ReentrantLock queueLock = new ReentrantLock(true);
	/** A semaphore used to signal the RobotMover has jobs queued */
	private Semaphore jobSem = new Semaphore(0, true);
	/** A semaphore used to signal the RobotMover has completed its job queue */
	private Semaphore waitSem = new Semaphore(0, true);
	
	public Test(String type) {
		super("RobotMover");
	}
	

	
	/** Settings info class to permit queueing of movements */
	private class MoverConfig {
		public double distance = 0;
		public String type = null;
		public int angle = 0;
		public int speed = 0;
		//private MovingPoint movPoint = null;
		public Mode mode;

	}
	
	private enum Mode {
		FORWARD, BACKWARD, STOP, GRAB, KICK, DELAY, ROTATE, SET_SPEED, FORWARDSC, MOVING
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
			System.out.println("FORWARD - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case BACKWARD:
			System.out.println("BACKWARD - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case STOP:
			System.out.println("STOP - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case GRAB:
			System.out.println("GRAB - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case KICK:
			System.out.println("KICK - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case DELAY:
			System.out.println("DELAY - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case ROTATE:
			System.out.println("ROTATE - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case SET_SPEED:
			System.out.println("SET_SPEED - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case FORWARDSC:
			System.out.println("FORWARDSC - " + moveQueue.size());
			Thread.sleep(500);
			break;
		case MOVING:
			System.out.println("MOVING - " + moveQueue.size());
			Thread.sleep(500);
			break;
		default:
			System.out.println("DERP! Unknown movement mode specified");
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
			System.out.println(die);
			while (!die && !moveQueue.isEmpty()) {
				

				// Wait for next movement operation
				jobSem.acquire();
				System.out.println("Command added");

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
			System.out.println("Strategy block complete");
/*			bRobot.stop("attack");
			bRobot.stop("defence");*/

		}
	}

	/**
	 * Tells the move thread to stop executing and immediately returns. <br/>
	 * Call join() after this if you want to wait for the mover thread to die.
	 * @throws InterruptedException 
	 */
	public void kill() throws InterruptedException {
		System.out.println("Kill called");
		die = true;
		//resetQueue();
		// Wake up the RobotMover thread if it's waiting for a new job
		jobSem.release();
	}

	/**
	 * Resets the queue of movements to allow for an immediate change in planned
	 * movements <br/>
	 * NOTE: This does not interrupt an active movement
	 * 
	 * @throws InterruptedException
	 *             if the RobotMover thread was interrupted
	 */
	public void resetQueue() throws InterruptedException {
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
		queueLock.unlock();

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
	
	public synchronized boolean setSpeed() {
		MoverConfig movement = new MoverConfig();
/*		movement.speed = speed;
		movement.mode = Mode.SET_SPEED;
		movement.type = robotType;*/

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
	public synchronized boolean forward() {
		MoverConfig movement = new MoverConfig();
//		movement.distance = distance;
		movement.mode = Mode.FORWARD;
	//	movement.type = robotType;

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}
	
	public synchronized boolean forwardsC() {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.FORWARDSC;
//		movement.type = robotType;
		
		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}	
	
	/**
	 * Queues a backward by a distance.
	 * 
	 * @param distance
	 *            Move backward by this value (robot will execute this in cm)
	 * @return true if the forward was successfully queued, false otherwise
	 * 
	 * @see #waitForCompletion()
	 */
	public synchronized boolean backward() {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.BACKWARD;
		//movement.type = robotType;*/

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
	public synchronized boolean grab() {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.GRAB;
//		movement.type = robotType;*/

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
	 */
	public synchronized boolean kick() {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.KICK;
//		movement.type = robotType;*/
		
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
	 */
	public synchronized boolean rotate() {
		MoverConfig movement = new MoverConfig();
//		movement.angle = (int) angle;
		movement.mode = Mode.ROTATE;
	//	movement.type = robotType;*/

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
	 * @see #interruptMove()
	 * @see #resetQueue()
	 */
	public synchronized boolean stopRobot() {
		MoverConfig movement = new MoverConfig();
		movement.mode = Mode.STOP;
///		movement.type = robotType;*/

		if (!pushMovement(movement))
			return false;

		// Let the mover know it has a new job
		jobSem.release();
		return true;
	}

}
