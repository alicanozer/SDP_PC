package comms;

public interface RobotController {

	public void connect() throws Exception;
	
	public boolean isAttackConnected();
	
	public boolean isDefenceConnected();
	
	public void disconnect(String robotType);
	
	public void stop(String robotType);
	
	public void grab(String robotType);
	
	public void kick(String robotType);
		
	public void rotateLEFT(String robotType, int angle);
	
	public void rotateRIGHT(String robotType, int angle);
	
	public void forward(String robotType, double distance);
	
	public void forwardsC(String robotType);
	
	public void setSpeed(String robotType, int speed);
	
	public void isMoving(String robotType);
	
	public void waitForRobotReady(String robotType) throws InterruptedException;
	
	public void backwardsC(String robotType);
	
}