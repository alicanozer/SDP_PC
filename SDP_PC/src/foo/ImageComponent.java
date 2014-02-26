package foo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ImageComponent extends JComponent{
	
	private BufferedImage img;
	
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

	public Dimension getPreferredSize() {
		if (img == null) {
			return new Dimension(640, 480);
		} else {
			return new Dimension(img.getWidth(null), img.getHeight(null));
		}
	}
	
	public BufferedImage getImage() {
		return img;
	}
	
	public void setImage(BufferedImage img) {
		this.img = img;
	}
}
