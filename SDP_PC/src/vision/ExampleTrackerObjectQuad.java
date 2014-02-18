package vision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import georegression.struct.shapes.Quadrilateral_F64;
import boofcv.abst.tracker.ConfigCirculantTracker;
import boofcv.abst.tracker.TrackerObjectQuad;
import boofcv.factory.tracker.FactoryTrackerObjectQuad;
import boofcv.gui.image.ShowImages;
import boofcv.gui.tracker.TrackerObjectQuadPanel;
import boofcv.io.MediaManager;
import boofcv.io.image.SimpleImageSequence;
import boofcv.io.wrapper.DefaultMediaManager;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.ImageUInt8;

/**
 * Demonstration on how to use the high level {@link TrackerObjectQuad} interface for tracking objects in a
 * video sequence.  This interface allows the target to be specified using an arbitrary quadrilateral.  Specific
 * implementations might not support that shape, so they instead will track an approximation of it.  The
 * interface also allows information on target visibility to be returned.  As is usually the case, tracker
 * specific information is lost in the high level interface and you should consider using the trackers
 * directly if more control is needed.
 *
 * This is an active area of research and all of the trackers eventually diverge given a long enough sequence.
 *
 * @author Peter Abeles
 */
public class ExampleTrackerObjectQuad {
 
	public static void main(String[] args) {
		MediaManager media = DefaultMediaManager.INSTANCE;
		String fileName = "test_images/sdp2013.mjpeg";
 
		SimpleImageSequence<ImageUInt8> video = media.openVideo(fileName, ImageType.single(ImageUInt8.class));
 
		// Create the tracker.  Comment/Uncomment to change the tracker.  Mean-shift trackers have been omitted
		// from the list since they use color information and including color images could clutter up the example.
		TrackerObjectQuad<ImageUInt8> tracker =
				FactoryTrackerObjectQuad.circulant(new ConfigCirculantTracker(), ImageUInt8.class);
//				FactoryTrackerObjectQuad.sparseFlow(new SfotConfig<ImageUInt8, ImageUInt16>(ImageUInt8.class));
//				FactoryTrackerObjectQuad.tld(new TldConfig<ImageUInt8, ImageUInt16>(true,ImageUInt8.class));
 
		// specify the target's initial location and initialize with the first frame
		Quadrilateral_F64 location = new Quadrilateral_F64(310,230,335,230,310,255,335,255);
		ImageUInt8 frame = video.next();
		tracker.initialize(frame,location);
 
		// For displaying the results
		TrackerObjectQuadPanel gui = new TrackerObjectQuadPanel(null);
		gui.setPreferredSize(new Dimension(frame.getWidth(),frame.getHeight()));
		gui.setBackGround((BufferedImage)video.getGuiImage());
		gui.setTarget(location,true);
		ShowImages.showWindow(gui,"Tracking Results");
 
		// Track the object across each video frame and display the results
		while( video.hasNext() ) {
			frame = video.next();
 
			boolean visible = tracker.process(frame,location);
 
			gui.setBackGround((BufferedImage) video.getGuiImage());
			gui.setTarget(location,visible);
			gui.repaint();
 
			BoofMiscOps.pause(200);
		}
	}
}