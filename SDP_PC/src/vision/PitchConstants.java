package vision;

public class PitchConstants {
	public PitchConstants(int upperLeftX, int upperLeftY, int croppedWidth,
			int croppedHeight, int region12x, int region23x, int region34x, int hUpperBound, int hLowerBound) {
		this.upperLeftX = upperLeftX;
		this.upperLeftY = upperLeftY;
		this.croppedWidth = croppedWidth;
		this.croppedHeight = croppedHeight;
		this.region12X = region12x;
		this.region23X = region23x;
		this.region34X = region34x;
		this.hUpperBound = hUpperBound;
		this.hLowerBound = hLowerBound;
	}
	public int getUpperLeftX() {
		return upperLeftX;
	}
	public int getUpperLeftY() {
		return upperLeftY;
	}
	public int getCroppedWidth() {
		return croppedWidth;
	}
	public int getCroppedHeight() {
		return croppedHeight;
	}
	public int getRegion12X() {
		return region12X;
	}
	public int getRegion23X() {
		return region23X;
	}
	public int getRegion34X() {
		return region34X;
	}
	public int gethUpperBound() {
		return hUpperBound;
	}
	public int gethLOwerBound() {
		return hLowerBound;
	}
	public void setUpperLeftX(int upperLeftX) {
		this.upperLeftX = upperLeftX;
	}
	public void setUpperLeftY(int upperLeftY) {
		this.upperLeftY = upperLeftY;
	}
	public void setCroppedWidth(int croppedWidth) {
		this.croppedWidth = croppedWidth;
	}
	public void setCroppedHeight(int croppedHeight) {
		this.croppedHeight = croppedHeight;
	}
	public void setRegion12X(int region12x) {
		region12X = region12x;
	}
	public void setRegion23X(int region23x) {
		region23X = region23x;
	}
	public void setRegion34X(int region34x) {
		region34X = region34x;
	}
	public void sethUpperBound(int hUpperBound) {
		this.hUpperBound = hUpperBound;
	}
	public void sethLowerBound(int hLowerBound) {
		this.hLowerBound = hLowerBound;
	}
	private int upperLeftX;
	private int upperLeftY;
	private int croppedWidth;
	private int croppedHeight;
	private int region12X;
	private int region23X;
	private int region34X;
	private int hUpperBound;
	private int hLowerBound;
	

	public static final PitchConstants newPitch = new PitchConstants(55,90,540,320,115,260,410,15,280);
	public static final PitchConstants oldPitch = new PitchConstants(0,0,0,0,130,280,430,0,0); //TODO : fill in

}
