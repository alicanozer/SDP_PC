package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import boofcv.gui.image.ShowImages;

public class KMeans {

	public static Map<Integer, ArrayList<Point2D_I32>> Cluster(BufferedImage img, int k, int maxIters, List<Color> seeds){
		Map<Integer,ArrayList<Point2D_I32>> clusterToPointMap = new HashMap<Integer,ArrayList<Point2D_I32>>();
		//
		// randomise seeds if no initial seeds given
		//
		if (seeds == null){
			seeds = new ArrayList<Color>(k);
			for(int i = 0; i < k; i++){
				seeds.add(i, new Color(img.getRGB((int)(Math.random() * img.getWidth()), (int) (Math.random() * img.getHeight()))));
			}
		}
		
		List<Color> seedsOrig = seeds;
		
		// main loop
		for(int m = 0; m < maxIters ; m++){
			
			for(int h = 0; h < k; h++){
				clusterToPointMap.put(h, new ArrayList<Point2D_I32>());
			}
			
			for(int i = 0; i < img.getWidth(); i++){	  	// loop over pixels
				for(int j = 0; j < img.getHeight(); j++){ 	// 
					List<Double> distancesToClusters = new ArrayList<Double>(k);
					for(int w = 0; w < k; w++){				// loop over clusters
						distancesToClusters.add(w, VisionOps.distanceRGB(
								new Color(img.getRGB(i, j)), 
								seeds.get(w)));
					}
					//
					// index will be the index of the cluster in the cluster array
					//
					int index = getMinIndex(distancesToClusters);
					clusterToPointMap.get(index).add(new Point2D_I32(i,j));
				}
			}
			//
			// recalculate clusters
			// 
			for(int i = 0; i < k; i++){
				Color newClusterMean = VisionOps.getColorCentroid(img,clusterToPointMap.get(i));
				if(newClusterMean != null){
					seeds.set(i, newClusterMean);
				}
			}
			System.out.println("Iteration "+m);
		}
//		for(int i = 0; i < k; i++){
//			for(Point2D_I32 p : clusterToPointMap.get(i)){
//				img.setRGB(p.x, p.y, seedsOrig.get(i).getRGB());
//			}
//		}
		return clusterToPointMap;
	}
		/**
		 * Retunrs the index of the maximum element of the arrayList
		 * @param l
		 * @return
		 */
		private static int getMinIndex(List<Double> l){
			int min_ind = -1;
			double min = Double.MAX_VALUE;
			for(int i = 0; i < l.size(); i++){
				if(l.get(i) != null && l.get(i) <= min){
					min = l.get(i);
					min_ind = i;
				}
			}
			return min_ind;
		}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage img1 = ImageIO.read(new File("test_images/00000008.jpg"));
		System.out.println("asd");
		List<Color> seeds1 = new ArrayList<Color>();
		List<Color> seeds2 = new ArrayList<Color>();
		
		seeds1.add(new Color(img1.getRGB(68, 152))); //yellow
		seeds1.add(new Color(img1.getRGB(192,152))); //blue

		seeds1.add(new Color(img1.getRGB(315,157))); //ball
		
		seeds1.add(new Color(img1.getRGB(186,75))); //green
		seeds1.add(new Color(img1.getRGB(260,150))); //white
		seeds1.add(new Color(img1.getRGB(7,9))); //black
		
		seeds2.add(Color.yellow);
		seeds2.add(Color.blue);
		seeds2.add(Color.red);
		seeds2.add(Color.green);
		seeds2.add(Color.white);
		seeds2.add(Color.black);
		
		Map <Integer,Color> clusterToColorMap = new HashMap<Integer,Color>(6);
		for(int i = 0 ; i < 6; i++){
			clusterToColorMap.put(i, seeds2.get(i));
		}
		
		ShowImages.showWindow(img1,"img1");
		
		Map<Integer, ArrayList<Point2D_I32>> points = Cluster(img1,6,100,seeds2);
		
		Graphics2D g = (Graphics2D) img1.getGraphics();
		
		for(Entry<Integer, ArrayList<Point2D_I32>> e: points.entrySet()){
			for(Point2D_I32 p: e.getValue()){
				g.setColor(clusterToColorMap.get(e.getKey()));
				g.drawLine(p.x, p.y, p.x, p.y);
			}
		}
		
		
		//ShowImages.showWindow(img1,"img1");
	}

}
