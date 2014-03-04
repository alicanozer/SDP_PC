package strategy.planning;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;
import Calculations.DistanceCalculator;
import Calculations.Intersection;
import World.RobotType;
import strategy.movement.Facing;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import movement.RobotMover;

public class PassA extends StrategyInterface {

	public PassA(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {

		System.out.println("Running Pass Attacker Strategy...");

		// Turn 90 degree to horizontal
		double angleNinety = TurnToObject.alignHorizontal(RobotType.AttackUs);
		attackMover.rotate("attack", angleNinety);
		
		try {
			
			// Check if the defender is facing attacker
			if (Facing.FacingTeammateRobot(RobotType.DefendUs)) {
				// Move to point of intersection between defender and attacker
				Vector intersection = Intersection.IntersectionRobots(ObjectLocations.getYellowATTACKmarker(), ObjectLocations.getYellowATTACKdot(), ObjectLocations.getYellowDEFENDmarker(), ObjectLocations.getYellowDEFENDdot());
				double distance = DistanceCalculator.Distance(ObjectLocations.getYellowATTACKmarker(), new Point2D_I32(ObjectLocations.getYellowATTACKmarker().x, (int) intersection.getY()));
				if (intersection.getY() > ObjectLocations.getYellowATTACKmarker().y) {
					attackMover.forward("attack", distance);
				} else {
					attackMover.backward("attack", distance);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
