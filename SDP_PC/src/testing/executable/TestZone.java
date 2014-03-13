package testing.executable;

import static org.junit.Assert.*;
import geometry.Vector;

import org.junit.Test;

import world.World;
import world.object.Zone;

public class TestZone {

	@Test
	public void test() {
		Zone zone = new Zone(
				new int[] {100,200,200,100, 50, 50}, 
				new int[] {100,100,380,380,330,150}, 6, World.REAL_UNITS_PER_PIXEL);
		
		//Center inside 135 degree point just inside (7.1 > 7.07)
		assertTrue(zone.containsCirclePixel(new Vector(75+7.1, 125+7.1), 10, 8));
		//Center inside 135 degree point just outside (7 < 7.07)
		assertFalse(zone.containsCirclePixel(new Vector(75+7, 125+7), 10, 8));
		//Center outside
		assertFalse(zone.containsCirclePixel(new Vector(75-1, 125-1), 10, 8));
		
	}

}
