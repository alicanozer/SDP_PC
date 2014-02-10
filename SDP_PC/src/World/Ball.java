package World;

public class Ball extends Entity {

	public double x;
	public double y;
	public double radius;
	public double speedX;
	public double speedY;

	public Ball() {
		super();
	}

	public double getRadius() {
		return radius;
	}

	public String name() {
		return "Ball";
	}

	public String position() {
		return String.format("Ball: %s", this.getPosition());
	}
	
}
