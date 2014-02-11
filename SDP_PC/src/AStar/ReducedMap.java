package AStar;

import vision.ObjectLocations;

public class ReducedMap implements TileBasedMap {

	public final int WIDTH;
	public final int HEIGHT;
	
	public double REDUCTION = 20.0;
	
	public static final int PITCH = 0;
	public static final int BLOCKED = 1;
	public static final int BALL = 3;
	
	public static final int US = 2;
	
	public int[][] terrain;
	private int[][] units;
	
	private boolean[][] visited;
	
	public boolean avoidBall = false;
	
	public ReducedMap(ObjectLocations objs, boolean avoidBall) {
		
		HEIGHT = reduceRound(316);
		WIDTH = reduceRound(550);
		
		terrain = new int[HEIGHT][WIDTH];
		units = new int[HEIGHT][WIDTH];
		visited = new boolean[HEIGHT][WIDTH];
		
		this.avoidBall = avoidBall;
		
		// Find location of ball on map
		int ballx = reduceRound(objs.getBall().x);
		int bally = reduceRound(objs.getBall().y);
		
		if (ballx <= 1)
			ballx += 2;
		if (bally <= 1)
			bally += 2;
		if (ballx >= WIDTH - 1)
			ballx -= 2;
		if (bally >= HEIGHT - 1)
			bally -= 2;
		
		if (avoidBall) {
			fillArea(ballx - 2, bally - 2, 4, 4, BLOCKED);
		} else {
			fillArea(ballx - 2, bally - 2, 4, 4, BALL);
		}
		
	}
	
	public int reduceRound(double n) {
		return (int) (Math.round(n / REDUCTION));
	}
	
	private void fillArea(int x, int y, int height, int width, int type) {
		int xMin = Math.max(x, 0);
		int yMin = Math.max(y, 0);
		int xMax = Math.min(x + HEIGHT, HEIGHT);
		int yMax = Math.min(y + WIDTH, WIDTH);
		for (int xp = xMin; xp < xMax; ++xp) {
			for (int yp = yMin; yp < yMax; ++yp) {
				terrain[xp][yp] = type;
			}
		}
	}
	
	public void clearVisited() {
		for (int x = 0; x < getTotalHeightTiles(); x++) {
			for (int y = 0; y < getTotalWidthTiles(); y++) {
				visited[x][y] = false;
			}
		}
	}
	
	public int getTerrain(int x, int y) {
		return terrain[x][y];
	}
	
	public int getUnit(int x, int y) {
		try {
			return units[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}
	
	public void setUnit(int x, int y, int unit) {
		units[x][y] = unit;
	}
	
	
	
	@Override
	public int getTotalWidthTiles() {
		// TODO Auto-generated method stub
		return WIDTH;
	}

	@Override
	public int getTotalHeightTiles() {
		// TODO Auto-generated method stub
		return HEIGHT;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		// TODO Auto-generated method stub
		visited[x][y] = true;
	}

	@Override
	public boolean blocked(int x, int y) {
		// TODO Auto-generated method stub
		if (getUnit(x, y) != 0) {
			return true;
		}
		try {
			return terrain[x][y] == BLOCKED;
		} catch (ArrayIndexOutOfBoundsException e) {
			return true;
		}
		
	}

	@Override
	public float getCost(int startX, int startY, int endX, int endY) {
		// TODO Auto-generated method stub
		int coef;
		if (blocked(startX - 1, startY) || blocked(startX - 1, startY - 1)
				|| blocked(startX, startY - 1) || blocked(startX + 1, startY - 1)
				|| blocked(startX + 1, startY) || blocked(startX + 1, startY + 1)
				|| blocked(startX, startY + 1) || blocked(startX - 1, startY + 1)) {
			coef = 3;
		} else {
			coef = 1;
		}
		if (startX == endX || startY == endY)
			return 10 * coef;
		else
			return 14 * coef;
	
	}
	
}
