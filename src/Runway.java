import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;


public class Runway extends RadarObject
{
	LinkedList<Plane> Q;
	Point[] approachPoint;
	Point target;
	Vector v;
	int elevation; //feet above sea level
	boolean isOpen;
	
	public Runway(int x, int y, int angle, Radar radar)
	{
		super(x, y, ID.Runway, radar);
		v = new Vector(100, angle);
		target = new Point(x + 4, y);
		approachPoint = new Point[2];
		approachPoint[0] = new Point((int)x + 5, (int)y - 100);
		approachPoint[1] = new Point((int)x + 5, (int)y + 200);
		Q = new LinkedList<Plane>();
		elevation = 845;
		isOpen = true;
	}

	@Override
	public void update()
	{
		if (!Q.isEmpty() && isOpen)
		{
			Plane p = popFromQ();
			if(p.state == State.QueuedForLanding || p.state == State.QueuedForTakeOff)
			{
				if (p.type == Type.Arriving) p.approachForLanding();
				else p.taxiForTakeoff();
				isOpen = false;
			}
		}
	}

	@Override
	public void render(Graphics g)
	{
		
		
		
		g.setColor(Color.pink);
		g.drawLine((int)x + 1,  (int)y - 100, (int)x + 1, (int)y + 200);
		g.drawLine((int)x + 8,  (int)y - 100, (int)x + 8, (int)y + 200);
		g.setColor(Color.white);
		g.fillRect((int)x,  (int)y, 3, 100);
		g.fillRect((int)x + 7,  (int)y, 3, 100);
		
		//for (int i = 0; i < approachPoint.length; i++) g.fillOval(approachPoint[i].x-10, approachPoint[i].y-10, 20, 20);
			
		
		
		/*
		g.setColor(Color.pink);
		g.drawLine((int)x, (int)y, (int)(x + v.getComponentX()), (int)(y - v.getComponentY()));
		 
		v.changeComponents(3, 100);
		g.setColor(Color.white);
		g.fillRect((int)x + 7, (int) y, (int)v.getComponentX(), (int)v.getComponentY());
		g.fillRect((int)x, (int)y, (int)v.getComponentX(), (int)v.getComponentY());
		*/
	}
	
	public Point getTarget()
	{
		return target;
	}
	
	public int getQSize()
	{
		return Q.size();
	}
	
	public boolean QisEmpty()
	{
		return Q.isEmpty();
	}
	
	public void AddToQ(Plane p)
	{
		radar.addToRunwayQueueList(p);
		Q.add(p);
	}
	
	public Plane popFromQ()
	{
		Plane p = Q.pop();
		radar.removeFromRunwayQueueList(p);
		return p;
	}
	
	public void setRunwayOpen()
	{
		isOpen = true;
	}
	

}
