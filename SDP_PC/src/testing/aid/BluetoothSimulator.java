/**
 * 
 */
package testing.aid;

import geometry.Vector;
import world.PixelWorld;
import world.object.MobilePixelObject;
import comms.RobotController;

/**
 * @author apljungquist
 * 
 */
public class BluetoothSimulator implements RobotController {

	private boolean connected = true;
	private boolean ready = true;

	private static final long DELAY_MILLIS = 20;
	private static final int FPS = 25;
	
	private double rotateSpeed = 120;
	private double travelSpeed = 20;

	Thread thread;

	PixelWorld world;
	MobilePixelObject robot;

	/**
	 * 
	 */
	public BluetoothSimulator(PixelWorld world, int robot) {
		thread = new Thread();
		thread.interrupt();
		this.world = world;
		this.robot = world.getMobileObject(robot);
	}

	@Override
	public void connect() throws Exception {
		System.out.println("connect()");
		connected = true;
	}

	@Override
	public boolean isConnected() {
		System.out.println("isConnected()");
		return connected;
	}

	@Override
	public boolean isReady() {
		System.out.println("isReady()");
		return ready;
	}

	@Override
	public void disconnect() {
		System.out.println("disconnect()");
		connected = false;
	}

	@Override
	public void stop() {
		System.out.println("stop()");
		while (thread.isAlive()) {
			thread.interrupt();
			Thread.yield();
		}
	}

	@Override
	public void kick() {
		System.out.println("kick()");
		thread.interrupt();
	}

	@Override
	public void move(final int speedX, final int speedY) {
		stop();
		System.out.println("move()");
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						robot.setPixelPosition(robot.getPixelPosition().add(new Vector(speedX / FPS, speedY / FPS)));
						Thread.sleep(1000 / FPS);
					}
				} catch (InterruptedException e) {
				}
			}
		});
		thread.start();
	}

	@Override
	public void rotateLEFT(final int angle) {
		stop();
		System.out.println("rotateLeft()");
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					double theta = angle;
					while (true && theta > 0) {
						robot.setPixelOrientation(robot.getPixelOrientation().getRotated(-rotateSpeed/FPS*Math.PI/180));
						theta -= rotateSpeed/FPS;
						Thread.sleep(1000 / FPS);
					}
				} catch (InterruptedException e) {
				}
			}
		});
		thread.start();
	}

	@Override
	public void rotateRIGHT(final int angle) {
		stop();
		System.out.println("rotateRight()");
	thread = new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				double theta = angle;
				while (true && theta > 0) {
					robot.setPixelOrientation(robot.getPixelOrientation().getRotated(rotateSpeed/FPS*Math.PI/180));
					theta -= rotateSpeed/FPS*Math.PI/180;
					Thread.sleep(1000 / FPS);
				}
			} catch (InterruptedException e) {
			}
		}
	});
	thread.start();
	}

	@Override
	public void forward() {
		stop();
		System.out.println("forward()");
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						robot.setPixelPosition(robot.getPixelPosition().add(robot.getPixelOrientation().scalarMultiplication(travelSpeed/robot.getPixelOrientation().getMagnitude()/FPS)));
						Thread.sleep(1000 / FPS);
					}
				} catch (InterruptedException e) {
				}
			}
		});
		thread.start();
	}

	@Override
	public void backwards() {
		stop();
		System.out.println("backwards()");
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						robot.setPixelPosition(robot.getPixelPosition().subtract(robot.getPixelOrientation().scalarMultiplication(travelSpeed/robot.getPixelOrientation().getMagnitude()/FPS)));
						Thread.sleep(1000 / FPS);
					}
				} catch (InterruptedException e) {
				}
			}
		});
		thread.start();
	}

	@Override
	public void setSpeed(int speed) {
		System.out.println("setSpeed()");
		travelSpeed = (double)speed;
	}

	@Override
	public boolean isMoving() {
		System.out.println("isMoving()");
		return thread.isAlive();
	}
}
