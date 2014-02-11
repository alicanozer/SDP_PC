package movement;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class RobotMover {
	/** A reentrant mutex lock for the movement queue */
	private ReentrantLock queueLock = new ReentrantLock(true);
	/** A flag to permit busy-waiting */
	private boolean running = false;
	/** A flag to interrupt any active movements */
	private boolean interruptMove = false;
	/** A flag to tell the RobotMover thread to die */
	private boolean die = false;
	/** A semaphore used to signal the RobotMover has jobs queued */
	private Semaphore jobSem = new Semaphore(0, true);
	/** A thread-safe queue for movement commands */
	private ConcurrentLinkedQueue<MoverConfig> moveQueue = new ConcurrentLinkedQueue<MoverConfig>();
	
	/** Settings info class to permit queueing of movements */
	private class MoverConfig {
		public double x = 0;
		public double y = 0;
		public double angle = 0;
		public boolean avoidBall = false;
		public boolean avoidEnemy = false;
		public long milliseconds = 0;
		private int dribblemode = 0;

	};
	
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

}
