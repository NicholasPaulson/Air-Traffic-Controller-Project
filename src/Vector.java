import java.awt.Point;




public class Vector
{
	private double magnitude, degree, scalar;
	
	public Vector()
	{
		magnitude = 0;
		degree = 0;
		scalar = 1;
	}
	
	public Vector(double magnitude, double degree)
	{
		this.magnitude = Math.abs(magnitude);
		this.degree = ((degree % 360) + 360) % 360;
		scalar = 1;
	}
	
	public Vector(double x, double y, boolean usingComponents)
	{
		if (usingComponents)
		{
			changeComponents(x, y);
			scalar = 1;
		}
		else new Vector(x, y);
	}
	
	public Vector (Point point)
	{
		changeComponents(point.x, point.y);
		scalar = 1;
	}
	
	
	
	
	

	public double getComponentX()
	{
		return magnitude * Math.cos(Math.toRadians(degree));
	}
	
	public double getComponentY()
	{
		return magnitude * Math.sin(Math.toRadians(degree));
	}
	
	public double getMagnitude()
	{
		return magnitude;
	}
	
	public double getDegree()
	{
		return degree;
	}
	
	public void changeMagnitude(double m)
	{
		magnitude = Math.abs(m);
	}
	
	public void changeDegree(double d)
	{
		degree = ((d % 360) + 360) % 360;
	}
	
	public void changeComponents(double x, double y)
	{
		magnitude = getMagnitude(x, y);
		degree = getDegree(x, y);
	}
	
	public void changeComponents(Point point)
	{
		changeComponents(point.x, point.y);
	}
	
	public void changeComponentX(double x)
	{
		changeComponents(x, getComponentY());
	}
	
	public void changeComponentY(double y)
	{
		changeComponents(getComponentX(), y);
	}
	
	public void changeScalar(double scalar)
	{
		this.scalar = scalar;
	}
	
	public void add(Vector a)
	{
		changeComponents(getComponentX() + a.getComponentX(), getComponentY() + a.getComponentY());
	}
	
	public void subtract(Vector v)
	{
		changeComponents(getComponentX() - v.getComponentX(), getComponentY() - v.getComponentY());
	}
	
	public void scaleBy(Double scalar)
	{
		changeComponents(getComponentX() * scalar, getComponentY() * scalar);
	}
	
	public void scale()
	{
		changeMagnitude(magnitude * scalar);
	}
	
	public void projectOnASelf(Vector a)
	{
		projectAOnB(a, this);
	}
	
	
	public double getScalar()
	{
		return scalar;
	}
	
	
	
	public static double getComponentX(double magnitude, double degree)
	{
		return magnitude * Math.cos(Math.toRadians(degree));
	}
	
	public static double getComponentY(double magnitude, double degree)
	{
		return Math.toDegrees(magnitude * Math.sin(Math.toRadians(degree)));
	}
	
	public static double getMagnitude(double x, double y)
	{
		return Math.sqrt(x*x + y*y);
	}
	
	public static double getDegree(double x, double y)
	{
		return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
	}
	
	public static Vector addVectors(Vector a, Vector b)
	{
		return new Vector(a.getComponentX() + b.getComponentX(), a.getComponentY() + b.getComponentY(), true);
	}
	
	public static Vector subtractVectors(Vector a, Vector b)
	{
		return new Vector(a.getComponentX() - b.getComponentX(), a.getComponentY() - b.getComponentY(), true);
	}
	
	public static double dotProduct(Vector a, Vector b)
	{
		return a.getComponentX() * b.getComponentX() + a.getComponentY() * b.getComponentY();
	}
	
	public static Vector projectAOnB(Vector a, Vector b)
	{
		b.scaleBy(dotProduct(a, b) / (b.getMagnitude() * b.getMagnitude()));
		return b;
	}
	
	public static Vector scaleBy(Vector v, double scalar)
	{
		v.scaleBy(scalar);
		return v;
	}
	
	
	
public void print()
{
	System.out.println("magnitude = " + magnitude + "  degree = " + degree + "   X -> " + getComponentX() + "  Y -> " + getComponentY());
}
}
