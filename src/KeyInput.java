import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyInput extends KeyAdapter
{
	Radar radar;
	Handler handler;
	
	public KeyInput(Handler handler, Radar radar)
	{
		this.radar = radar;
		this.handler = handler;
	}
	
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		//for ()
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		
		if (key == KeyEvent.VK_SPACE)
		{
			for (Plane plane : handler.planeList)
	    	{
	    		if (plane.isSelected && plane.state == State.Free)
	    		{
	    			plane.joinQueueToLandRequest();
	    		}
	    	}
	    	
	    	{
	        	for (Plane plane : handler.planeList)
	        	{
	        		if (plane.isSelected && plane.state == State.Parked)
	        		{
	        			plane.joinQueueToTakeoffRequest();
	        		}
	        	}
	        }
		}
		
		if (key == KeyEvent.VK_A)
		{
			for (Plane p : handler.planeList)
			{
				if (e.isControlDown())
				{
					p.setSelected();
				}
				else if (e.isAltDown())
				{
					if (p.type == Type.Arriving && p.state != State.Crashed)
					{
						p.setSelected();
					}
					else
					{
						p.deselect();
					}
				}
				else
				{
					 if (p.type == Type.Arriving)
					 {
						 p.setSelected();
					 }
					 else
					 {
						 p.deselect();
					 }
				}
			}
		}
		
		if (key == KeyEvent.VK_D)
		{
			for (Plane p : handler.planeList)
			{
				
				 if (p.type == Type.Departing)
				 {
					 p.setSelected();
				 }
				 else
				 {
					 p.deselect();
				 }
				
			}
		}
		
		if (key == KeyEvent.VK_S)
		{
			for (Plane p : handler.planeList)
			{
				
				if (e.isControlDown())
				{
					if (p.situation && p.state != State.Crashed)
					{
						p.setSelected();
					}
					else
					{
						p.deselect();
					}
				}
				else if (e.isAltDown())
				{
					if (p.state == State.Crashed)
					{
						p.setSelected();
					}
					else
					{
						p.deselect();
					}
				}
				else
				{
					if (p.situation)
					{
						p.setSelected();
					}
					else
					{
						p.deselect();
					}
				}
			}
		}
		
		if (key == KeyEvent.VK_R)
		{
			for (Plane p : handler.planeList)
			{
				if (p.state == State.QueuedForLanding || p.state == State.QueuedForTakeOff)
				{
					p.setSelected();				}
				else
				{
					p.deselect();
				}
			}
		}
	}
}
