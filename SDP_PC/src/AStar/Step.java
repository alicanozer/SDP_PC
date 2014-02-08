package AStar;

public class Step {

	private int x;
	private int y;
	
	public Step(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
		
	public int hashCode() {
		return x*y;
	}
	
//	public boolean equals(Object other) {
//		
//	}
	
}
