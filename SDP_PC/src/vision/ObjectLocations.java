package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
/**
 * stub wrapper around object positions
 * @author bilyan
 *
 */
public class ObjectLocations {
	public Point2D_I32 ball;
	public ArrayList<Point2D_I32> yellowMarkers;
	public ArrayList<Point2D_I32> blueMarkers;
	public ArrayList<Point2D_I32> dots;

	public ObjectLocations(
			Point2D_I32 ball,
			ArrayList<Point2D_I32>  yellowMarkers,
			ArrayList<Point2D_I32>  blueMarkers,
			ArrayList<Point2D_I32>  dots){
		this.ball = ball;
		this.yellowMarkers = yellowMarkers;
		this.blueMarkers = blueMarkers;
		this.dots = dots;
	}
	/**
	 * draws crosses over all objects of interest
	 * @param g
	 */
	public void drawCrosses(Graphics2D g){
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		if (this.ball != null) {
			// drawing X over ball			//		
			g.drawLine(this.ball.x - 10, this.ball.y, this.ball.x + 10, this.ball.y);
			g.drawLine(this.ball.x, this.ball.y - 10, this.ball.x, this.ball.y + 10);
		}
		if (this.yellowMarkers != null) {
			g.drawLine(this.yellowMarkers.get(0).x - 10, 
					this.yellowMarkers.get(0).y,
					this.yellowMarkers.get(0).x + 10, 
					this.yellowMarkers.get(0).y);
			g.drawLine(this.yellowMarkers.get(0).x, 
					this.yellowMarkers.get(0).y + 10,
					this.yellowMarkers.get(0).x, 
					this.yellowMarkers.get(0).y - 10);
			

			try {
				g.drawLine(this.yellowMarkers.get(1).x - 10,
						this.yellowMarkers.get(1).y, 
						this.yellowMarkers.get(1).x + 10,
						this.yellowMarkers.get(1).y);
				g.drawLine(this.yellowMarkers.get(1).x,
						this.yellowMarkers.get(1).y + 10, 
						this.yellowMarkers.get(1).x,
						this.yellowMarkers.get(1).y - 10);
			} catch (NullPointerException e) {
			}
		}
		if (this.blueMarkers != null) {
			// drawing X over blue markers
			g.drawLine(this.blueMarkers.get(0).x - 10, 
					this.blueMarkers.get(0).y,
					this.blueMarkers.get(0).x + 10, 
					this.blueMarkers.get(0).y);
			g.drawLine(this.blueMarkers.get(0).x, 
					this.blueMarkers.get(0).y + 10,
					this.blueMarkers.get(0).x, 
					this.blueMarkers.get(0).y - 10);

			try {
				g.drawLine(this.blueMarkers.get(1).x - 10, 
						this.blueMarkers.get(1).y,
						this.blueMarkers.get(1).x + 10, 
						this.blueMarkers.get(1).y);
				g.drawLine(this.blueMarkers.get(1).x, 
						this.blueMarkers.get(1).y + 10,
						this.blueMarkers.get(1).x, 
						this.blueMarkers.get(1).y - 10);
			} catch (NullPointerException e) {
			}
		}
		
		if (this.dots != null) {
			// drawing X over blue markers
			g.setColor(Color.RED);

			try {
				g.drawLine(this.dots.get(0).x - 10, 
						this.dots.get(0).y,
						this.dots.get(0).x + 10, 
						this.dots.get(0).y);
				g.drawLine(this.dots.get(0).x, 
						this.dots.get(0).y + 10,
						this.dots.get(0).x, 
						this.dots.get(0).y - 10);
				
				g.drawLine(this.dots.get(1).x - 10, 
						this.dots.get(1).y,
						this.dots.get(1).x + 10, 
						this.dots.get(1).y);
				g.drawLine(this.dots.get(1).x, 
						this.dots.get(1).y + 10,
						this.dots.get(1).x, 
						this.dots.get(1).y - 10);
				
				g.drawLine(this.dots.get(2).x - 10, 
						this.dots.get(2).y,
						this.dots.get(2).x + 10, 
						this.dots.get(2).y);
				g.drawLine(this.dots.get(2).x, 
						this.dots.get(2).y + 10,
						this.dots.get(2).x, 
						this.dots.get(2).y - 10);
				
				g.drawLine(this.dots.get(3).x - 10, 
						this.dots.get(3).y,
						this.dots.get(3).x + 10, 
						this.dots.get(3).y);
				g.drawLine(this.dots.get(3).x, 
						this.dots.get(3).y + 10,
						this.dots.get(3).x, 
						this.dots.get(3).y - 10);
			} catch (NullPointerException e) {
			}
		}
		
		
		g.setColor(c);
	}
	
}
