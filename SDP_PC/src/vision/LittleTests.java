package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import boofcv.gui.image.ShowImages;

public class LittleTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("max3(2,3,4):" + ExampleSegmentColor.max3(2f, 3f, 4f));
		System.out.println("min3(2,3,4):" + ExampleSegmentColor.min3(2.1f, 3.1f, 4.1f));
		
		ArrayList<Point2D_I32> list = new ArrayList<Point2D_I32>();
		int n = 10;
		int imgSize = 800;
		for(int i = 0 ; i < n; i++){
			double x = Math.random()*imgSize;
			double y = Math.random()*imgSize;
			list.add(new Point2D_I32((int) x,(int)y));
		}
		
		Polygon p = FastConvexHull.getConvexHullOfPoints(list);
		
		BufferedImage img1 = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = img1.createGraphics();
		g.setColor(Color.WHITE);
		for(Point2D_I32 point: list){
			g.drawLine(point.x - 5, point.y, point.x + 5, point.y);
			g.drawLine(point.x, point.y - 5, point.x, point.y + 5);
		}
		g.draw(p);
		
		ShowImages.showWindow(img1,"img1");
	}

}
