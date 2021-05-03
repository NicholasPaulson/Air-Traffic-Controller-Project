import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;


public class TestWindow extends JFrame
{
	Plane p;
	
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 500, HEIGHT = 500;
	
	private JPanel panel, sliderPanel;
	private JLabel messageLabel;
	private JTextField textField;
	private JButton button;
	private JRadioButton radioButton;
	private JToggleButton toggleButton;
	 JSlider slider, altitudeSlider;
	private JList planeList;
	
	private Radar radar;
	
	private String[] names = {"nick", "matt", "Jessi", "Larry"};
	private Plane[] planeArray;
	
	@SuppressWarnings("unchecked")
	public TestWindow(int height, int width, String title, Radar radar)
	{
		this.radar = radar;
		
		p = radar.selectedPlane;
		
		planeArray = new Plane[radar.handler.planeList.size()];
		
		setTitle("Controls");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		sliderPanel = new JPanel();
		messageLabel = new JLabel("NEW WINDOW");
		textField = new JTextField(10);
		radioButton = new JRadioButton();
		toggleButton = new JToggleButton();
		slider = new JSlider(JSlider.VERTICAL, 0, 250, 250);
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		altitudeSlider = new JSlider(JSlider.VERTICAL, 0, 25000, 25000);
		altitudeSlider.setMajorTickSpacing(5000);
		altitudeSlider.setMinorTickSpacing(1000);
		altitudeSlider.setPaintTicks(true);
		altitudeSlider.setPaintLabels(true);
		planeList = new JList();
		button = new JButton("CLICK ME");
		button.addActionListener(new ButtonListener());
		panel.add(messageLabel);
		panel.add(textField);
		panel.add(button);
		panel.add(radioButton);
		panel.add(toggleButton);
		sliderPanel.add(slider);
		sliderPanel.add(altitudeSlider);
		setLayout(new GridLayout(1,3));
		add(planeList);
		add(panel);
		add(sliderPanel);
		//pack();
		setVisible(true);
	}
	
	
	private class ButtonListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
				System.out.println("BUTTON PUSHED");
				if (radar.rh.polarGridOn) radar.rh.polarGridOn = false;
				else radar.rh.polarGridOn = true;
				//System.out.println(slider.getValue());
				                                       //miles per sec   kn2mph 
				p.getVector().changeMagnitude(slider.getValue() * 1.15 / 3600);
		}
		
	}
	
	public Plane[] getPlaneList()
	{
		RadarObject temp;
		for (int i = 0; i < radar.handler.list.size(); i++)
		{
			temp = radar.handler.list.get(i);
			if (temp.id == ID.Plane)
			{
				planeArray[i] = (Plane)temp;
			}
		}
		return planeArray;
	}
	
	public void update()
	{
		
	}
	
}
