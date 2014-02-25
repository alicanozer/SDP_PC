package World;

import geometry.Vector;
import vision.ObjectLocations;
import Calculations.AngleCalculator;
import World.Robot;
import World.Ball;
import World.RobotType;
import Calculations.*;

public class WorldState {

	public final GoalInfo goalInfo;
	
	public Ball ball = new Ball();
	public Robot theirAttackRobot = new Robot(RobotType.AttackThem);
	public Robot theirDefenseRobot = new Robot(RobotType.DefendThem);
	public Robot ourAttackRobot = new Robot(RobotType.AttackUs);
	public Robot ourDefenseRobot = new Robot(RobotType.DefendUs);
	
	public WorldState(GoalInfo goalInfo) {
		this.goalInfo = goalInfo;
		}
	

	public Ball getBall() {
		return this.ball;
	}
	
	public void setBall() {
		this.ball.setPosition(new Vector(ObjectLocations.getBall().x,ObjectLocations.getBall().y));
	}
	
	public Robot getOurAttackRobot() {
		return this.ourAttackRobot;
	}
	
	public void setOurAttackRobot() {
		this.ourAttackRobot.setPosition(new Vector(ObjectLocations.getYellowATTACKmarker().x,ObjectLocations.getYellowATTACKmarker().y));
	}
	
	public Robot getOurDefenseRobot() {
		return this.ourAttackRobot;
	}
	
	public void setOurDefenseRobot() {
		this.ourAttackRobot.setPosition(new Vector(ObjectLocations.getYellowDEFENDmarker().x,ObjectLocations.getYellowDEFENDmarker().y));
	}
}
