import java.awt.Graphics;
import java.util.LinkedList;


public class Handler
{
	Radar radar;
	ControlWindow gui;
	public LinkedList<RadarObject> list;
	public LinkedList<Plane> planeList;
	
	
	public Handler(Radar radar)
	{
		this.radar = radar;
		gui = radar.gui;
		
		list = new LinkedList<RadarObject>();
		planeList = new LinkedList<Plane>();
	}
	
	public boolean addObject(RadarObject object)
	{
		if (object.id == ID.Plane)
		{
			Plane p = (Plane) object;
			planeList.add(p);
			if (p.type == Type.Arriving)
			{
				radar.addToArrivingList(p);
			}
			else
			{
				radar.addToDepartingList(p);
			}
		}
		return list.add(object);
	}
	
	public boolean removeObject(RadarObject object)
	{
		if (object.id == ID.Plane)
		{
			Plane p = (Plane) object;
			planeList.remove(p);
			if (p.type == Type.Arriving)
			{
				radar.removeFromArrivingList(p);
			}
			else
			{
				radar.removeFromDepartingList(p);
			}
		}
		return list.remove(object);
	}
	
	public void update()
	{
		for (int i = 0; i < list.size(); i++)
		{
			RadarObject selectedObject = list.get(i);
			selectedObject.update();
		}
	}
	
	public void render(Graphics g)
	{
		for (int i = 0; i < list.size(); i++)
		{
			RadarObject selectedObject = list.get(i);
			selectedObject.render(g);
		}
	}
	
	public int getTotalSelected()
	{
		int sum = 0;
		for (Plane p : planeList)
		{
			if (p.isSelected)
			{
				sum++;
			}
		}
		return sum;
	}
	
	public boolean multiSelect()
	{
		int sum = 0;
		for (Plane p : planeList)
		{
			if (p.isSelected)
			{
				if (++sum > 1) return true;
			}
		}
		return false;
	}
}
