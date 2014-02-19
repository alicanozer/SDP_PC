package comms;

import geometry.Vector;
import georegression.struct.point.Point2D_I32;

import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.SwingUtilities;

import lejos.nxt.Button;
import strategy.movement.TurnToBall;
import strategy.movement.TurnToObject;
import vision.ObjectLocations;
import vision.PitchConstants;
import vision.PointUtils;
import vision.FrameHandler;
import vision.VisionRunner;
import Calculations.DistanceCalculator;
import World.Ball;
import World.Robot;
import World.RobotType;
import World.WorldState;

public class Test2 {

	static BluetoothRobot bRobot;
	public static final String HERCULES = "0016530D4ED8";
	private static Bluetooth connection;
	
	public static void main(String[] args) throws Exception {
		
		bRobot = new BluetoothRobot(RobotType.AttackUs, connection);
		bRobot.connect();
		bRobot.setSpeed(20);
		
		bRobot.rotateLEFT(90);
		
	}
	
	
		
}
