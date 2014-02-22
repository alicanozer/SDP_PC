package vision;

public class PitchColours {
	
	private float[] blueValue;
	private float[] yellowValue;
	private float[] blackValue;
	private float[] redValue;
	private float[] greenPlateValue;
	private float[] greenPitchValue;
	private float[] whiteValue;
	
	public PitchColours(float[] blueValue, float[] yellowValue, float[] blackValue,
			float[] redValue, float[] greenPlateValue, float[] greenPitchValue,
			float[] whiteValue)
	{
		this.blueValue = blueValue;
		this.yellowValue = yellowValue;
		this.blackValue = blackValue;
		this.redValue = redValue;
		this.greenPlateValue = greenPlateValue;
		this.greenPitchValue = greenPitchValue;
		this.whiteValue = whiteValue;	
	}
	
	
	public float[] getBlueValue(){
		return blueValue;
	}
	
	public float[] getYellowValue(){
		return yellowValue;
	}
	
	public float[] getBlackValue(){
		return blackValue;
	}
	
	public float[] getRedValue(){
		return redValue;
	}
	public float[] getGreenPlateValue(){
		return greenPlateValue;
	}
	
	public float[] getGreenPitchValue(){
		return greenPitchValue;
	}
	
	public float[] getWhiteValue(){
		return whiteValue;
	}
	
	public void setBlueValue(float[] blueValue){
		this.blueValue = blueValue;
	}
	
	public void setYellowValue(float[] yellowValue){
		this.yellowValue = yellowValue;
	}
	
	public void setBlackValue(float[] blackValue){
		this.blackValue = blackValue;
	}
	
	public void setRedValue(float[] redValue){
		this.redValue = redValue;
	}
	
	public void setGreenPlateValue(float[] greenPlateValue){
		this.greenPlateValue = greenPlateValue;
	}
	
	public void setGreenPitchValue(float[] greenPitchValue){
		this.greenPitchValue = greenPitchValue;
	}
	
	public void setWhiteValue(float[] whiteValue){
		this.whiteValue = whiteValue;
	}
	
}
