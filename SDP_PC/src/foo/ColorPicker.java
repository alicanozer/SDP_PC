package foo;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.*;

///*
// * RadioButtonDemo.java requires these files:
// *   images/Bird.gif
// *   images/Cat.gif
// *   images/Dog.gif
// *   images/Rabbit.gif
// *   images/Pig.gif
// */
public class ColorPicker extends JPanel implements ActionListener, MouseListener {
	static String redString = "Red";
	static String yellowString = "Yellow";
	static String blueString = "Blue";
	static String lightGreenString = "LightGreen";
	static String darkGreenString = "DarkGreen";
	static String blackString = "Black";
	static String whiteString = "White";
	static String dotString = "Dot";
	
	static String refreshString = "Refresh";
	static String thresholdString = "Threshold";
	
	JFormattedTextField thresholdField;
	ImageComponent imageComponent;
	ImageSink imageSink;

	int colorIndex;
	public Color[] colors = new Color[8];
	public double[] thresholds = new double[8];
	
//	JLabel picture;

	public ColorPicker(ImageSink imageSink) {
		super(new BorderLayout());
		
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
		JRadioButton birdButton = new JRadioButton(redString);
		birdButton.setMnemonic(KeyEvent.VK_B);
		birdButton.setActionCommand(redString);
		birdButton.setSelected(true);

		JRadioButton catButton = new JRadioButton(yellowString);
		catButton.setMnemonic(KeyEvent.VK_Y);
		catButton.setActionCommand(yellowString);

		JRadioButton dogButton = new JRadioButton(blueString);
		dogButton.setMnemonic(KeyEvent.VK_B);
		dogButton.setActionCommand(blueString);

		JRadioButton rabbitButton = new JRadioButton(lightGreenString);
		rabbitButton.setMnemonic(KeyEvent.VK_L);
		rabbitButton.setActionCommand(lightGreenString);

		JRadioButton pigButton = new JRadioButton(darkGreenString);
		pigButton.setMnemonic(KeyEvent.VK_D);
		pigButton.setActionCommand(darkGreenString);
		
		JRadioButton blackButton = new JRadioButton(blackString);
		pigButton.setMnemonic(KeyEvent.VK_N);
		pigButton.setActionCommand(blackString);
		
		JRadioButton whiteButton = new JRadioButton(whiteString);
		pigButton.setMnemonic(KeyEvent.VK_W);
		pigButton.setActionCommand(whiteString);
		
		JRadioButton dotButton = new JRadioButton(dotString);
		pigButton.setMnemonic(KeyEvent.VK_I);
		pigButton.setActionCommand(dotString);

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(birdButton);
		group.add(catButton);
		group.add(dogButton);
		group.add(rabbitButton);
		group.add(pigButton);
		group.add(blackButton);
		group.add(whiteButton);
		group.add(dotButton);
		

		// Register a listener for the radio buttons.
		birdButton.addActionListener(this);
		catButton.addActionListener(this);
		dogButton.addActionListener(this);
		rabbitButton.addActionListener(this);
		pigButton.addActionListener(this);
		blackButton.addActionListener(this);
		whiteButton.addActionListener(this);
		dotButton.addActionListener(this);

		imageComponent = new ImageComponent();
		imageComponent.addMouseListener(this);
		
		JButton refreshButton = new JButton(refreshString);
		refreshButton.setActionCommand(refreshString);
		refreshButton.addActionListener(this);
		
		thresholdField = new JFormattedTextField(NumberFormat.getNumberInstance());
		thresholdField.setActionCommand(thresholdString);
		thresholdField.addActionListener(this);
//		// Set up the picture label.
//		picture = new JLabel(createImageIcon("images/" + redString + ".gif"));

//		// The preferred size is hard-coded to be the width of the
//		// widest image and the height of the tallest image.
//		// A real program would compute this.
//		picture.setPreferredSize(new Dimension(177, 122));

		// Put the radio buttons in a column in a panel.
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(birdButton);
		radioPanel.add(catButton);
		radioPanel.add(dogButton);
		radioPanel.add(rabbitButton);
		radioPanel.add(pigButton);
		radioPanel.add(blackButton);
		radioPanel.add(whiteButton);
		radioPanel.add(dotButton);

		add(imageComponent, BorderLayout.LINE_START);
		add(refreshButton, BorderLayout.CENTER);
		add(thresholdField, BorderLayout.PAGE_END);
		add(radioPanel, BorderLayout.LINE_END);
//		add(picture, BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	/** Listens to the radio buttons. */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(refreshString)) {
			try {
				imageComponent.setImage(imageSink.getLatest());
				imageComponent.repaint();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getActionCommand().equals(thresholdString)) {
			thresholds[colorIndex] = Double.parseDouble(thresholdField.getText());
		}else if (e.getActionCommand().equals(redString)) {
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
		thresholdField.setText(String.valueOf(thresholds[colorIndex]));
	}


	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	public static void createAndShowGUI(ImageSink imageSink) {
		// Create and set up the window.
		JFrame frame = new JFrame("RadioButtonDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new ColorPicker(new ImageSink());
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(new ImageSink());
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
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
	
	public double getThreshold(int index) {
		return thresholds[index];
	}
}