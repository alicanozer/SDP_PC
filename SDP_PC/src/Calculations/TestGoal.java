package Calculations;

import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import boofcv.gui.image.ShowImages;

public class TestGoal {

	public static void main(String args[]) throws IOException{
		
		BufferedImage img1 = ImageIO.read(new File("test_images/00000007.jpg"));
		
		final GoalInfo goalInfo = new GoalInfo(img1);
		
		Graphics2D g = (Graphics2D) img1.getGraphics();
		
		goalInfo.drawGoalLine(g);
		ShowImages.showWindow(img1,"identifying objects");
	}
}
