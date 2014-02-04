package vision;

import java.awt.Color;
import java.awt.Graphics2D;

import georegression.struct.point.Point2D_I32;
/**
 * stub wrapper around object positions
 * @author bilyan
 *
 */
public class ObjectLocations {
	public Point2D_I32 ball;
	public Point2D_I32[] yellowMarkers;
	public Point2D_I32[] blueMarkers;
	public Point2D_I32[] dots;

	public ObjectLocations(
			Point2D_I32 ball,
			Point2D_I32[] yellowMarkers,
			Point2D_I32[] blueMarkers,
			Point2D_I32[] dots){
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
			g.drawLine(this.yellowMarkers[0].x - 10, 
					this.yellowMarkers[0].y,
					this.yellowMarkers[0].x + 10, 
					this.yellowMarkers[0].y);
			g.drawLine(this.yellowMarkers[0].x, 
					this.yellowMarkers[0].y + 10,
					this.yellowMarkers[0].x, 
					this.yellowMarkers[0].y - 10);
			

			try {
				g.drawLine(this.yellowMarkers[1].x - 10,
						this.yellowMarkers[1].y, 
						this.yellowMarkers[1].x + 10,
						this.yellowMarkers[1].y);
				g.drawLine(this.yellowMarkers[1].x,
						this.yellowMarkers[1].y + 10, 
						this.yellowMarkers[1].x,
						this.yellowMarkers[1].y - 10);
			} catch (NullPointerException e) {
			}
		}
		if (this.blueMarkers != null) {
			// drawing X over blue markers
			g.drawLine(this.blueMarkers[0].x - 10, 
					this.blueMarkers[0].y,
					this.blueMarkers[0].x + 10, 
					this.blueMarkers[0].y);
			g.drawLine(this.blueMarkers[0].x, 
					this.blueMarkers[0].y + 10,
					this.blueMarkers[0].x, 
					this.blueMarkers[0].y - 10);

			try {
				g.drawLine(this.blueMarkers[1].x - 10, 
						this.blueMarkers[1].y,
						this.blueMarkers[1].x + 10, 
						this.blueMarkers[1].y);
				g.drawLine(this.blueMarkers[1].x, 
						this.blueMarkers[1].y + 10,
						this.blueMarkers[1].x, 
						this.blueMarkers[1].y - 10);
			} catch (NullPointerException e) {
			}
		}
		g.setColor(c);
	}
	
}
