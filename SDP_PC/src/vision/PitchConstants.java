package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Polygon;

public class PitchConstants {
	public PitchConstants(int upperLeftX, int upperLeftY, int croppedWidth,
			int croppedHeight, int region12x, int region23x, int region34x, int hUpperBound, int hLowerBound,
			int rightGoalTopX, int rightGoalTopY, int rightGoalBottomX, int rightGoalBottomY,
			int leftGoalTopX, int leftGoalTopY, int leftGoalBottomX, int leftGoalBottomY) {
		

		
		this.upperLeftX = upperLeftX;
		this.upperLeftY = upperLeftY;
		this.croppedWidth = croppedWidth;
		this.croppedHeight = croppedHeight;
		this.region12X = region12x;
		this.region23X = region23x;
		this.region34X = region34x;
		this.hUpperBound = hUpperBound;
		this.hLowerBound = hLowerBound;
		this.middleLine = croppedWidth/2; //TODO: this or regin23X ?
		
		this.rightGoalTop = new Point2D_I32(rightGoalTopX,rightGoalTopY);
		this.rightGoalBottom = new Point2D_I32(rightGoalBottomX,rightGoalBottomY);
		this.leftGoalTop = new Point2D_I32(leftGoalTopX,leftGoalTopY);
		this.leftGoalBottom = new Point2D_I32(leftGoalBottomX,leftGoalBottomY);
	}
	public int getMiddleLine() {
		return middleLine;
	}
	public int getUpperLeftX() {
		return upperLeftX;
	}
	public int getUpperLeftY() {
		return upperLeftY;
	}
	public int getCroppedWidth() {
		return croppedWidth;
	}
	public int getCroppedHeight() {
		return croppedHeight;
	}
	public int getRegion12X() {
		return region12X;
	}
	public int getRegion23X() {
		return region23X;
	}
	public int getRegion34X() {
		return region34X;
	}
	public int gethUpperBound() {
		return hUpperBound;
	}
	public int gethLOwerBound() {
		return hLowerBound;
	}
	public void setUpperLeftX(int upperLeftX) {
		this.upperLeftX = upperLeftX;
	}
	public void setUpperLeftY(int upperLeftY) {
		this.upperLeftY = upperLeftY;
	}
	public void setCroppedWidth(int croppedWidth) {
		this.croppedWidth = croppedWidth;
	}
	public void setCroppedHeight(int croppedHeight) {
		this.croppedHeight = croppedHeight;
	}
	public void setRegion12X(int region12x) {
		region12X = region12x;
	}
	public void setRegion23X(int region23x) {
		region23X = region23x;
	}
	public void setRegion34X(int region34x) {
		region34X = region34x;
	}
	public void sethUpperBound(int hUpperBound) {
		this.hUpperBound = hUpperBound;
	}
	public void sethLowerBound(int hLowerBound) {
		this.hLowerBound = hLowerBound;
	}
	public static Polygon pitchPolygon;
	public static Polygon region1;
	public static Polygon region2;
	public static Polygon region3;
	public static Polygon region4;
	public static int setPitch;
	private int upperLeftX;
	private int upperLeftY;
	private int croppedWidth;
	private int croppedHeight;
	private int region12X;
	private int region23X;
	private int region34X;
	private int hUpperBound;
	private int hLowerBound;
	private int middleLine;
	
	public Point2D_I32 getRightGoalTop() {
		return rightGoalTop;
	}
	public Point2D_I32 getRightGoalBottom() {
		return rightGoalBottom;
	}
	public Point2D_I32 getLeftGoalTop() {
		return leftGoalTop;
	}
	public Point2D_I32 getLeftGoalBottom() {
		return leftGoalBottom;
	}
	private Point2D_I32 rightGoalTop;
	private Point2D_I32 rightGoalBottom;
	private Point2D_I32 leftGoalTop;
	private Point2D_I32 leftGoalBottom;

	public static Polygon getRegion1() {
		return region1;
	}
	
	public static Polygon getRegion2() {
		return region2;
	}
	
	public static Polygon getRegion3() {
		return region3;
	}
	
	public static Polygon getRegion4() {
		return region4;
	}
	
	public static final PitchConstants newPitch = new PitchConstants(60,98,520,307,115,260,410,15,280,510,73,510,209,4,72,7,212);
	public static final PitchConstants oldPitch = new PitchConstants(69,58,535,292,105,255,405,15,280,498,72,503,212,19,77,13,212); //TODO : fill in

}
