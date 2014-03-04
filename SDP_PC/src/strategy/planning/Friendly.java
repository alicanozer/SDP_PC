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
		//TODO add different strategy CLASSES e.g. Defending, Attacking, MoveToBall?
		DefendA, DefendD, PassA, PassD, InterceptBall, AttackA, AttackD, Defend1A, Defend1D,
	}

	state currentStateARobot = state.InterceptBall;
	private state newStateARobot = currentStateARobot;
	
	state currentStateDRobot = state.InterceptBall;
	private state newStateDRobot = currentStateDRobot;

	public Friendly(BluetoothRobot attackRobot, BluetoothRobot defenceRobot) {
		super(attackRobot, defenceRobot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		StrategyInterface activeAStrat;
		StrategyInterface activeDStrat;
		Thread strategyAThread;
		Thread strategyDThread;
		activeAStrat = new InterceptBall(attackRobot, defenceRobot);
		activeDStrat = new InterceptBall(attackRobot, defenceRobot);
		strategyAThread = new Thread((InterceptBall) activeAStrat,
				"Move to ball Thread");
		strategyDThread = new Thread((InterceptBall) activeDStrat,
				"Move to ball Thread");
		strategyAThread.start();
		strategyDThread.start();

		while (!Strategy.alldie && !shouldidie) {
			//if ball in their attack zone then DEFEND
			//else normal defence?
			if (ObjectLocations.getYellowUs()) {
				if (BallPossession.hasPossession(RobotType.DefendThem, ObjectLocations.getBlueDEFENDmarker())) {
					//DEFENCE STRATEGY!
					System.out.println("We are in their defending zone");
					newStateDRobot = state.DefendD;
					newStateARobot = state.DefendA;
				} else if (BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getYellowDEFENDmarker())) {
					//PASSING STRATEGY!
					//newStateARobot = state.Pass
					System.out.println("We are in out defending zone");
					newStateDRobot = state.PassD;
					newStateARobot = state.PassA;
				} else if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getYellowATTACKmarker())) {
					//SHOOTING STRATEGY!
					System.out.println("We are in our attack zone");
					newStateDRobot = state.AttackD;
					newStateARobot = state.AttackA;
				} else if (BallPossession.hasPossession(RobotType.AttackThem, ObjectLocations.getBlueATTACKmarker())) {
					//DEFENCE STRATEGY!
					System.out.println("We are in their attacking zone");
					newStateDRobot = state.Defend1D;
					newStateARobot = state.Defend1A;
				} else {
					//When no-one has the ball, INTERCEPT!!
					System.out.println("no one has ball");
					newStateDRobot = state.InterceptBall;
					newStateARobot = state.InterceptBall;
				}
			} else {
				if (BallPossession.hasPossession(RobotType.DefendThem, ObjectLocations.getYellowDEFENDmarker())) {
					//DEFENCE STRATEGY!
					newStateDRobot = state.DefendD;
					newStateARobot = state.DefendA;
				} else if (BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getBlueDEFENDmarker())) {
					//PASSING STRATEGY!
					newStateDRobot = state.PassD;
					newStateARobot = state.PassA;
				} else if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getBlueATTACKmarker())) {
					//SHOOTING STRATEGY!
					newStateDRobot = state.AttackD;
					newStateARobot = state.AttackA;
				} else if (BallPossession.hasPossession(RobotType.AttackThem, ObjectLocations.getYellowATTACKmarker())) {
					//DEFENCE STRATEGY!
					newStateDRobot = state.Defend1D;
					newStateARobot = state.Defend1A;
				} else {
					//When no-one has the ball, INTERCEPT!!
					newStateDRobot = state.InterceptBall;
					newStateARobot = state.InterceptBall;
				}
			}
			
			// If state did not change, do nothing
			// Otherwise kill old strategy and start new one
			if (currentStateARobot != newStateARobot && currentStateDRobot != newStateDRobot) {
				currentStateARobot = newStateARobot;
				currentStateDRobot = newStateDRobot;
				
				if (strategyAThread.isAlive()) {
					activeAStrat.kill();
				}
				if (strategyDThread.isAlive()) {
					activeDStrat.kill();
				}
				if (currentStateARobot == state.InterceptBall) {
					activeAStrat = new InterceptBall(attackRobot, defenceRobot);
					strategyAThread = new Thread(activeAStrat,
							"Move to ball Thread");
					System.out.println("Intercept Ball Started");
				} else if (currentStateARobot == state.DefendA) {
					activeAStrat = new DefendA(attackRobot, defenceRobot);
					strategyAThread = new Thread(activeAStrat, "Defense Thread");
					System.out.println("Defense thread started.");
				} else if (currentStateARobot == state.AttackA) {
					activeAStrat = new AttackA(attackRobot, defenceRobot);
					strategyAThread = new Thread(activeAStrat, "Offense Thread");
				} else if (currentStateARobot == state.PassA) {
					System.out.println("Passing.");
					activeAStrat = new PassA(attackRobot, defenceRobot);
					strategyAThread = new Thread(activeAStrat,
							"Passing A");
				} else if (currentStateARobot == state.Defend1A) {
					System.out.println("Defending.");
					activeAStrat = new Defend1A(attackRobot, defenceRobot);
					strategyAThread = new Thread(activeAStrat,
							"Defending A");
				}
				if (currentStateDRobot == state.InterceptBall) {
					activeDStrat = new InterceptBall(attackRobot, defenceRobot);
					strategyDThread = new Thread(activeDStrat,
							"Move to ball Thread");
					System.out.println("Intercept Ball Started");
				} else if (currentStateDRobot == state.DefendD) {
					activeDStrat = new DefendD(attackRobot, defenceRobot);
					strategyDThread = new Thread(activeDStrat, "Defense Thread");
					System.out.println("Defense thread started.");
				} else if (currentStateDRobot == state.AttackD) {
					activeDStrat = new AttackD(attackRobot, defenceRobot);
					strategyDThread = new Thread(activeDStrat, "Offense Thread");
				} else if (currentStateDRobot == state.PassD) {
					System.out.println("Passing.");
					activeDStrat = new PassD(attackRobot, defenceRobot);
					strategyDThread = new Thread(activeDStrat,
							"Passing D");
				} else if (currentStateDRobot == state.Defend1D) {
					System.out.println("Defending.");
					activeDStrat = new Defend1D(attackRobot, defenceRobot);
					strategyDThread = new Thread(activeDStrat,
							"Defending D");
				}
				strategyAThread.start();
				strategyDThread.start();
			}
		}	

	}
}
