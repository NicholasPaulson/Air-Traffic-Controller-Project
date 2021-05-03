import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;


public class Window extends Canvas
{
	private static final long serialVersionUID = 1L;
	Dimension screenSize;
	public Window(int width, int height, String title, Radar radar)
	{
		
		JFrame frame = new JFrame(title);
	
		screenSize = frame.getToolkit().getScreenSize();
		//frame.setBounds(screenSize.width/2 - 80, 0, width, height);
		frame.setBounds(screenSize.width - width, 0, width, height);
		
		//frame.setPreferredSize(new Dimension(width, height));
		//frame.setMaximumSize(new Dimension(width, height));
	//	frame.setMinimumSize(new Dimension(width, height));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		//frame.setLocationRelativeTo(null);
		frame.add(radar);
		frame.setVisible(true);
		radar.start();
		System.out.println("width = " + width + "   height = "+ height);
		Rectangle rect = new Rectangle();
	}
	
}
 