package Calculations;

import georegression.struct.point.Point2D_I32;
import java.awt.Color;
import java.awt.Graphics2D;
import vision.PitchConstants;

import vision.ObjectLocations;

public class GoalInfo {
	
	public final static int width = 540;
	public final static int height = 320;
	public final static int goalWidth = 15;
	public final static int goalRadius = 75;
	Point2D_I32 leftGoalCentre = getLeftGoalCenterNew();
	Point2D_I32 leftGoalTop = getLeftGoalTopNew();
	Point2D_I32 leftGoalBottom = getLeftGoalBottomNew();
	Point2D_I32 rightGoalCentre = getRightGoalCenterNew();
	Point2D_I32 rightGoalTop = getRightGoalTopNew();
	Point2D_I32 rightGoalBottom = getRightGoalBottomNew();
	
	PitchConstants pitchconstants = PitchConstants.newPitch;

	/**
	 * Gets the position of the left goal
	 *
	 * @return a Point2D_I32 object for the left goal
	 */
	public static Point2D_I32 getLeftGoalCenterNew() {
		int middleY = height / 2;
		Point2D_I32 result = new Point2D_I32(goalWidth, middleY);
		return result;
	}

	/**
	 * Gets the position of the top of the left goal
	 *
	 * @return a Point2D_I32 object for the top of the left goal
	 */
	public static Point2D_I32 getLeftGoalTopNew() {
		Point2D_I32 result = getLeftGoalCenterNew();
		result.setY(result.getY() - goalRadius);
		return result;
}
	
	/**
	 * Gets the position of the top of the left goal
	 *
	 * @return a Position object for the top of the left goal
	 */
	public static Point2D_I32 getLeftGoalBottomNew() {
		Point2D_I32 result = getLeftGoalCenterNew();
		result.setY(result.getY() + goalRadius);
		return result;
	}

	/**
	 * Gets the position of the left goal
	 *
	 * @return a Point2D_I32 object for the right goal
	 */
	public static Point2D_I32 getRightGoalCenterNew() {
		int middleY = height / 2;
		Point2D_I32 result = new Point2D_I32(width - goalWidth, middleY);
		return result;
	}

	/**
	 * Gets the position of the top of the left goal
	 *
	 * @return a Point2D_I32 object for the top of the left goal
	 */
	public static Point2D_I32 getRightGoalTopNew() {
		Point2D_I32 result = getRightGoalCenterNew();
		result.setY(result.getY() - goalRadius);
		return result;
	}

	/**
	 * Gets the position of the top of the left goal
	 *
	 * @return a Point2D_I32 object for the top of the left goal
	 */
	public static Point2D_I32 getRightGoalBottomNew() {
		Point2D_I32 result = getRightGoalCenterNew();
		result.setY(result.getY() + goalRadius);
		return result;
	}
	
//	public Point2D_I32 getBotRightCorner() {
//		Point2D_I32 result = new Point2D_I32(604, 400);
//
//		return result;
//	}
//	public Point2D_I32 getTopLeftCorner() {
//		Point2D_I32 result = new Point2D_I32(35, 92);
//
//		return result;
//	}
//	public Point2D_I32 getTopRightCorner() {
//		Point2D_I32 result = new Point2D_I32(601, 84);
//
//		return result;
//	}
//	public Point2D_I32 getBotLeftCorner() {
//		Point2D_I32 result = new Point2D_I32(35, 392);
//
//		return result;
//	}
	
	public void drawGoalLine(Graphics2D g) {

		Color c = g.getColor();
		g.setColor(Color.RED);

		g.drawLine(leftGoalCentre.getX()-10, leftGoalCentre.getY(), leftGoalCentre.getX()+10, leftGoalCentre.getY());
		g.drawLine(leftGoalTop.getX()-10, leftGoalTop.getY(), leftGoalTop.getX()+10, leftGoalTop.getY());
		g.drawLine(leftGoalBottom.getX()-10, leftGoalBottom.getY(), leftGoalBottom.getX()+10, leftGoalBottom.getY());

		g.drawLine(rightGoalCentre.getX()-10, rightGoalCentre.getY(), rightGoalCentre.getX()+10, rightGoalCentre.getY());
		g.drawLine(rightGoalTop.getX()-10, rightGoalTop.getY(), rightGoalTop.getX()+10, rightGoalTop.getY());
		g.drawLine(rightGoalBottom.getX()-10, rightGoalBottom.getY(), rightGoalBottom.getX()+10, rightGoalBottom.getY());
		
		g.setColor(c);

	}
}
