import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.TimerTask;


public class Mouse extends MouseAdapter
{
	
	Handler handler;
	Radar radar;
	Grid grid;
	Random rand;
	Point pressed, released;
	Vector v;
	boolean isDown;
	double speed;
	


	
	public Mouse(Handler handler, Radar radar)
	{
		this.handler = handler;
		this.radar = radar;
		grid = radar.getGrid();
		rand = new Random();
		v = new Vector();
		isDown = false;
		
		//speed = 0.002;
		speed = Plane.KnotsToMagnitude(250);
	}
	
	

	
	public void mousePressed(MouseEvent e)
	{
		pressed = e.getPoint();
		v.changeComponents(grid.pixelToPoint(pressed));
		isDown = true;
		
		
		
		double delta = 0, closest = 31;
		int clickSum = e.getClickCount();
		Plane closestPlane = null;
		
		//System.out.println(e.getButton());
		
		if (e.getButton() == 3)
		{
			for (Plane p : handler.planeList)
			{
				if (p.isSelected)
				{
					Vector v = new Vector(new Point(grid.pointToPixelX(e.getX()) - grid.pointToPixelX((int)p.getX()), grid.pointToPixelY(e.getY()) - grid.pointToPixelY((int)p.getY())));
					p.getVector().changeDegree(v.getDegree());
				}
			}
		}
		
		
		if (e.getButton() == 1)
		{
			
			if (v.getMagnitude() > 500 /* && e.getClickCount() == 2 */) // if you want to click on planes outside of airspace uncomment
			{
				if (e.getClickCount() == 2 || e.isAltDown())
				{
					handler.addObject(new Plane(e.getX(), e.getY(), radar.gui.getSpanwerAltitudeSliderValue(), new Vector(( Plane.KnotsToMagnitude(radar.gui.getSpanwerSpeedSliderValue())), rand.nextInt(360)), radar));
				}
			}
			else
			{
				//System.out.println(v.getMagnitude());
			
				for (RadarObject temp : handler.list)
				{
					if (temp.getID() == ID.Plane)
					{
						Plane p = (Plane) temp;
						
						if (!e.isControlDown())
							{
								p.deselect();
								radar.gui.deselectAll();
							}
						if ((delta = temp.getDistanceFrom(e.getPoint())) <= 30 && delta < closest)
						{
							closestPlane = p;
							closest = delta;
						}		
						
						if (clickSum == 2)
						{
							if (p == closestPlane)
							{
								System.out.println("DOUBLE CLICKED ON PLANE");
							}
						}
					}
				}
				if (closestPlane != null)
				{
	
					closestPlane.setSelected();
					//radar.gui.selectMatchingListObject();
				}
				else if (e.getClickCount() == 2 && v.getMagnitude() <= 100)
				{
					handler.addObject(new Plane(e.getX(), e.getY(), 0, new Vector(0, rand.nextInt(360)), radar));
				}
			}
		}
	}
	
	
	
	
	public void mouseReleased(MouseEvent e)
	{
		released = e.getPoint();
		isDown = false;
		
		if(e.getButton() == 2)
		{
			Vector v = new Vector(new Point(grid.pointToPixelX(released.x) - grid.pointToPixelX(pressed.x), grid.pointToPixelY(released.y) - grid.pointToPixelY(pressed.y)));
			v.changeMagnitude(v.getMagnitude() * 0.01);
			v.print();
			System.out.println(e.getX() + ", " + e.getY());
			
			if (v.getMagnitude() > 0) handler.addObject(new Plane(pressed.x, pressed.y, 200, v, radar));
		}
		
		if (e.getClickCount() == 2)
		{
			System.out.println("DOUBLE !!!!CLICKED");
		}
		else if (e.getClickCount() == 1)
		{
			//System.out.println("SINGLE !!!!CLICKED");
		}
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited (MouseEvent e)
	{
		
	}
	
	public void mouseClicked(MouseEvent e)
	{
		
	}
}
