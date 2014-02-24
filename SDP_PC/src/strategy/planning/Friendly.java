package strategy.planning;
/**
 * Strategy for our first friendly. Still an outline.
 */

import Calculations.BallPossession;
import World.RobotType;
import World.WorldState;

import comms.BluetoothRobot;

public class Friendly extends StrategyInterface{

	enum state {
		//TODO add different strategy states e.g. Defending, Attacking, MoveToBall?
		Defend,
	}

	state currentState; // = MoveToBall
	private state newState = currentState;

	public Friendly(BluetoothRobot bRobot) {
		super(bRobot);
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
			if (BallPossession.hasPossession(RobotType.DefendThem)) {
				//STOP THE BALL!
				newState = state.Defend;
			}

			//if ball in our defence possession then pass to attack
			if (BallPossession.hasPossession(RobotType.DefendUs)) {
				//PASS!
				//newstate = state.Pass
			}

			//if ball in our attack possession then shoot
			if (BallPossession.hasPossession(RobotType.AttackUs)) {
				//SHOOT! SCORE!
				//newstate = state.Shoot
			}

			// If state did not change, do nothing
			// Otherwise kill old strategy and start new one
			// TODO Add possible states
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
