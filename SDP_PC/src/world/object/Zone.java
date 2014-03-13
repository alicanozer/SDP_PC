package world.object;

import geometry.Vector;

import java.awt.Polygon;
import java.awt.Shape;

import world.support.Corrections;

public class Zone {

	private Polygon pixelZone;
	private Polygon realZone;

	public Zone(int[] xpoints, int[] ypoints, int npoints, double cmPerPixel) {
		pixelZone = new Polygon(xpoints, ypoints, npoints);
		for (int i = 0; i < npoints; i++) {
			Vector v = Corrections.correctUnit(new Vector(xpoints[i], ypoints[i]), cmPerPixel);
			xpoints[i] = (int) v.getX();
			ypoints[i] = (int) v.getY();
		}
		realZone = new Polygon(xpoints, ypoints, npoints);
	}
	
	public boolean containsCircleReal(Vector point, double radius, int samples) {
		return containsCircle(realZone, point, radius, samples);
	}
	
	public boolean containsCirclePixel(Vector point, double radius, int samples) {
		return containsCircle(pixelZone, point, radius, samples);
	}
	
	public static boolean containsCircle(Shape shape, Vector point, double radius, int samples) {
		for (int i = 0; i < samples; i++) {
			if ( ! shape.contains(point.getX()+Math.sin(i*Math.PI*2/samples)*radius, point.getY()+Math.cos(i*Math.PI*2/samples)*radius)) {
				return false;
			}
		}
		return true;
	}

}
