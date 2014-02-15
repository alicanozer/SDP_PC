package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import boofcv.gui.image.ShowImages;

public class KMeans {

	public static BufferedImage Cluster(BufferedImage img, int k, List<Point2D_I32> seeds){
		Map<Integer,ArrayList<Point2D_I32>> clusterToPointMap = new HashMap<Integer,ArrayList<Point2D_I32>>();
		
		
		List<Point2D_I32> seedsOrig = seeds;
//		//
//		// randomise seeds initially and initialises the cluster to point mappings
//		//
//		for(int i = 0; i < k; i++){
//			seeds.add(i, new Point2D_I32((int)(Math.random() * img.getWidth()), (int) (Math.random() * img.getHeight())));
//		}
		
		int maxIters = 30;
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
								new Color(img.getRGB(seeds.get(w).x, seeds.get(w).y))));
					}
					//
					// index will be the index of the cluster in the cluster array
					//
					int index = getMinIndex(distancesToClusters);
					clusterToPointMap.get(index).add(new Point2D_I32(i,j));
				}
			}
			//
			// move seeds
			// 
			for(int i = 0; i < k; i++){
				Point2D_I32 newClusterMean = PointUtils.getListCentroid(clusterToPointMap.get(i));
				if(newClusterMean != null){
					seeds.get(i).x = newClusterMean.x;
					seeds.get(i).y = newClusterMean.y;
				}
			}
		}
//		for(int i = 0; i < 2; i++){
			for(Point2D_I32 p : clusterToPointMap.get(2)){
				img.setRGB(p.x, p.y, 1);//img.getRGB(seedsOrig.get(4).x, seedsOrig.get(4).y));
			}
//		}
		return img;
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
		List<Point2D_I32> seeds = new ArrayList<Point2D_I32>();
		
		seeds.add(new Point2D_I32(68,152)); //yellow
		seeds.add(new Point2D_I32(192,152)); //blue

		seeds.add(new Point2D_I32(315,157)); //ball
		
		seeds.add(new Point2D_I32(186,75)); //green
		seeds.add(new Point2D_I32(260,150)); //white
		seeds.add(new Point2D_I32(7,9)); //black
		

		img1 = Cluster(img1,6,seeds);
		
		img1.getGraphics().drawString("asd", 69, 153);
		ShowImages.showWindow(img1,"img1");
	}

}
