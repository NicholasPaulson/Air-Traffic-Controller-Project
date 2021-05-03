import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;






public class Plane extends RadarObject
{
	private float alpha = 1;
	private Color color;
	private String airline, aircraftID, destination;
	private Vector vector;
	RoundRectangle2D circle;
	boolean underControl, hasCrashed, isSelected, isOrbiting, isLanding, requestedToLand, requestedToTakeoff, situation, increasingAltitude, decreasingAltitude;
	public State state;
	public Type type;
	public ControlWindow gui;
	double requestedAltitude;
	double climbRate;

	
	
	
	
	
	
	
	public Plane(int x, int y, int altitude, Vector v, Radar radar)
	{
		super(x, y, ID.Plane, radar);
		this.altitude = altitude;
		vector = v;
		gui = radar.gui;
		color = Color.green;
		if (inAirspace())
		{
			if (inDepartingZone())
			{
				state = State.Parked;
				type = Type.Departing;
				destination = gui.getSpawnerDestination();
				if (destination == "GFK") destination = "MSP";
			}
			else
			{
				state = State.Free;
				type = Type.Arriving;
				destination = "GFK";
			}
		}
		else
		{
			state = State.Entering;
			type = Type.Arriving;
			destination = "GFK";
		}
		situation = false;
		hasCrashed = false;
		isSelected = false;
		isOrbiting = false;
		requestedToLand = false;
		requestedToTakeoff = false;
		increasingAltitude = false;
		decreasingAltitude = false;
		
		airline = gui.getSpawnerAirline();
		aircraftID = String.format("%03d", handler.planeList.size() + 1);
		
		requestedAltitude = 0;
		
		climbRate = 5 * radar.timeMutiplier;
		
		vector.changeScalar(radar.timeMutiplier);
		vector.scale();
		
		
		
		logPlaneSpawn();
	}
	


	
	public void update()
	{
		//updateVelocity();
		 x += vector.getComponentX();
		 y += vector.getComponentY() * -1;
		 
		 if (y <= 0 -100 || y >= WINDOW_HEIGHT + 100) handler.removeObject(this); //vector.changeComponentY(vector.getComponentY() * -1);
		 if (x <= 0 -100 || x >= WINDOW_WIDTH + 100) handler.removeObject(this); //vector.changeComponentX(vector.getComponentX() * -1);
		 
		 handleAltitude();
			 
		 borderPatrol();
		 
		 if (state == State.Entering)
		 {
			 if (inAirspace()) state = State.Free;
			 goToGate();
		 }
		 
		 
		 if (hasCrashed || vector.getMagnitude() == 0)
		 {
			 altitude -= 10;
			 if (altitude < 0)
			 {
				 altitude = 0;
			 }
		 }
	
		 
		 
		 if (state != State.QueuedForLanding && state != State.QueuedForTakeOff)
		 {
			 if (requestedToLand) joinQueue();
			 else if (requestedToTakeoff) joinQueue();
		 }
		 
		 takeoff();
		 land();
		 
		 
		 if (alpha > 0.01f) alpha -= 0.01f;
		 if ((int)degreeFromOrigin() > (int)radar.getRH().degree - Math.abs(3) && (int)degreeFromOrigin() < (int)radar.getRH().degree + Math.abs(3)) alpha = 1;	
		 //System.out.println("(" + getPointX() + ", " + getPointY() + ")   quadrant: "  + getQuadrant());
		 //System.out.println((int)degreeFromOrigin());
		 //System.out.println((int)radar.getRH().degree);


		 collisionAvoidance(radar.collisionAvoidanceOn);
		 
		 lastLocation.x = (int) x;
		 lastLocation.y = (int) y;
	}

	
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(alpha));
		
		
		if (isSelected)
		{
			g.setColor(Color.blue);
			g.fillOval((int)x-15, (int)y-15, 30, 30);
		}
		
		if (hasCrashed || collision() == 2)
		{
			g.setColor(Color.red);
			g2d.setComposite(makeTransparent(1));
			if (!hasCrashed)
			{
				hasCrashed = true;
				state = State.Crashed;
				if(radar.soundOn)radar.gui.playSound(radar.gui.audio);
			}
		}
		else if (collision() == 1)
		{
			g.setColor(Color.yellow);
			//g2d.setComposite(makeTransparent(1));
		}
		else
		{
			if (getDistanceFromOrigin() > 500) g.setColor(Color.cyan);
			else g.setColor(color);
		}
		
		
		
		g.fillRect((int)x,  (int)y, 1, 1);
		g.drawString(airline + aircraftID + " " + destination + " " + (isSelected ? (type == Type.Arriving ? "A" : "D" ) : ""),  (int)x+20, (int)y);
		g.drawString(String.format("%03d %03d %03d", (int)(altitude * .01), GetSpeed(), (int)vector.getDegree()),  (int)x+20, (int)y + 12);
		
		if (radar.showCollisionBounds) g2d.draw(new RoundRectangle2D.Double(x-15, y-15, 30, 30, 30, 30));
		
		g2d.setComposite(makeTransparent(1));
		
		if (getDistanceFromOrigin() >= 500 && underControl)
		 {
			 g.setColor(Color.cyan);
			 //changeDirectionTowards(grid.getOrigin());
			 //changeDirectionTowards(grid.getOriginX(), grid.getOriginY());
			 //vector.changeDegree(vector.getDegree() * -1 + vector.getDegree()*-1);
		 }
		
		if (isSelected) g.drawString("(" + getPointX() + ", " + getPointY() + ")", 10,  60);
		
	}
	
	
	
	private int collision()
	{
		double delta;
		
		for (RadarObject o : handler.list)
		{
			
			
				if(o.getID() == ID.Plane && o != this)
				{
					delta = getDistanceFrom(o);
				
					if (Math.abs(o.getAltitude() - altitude) <= 1)
					{
						if (delta <= 1)
						{
							//System.out.println("COLLISION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							situation = true;
							vector.changeMagnitude(0);
							return 2;
						}
						else if (delta <= 30)
						{
							//System.out.println(this + "   " + deltaX + "    " + deltaY + "    " + delta);
							situation = true;
							return 1;
						}
						else
						{
							situation = false;
						}
					}
					
				}
		}
		return 0;
	}
	
	public Vector getVector()
	{
		return vector;
	}
	
	public void changeDirectionTowards(Point point)
	{
		changeDirectionTowards(point.x, point.y);
	}
	
	public void changeDirectionTowards(double x, double y)
	{
		vector.changeDegree(Vector.getDegree(x - getX(), getY() - y));
	}
	
	
	public Vector getPositionVector()
	{
		return new Vector(getPoint());
	}
	
	public void redirect()
	{
		Vector n = getPositionVector();
		vector.subtract(Vector.scaleBy(Vector.projectAOnB(vector, n), 2.0));
	}
	
	public void goToGate()
	{
		
		double min = grid.getWidth(), delta = 0;
		for (int i = 0; i < radar.gate.gatePoint.length; i++)
		{
			delta = getDistanceFrom(radar.gate.gatePoint[i]);
			if (delta < min)
			{
				min = delta;
				changeDirectionTowards(radar.gate.gatePoint[i]);
			}
		}
	}
	
	
	
	
	
	
	public void setSelected()
	{
		isSelected = true;
		
		if (!handler.multiSelect())
		{
			//System.out.println(!handler.multiSelect());
			gui.updateSelected(this);
			
		}
		
	}
	
	public void deselect()
	{
		isSelected = false;
		gui.clearControl();
	}
	
	
	
	
	
	
	
	
	
	
	private void joinQueue()
	{
		radar.runway.AddToQ(this);
	}
	
	public boolean joinQueueToLandRequest()
	{
		requestedToLand = false;
		if (state == State.Crashed) return false;
		System.out.println(this + " Requested To land");
		if (state != State.QueuedForLanding)
		{
			System.out.println(this + "Request to land granted, added to runway queue");
			changeState(State.QueuedForLanding);
			joinQueue();
			return true;
		}
		System.out.println(this + "'s Request Denied");
		return false;
	}
	
	public boolean joinQueueToTakeoffRequest()
	{
		System.out.println(this + ": Requested To Takeoff");
		requestedToTakeoff = false;
		if (state == State.Parked)
		{
			changeState(State.QueuedForTakeOff);
			joinQueue();
			return true;
		}
		System.out.println(this + "'s Request Denied");
		return false;
	}
	
	
	public Point approachForLanding()
	{
		changeState(State.Approaching);
		Point approachPoint = null;
		double min = grid.getWidth(), delta = 0;
		for (int i = 0; i < radar.runway.approachPoint.length; i++)
		{
			delta = getDistanceFrom(radar.runway.approachPoint[i]);
			if (delta < min)
			{
				min = delta;
				changeDirectionTowards(approachPoint = radar.runway.approachPoint[i]);
				changeAltitude(2500);
			}
		}
		return approachPoint;
	}
	
	
	public void land()
	{
		if (state == State.Approaching)
		 {
			 Point p = approachForLanding();
			 if (getDistanceFrom(p) < 5)
			 {
				 System.out.println("PREPARE FOR LANDING");
				 changeState(State.Landing);
				 changeDirectionTowards(radar.runway.getTarget());
				 //vector.scale();
			 }
		 }
		 
		 if (state == State.Landing)
		 {
			 if (getDistanceFrom(grid.getOrigin()) > 5)
			 {
				 changeSpeed(100);
				 altitude = 500;
				 if (altitude < 20) altitude = 0;
				 //System.out.println(vector.getMagnitude());
			 }
			 else
			 {
				 //vector.changeMagnitude(0);
				 changeState(State.Landed);
				 handler.removeObject(this);
				 radar.runway.setRunwayOpen();
				 System.out.println("LANDED");
			 }
		 }
	}
	
	public Point taxiForTakeoff()
	{
		System.out.println("PREPARING FOR TAKEOFF");
		changeState(State.Taxiing);
		changeDirectionTowards(grid.getOrigin());
		changeSpeed(20);
		return grid.getOrigin();
		
		
	}
	
	public void takeoff()
	{
		if (state == State.Taxiing)
		{
			Point p = taxiForTakeoff();
			if (getDistanceFrom(p) < 5)
			{
				System.out.println("PREPARE FOR TAKEOFF");
				changeState(State.TakingOff);
				changeDirectionTowards(radar.runway.approachPoint[0]);
				changeAltitude(10000);
			}
		}
		
		if (state == State.TakingOff)
		{
			if (vector.getMagnitude() < 1)
			{
				vector.changeMagnitude(vector.getMagnitude() + .003);
			}
			else
			{
				goToGate();
				changeState(State.Exiting);
				radar.runway.setRunwayOpen();
				System.out.println("Successful takeoff");
			}
		}
		
		if (state == State.Exiting)
		{
				
		}
		
	}
	
	public void borderPatrol()
	{
		if (!inAirspace() && state != State.Exited)
		 {
			 if (state == State.Exiting)
			 {
				 for (Point point : radar.gate.gatePoint)
				 {
					if (getDistanceFrom(point) < 50)
					{
						 changeState(State.Exited);
						 if (isSelected) deselect();
						 return;
					}
				 }
			 }
			 redirect();
		 }
	}
	
	public void logPlaneSpawn()
	{
		gui.printToLogDate(airline + aircraftID +  " " + type + " added");
	}
	
	public void changeState(State s)
	{
		state = s;
		if (isSelected && !handler.multiSelect()) gui.updateSelected(this);
	}
	
	public String getAirline()
	{
		return airline;
	}
	
	public String getPlaneID()
	{
		return airline + aircraftID;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public boolean isArriving()
	{
		if (type == Type.Arriving) return true;
		return false;
	}
	
	public boolean isDeparting()
	{
		if (type == Type.Departing) return true;
		return false;
	}
	
	public boolean isQueued()
	{
		if (state == State.QueuedForLanding || state == State.QueuedForTakeOff) return true;
		return false;
	}
	
	public double changeHeading(double d)
	{
		vector.changeDegree(d);
		return vector.getDegree();
	}
	
	
	public void changeSpeed(double s)
	{
		if (s >= 0) vector.changeMagnitude(Plane.KnotsToMagnitude(s) * radar.timeMutiplier);
	}
	
	public double getSpeed()
	{
		return Plane.magnitudeToKnots(vector.getMagnitude()) * radar.timeMutiplier;
	}
	
	public void changeAltitude(double a)
	{
		if (a >= 0)
		{
			requestedAltitude = a;
			if (requestedAltitude > altitude)
			{
				increasingAltitude = true;
			}
			else if (requestedAltitude < altitude)
			{
				decreasingAltitude = true;
			}
		}
	}
	
	public double getAltitude()
	{
		return altitude;
	}
	
	public double getHeading()
	{
		return vector.getDegree();
	}
	
	public void handleAltitude()
	{
		 if (increasingAltitude)
		 {
			 if (requestedAltitude > altitude)
			 {
				 altitude += climbRate;
			 }
			 else
			 {
				 altitude = requestedAltitude;
				 increasingAltitude = false;
			 }
		 }
		 if (decreasingAltitude)
		 {
			 if (requestedAltitude < altitude)
			 {
				 altitude -= climbRate;
			 }
			 else
			 {
				 altitude = requestedAltitude;
				 decreasingAltitude = false;
			 }
		 }
	}
	
	public boolean isLanding()
	{
		//if (state == State.Approaching || state == State.)
		return false;
	}
	
	public void collisionAvoidance(Boolean isEnabled)
	{
		if (isEnabled && situation)
		{
			for (Plane p : handler.planeList)
			{
				if (p.situation && p != this && this.getDistanceFrom(p) <= 30)
				{
					if (altitude < 8000)
					{
						p.altitude += 2000;
					}
				}
			}
			if (altitude > 4000)
			{
				altitude -= 2000;
			}
		}
	}
	
	public String toString()
	{
		return airline + aircraftID + " " + destination +  " " + (type == Type.Arriving ? "A" : "D");
	}
	
	public String getDestination()
	{
		return destination;
	}
	
	public void setDestination(String d)
	{
		destination = d;
	}
	
	public int GetSpeed()
	{
		return (int)magnitudeToKnots(vector.getMagnitude()/vector.getScalar());
	}
}
