package AStar;

import java.util.ArrayList;
import java.util.Collections;

public class AStarPathFinder implements PathFinder {

	private ArrayList<Node> closedList = new ArrayList<Node>();
	private SortedList openList = new SortedList();
	
	private TileBasedMap map;
	
	private Node[][] nodes;
	
	private int maxSearchDistance;
	private boolean allowDiagonalMovement;
	
	private AStarHeuristic aStarHeuristic;
	
	public AStarPathFinder(TileBasedMap map, int maxSearchDisctance, boolean allowDiagonalMovement) {
		this(map, maxSearchDisctance, allowDiagonalMovement, new ClosestHeuristic());
	}
	
	public AStarPathFinder(TileBasedMap map, int maxSearchDisctance, boolean allowDiagonalMovement, AStarHeuristic aStarHeuristic) {
		this.allowDiagonalMovement = allowDiagonalMovement;
		this.aStarHeuristic = aStarHeuristic;
		this.maxSearchDistance = maxSearchDisctance;
		this.map = map;
		
		nodes = new Node[map.getTotalWidthTiles()][map.getTotalHeightTiles()];
		for (int x = 0; x < map.getTotalWidthTiles(); x++) {
			for (int y = 0; y < map.getTotalHeightTiles(); y++) {
				nodes[x][y] = new Node(x, y);
			}
		}
		
	}
	
	//Add Mover
	public Path findPath( int startX, int startY, int endX, int endY) {
		
		if (map.blocked(endX, endY)) {
			return null;
		}
		
		// Initial state for A*
		
		nodes[startX][startY].cost = 0;
		nodes[startX][startY].depth = 0;
		
		closedList.clear();
		openList.clear();
		openList.add(nodes[startX][startY]);
		
		nodes[endX][endY].parent = null;
		
		int maxDepth = 0;
		
		while ((maxDepth < maxSearchDistance) && (openList.getSize() != 0)) {
			
			Node current = getFirstInOpen();
			
			if (current == nodes[endX][endY]) {
				break;
			}
			
			removeFromOpen(current);
			addToClosed(current);
			
			
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					
					if ((x == 0) && (y == 0)) {
						// at current position
						continue;
					}
					
					// If diagonal movement is not allowed then only one of x and y can be set 
					// so that the robot only moves along its x-axis or y-axis
					if (!allowDiagonalMovement) {
						if ((x==0)&&(y==0)) {
							continue;
						}
					}
					
					int xp = x + current.x;
					int yp = y + current.y;
					
					//Add mover
					if (isValidLocation(startX, startY, xp, yp)) {
						
						// Cost of total step for robot to move from its current tile to the next						
						float nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
						
						
						Node neighbour = nodes[xp][yp];
						map.pathFinderVisited(xp, yp);

						// If the cost for this step lower than its previous then it will not be discarded
						if (nextStepCost < neighbour.cost) {
							if (inOpenList(neighbour)) {
								removeFromOpen(neighbour);
							} 
							if (inClosedList(neighbour)) {
								removeFromClosedList(neighbour);
							}
							
						}
						
						if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
							neighbour.cost = nextStepCost;
							//Add Mover
							neighbour.heuristic = getHeuristicCost(xp, yp, endX, endY);
							
							maxDepth = Math.max(maxDepth, neighbour.setParent(current));
							addToOpen(neighbour);
						
						}
						
					}
					
				}
			}
			
		}
		
		if (nodes[endX][endY].parent == null) {
			return null;
		}

		// Records the Path using the parent nodes to construct its path from the 
		// target node to its starting location
		Path path = new Path();
		Node target = nodes[endX][endY];
		while (target != nodes[startX][startY]) {
			path.prependStep(target.x, target.y);
			target = target.parent;
		}
		
		path.prependStep(startX, startY);

		// returns Path for robot to take
		return path;
		
	}
	
	protected void addToClosed(Node node) {
		closedList.add(node);
	}

	protected Node getFirstInOpen() {
		return (Node) openList.first();
	}
	
	protected void addToOpen(Node node) {
		openList.add(node);
	}
	
	protected boolean inOpenList(Node node) {
		return openList.contains(node);
	}
	
	protected void removeFromOpen(Node node) {
		openList.remove(node);
	}
	
	protected boolean inClosedList(Node node) {
		return closedList.contains(node);
	}
	
	protected void removeFromClosedList(Node node) {
		closedList.remove(node);
	}
	
	//Add mover
	public float getMovementCost(int startX, int startY, int endX, int endY) {
		return map.getCost(startX, startY, endX, endY);
	}
	
	//Add mover
	public float getHeuristicCost( int x, int y, int fx, int fy) {
		return aStarHeuristic.getCost(map, x, y, fx, fy);
	}
	
	//Add mover
	protected boolean isValidLocation( int startX, int startY, int checkX, int checkY) {
		
		boolean invalid = (checkX < 0) || (checkY < 0) || (checkX >= map.getTotalWidthTiles())
				|| (checkY >= map.getTotalHeightTiles());

		if ((!invalid) && ((startX != checkX) || (startY != checkY))) {
			invalid = map.blocked(checkX, checkY);
		}

		return !invalid;
	
	}
	
	private class SortedList {
		
		private ArrayList<Node> openList = new ArrayList<Node>();
		
		public Object first() {
			return openList.get(0);
		}
		
		public void clear() {
			openList.clear();
		}
		
		public int getSize() {
			return openList.size();
		}
		
		public void add(Node node) {
			openList.add(node);
			Collections.sort(openList);
		}
		
		public void remove(Node node) {
			openList.remove(node);
		}
		
		public boolean contains(Node node) {
			return openList.contains(node);
		}
		
	}
	
	private class Node implements Comparable<Object> {
		
		private int x;
		private int y;
		
		private float cost;

		private float heuristic;
		private int depth;
		
		private Node parent;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;
			
			return depth;
		}
		
		public int compareTo(Object other) {
			Node next = (Node) other;
			
			float f = heuristic + cost;
			float nextf = next.heuristic + next.cost;
			
			if (f < nextf) {
				return -1;
			} else if(f > nextf) {
				return 1;
			} else {
				return 0;
			}
			
		}
		
	}
	
}
