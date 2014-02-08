package AStar;

import java.util.ArrayList;

public class Path {

	private ArrayList<Step> steps = new ArrayList<Step>();
	
	public Path() {
		
	}
	
	public int getLength() {
		return steps.size();
	}
	
	public Step getStep(int x) {
		return steps.get(x);
	}
	
	public void addStep(int x, int y) {
		steps.add(new Step( x, y));
	}
	
	public void prependStep(int x, int y) {
		steps.add(0, new Step( x, y));
	}
	
	public boolean contains(int x, int y) {
		return steps.contains(new Step( x, y));
	}
	
}
