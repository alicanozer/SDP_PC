package comms;

public interface RobotController {

	public void connect() throws Exception;
	
	public boolean isConnected();
	
	public boolean isReady();
	
	public void disconnect();
	
	public void stop();
	
	public void kick();
	
	public void move(int speedX, int speedY);
	
	public void rotateLEFT(int angle);
	
	public void rotateRIGHT(int angle);
	
	public void forward();
	
	public void backwards();
		
	public void setSpeed(int speed);
	
}
