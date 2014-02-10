package World;

import Calculations.*;

public class WorldState {

	private static final int NUM_FRAMES = 10;
	
	private AngleCalculator angleCalc = new AngleCalculator();
	private long counter;
	private int direction;
	private int color;
	private int pitch;
	
	private int greenX;
	private int greenY;
	
	// Buffers for smoothing positions/angles and calculating velocities
	private int[] blueXBuf = new int[NUM_FRAMES];
	private int[] blueYBuf = new int[NUM_FRAMES];
	private double[] blueOrientBuf = new double[NUM_FRAMES];

	private int[] yellowXBuf = new int[NUM_FRAMES];
	private int[] yellowYBuf = new int[NUM_FRAMES];
	private double[] yellowOrientBuf = new double[NUM_FRAMES];

	private int[] ballXBuf = new int[NUM_FRAMES];
	private int[] ballYBuf = new int[NUM_FRAMES];

	// Smoothed positions/angles
	private int blueX;
	private int blueY;
	private double blueOrient;

	private int yellowX;
	private int yellowY;
	private double yellowOrient;

	private int ballX;
	private int ballY;

	// Velocities
	private double blueVelX;
	private double blueVelY;

	private double yellowVelX;
	private double yellowVelY;

	private double ballVelX;
	private double ballVelY;

	private boolean weAreBlue = true;
	private boolean weAreOnLeft = true;
	private boolean mainPitch = true;
	private boolean blueHasBall = false;
	private boolean yellowHasBall = false;
	
}
