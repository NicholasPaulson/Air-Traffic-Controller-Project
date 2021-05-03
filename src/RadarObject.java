import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;


public abstract class RadarObject
{
	protected static int WINDOW_WIDTH, WINDOW_HEIGHT;
	//protected int width, height;
	protected double x, y;
	protected ID id;
	public Handler handler;
	protected Grid grid;
	protected Radar radar;
	protected Point lastLocation;
	protected double altitude;
	protected ControlWindow gui;

	
	
	
	public RadarObject(int x, int y, ID id, Radar radar)
	{
		lastLocation = new Point(x, y);
		this.x = x;
		this.y = y;
		this.id = id;
		this.radar = radar;
		
		WINDOW_WIDTH = Radar.WIDTH;
		WINDOW_HEIGHT = Radar.HEIGHT;
		handler = radar.getHandler();
		grid = radar.getGrid();
		gui = radar.gui;
		altitude = 0;
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
	
	///////////////////////////////////////////////////////////
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setID(ID id)
	{
		this.id = id;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public ID getID()
	{
		return id;
	}
	
	public double getAltitude()
	{
		return altitude;
	}
	
	public void setAltitude(double alt)
	{
		if (alt <= 0) altitude = alt;
	}
	
	///////////////////////////////////////////////////////
	public double polarToX(double r, double degree)
	{
		return (r * Math.cos(Math.toRadians(degree)));
	}
	
	public double polarToY(double r, double degree)
	{
		return (r * Math.sin(Math.toRadians(degree)));
	}
	
	public Polar pointToPolar()
	{
		int x = getPointX();
		int y = getPointY();
		int quadrant = getQuadrant();
		double r = Math.sqrt(x*x + y*y);
		double degree = Math.toDegrees(Math.atan2(y, x));
		
		if (quadrant > 2) degree += 360;
			
		return new Polar(r, degree);
	}
	
	public double degreeFromOrigin()
	{
		Polar polar = pointToPolar();
		return polar.degree;
	}
	
	public double radiusFromOrigin(int x, int y)
	{
		Polar polar = pointToPolar();
		return polar.radius;
	}
	/////////////////////////////////////////////////////
	
	protected AlphaComposite makeTransparent(float alpha)
	{
		int type = AlphaComposite.SRC_OVER;
		return AlphaComposite.getInstance(type, alpha);
	}
	
	public void setPoint(Point point)
	{
		Point pixel = grid.pointToPixel(point);
		x = pixel.x;
		y = pixel.y;		
	}
	
	public void setPoint(int x, int y)
	{
		Point pixel = grid.pointToPixel(x, y);
		x = pixel.x;
		y = pixel.y;
	}
	
	public Point getPoint()
	{
		return new Point((int)(x - grid.getOriginX()),(int)(grid.getOriginY() - y));
	}
	
	public int getPointX()
	{
		return (int)(x - grid.getOriginX());
	}
	
	public int getPointY()
	{
		return (int)(grid.getOriginY() - y);
	}
	
	public Point getPixels()
	{
		return new Point((int)x, (int)y);
	}
	
	public int getQuadrant()
	{
		int quadrant = 0;
		if (x != grid.getOriginX() && y != grid.getOriginY())
		{
			if (x > grid.getOriginX())
			{
				if (y < grid.getOriginY()) quadrant = 1;
				else quadrant = 4;
			}
			else
			{
				if (y < grid.getOriginY()) quadrant = 2;
				else quadrant = 3;
			}
		}
		return quadrant;
	}

	
	/*////////////////////////////////////////////////////
	public double kn2mph(double kn)
	{
		return kn * 1.15078;
	}
	
	public double mph2kn(double mph)
	{
		return mph * 0.868976;
	}

	public double mph2pph(double mph)
	{
		return  mph * 10;
	}
	
	public double pph2mph(double pph)
	{
		return pph / 10;
	}
	public double knotsToPixelSpeed()
	{
		return knotsToPixelSpeed(knots);
	}
	
	public void setKnots(int knots)
	{
		this.knots = knots;
	}
	public int getKnots()
	{
		return knots;
	}
	
	public double getSpeed()
	{
		return speed;
	}

	public double knotsToPixelSpeed(int knots)
	{
		return mph2pph(kn2mph(knots));
	}
	
	public void updateVelocity()
	{
		speed = knotsToPixelSpeed();
		velX = speed * Math.cos(heading);
		velY = speed * Math.sin(heading);
	}
	
	///////////////////////////////////////////////////////*/
	
	public static double KnotsToMagnitude(double kn)
	{
		return (kn * 1.15) / 3600;
	}
	
	public static double magnitudeToKnots(double m)
	{
		return (3600 * m) / 1.15;
	}
	//////////////////////////////////////////////////
	
	public double getDistanceFrom(double x, double y)
	{
		return Vector.getMagnitude(this.getX() - x,  this.getY() - y);
	}
	
	public double getDistanceFrom(RadarObject o)
	{
		return getDistanceFrom(o.getX(), o.getY());	
	}
	
	public double getDistanceFrom(Point point)
	{
		return getDistanceFrom(point.x, point.y);
	}
	
	public double getDistanceFromOrigin()
	{
		return getDistanceFrom(grid.getOrigin());
	}
	
	public boolean inAirspace()
	{
		if (getDistanceFromOrigin() <= 500) return true;
		return false;
	}
	
	public boolean inDepartingZone()
	{
		if (getDistanceFromOrigin() <= 100) return true;
		{
			return false;
		}
	}
	
	public static double getDistanceFromAtoB(Point a, Point b)
	{
		return Vector.getMagnitude(b.x - a.x, b.y - a.y);
	}
}
