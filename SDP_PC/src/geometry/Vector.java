package geometry;

import java.lang.Math;

//contains lots of maths stuff
public class Vector {
	private double x;
	private double y;
	
	public double getX() { return x; }
	public double getY() { return y; }
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}	
	
	public static Vector subtract(Vector v1, Vector v2) {
		return new Vector(v1.x - v2.x, v1.y - v2.y);
	}
	
	public static double dotProduct(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	public static double magnitudeSquared(Vector v) {
		return dotProduct(v, v);
	}
	
	public static double magnitude(Vector v) {
		return Math.sqrt(magnitudeSquared(v));
	}
	
	public static double distance(Vector v1, Vector v2) {
		return magnitude(subtract(v1, v2));
	}
}
