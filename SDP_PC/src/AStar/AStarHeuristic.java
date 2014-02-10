package AStar;

public interface AStarHeuristic {

	public float getCost(TileBasedMap map, int x, int y, int fx, int fy);
	
}
