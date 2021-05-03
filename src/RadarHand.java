import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;


public class RadarHand extends RadarObject
{
	double degree, velocity;
	private float r;
	boolean polarGridOn, handFadeOn;
	int polarGridDivision;
	
	public RadarHand(int x, int y, Radar radar)
	{
		super(x, y, ID.RadarHand, radar);
		degree = 0;
		r = 500;
		velocity = 4;
		polarGridDivision = 5;
		polarGridOn = true;
		handFadeOn = false;
	}
	
	public RadarHand(Point point, Radar radar)
	{
		this(point.x, point.y, radar);
	}


	

	
	public void update()
	{
		if (degree < 360) degree += velocity;
		else degree = velocity;
		int i;
		for (i = 0; i  < velocity*10; i++)
		{
			//handler.addObject(new Trail(grid.getOrigin(), ID.RadarHand, radar, Color.green, grid.pointToPixelX((int)polarToX(r, degree+i*0.1)), grid.pointToPixelY((int)polarToY(r, degree+i*0.1))));
		}
	}

	
	public void render(Graphics g)
	{
		drawGrid(0, Color.pink, g);
		drawPolarGrid(polarGridDivision, Color.magenta, g);
		drawGates(g);
		drawSpinningHand(g);
	}
	
	
	
	public void drawGrid(int delta, Color color, Graphics g)
	{
		if  (delta > 0)
		{
			for (int i = 0; i < WINDOW_WIDTH; i += WINDOW_WIDTH/delta)
			{
				g.drawLine(i, 0, i, WINDOW_HEIGHT);
				g.drawLine(0, i, WINDOW_WIDTH, i);
			}
		}
	}
	
	public void setPolarGridDivision(int spacing)
	{
		if (spacing >= 0) polarGridDivision = spacing;
	}
	
	
	
	
	public void drawPolarGrid(int delta, Color color, Graphics g)
	{
		Color originalColor = g.getColor();
		Graphics2D g2d = (Graphics2D)g;
		g.setColor(color);
		for (int i = 0; i <= 1000; i += 1000/delta)
		{
			g2d.setComposite(makeTransparent(0.5f));
			g.drawOval(grid.getOriginX() - i/2 , grid.getOriginY() - i/2, i, i);
			g2d.setComposite(makeTransparent(1f));
			g.drawString(""+(i > 0 ? i/20 : " "), i/2 + grid.getOriginX() - 20, grid.getOriginY());
			
		}
		g2d.setComposite(makeTransparent(1));
		g.drawOval(grid.getOriginX() - 1000/2 , grid.getOriginY() - 1000/2, 1000, 1000);
		g.setColor(originalColor);
		
	}
	
	public void drawGates(Graphics g)
	{
		g.setColor(Color.black);
		for (int i = 0; i < radar.gate.gatePoint.length; i++) g.fillOval(radar.gate.gatePoint[i].x - radar.gate.GATESIZE/2, radar.gate.gatePoint[i].y - radar.gate.GATESIZE/2, radar.gate.GATESIZE, radar.gate.GATESIZE);
	}

	
	public void drawSpinningHand(Graphics g)
	{
		g.setColor(Color.green);
		//g.drawOval(grid.getOriginX() - (int)r , grid.getOriginY() - (int)r, 2*(int)r, 2*(int)r);
		
		g.drawLine(grid.getOriginX(), grid.getOriginY(), grid.pointToPixelX((int)polarToX(r, degree)), grid.pointToPixelY((int)polarToY(r, degree)));
		if (handFadeOn)
		{
			Graphics2D g2d = (Graphics2D)g;
			float alpha = 0.3f;
			for (int i = 0; i < 300 || alpha <= 0; i++ , alpha -= 0.001f)
			{
				g2d.setComposite(makeTransparent(alpha));
				g2d.drawLine(grid.getOriginX(), grid.getOriginY(), grid.pointToPixelX((int)polarToX(r, degree-i*.2)), grid.pointToPixelY((int)polarToY(r, degree-i*.2)));
			}
			g2d.setComposite(makeTransparent(1));
		}
	}
}
