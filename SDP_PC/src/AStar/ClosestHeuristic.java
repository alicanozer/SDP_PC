package AStar;

public class ClosestHeuristic implements AStarHeuristic {

	public float getCost(TileBasedMap map, int x, int y, int fx, int fy) {
		
		float dx = fx - x;
		float dy = fy - y;
		
		float result = (float) (10 * Math.sqrt((dx*dx)+(dy*dy)));
		
		return result;
		
	}
	
}
