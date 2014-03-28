/**
 * 
 */
package world.support;

import geometry.Vector;

/**
 * @author apljungquist
 *
 */
public class RateOfChangePredictor{

	
	/**
	 * Calculates the rate of change between two timed vectors. The vector in the
	 * result represents the rate of change per second. The time in the
	 * resulting timed vector represents the time at which this rate of change existed.
	 * This time is taken as the time of the ultimate timed vector.
	 * 
	 * @param ultimate
	 *            The later of the two timed vectors (e.g. the most recent position)
	 * @param penultimate
	 *            The earlier of the two timed vectors (e.g. some historical position)
	 * @return Returns a timed vector representing the rate of change at some point in
	 *         time.
	 */
	public static TimedVector findRateOfChange(TimedVector ultimate, TimedVector penultimate) {
		//Calculate the change in the vector and in the time.
		TimedVector delta = ultimate.subtract(penultimate);
		//Normalize the vector to represent the change over one second.
		Vector rateOfChange = delta.getVector().scalarMultiplication(1000.0 / delta.getTime());
		//Return a frame with the above rate of change as the vector and the time of the first given frame as time.
		return new TimedVector(rateOfChange, ultimate.getTime());
		
	}
	
	/**
	 * Predict the current value of a vector based on two previous values.
	 * 
	 * @param ultimate The last (or later) timed vector to use in the prediction.
	 * @param penultimate The second to last (or earlier) timed vector to use in the prediction.
	 * @return Returns a timed vector with the current time and the predicted value for that time.
	 */
	public static TimedVector findCurrent(TimedVector ultimate, TimedVector penultimate) {
		//Calculate the change in time since the last record.
		long deltaTime = System.currentTimeMillis()-ultimate.getTime();
		//Get the rate of change between to two last records.
		TimedVector rateOfChange = findRateOfChange(ultimate, penultimate);
		//Assuming the rate of change is constant, calculate the current value
		TimedVector current = ultimate.add(rateOfChange.scalarMultiplication(deltaTime/1000.0), deltaTime);
		return current;
	}
}
