package vision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import AStar.AStarPathFinder;
import AStar.Mover;
import AStar.Path;
import AStar.ReducedMap;
import AStar.TileBasedMap;

public class TestAStar {
	
	ObjectLocations objs;
	
	public TestAStar(ObjectLocations objs) {
		this.objs = objs;
	}

	public void drawRedLine(Graphics2D g) {
		
		Color c = g.getColor();
		g.setColor(Color.RED);
				
		int ballX = objs.getBall().x;
		int ballY = objs.getBall().y;
		
		/* the objects in Object locations are static fields so use them directly */
//		int yellowX = objs.yellowMarkers[0].x;
//		int yellowY = objs.yellowMarkers[0].y;
			
//		ReducedMap map = new ReducedMap(objs, true);
				
//		AStarPathFinder test = new AStarPathFinder(map, 20, true);
//		Path found = test.findPath( yellowX, yellowY, ballX, ballY);
		
//		for (int x = 0; x < found.getLength(); x++) {			
//			//Draw red Line from marker to point
//			g.drawLine(found.getStep(x).getX(), found.getStep(x).getY(), 
//					found.getStep(x+1).getX(), found.getStep(x+1).getY());
//		}

		g.drawLine(0, 0, 60, 60);
		
		g.setColor(c);
		
	}
	
}
