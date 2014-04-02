/**
 * 
 */
package strategy.planning;

import vision.ObjectLocations;
import world.RobotType;
import Calculations.BallPossession;
import movement.RobotMover;

/**
 * @author s1101380
 *
 */
public class DefenceThread implements Runnable {

	private String type;
	private RobotMover mover;
	private static boolean die = false;
	private static Object lock = new Object();
	public DefenceThread(String type,RobotMover mover) {
		this.type = "defence";
		this.mover = mover;
		die = false;
	}
	public static void kill(){
		synchronized(lock){
			die = true;
		}
	}
	public static boolean getDie(){
		synchronized(lock){
			return die;
		}
	}
	@Override
	public void run() {
		// big while loop that never ends
		while (true) {
			try {
				if (BallPossession.hasPossession(RobotType.AttackUs,
						ObjectLocations.getUSAttack())) {
					System.out.println("Attack Strategy");
					System.out.println("Attack Strategy");
					mover.resetQueue(type);
				} else if (BallPossession.hasPossession(RobotType.DefendUs,
						ObjectLocations.getUSDefend())) {
					System.out.println("Passing Strategy");
					System.out.println("Passing Strategy");
					mover.resetQueue(type);
					PassingDefender.passingDefender(type, mover);
				} else if (BallPossession.hasPossession(
						RobotType.AttackThem,
						ObjectLocations.getTHEMAttack())) {
					mover.resetQueue(type);
					System.out.println("Block Shot Strategy");
					System.out.println("Block Shot Strategy");
				} else if (BallPossession.hasPossession(
						RobotType.DefendThem,
						ObjectLocations.getTHEMDefend())) {
					mover.resetQueue(type);
					System.out.println("Block Pass Strategy");
					System.out.println("Block Pass Strategy");
				} else {
					// Intercept Strategy
					System.out.println("Intercept");
					System.out.println("Intercept");
					System.out.println("Intercept");
					mover.resetQueue(type);
					InterceptBall.intercept("defence", mover);
					//InterceptBall.intercept("attack",attackMover);
				}
				if(getDie()){
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}



