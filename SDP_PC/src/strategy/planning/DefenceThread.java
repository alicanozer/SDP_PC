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
			if(Math.random() < 0.05){
				try {
					System.out.println("Emergency movement kill defender");
					mover.resetQueue(type);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				if (BallPossession.hasPossession(RobotType.AttackUs,
						ObjectLocations.getUSAttack())) {
					System.out.println("Defender Attack Strategy");
					mover.resetQueue(type);
				} else if (BallPossession.hasPossession(RobotType.DefendUs,
						ObjectLocations.getUSDefend())) {
					System.out.println("Defender Passing Strategy");
					mover.resetQueue(type);
					PassingDefender.passingDefender(type, mover);
				} else if (BallPossession.hasPossession(
						RobotType.AttackThem,
						ObjectLocations.getTHEMAttack())) {
					mover.resetQueue(type);
					BlockShot.blockShot(type, mover);
					System.out.println("Defender Block Shot Strategy");
				} else if (BallPossession.hasPossession(
						RobotType.DefendThem,
						ObjectLocations.getTHEMDefend())) {
					mover.resetQueue(type);
					System.out.println("Defender Block Pass Strategy");
				} else {
					// Intercept Strategy
					System.out.println("Defender Intercept");
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



