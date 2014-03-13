package world.object;

import world.World;

public class Zones {

	public enum Pitch {
		ONE, TWO
	};

	public static Zone zone(Pitch pitch, int zone) {
		switch (pitch) {
		case ONE:
			switch (zone) {
			case 0:
				return zone1_0;
			case 1:
				return zone1_1;
			case 2:
				return zone1_2;
			case 3:
				return zone1_3;
			}
			break;
		case TWO:
			switch (zone) {
			case 0:
				return zone2_0;
			case 1:
				return zone2_1;
			case 2:
				return zone2_2;
			case 3:
				return zone2_3;
			}
			break;

		default:
			break;
		}
		return null;
	}

	private static Zone zone1_0 = new Zone(
			new int[] { 97, 170, 165,  95,  60,  60 }, 
			new int[] { 85,  85, 365, 360, 290, 150 }, 6, World.REAL_UNITS_PER_PIXEL);
	private static Zone zone1_1 = new Zone(
			new int[] {170, 318, 318, 165}, 
			new int[] { 85,  80, 365, 365}, 4, World.REAL_UNITS_PER_PIXEL);
	private static Zone zone1_2 = new Zone(
			new int[] {318, 465, 465, 316}, 
			new int[] { 80,  84, 365, 367}, 4, World.REAL_UNITS_PER_PIXEL);
	private static Zone zone1_3 = new Zone(
			new int[] {465, 536, 572, 576, 534, 465,}, 
			new int[] { 84,  86, 152, 292, 363, 366}, 6, World.REAL_UNITS_PER_PIXEL);

	private static Zone zone2_0 = new Zone(
			new int[] {101, 180, 177, 102,  59,  57}, 
			new int[] { 90,  87, 383, 381, 315, 158}, 6, World.REAL_UNITS_PER_PIXEL);
	private static Zone zone2_1 = new Zone(
			new int[] {180, 330, 325, 177}, 
			new int[] { 87,  90, 385, 383}, 4, World.REAL_UNITS_PER_PIXEL);
	private static Zone zone2_2 = new Zone(
			new int[] {330, 475, 468, 325}, 
			new int[] { 90,  97, 383, 385}, 4, World.REAL_UNITS_PER_PIXEL);
	private static Zone zone2_3 = new Zone(
			new int[] {475, 547, 583, 578, 534, 468}, 
			new int[] { 97, 101, 171, 316, 380, 383}, 6, World.REAL_UNITS_PER_PIXEL);
}
