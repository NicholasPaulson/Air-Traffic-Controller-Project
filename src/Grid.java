import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;


public class Grid extends RadarObject
{
	
	private int width, height, originX, originY;
	private Point origin;
	
	public Grid(int width, int height, Radar radar)
	{
		super (0, 0, ID.Grid, radar);
		this.width = width;
		this.height = height;
		originX = width/2 + (int)x;
		originY = height/2 + (int)y;
		origin = new Point(originX, originY);
	}
	
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public int getOriginX()
	{
		return originX;
	}
	public int getOriginY()
	{
		return originY;
	}
	public Point getOrigin()
	{
		return new Point(originX, originY);
	}
	
	public Point pixelToPoint(int x, int y)
	{
		return new Point(x - originX, originY - y);
	}
	
	public Point pixelToPoint(Point point)
	{
		return pixelToPoint(point.x, point.y);
	}
	
	public Point pointToPixel(int x, int y)
	{
		return new Point(originX + x, originY - y);
	}
	
	public Point pointToPixel(Point point)
	{
		return pointToPixel(point.x, point.y);
	}
	
	public int pointToPixelX(int x)
	{
		return originX + x;
	}
	
	public int pointToPixelY(int y)
	{
		return originY - y;
	}
	
	public int getQuadrant(int x, int y)
	{
		int quadrant = 0;
		if (x != 0 && y != 0)
		{
			if (x > 0)
			{
				if (y > 0) quadrant = 1;
				else quadrant = 4;
			}
			else
			{
				if (y > 0) quadrant = 2;
				else quadrant = 3;
			}
		}
		return quadrant;
	}
	
	public double polarToX(double r, double degree)
	{
		return (r * Math.cos(Math.toRadians(degree)));
	}
	
	public double polarToY(double r, double degree)
	{
		return (r * Math.sin(Math.toRadians(degree)));
	}
	
	public static Polar pointToPolar(Point point)
	{
		return pointToPolar(point.x, point.y);
	}
	public static Polar pointToPolar(int x, int y)
	{
		double r = Math.sqrt(x*x + y*y);
		double degree = Math.toDegrees(Math.atan2(y, x));
		return new Polar(r, degree);
	}
	
	public static double degreeFromOrigin(int x, int y)
	{
		Polar polar = pointToPolar(x, y);
		return polar.degree;
	}
	
	public void moveOrigin()
	{
		originX += 1;	
	}
	

	@Override
	public void update()
	{
		//moveOrigin();
	}

	@Override
	public void render(Graphics g) 
	{
		
	}
}
