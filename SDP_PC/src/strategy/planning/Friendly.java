package strategy.planning;
/**
 * Strategy for our first friendly. Still an outline.
 */

import vision.ObjectLocations;
import Calculations.BallPossession;
import World.RobotType;
import comms.BluetoothRobot;

public class Friendly extends StrategyInterface{

	enum state {
		//TODO add different strategy states e.g. Defending, Attacking, MoveToBall?
		Defend, Attack, Passing, 
	}

	state currentState; // = MoveToBall
	private state newState = currentState;

	public Friendly(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		StrategyInterface activeStrat = null;
		Thread strategyThread = null;
		// set activeStrat to InterceptBall
		// strategyThread = Thread((InterceptBall) activeStrat);
		strategyThread.start();

		while (!Strategy.alldie && !shouldidie) {

			//if no one has the ball then try and get the ball

			//if ball in their attack zone then DEFEND
			//else normal defence?
			if (ObjectLocations.getYellowUs()) {
				if (BallPossession.hasPossession(RobotType.DefendThem, ObjectLocations.getBlueDEFENDmarker())) {
					//DEFENCE STRATEGY!
					newState = state.Defend;
				} else if (BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getYellowDEFENDmarker())) {
					//PASSING STRATEGY!
				} else if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getYellowATTACKmarker())) {
					//SHOOTING STRATEGY!
				} else if (BallPossession.hasPossession(RobotType.AttackThem, ObjectLocations.getBlueATTACKmarker())) {
					//DEFENCE STRATEGY!
				} else {
					//When no-one has the ball, INTERCEPT!!
				}
			} else {
				if (BallPossession.hasPossession(RobotType.DefendThem, ObjectLocations.getYellowDEFENDmarker())) {
					//DEFENCE STRATEGY!
					newState = state.Defend;
				} else if (BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getBlueDEFENDmarker())) {
					//PASSING STRATEGY!
				} else if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getBlueATTACKmarker())) {
					//SHOOTING STRATEGY!
				} else if (BallPossession.hasPossession(RobotType.AttackThem, ObjectLocations.getYellowATTACKmarker())) {
					//DEFENCE STRATEGY!
				} else {
					//When no-one has the ball, INTERCEPT!!
				}
			}
			
			// If state did not change, do nothing
			// Otherwise kill old strategy and start new one
			if (currentState != newState) {
				currentState = newState;
				if (strategyThread.isAlive()) {
					activeStrat.kill();
				}
				strategyThread.start();
			}

		}	

	}
}
