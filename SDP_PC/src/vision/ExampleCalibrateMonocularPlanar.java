package vision;

import java.awt.image.BufferedImage;
import java.util.List;

import boofcv.abst.calib.CalibrateMonoPlanar;
import boofcv.abst.calib.ConfigChessboard;
import boofcv.abst.calib.ConfigSquareGrid;
import boofcv.abst.calib.PlanarCalibrationDetector;
import boofcv.alg.geo.calibration.PlanarCalibrationTarget;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.calib.FactoryPlanarCalibrationTarget;
import boofcv.io.image.UtilImageIO;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.calib.IntrinsicParameters;
import boofcv.struct.image.ImageFloat32;





/**
 * Example of how to calibrate a single (monocular) camera using a high level interface that processes images of planar
 * calibration targets.  The entire calibration target must be observable in the image and for best results images
 * should be in focus and not blurred.  For a lower level example of camera calibration which processes a set of
 * observed calibration points see {@link ExampleCalibrateMonocularPlanar}.
 *
 * After processing both intrinsic camera parameters and lens distortion are estimated.  Square grid and chessboard
 * targets are demonstrated by this example. See calibration tutorial for a discussion of different target types
 * and how to collect good calibration images.
 *
 * All the image processing and calibration is taken care of inside of {@link CalibrateMonoPlanar}.  The code below
 * loads calibration images as inputs, calibrates, and saves results to an XML file.  See in code comments for tuning
 * and implementation issues.
 *
 * @see CalibrateMonoPlanar
 *
 * @author Peter Abeles
 */
public class ExampleCalibrateMonocularPlanar {
 
	// Detects the target and calibration point inside the target
	PlanarCalibrationDetector detector;
 
	// Description of the target's physical dimension
	PlanarCalibrationTarget target;
 
	// List of calibration images
	List<String> images;
 
	// Many 3D operations assumed a right handed coordinate system with +Z pointing out of the image.
	// If the image coordinate system is left handed then the y-axis needs to be flipped to meet
	// that requirement.  Most of the time this is false.
	boolean flipY;
 
	/**
	 * Images from Zhang's website.  Square grid pattern.
	 */
	private void setupZhang99() {
		// Use the wrapper below for square grid targets.
		detector = FactoryPlanarCalibrationTarget.detectorChessboard(new ConfigChessboard(7,10));
 
		// physical description
		target = FactoryPlanarCalibrationTarget.gridChess(7, 10, 40);
 
		// load image list
		String directory = "test_images_checkered/";
		images = BoofMiscOps.directoryList(directory,"00");
 
		// standard image format
		flipY = false;
	}
 
	/**
	 * Images collected from a Bumblee Bee stereo camera.  Large amounts of radial distortion. Chessboard pattern.
	 */
	private void setupBumbleBee() {
		// Use the wrapper below for chessboard targets.  The last parameter adjusts the size of the corner detection
		// region.  TUNE THIS PARAMETER FOR OPTIMAL ACCURACY!
		detector = FactoryPlanarCalibrationTarget.detectorChessboard(new ConfigChessboard(5,7));
 
		// physical description
		target = FactoryPlanarCalibrationTarget.gridChess(5, 7, 30);
 
		// load image list
		String directory = "test_images/00000001.jpg";
		images = BoofMiscOps.directoryList(directory,"00");
 
		// standard image format
		flipY = false;
	}
 
	/**
	 * Process calibration images, compute intrinsic parameters, save to a file
	 */
	public void process() {
 
		// Declare and setup the calibration algorithm
		CalibrateMonoPlanar calibrationAlg = new CalibrateMonoPlanar(detector, flipY);
 
		// tell it type type of target and which parameters to estimate
		calibrationAlg.configure(target, true, 2);
 
		for( String n : images ) {
			BufferedImage input = UtilImageIO.loadImage(n);
			if( n != null ) {
				ImageFloat32 image = ConvertBufferedImage.convertFrom(input,(ImageFloat32)null);
				if( !calibrationAlg.addImage(image) )
					System.err.println("Failed to detect target in "+n);
			}
		}
		// process and compute intrinsic parameters
		IntrinsicParameters intrinsic = calibrationAlg.process();
 
		// save results to a file and print out
		BoofMiscOps.saveXML(intrinsic, "intrinsic.xml");
 
		calibrationAlg.printStatistics();
		System.out.println();
		System.out.println("--- Intrinsic Parameters ---");
		System.out.println();
		intrinsic.print();
	}
 
 
	public static void main( String args[] ) {
		ExampleCalibrateMonocularPlanar alg = new ExampleCalibrateMonocularPlanar();
 
		// which target should it process
		alg.setupZhang99();
		//alg.setupBumbleBee();
 
		// compute and save results
		alg.process();
	}
}
