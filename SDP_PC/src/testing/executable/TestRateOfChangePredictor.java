/**
 * 
 */
package testing.executable;

import static org.junit.Assert.*;

import org.junit.Test;

import world.support.RateOfChangePredictor;
import world.support.TimedVector;

/**
 * @author apljungquist
 *
 */
public class TestRateOfChangePredictor {

	@Test
	public void testFindRateOfChange() {
		long now = System.currentTimeMillis();
		TimedVector[] vectors = new TimedVector[6];
		vectors[0] = new TimedVector(0,0, now-5000);
		vectors[1] = new TimedVector(1,1, now-4000);
		vectors[2] = new TimedVector(3,3, now-3000);
		vectors[3] = new TimedVector(5,5, now-2000);
		vectors[4] = new TimedVector(5,5, now-1000);
		vectors[5] = new TimedVector(6,6, now-500);
		
		TimedVector[] rateOfChanges = new TimedVector[5];
		rateOfChanges[0] = new TimedVector(1,1, now-4000);
		rateOfChanges[1] = new TimedVector(2,2, now-3000);
		rateOfChanges[2] = new TimedVector(2,2, now-2000);
		rateOfChanges[3] = new TimedVector(0,0, now-1000);
		rateOfChanges[4] = new TimedVector(2,2, now-500);
		
		assertEquals(rateOfChanges[0], RateOfChangePredictor.findRateOfChange(vectors[1], vectors[0]));
		assertEquals(rateOfChanges[1], RateOfChangePredictor.findRateOfChange(vectors[2], vectors[1]));
		assertEquals(rateOfChanges[2], RateOfChangePredictor.findRateOfChange(vectors[3], vectors[2]));
		assertEquals(rateOfChanges[3], RateOfChangePredictor.findRateOfChange(vectors[4], vectors[3]));
		assertEquals(rateOfChanges[4], RateOfChangePredictor.findRateOfChange(vectors[5], vectors[4]));
	}
	
	@Test
	public void testFindCurrent() {
		long now = System.currentTimeMillis();
		TimedVector[] vectors = new TimedVector[6];
		vectors[0] = new TimedVector(0,0, now-5000);
		vectors[1] = new TimedVector(1,1, now-4000);
		vectors[2] = new TimedVector(3,3, now-3000);
		vectors[3] = new TimedVector(5,5, now-2000);
		vectors[4] = new TimedVector(5,5, now-1000);
		vectors[5] = new TimedVector(6,6, now-500);
		
		TimedVector[] currents = new TimedVector[5];
		currents[0] = new TimedVector(5,5, now);
		currents[1] = new TimedVector(9,9, now);
		currents[2] = new TimedVector(9,9, now);
		currents[3] = new TimedVector(5,5, now);
		currents[4] = new TimedVector(7,7, now);
		
		assertTrue(currents[0].approxEquals(RateOfChangePredictor.findCurrent(vectors[1], vectors[0])));
		assertTrue(currents[1].approxEquals(RateOfChangePredictor.findCurrent(vectors[2], vectors[1])));
		assertTrue(currents[2].approxEquals(RateOfChangePredictor.findCurrent(vectors[3], vectors[2])));
		assertTrue(currents[3].approxEquals(RateOfChangePredictor.findCurrent(vectors[4], vectors[3])));
		assertTrue(currents[4].approxEquals(RateOfChangePredictor.findCurrent(vectors[5], vectors[4])));
	}

}
