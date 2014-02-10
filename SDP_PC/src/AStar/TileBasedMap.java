package AStar;

public interface TileBasedMap {

	public int getTotalWidthTiles();
	
	public int getTotalHeightTiles();
	
	public void pathFinderVisited(int x, int y);
	
	public boolean blocked(int x, int y);
	
	public float getCost(int startX, int startY, int endX, int endY);
	
}
