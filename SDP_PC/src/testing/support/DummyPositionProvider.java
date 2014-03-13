package testing.support;

import world.PositionProvider;
import world.support.TimedVector;

public class DummyPositionProvider implements PositionProvider {

	TimedVector[] positions;
	long currentFrame;
	
	public DummyPositionProvider() {
		positions = new TimedVector[9];
		positions[0] = new TimedVector(0, 0, 0);
		positions[1] = new TimedVector(1, 1, 40);
		positions[2] = new TimedVector(2, 2, 80);
		positions[3] = new TimedVector(4, 4, 160);
		positions[4] = new TimedVector(6, 6, 200);
		positions[5] = new TimedVector(14, 14, 1000);
		positions[6] = new TimedVector(15, 15, 1000);
		positions[7] = new TimedVector(15, 15, 1001);
		positions[8] = new TimedVector(16, 16, 1000);
		
		currentFrame = 0;
	}
	
	public void step() {
		currentFrame++;
	}

	private TimedVector getPosition(long frameNumber) throws Exception{
		if (frameNumber >= 0 && frameNumber < positions.length) {
			return positions[(int) frameNumber];
		}else {
			throw new Exception("Frame not in history");
		}
	}
	
	@Override
	public TimedVector getRobot0Position(long frameNumber) throws Exception {
		return getPosition(frameNumber);
	}

	@Override
	public TimedVector getRobot1Position(long frameNumber) throws Exception {
		return getPosition(frameNumber);
	}

	@Override
	public TimedVector getRobot2Position(long frameNumber) throws Exception {
		return getPosition(frameNumber);
	}

	@Override
	public TimedVector getRobot3Position(long frameNumber) throws Exception {
		return getPosition(frameNumber);
	}

	@Override
	public TimedVector getBallPosition(long frameNumber) throws Exception {
		return getPosition(frameNumber);
	}

	@Override
	public long getCurrentFrameNumber() {
		return currentFrame;
	}

}
