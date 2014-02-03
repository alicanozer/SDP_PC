package vision;

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
	
}
