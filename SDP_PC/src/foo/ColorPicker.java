package foo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
/**
 * The colorpicker takes some image source and allows us to pick colors from images from that source.
 * It keeps track of the colors and thresholds we set and can be queried for them.
 * @author apljungquist
 *
 */
public class ColorPicker extends JPanel implements ActionListener, MouseListener {
	//String constants that will be used for displaying radio buttons and handling events
	static String redString = "Red";
	static String yellowString = "Yellow";
	static String blueString = "Blue";
	static String lightGreenString = "LightGreen";
	static String darkGreenString = "DarkGreen";
	static String blackString = "Black";
	static String whiteString = "White";
	static String dotString = "Dot";
	
	//String constants that will be used for han
	static String refreshString = "Refresh";
	static String thresholdString = "Threshold";
	
	//We will need to access these from multiple functions.
	JFormattedTextField thresholdField;
	ImageComponent imageComponent;
	ImageSink imageSink;

	//This is for keeping track of selected colors and thresholds.
	//I put them in arrays to make implementation easier.
	//See beginning of constructor for assumptions of which index belongs to what color.
	
	int colorIndex; //This is used to keep track of which color we are currently manipulating
	private Color[] colors = new Color[8];
	private double[] thresholds = new double[8];
	

	public ColorPicker(ImageSink imageSink) {
		super(new BorderLayout());
		
		//Default values for all the colors. Prevents nulls and lets us skip picking some colors after starting the program.
		colors = new Color[8];
		colors[0] = new Color(140,   0,   0);//Red
		colors[1] = new Color(160,  80,   0);//Yellow
		colors[2] = new Color( 33,  66,  99);//Blue
		colors[3] = new Color( 30,  110,  30);//Light Green
		colors[4] = new Color( 38,  63,  24);//Dark Green
		colors[5] = new Color( 35,  31,  25);//Black
		colors[6] = new Color(140, 120, 100);//White
		colors[7] = new Color( 30,  50,  20);//Dot

		this.imageSink = imageSink;
		
		// Create the radio buttons.
		JRadioButton redButton = new JRadioButton(redString);
		redButton.setMnemonic(KeyEvent.VK_B);
		redButton.setActionCommand(redString);
		redButton.setSelected(true);

		JRadioButton yellowButton = new JRadioButton(yellowString);
		yellowButton.setMnemonic(KeyEvent.VK_Y);
		yellowButton.setActionCommand(yellowString);

		JRadioButton blueButton = new JRadioButton(blueString);
		blueButton.setMnemonic(KeyEvent.VK_B);
		blueButton.setActionCommand(blueString);

		JRadioButton lightGreenButton = new JRadioButton(lightGreenString);
		lightGreenButton.setMnemonic(KeyEvent.VK_L);
		lightGreenButton.setActionCommand(lightGreenString);

		JRadioButton darkGreenButton = new JRadioButton(darkGreenString);
		darkGreenButton.setMnemonic(KeyEvent.VK_D);
		darkGreenButton.setActionCommand(darkGreenString);
		
		JRadioButton blackButton = new JRadioButton(blackString);
		blackButton.setMnemonic(KeyEvent.VK_N);
		blackButton.setActionCommand(blackString);
		
		JRadioButton whiteButton = new JRadioButton(whiteString);
		whiteButton.setMnemonic(KeyEvent.VK_W);
		whiteButton.setActionCommand(whiteString);
		
		JRadioButton dotButton = new JRadioButton(dotString);
		dotButton.setMnemonic(KeyEvent.VK_I);
		dotButton.setActionCommand(dotString);

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(redButton);
		group.add(yellowButton);
		group.add(blueButton);
		group.add(lightGreenButton);
		group.add(darkGreenButton);
		group.add(blackButton);
		group.add(whiteButton);
		group.add(dotButton);
		

		// Register a listener for the radio buttons.
		redButton.addActionListener(this);
		yellowButton.addActionListener(this);
		blueButton.addActionListener(this);
		lightGreenButton.addActionListener(this);
		darkGreenButton.addActionListener(this);
		blackButton.addActionListener(this);
		whiteButton.addActionListener(this);
		dotButton.addActionListener(this);

		//Create the component that will show the image
		imageComponent = new ImageComponent();
		imageComponent.addMouseListener(this);
		
		//Create the button to refresh the image
		JButton refreshButton = new JButton(refreshString);
		refreshButton.setActionCommand(refreshString);
		refreshButton.addActionListener(this);
		
		//Create the field that will take threshold values
		thresholdField = new JFormattedTextField(NumberFormat.getNumberInstance());
		thresholdField.setActionCommand(thresholdString);
		thresholdField.addActionListener(this);

		// Put the radio buttons in a column in a panel.
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(redButton);
		radioPanel.add(yellowButton);
		radioPanel.add(blueButton);
		radioPanel.add(lightGreenButton);
		radioPanel.add(darkGreenButton);
		radioPanel.add(blackButton);
		radioPanel.add(whiteButton);
		radioPanel.add(dotButton);

		//Add everything to the panel
		add(imageComponent, BorderLayout.LINE_START);
		add(refreshButton, BorderLayout.CENTER);
		add(thresholdField, BorderLayout.PAGE_END);
		add(radioPanel, BorderLayout.LINE_END);
	}

	/** Listens to the radio buttons. */
	public void actionPerformed(ActionEvent e) {
		//If the refresh button is clicked, get a frame from the image sink and show it
		if (e.getActionCommand().equals(refreshString)) {
			try {
				imageComponent.setImage(imageSink.getLatest());
				imageComponent.repaint();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//Else if the threshold is modified update the appropriate threshold
		else if(e.getActionCommand().equals(thresholdString)) {
			thresholds[colorIndex] = Double.parseDouble(thresholdField.getText());
		}
		//Else if one of the radiobuttons are selected, set the colorIndex appropriately.
		else if (e.getActionCommand().equals(redString)) {
			colorIndex = 0;
		}else if (e.getActionCommand().equals(yellowString)) {
			colorIndex = 1;
		}else if (e.getActionCommand().equals(blueString)) {
			colorIndex = 2;
		}else if (e.getActionCommand().equals(lightGreenString)) {
			colorIndex = 3;
		}else if (e.getActionCommand().equals(darkGreenString)) {
			colorIndex = 4;
		}else if (e.getActionCommand().equals(blackString)) {
			colorIndex = 5;
		}else if (e.getActionCommand().equals(whiteString)) {
			colorIndex = 6;
		}else if (e.getActionCommand().equals(dotString)) {
			colorIndex = 7;
		}
		
		//Whenever something is clicked make sure the threshold field shows the right value
		thresholdField.setText(String.valueOf(thresholds[colorIndex]));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//When the image is clicked store the clicked color.
		this.colors[colorIndex] = new Color(imageComponent.getImage().getRGB(e.getX(), e.getY()));
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public Color getColor(int index) {
		return colors[index];
	}
	
	public Color[] getColors() {
		return colors;
	}
	
	public double getThreshold(int index) {
		return thresholds[index];
	}
}