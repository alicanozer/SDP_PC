package strategy.planning;

import georegression.struct.point.Point2D_I32;
import Calculations.IntersectionLines;
import strategy.movement.MoveToPointXY;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import world.RobotType;
import movement.RobotMover;

public class DefendPassA extends StrategyInterface{

	public DefendPassA(RobotMover attackMover, RobotMover defenceMover) {
		super(attackMover, defenceMover);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {

		System.out.println("Starting DefendPassA (Defending against a pass with the attacker)...");
		
//		Point2D_I32 marker;
//		Point2D_I32 dot;
//		Point2D_I32 intersection;
//		Point2D_I32 opponentAttack;
//		Point2D_I32 opponentDefend;
//		
//		//Turn 90 degree to horizontal
//		double angltNinety = TurnToObject.alignHorizontal(RobotType.AttackUs);
//
//		//Calculate intersection between defender marker and attaker marker line with our robot
//		if (ObjectLocations.getYellowUs()) {
//			
//			opponentAttack = ObjectLocations.getBlueATTACKmarker();
//			opponentDefend = ObjectLocations.getBlueDEFENDmarker();
//
//			marker = ObjectLocations.getYellowATTACKmarker();
//			dot = ObjectLocations.getYellowATTACKdot(); 		
//									
//		} else {
//			
//			opponentAttack = ObjectLocations.getYellowATTACKmarker();
//			opponentDefend = ObjectLocations.getYellowDEFENDmarker();
//
//			marker = ObjectLocations.getBlueATTACKmarker();			
//			dot = ObjectLocations.getYellowATTACKdot(); 		
//			
//		}
//		
//		Point2D_I32 horizontal = new Point2D_I32(marker.x, marker.y + 500);
//		intersection = IntersectionLines.intersectLines(opponentAttack, opponentDefend, marker, horizontal);
//		
//		Point2D_I32 move = new Point2D_I32(marker.x, intersection.y);
//		
//		//Move to intersection point
//		MoveToPointXY.moveToPointXY("defence", attackMover, dot, marker, move);
		
	}

}




