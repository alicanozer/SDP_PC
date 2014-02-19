package vision;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

import sun.awt.image.ToolkitImage;
import boofcv.gui.image.ShowImages;

public class KMeans {

	private static Point2D_I32 findAverageXY(ArrayList<Point2D_I32> list){
		int tX = 0;
		int tY = 0;
		for (int i = 0; i<list.size(); i++){
			tX = tX + list.get(i).x;
			tY = tY + list.get(i).y;
		}
		Point2D_I32 cent = new Point2D_I32(tX/list.size(),tY/list.size());
		return cent;
	}

	private static int min(double[] list){ //find position of min value
		double mini=list[0];
		int pos =0;
		for (int i = 1; i < list.length; i++) {
			if(mini>list[i]) {
				mini=list[i];
				pos = i;
			}
		}
		return pos;
	}

	public static ArrayList<Point2D_I32> Cluster2DPoints(ArrayList<Point2D_I32> dataset, int k, int max){
		ArrayList<Point2D_I32> centers = new ArrayList<Point2D_I32>();
		HashMap<Point2D_I32, ArrayList<Point2D_I32>> dataMap = new HashMap<Point2D_I32, ArrayList<Point2D_I32>>();

		int minX = 0;
		int maxX = 540;
		int minY = 0;
		int maxY = 320;
	
		for (int x = 0;x<k;x++){ //init k random points and add to list of centers
			int randX = minX + (int)(Math.random() * ((maxX - minX) + 1));
			int randY = minY + (int)(Math.random() * ((maxY - minY) + 1));

			Point2D_I32 temp = new Point2D_I32(randX,randY);
			System.out.println("initialize center at "+temp);
			centers.add(temp);

			//init dataMap as center key data point array pairs
			ArrayList<Point2D_I32> tempA = new ArrayList<Point2D_I32>();
			dataMap.put(temp, tempA);
		}

		int it = 0;
		while (it < max){

			//assign each point in dataset to closest center, put into dataMap
			for (int i = 0;i<dataset.size();i++){
				for (int j = 0;j<centers.size();j++){
					double[] dists = new double[k];
					int dX = Math.abs((dataset.get(i).x - centers.get(j).x));
					int dY = Math.abs((dataset.get(i).y - centers.get(j).y));
					dists[j] = Math.sqrt((dX*dX + dY*dY)); //find closest distance from cur point to center
					dataMap.get(centers.get(min(dists))).add(dataset.get(i)); //add point into map with center as key
					System.out.println("closest center to "+dataset.get(i)+" is "+centers.get(min(dists)));
				}
			}

			//update center to average of assigned points
			for (int i = 0;i<centers.size();i++){
				ArrayList<Point2D_I32> temp = new ArrayList<Point2D_I32>();
				temp = (dataMap.get(centers.get(i))); //sets of points belonging to cetner.get(i)
				if (temp.size()>0){
				Point2D_I32 avg = findAverageXY(temp); //average of points for this center becomes the new center
				System.out.println("average point for center "+centers.get(i)+" : "+avg+" , updating center to "+avg);

				centers.add(avg);
				dataMap.put(avg, temp);
				dataMap.remove(centers.get(i));
				centers.remove(i);
				}
			}
			
			it++;
		}
		return centers;
	}

	private KMeans(){

	}
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
			// assign points to new colors and recalculate clusters
			//
			for(int i = 0; i < k; i++){
				ArrayList<Point2D_I32> cluster = clusterToPointMap.get(i);
				for(Point2D_I32 p: cluster){
					img.setRGB(p.x, p.y, seeds.get(i).getRGB());
				}
				Color newClusterMean = VisionOps.getColorMedian(img,cluster);
				if(newClusterMean != null){
					seeds.set(i, newClusterMean);
				}
			}
		}
		return clusterToPointMap;
	}
	public class Tuple<V>{
		public Double fst;
		public V snd;
		public Tuple(Double t, V v){
			fst = t;
			snd = v;
		}
		public int compareTo(Tuple<V> other){
			return fst.compareTo(other.fst);
		}
	}
	public class CompareTuples implements Comparator<Tuple<Point2D_I32>>{

		@Override
		public int compare(Tuple<Point2D_I32> arg0, Tuple<Point2D_I32> arg1) {
			return arg0.compareTo(arg1);
		}

	}
	/**
	 * Does KMeans clustering using priority queues on every cluster
	 * @param img
	 * @param k
	 * @param maxIters
	 * @param seeds
	 * @return
	 */
	public static Map<Integer, PriorityQueue<Tuple<Point2D_I32>>> ClusterHeaps(BufferedImage img, int k, int maxIters, List<Color> seeds,int numPoints){
		Map<Integer, PriorityQueue<Tuple<Point2D_I32>>> clusterToQueuesMap = new HashMap<Integer,PriorityQueue<Tuple<Point2D_I32>>>();
		//
		// randomise seeds if no initial seeds given
		//
		if (seeds == null){
			seeds = new ArrayList<Color>(k);
			for(int i = 0; i < k; i++){
				seeds.add(i, new Color(img.getRGB((int)(Math.random() * img.getWidth()), (int) (Math.random() * img.getHeight()))));
			}
		}
		//
		// initialise queues
		//
		KMeans x = new KMeans();
		for(int h = 0; h < k; h++){
			clusterToQueuesMap.put(h, new PriorityQueue<Tuple<Point2D_I32>>(img.getWidth()*img.getHeight()/seeds.size(), x.new CompareTuples()));
		}

		List<Color> seedsOrig = seeds;
		//
		// main loop
		//
		for(int m = 0; m < maxIters ; m++){
			//
			// clear up queues
			//
			for(int h = 0; h < k; h++){
				clusterToQueuesMap.get(h).clear();
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
					Tuple<Integer> dist_Ind = getMinIndexAndDistance(distancesToClusters);
					clusterToQueuesMap.get(dist_Ind.snd).add(x.new Tuple<Point2D_I32>(dist_Ind.fst,new Point2D_I32(i,j)));
				}
			}
			//
			// assign points to new colors - only takes the n top most points from the queues, also recalculates clusters
			//
			for(int i = 0; i < k; i++){
				ArrayList<Point2D_I32> l = new ArrayList<Point2D_I32>();
				for(int j = 0; j < Math.min(numPoints,clusterToQueuesMap.get(i).size()); j++){
					Point2D_I32 p = clusterToQueuesMap.get(i).poll().snd;
					img.setRGB(p.x, p.y, 1);//seeds.get(i).getRGB());
					l.add(p);
				}
				Color newClusterMean = VisionOps.getColorCentroid(img,l);
				if(newClusterMean != null){
					seeds.set(i, newClusterMean);
				}
			}	

		}
		return clusterToQueuesMap;
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

	private static Tuple<Integer> getMinIndexAndDistance(List<Double> l){
		int min_ind = -1;
		double min = Double.MAX_VALUE;
		for(int i = 0; i < l.size(); i++){
			if(l.get(i) != null && l.get(i) <= min){
				min = l.get(i);
				min_ind = i;
			}
		}
		KMeans x = new KMeans();
		return x.new Tuple<Integer>(min, min_ind);
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage img1 = ImageIO.read(new File("test_images/img2.jpg"));
		long time1 = System.currentTimeMillis();
		//		BufferedImage img1 = new BufferedImage(img1_in.getWidth()/2, img1_in.getHeight()/2,BufferedImage.TYPE_INT_RGB);
		//		System.out.println("Scaling took: " + (System.currentTimeMillis() - time1)/1000.0);
		//		Image img1_scaled= img1_in.getScaledInstance(img1_in.getWidth()/2, img1_in.getHeight()/2, BufferedImage.SCALE_FAST);
		//		Graphics2D img1_g = img1.createGraphics();
		//		img1_g.drawImage(img1_scaled,0,0,null);
		//		img1_g.dispose();

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

		time1 = System.currentTimeMillis();
		Cluster(img1,100,10,null);
		System.out.println("Kmeans took: " + (System.currentTimeMillis() - time1)/1000.0);


		//				
		//		Graphics2D g = (Graphics2D) img1.getGraphics();
		//		
		//		for(Entry<Integer, ArrayList<Point2D_I32>> e: points.entrySet()){
		//			for(Point2D_I32 p: e.getValue()){
		//				g.setColor(clusterToColorMap.get(e.getKey()));
		//				g.drawLine(p.x, p.y, p.x, p.y);
		//			}
		//		}
		//		g.dispose();

		ShowImages.showWindow(img1,"img1");
	}

}
