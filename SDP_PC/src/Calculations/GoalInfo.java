package Calculations;

import georegression.struct.point.Point2D_I32;
import java.awt.Color;
import java.awt.Graphics2D;
import vision.PitchConstants;

import vision.ObjectLocations;

public class GoalInfo {
	
	public final int width = 540;
	public final int height = 320;
	public final int goalWidth = 15;
	public final int goalRadius = 60;
	Point2D_I32 leftGoalCentre = getLeftGoalCenter();
	Point2D_I32 leftGoalTop = getLeftGoalTop();
	Point2D_I32 leftGoalBottom = getLeftGoalBottom();
	Point2D_I32 rightGoalCentre = getRightGoalCenter();
	Point2D_I32 rightGoalTop = getRightGoalTop();
	Point2D_I32 rightGoalBottom = getRightGoalBottom();
	
	PitchConstants pitchconstants = PitchConstants.newPitch;
	
	public GoalInfo(PitchConstants pitchconstants){
		this.pitchconstants = pitchconstants;
	}
	
	/**
	 * Gets the position of the left goal
	 *
	 * @return a Point2D_I32 object for the left goal
	 */
	public Point2D_I32 getLeftGoalCenter() {
		int middleY = height / 2;
		Point2D_I32 result = new Point2D_I32(goalWidth, middleY);
		return result;
	}

	/**
	 * Gets the position of the top of the left goal
	 *
	 * @return a Point2D_I32 object for the top of the left goal
	 */
	public Point2D_I32 getLeftGoalTop() {
		Point2D_I32 result = getLeftGoalCenter();
		result.setY(result.getY() - goalRadius);
		return result;
}
	
	/**
	 * Gets the position of the top of the left goal
	 *
	 * @return a Position object for the top of the left goal
	 */
	public Point2D_I32 getLeftGoalBottom() {
		Point2D_I32 result = getLeftGoalCenter();
		result.setY(result.getY() + goalRadius);
		return result;
	}

	/**
	 * Gets the position of the left goal
	 *
	 * @return a Point2D_I32 object for the right goal
	 */
	public Point2D_I32 getRightGoalCenter() {
		int middleY = height / 2;
		Point2D_I32 result = new Point2D_I32(width - goalWidth, middleY);
		return result;
	}

	/**
	 * Gets the position of the top of the left goal
	 *
	 * @return a Point2D_I32 object for the top of the left goal
	 */
	public Point2D_I32 getRightGoalTop() {
		Point2D_I32 result = getRightGoalCenter();
		result.setY(result.getY() - goalRadius);
		return result;
	}

	/**
	 * Gets the position of the top of the left goal
	 *
	 * @return a Point2D_I32 object for the top of the left goal
	 */
	public Point2D_I32 getRightGoalBottom() {
		Point2D_I32 result = getRightGoalCenter();
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
