package vision;

import georegression.metric.UtilAngle;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import boofcv.alg.color.ColorHsv;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.MultiSpectral;

/**
 * Example which demonstrates how color can be used to segment an image.  The color space is converted from RGB into
 * HSV.  HSV separates intensity from color and allows you to search for a specific color based on two values
 * independent of lighting conditions.  Other color spaces are supported, such as YUV.
 *
 * @author Peter Abeles
 */
public class ExampleSegmentColor {
 
	/**
	 * Shows a color image and allows the user to select a pixel, convert it to HSV, print
	 * the HSV values, and calls the function below to display similar pixels.
	 */
	public static void printClickedColor( final BufferedImage image, final float dist ) {
		ImagePanel gui = new ImagePanel(image);
		gui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				float[] color = new float[3];
				int rgb = image.getRGB(e.getX(),e.getY());
				ColorHsv.rgbToHsv((rgb >> 16) & 0xFF,(rgb >> 8) & 0xFF , rgb&0xFF,color);
				System.out.println("h = " + color[0]);
				System.out.println("s = "+color[1]);
				System.out.println("v = "+color[2]);
 
				showSelectedColor("Selected",image,(float)color[0],(float)color[1],dist);
			}
		});
 
		ShowImages.showWindow(gui,"Color Selector");
	}
 
	/**
	 * Selectively displays only pixels which have a similar hue and saturation values to what is provided.
	 * This is intended to be a simple example of color based segmentation.  Color based segmentation can be done
	 * in RGB color, but is more problematic.  More robust techniques can use Gaussian
	 * models.
	 */
	public static void showSelectedColor( String name , BufferedImage image , float hue , float saturation ,float distance) {
		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(image,null,true,ImageFloat32.class);
		MultiSpectral<ImageFloat32> hsv = new MultiSpectral<ImageFloat32>(ImageFloat32.class,input.width,input.height,3);
 
		// Convert into HSV
		ColorHsv.rgbToHsv_F32(input,hsv);
 
		// Pixels which are more than this different from the selected color are set to black
		float maxDist2 = distance;
 
		// Extract hue and saturation bands which are independent of intensity
		ImageFloat32 H = hsv.getBand(0);
		ImageFloat32 S = hsv.getBand(1);
		
//		BlurImageOps.gaussian(H, H, 1, -1, null);
		BlurImageOps.gaussian(S, S, 2, -1, null);
		
		// Adjust the relative importance of Hue and Saturation
		float adjustUnits = (float)(Math.PI/2.0);
 
		// step through each pixel and mark how close it is to the selected color
		BufferedImage output = new BufferedImage(input.width,input.height,BufferedImage.TYPE_INT_RGB);
		for( int y = 0; y < hsv.height; y++ ) {
			for( int x = 0; x < hsv.width; x++ ) {
				// remember Hue is an angle in radians, so simple subtraction doesn't work
				float dh = UtilAngle.dist(H.unsafe_get(x,y),hue);
				float ds = (S.unsafe_get(x,y)-saturation)*adjustUnits;
 
				// this distance measure is a bit naive, but good enough for this demonstration
				float dist2 = dh*dh + ds*ds;
				if( dist2 <= maxDist2  && dist2 >= maxDist2 - 0.05f) {
					output.setRGB(x,y,image.getRGB(x,y));
				}
			}
		}
 
		ShowImages.showWindow(output,"Showing "+name);
	}
 
	public static void main( String args[] ) {
		BufferedImage image = UtilImageIO.loadImage("test_images/00000008.jpg");
 
		// Let the user select a color
		printClickedColor(image,0.01f);
		// Display pre-selected colors
//		showSelectedColor("Yellow",image,1f,1f,0.1f);
//		showSelectedColor("Green",image,1.5f,0.65f,0.1f);
	}
}
