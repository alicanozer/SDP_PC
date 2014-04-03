package Calculations;

import java.awt.Point;
import java.awt.Polygon;
import georegression.struct.point.Point2D_I32;
import vision.ObjectLocations;
import vision.PitchConstants;
import world.RobotType;

public class BallPossession {
	
	public static boolean hasPossession(RobotType type, Point2D_I32 robotMarker) {
		
		Polygon polygon = null;
		Point2D_I32 marker = null;
		double distance = 0.0;

		if (ObjectLocations.getYellowDefendingLeft()) {
			if (ObjectLocations.getYellowUs()) {
					if (type == RobotType.AttackUs) {
						polygon = PitchConstants.getRegion3();
						marker = ObjectLocations.getYellowATTACKmarker();
					} else if (type == RobotType.AttackThem) {
						polygon = PitchConstants.getRegion2();
						marker = ObjectLocations.getBlueATTACKmarker();
					} else if (type == RobotType.DefendUs) {
						polygon = PitchConstants.getRegion1();
						marker = ObjectLocations.getYellowDEFENDmarker();
					} else if (type == RobotType.DefendThem) {
						polygon = PitchConstants.getRegion4();
						marker = ObjectLocations.getBlueDEFENDmarker();
					}			
			} else {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion2();
					marker = ObjectLocations.getBlueATTACKmarker();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion3();
					marker = ObjectLocations.getYellowATTACKmarker();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion4();
					marker = ObjectLocations.getBlueDEFENDmarker();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion1();
					marker = ObjectLocations.getYellowDEFENDmarker();
				}
			}
		} else {
			if (ObjectLocations.getYellowUs()) {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion2();
					marker = ObjectLocations.getBlueATTACKmarker();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion3();
					marker = ObjectLocations.getYellowATTACKmarker();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion4();
					marker = ObjectLocations.getBlueDEFENDmarker();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion1();
					marker = ObjectLocations.getYellowDEFENDmarker();
				}			
			} else {
				if (type == RobotType.AttackUs) {
					polygon = PitchConstants.getRegion3();
					marker = ObjectLocations.getYellowATTACKmarker();
				} else if (type == RobotType.AttackThem) {
					polygon = PitchConstants.getRegion2();
					marker = ObjectLocations.getBlueATTACKmarker();
				} else if (type == RobotType.DefendUs) {
					polygon = PitchConstants.getRegion1();
					marker = ObjectLocations.getYellowDEFENDmarker();
				} else if (type == RobotType.DefendThem) {
					polygon = PitchConstants.getRegion4();
					marker = ObjectLocations.getBlueDEFENDmarker();
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
		
		if (ball != null && marker != null) {
			distance = DistanceCalculator.Distance(ball, marker);
		}
		
		//if the robot is near the ball and facing the ball it has possession
//		System.out.println("ball speed" + ObjectLocations.getBallSpeed());
		if (BallRegion) {
//			if (distance != 0.0) {
//				if (distance < 16) { 
//					possession = true;
//				}
//			} else {
				if (ObjectLocations.getBallSpeed() < 150) {
					possession = true;
				}
//			}
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
