import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;


public class Trail extends RadarObject
{
	private float alpha = 0.1f;
	private double degree, r, lineStart, lineEnd;
	private Color color;

	
	public Trail(Point point, ID id, Radar radar, Color color, double lineStart, double lineEnd)
	{
		 super(point.x, point.y, id, radar);
		 degree = 0;
		 r = Radar.WIDTH;
		 this.lineStart = lineStart;
		 this.lineEnd = lineEnd;
		 this.color = color;
	}
	
	public Trail(int x, int y, ID id, Radar radar)
	{
		super(x, y, id, radar);
		degree = 0;
		r = Radar.WIDTH;
	}

	@Override
	public void update()
	{
		if (alpha > 0.01f) alpha -= 0.003f;
		else handler.list.remove(this);
	}

	@Override
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		g2d.setComposite(makeTransparent(alpha));
		
		g.setColor(Color.green);
		g.drawLine((int)x, (int)y, (int)lineStart, (int)lineEnd);
		//Shape drawline = new Line2D.Float(grid.getOriginX(), grid.getOriginY(), grid.pointToPixelX((int)polarToX(r, degree)), grid.pointToPixelY((int)polarToY(r, degree)));
		//g2d.draw(drawline);
		g2d.setComposite(makeTransparent(1));
	}
}
