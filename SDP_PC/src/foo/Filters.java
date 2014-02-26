package foo;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class Filters {
	
	private Color[] referenceColors;
	private double maxDistance = Math.sqrt(3*Math.pow(255,2));
	
	public enum ColorNames {
		RED (0),
		YELLOW (1),
		BLUE (2),
		LIGHT_GREEN(3),
		DARK_GREEN(4),
		BLACK(5),
		WHITE(6),
		DOT(7);
		
		private int i;
		ColorNames(int i) {
			this.i = i;
		}
		
	}
	public void setReferenceColor(ColorNames colorName, Color color) {
		referenceColors[colorName.i] = color;
	}
	
	public Filters() {
		referenceColors = new Color[8];
		referenceColors[0] = new Color(140,   0,   0);//Red
		referenceColors[1] = new Color(160,  80,   0);//Yellow
		referenceColors[2] = new Color( 33,  66,  99);//Blue
		referenceColors[3] = new Color( 30,  110,  30);//Light Green
		referenceColors[4] = new Color( 38,  63,  24);//Dark Green
		referenceColors[5] = new Color( 35,  31,  25);//Black
		referenceColors[6] = new Color(140, 120, 100);//White
		referenceColors[7] = new Color( 30,  50,  20);//Dot
	}
	
	public BufferedImage distanceToTarget(BufferedImage img) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int rgb = img.getRGB(i, j);
				Color clr = new Color(rgb);
				double d = distance(clr, referenceColors[0]);
				out.setRGB(i, j, new Color((float)(d/maxDistance), (float)(d/maxDistance), (float)(d/maxDistance)).getRGB());
			}
		}
		return out;
	}
	
	public BufferedImage nearestNeightbour(BufferedImage img) {
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int rgb = img.getRGB(i, j);
				Color clr = new Color(rgb);
				clr = nearestNeighbour(clr);
				img.setRGB(i, j, clr.getRGB());
			}
		}
		return img;
	}
	
	public BufferedImage nearestNeightbour(BufferedImage img, Color[] colors) {
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int rgb = img.getRGB(i, j);
				Color clr = new Color(rgb);
				clr = nearestNeighbour(clr, colors);
				img.setRGB(i, j, clr.getRGB());
			}
		}
		return img;
	}
	
	public Color nearestNeighbour(Color clr) {
		int index = 0;
		double distance = distance(clr, referenceColors[0]);
		for (int i = 1; i < referenceColors.length; i++) {
			double d = distance(clr, referenceColors[i]);
			if (d < distance) {
				distance = d;
				index = i;
			}
		}
		return referenceColors[index];
	}
	
	public Color nearestNeighbour(Color clr, Color[] colors) {
		int index = 0;
		double distance = distance(clr, colors[0]);
		for (int i = 1; i < colors.length; i++) {
			double d = distance(clr, colors[i]);
			if (d < distance) {
				distance = d;
				index = i;
			}
		}
		return colors[index];
	}
	
	public double distance(Color clr1, Color clr2) {
		return Math.sqrt(
				Math.pow(clr1.getRed()-clr2.getRed(), 2) +
				Math.pow(clr1.getGreen()-clr2.getGreen(), 2) +
				Math.pow(clr1.getBlue()-clr2.getBlue(), 2)
				);
	}

}
