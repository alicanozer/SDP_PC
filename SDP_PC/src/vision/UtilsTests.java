package vision;

import java.awt.Polygon;

public class UtilsTests {

	
	static void testGetRegions(){
		int npoints = 4;
		int[] xs = {0,2,1,0};
		int[] ys = {0,0,2,2};
		Polygon p = new Polygon(xs,ys,npoints);
		assert p.contains(1, 10) == true : "Not true";
		System.out.println(p.contains(1, 10));
		System.out.println(p.contains(1, 1));
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetRegions();
	}

}
