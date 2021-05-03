import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;


public class Gate extends RadarObject
{
	public static final int GATESIZE = 100;
	Vector v;
	Point[] gatePoint;
	
	
	public Gate(int x, int y, Radar radar)
	{
		super(x, y, ID.Gate, radar);
		
		gatePoint = new Point[4];
		v = new Vector(500, 45);
		for (int i = 0; i < gatePoint.length; i++, v.changeDegree(v.getDegree() + 90))
		{
			gatePoint[i] = new Point(grid.getOriginX() + (int)v.getComponentX(), grid.getOriginY() + (int)v.getComponentY());
		}
		printPoints();
	}

	public void update()
	{
		
	}


	public void render(Graphics g)
	{
			//g.setColor(Color.black);
			//for (int i = 0; i < gatePoint.length; i++) g.fillOval(gatePoint[i].x - GATESIZE/2, gatePoint[i].y - GATESIZE/2, GATESIZE, GATESIZE);		
	}
	
	public void printPoints()
	{
		System.out.println("Gate Points");
		for (int i = 0; i < gatePoint.length; i++)
		{
			System.out.println(grid.pixelToPoint(gatePoint[i]));
		}
	}
}
