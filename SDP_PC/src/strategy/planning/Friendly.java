package strategy.planning;
/**
 * Strategy for our first friendly. Still an outline.
 */

import movement.RobotMover;
import vision.ObjectLocations;
import vision.PitchConstants;
import Calculations.BallPossession;
import World.RobotType;
import comms.BluetoothRobot;

public class Friendly extends StrategyInterface{

	enum state {
		//TODO add different strategy CLASSES e.g. Defending, Attacking, MoveToBall?
		PassA, PassD, InterceptBall, AttackA, AttackD, DefendPassA, DefendPassD, DefendShootA, DefendShootD
	}

	state currentStateARobot = state.InterceptBall;
	private state newStateARobot = currentStateARobot;
	
	state currentStateDRobot = state.InterceptBall;
	private state newStateDRobot = currentStateDRobot;

	public Friendly(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
			
			// TODO Auto-generated method stub
			StrategyInterface activeAStrat;
			StrategyInterface activeDStrat;
			Thread strategyAThread;
			Thread strategyDThread;
			activeAStrat = new InterceptBall(attackMover, defenceMover);
			activeDStrat = new InterceptBall(attackMover, defenceMover);
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
						System.out.println("blue defender has possession");
						newStateDRobot = state.DefendPassD;
						newStateARobot = state.DefendPassA;
					} else if (BallPossession.hasPossession(RobotType.DefendUs, ObjectLocations.getYellowDEFENDmarker())) {
						//PASSING STRATEGY!
						System.out.println("yellow defender has possession");
						newStateDRobot = state.PassD;
						newStateARobot = state.PassA;
					} else if (BallPossession.hasPossession(RobotType.AttackUs, ObjectLocations.getYellowATTACKmarker())) {
						//SHOOTING STRATEGY!
						System.out.println("yellow attacker has possession");
						newStateDRobot = state.AttackD;
						newStateARobot = state.AttackA;
					} else if (BallPossession.hasPossession(RobotType.AttackThem, ObjectLocations.getBlueATTACKmarker())) {
						//DEFENCE STRATEGY!
						System.out.println("blue attacker has possession");
						newStateDRobot = state.DefendShootD;
						newStateARobot = state.DefendShootA;
					} else {
						//When no-one has the ball, INTERCEPT!!
//						newStateDRobot = state.InterceptBall;
//						newStateARobot = state.InterceptBall;
//						System.out.println("InterceptBall");
					}
				} else {
					if (BallPossession.hasPossession(RobotType.DefendThem, ObjectLocations.getYellowDEFENDmarker())) {
						//DEFENCE STRATEGY!
						newStateDRobot = state.DefendPassD;
						newStateARobot = state.DefendPassA;
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
						newStateDRobot = state.DefendShootD;
						newStateARobot = state.DefendShootA;
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
						activeAStrat = new InterceptBall(attackMover, defenceMover);
						strategyAThread = new Thread(activeAStrat,
								"Move to ball Thread");
						System.out.println("Intercept Ball Started");
					} else if (currentStateARobot == state.DefendPassA) {
						activeAStrat = new DefendPassA(attackMover, defenceMover);
						strategyAThread = new Thread(activeAStrat, "Defense Thread");
						System.out.println("Defense thread started.");
					} else if (currentStateARobot == state.AttackA) {
						activeAStrat = new AttackA(attackMover, defenceMover);
						strategyAThread = new Thread(activeAStrat, "Offense Thread");
					} else if (currentStateARobot == state.PassA) {
						System.out.println("Passing.");
						activeAStrat = new PassA(attackMover, defenceMover);
						strategyAThread = new Thread(activeAStrat,
								"Passing A");
					} else if (currentStateARobot == state.DefendShootA) {
						System.out.println("Defending.");
						activeAStrat = new DefendShootA(attackMover, defenceMover);
						strategyAThread = new Thread(activeAStrat,
								"Defending A");
					}
					if (currentStateDRobot == state.InterceptBall) {
						activeDStrat = new InterceptBall(attackMover, defenceMover);
						strategyDThread = new Thread(activeDStrat,
								"Move to ball Thread");
						System.out.println("Intercept Ball Started");
					} else if (currentStateDRobot == state.DefendPassD) {
						activeDStrat = new DefendPassD(attackMover, defenceMover);
						strategyDThread = new Thread(activeDStrat, "Defense Thread");
						System.out.println("Defense thread started.");
					} else if (currentStateDRobot == state.AttackD) {
						activeDStrat = new AttackD(attackMover, defenceMover);
						strategyDThread = new Thread(activeDStrat, "Offense Thread");
					} else if (currentStateDRobot == state.PassD) {
						System.out.println("Passing.");
						activeDStrat = new PassD(attackMover, defenceMover);
						strategyDThread = new Thread(activeDStrat,
								"Passing D");
					} else if (currentStateDRobot == state.DefendShootD) {
						System.out.println("Defending.");
						activeDStrat = new DefendShootD(attackMover, defenceMover);
						strategyDThread = new Thread(activeDStrat,
								"Defending D");
					}
					strategyAThread.start();
					strategyDThread.start();
				}
			}	
		}
	
}
