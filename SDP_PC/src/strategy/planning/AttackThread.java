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
public class AttackThread implements Runnable {

	private String type;
	private RobotMover mover;
	private static boolean die = false;
	private static Object lock = new Object();
	public AttackThread(String type,RobotMover mover) {
		this.type = "attack";
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
			if(Math.random() < 0.05){
				try {
					System.out.println("Emergency movement kill attacker");
					mover.resetQueue(type);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				if (BallPossession.hasPossession(RobotType.AttackUs,
						ObjectLocations.getUSAttack())) {
					System.out.println("Attacker Attack Strategy");
					mover.resetQueue(type);
					ShootAttacker.shootAttacker(type, mover);
				} else if (BallPossession.hasPossession(RobotType.DefendUs,
						ObjectLocations.getUSDefend())) {
					System.out.println("Attacker Passing Strategy");
					mover.resetQueue(type);
					//PassingDefender.passingAttacker(type, mover);
				} else if (BallPossession.hasPossession(
						RobotType.AttackThem,
						ObjectLocations.getTHEMAttack())) {
					mover.resetQueue(type);
					System.out.println("Attacker Block Shot Strategy");
				} else if (BallPossession.hasPossession(
						RobotType.DefendThem,
						ObjectLocations.getTHEMDefend())) {
					mover.resetQueue(type);
					System.out.println("Attacker Block Pass Strategy");
				} else {
					System.out.println("Attacker Intercept");
					mover.resetQueue(type);
					InterceptBall.intercept(type, mover);
				}
				if(getDie()){
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}



