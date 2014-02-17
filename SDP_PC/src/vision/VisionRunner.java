package vision;

import javax.swing.SwingUtilities;

public class VisionRunner {
	private VisionRunner(){};
	public static void start(final boolean debug, final PitchConstants consts){
		
		ObjectLocations.setYellowDefendingLeft(true);
		ObjectLocations.setYellowUs(true);
		
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new FrameHandler(debug,consts);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("i failed miserably and now I must die to repent for my sins... ");
		}
		System.out.println("Vision started successfully!");
	}
}
