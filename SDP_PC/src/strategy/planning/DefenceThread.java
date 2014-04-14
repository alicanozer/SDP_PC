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
				} else if (BallPossession.hasPossession(RobotType.DefendUs,
						ObjectLocations.getUSDefend())) {
					System.out.println("Passing Strategy");
					PassingDefender.passingDefender(type, mover);
				} else if (BallPossession.hasPossession(RobotType.AttackThem, ObjectLocations.getTHEMAttack())) {
					System.out.println("Block Shot Strategy");
				} else if (BallPossession.hasPossession(
						RobotType.DefendThem,
						ObjectLocations.getTHEMDefend())) {
					System.out.println("Block Pass Strategy");
				} else {
					// Intercept Strategy
					InterceptBall.intercept("defence", mover);
					System.out.println("Intercept");

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



