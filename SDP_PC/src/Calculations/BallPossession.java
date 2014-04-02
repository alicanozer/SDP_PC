package Calculations;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import georegression.struct.point.Point2D_I32;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import world.RobotType;

public class BallPossession {
	
	public static boolean hasPossession(RobotType type, Point2D_I32 robotMarker) {
		
		Polygon polygon = null;
		
		if (ObjectLocations.getYellowDefendingLeft()) {
			if (ObjectLocations.getYellowUs()) {
					if (type == RobotType.AttackUs) {
						polygon = PitchConstants.getRegion3();
					} else if (type == RobotType.AttackThem) {
						polygon = PitchConstants.getRegion2();
					} else if (type == RobotType.DefendUs) {
						polygon = PitchConstants.getRegion1();
					} else if (type == RobotType.DefendThem) {
						polygon = PitchConstants.getRegion4();
					}			
			} else {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion2();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion3();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion4();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion1();
				}
			}
		} else {
			if (ObjectLocations.getYellowUs()) {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion2();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion3();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion4();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion1();
				}			
			} else {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion3();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion2();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion1();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion4();
				}
			}
		}

		boolean possession = false;
		
		Point2D_I32 ball = ObjectLocations.getBall();
				
//		try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		while(ball == null) {
			ball = ObjectLocations.getBall();
		}
		boolean BallRegion = BallRegion(ball, polygon);
		
		//if the robot is near the ball and facing the ball it has possession
//		System.out.println("ball speed" + ObjectLocations.getBallSpeed());
		if (BallRegion) {
			if (ObjectLocations.getBallSpeed() < 100) { 
				possession = true;
			} 
		}
		
		return possession;
	}

	public static boolean BallRegion(Point2D_I32 ball2D, Polygon polygon) {
		
		int x = ball2D.x;
		int y = ball2D.y;
		
		Point ball = new Point(x,y);
		
		if (polygon.contains(ball)) {
			return true;
		}
		
		return false;
		
	}

	public static boolean LegalMove(Point2D_I32 point, RobotType type) {

		Polygon polygon = null;
		
		if (ObjectLocations.getYellowDefendingLeft()) {
			if (ObjectLocations.getYellowUs()) {
					if (type == RobotType.AttackUs) {
						polygon = PitchConstants.getRegion3();
					} else if (type == RobotType.AttackThem) {
						polygon = PitchConstants.getRegion2();
					} else if (type == RobotType.DefendUs) {
						polygon = PitchConstants.getRegion1();
					} else if (type == RobotType.DefendThem) {
						polygon = PitchConstants.getRegion4();
					}			
			} else {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion2();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion3();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion4();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion1();
				}
			}
		} else {
			if (ObjectLocations.getYellowUs()) {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion2();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion3();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion4();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion1();
				}			
			} else {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion3();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion2();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion1();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion4();
				}
			}
		}
		
		int x = point.x;
		int y = point.y;
		
		Point legal = new Point(x,y);
		
		if (polygon.contains(legal)) {
			return true;
		}
		
		return false;
		
	}
	
}
