import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Radar extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1040, HEIGHT = WIDTH; // 16 * 9;
	int center = WIDTH/2;
	
	
	public String[] airlineCodeArray = {"AS", "AQ", "HP", "AA", "AP", "CO", "DL", "HA", "YX", "NW", "WN", "FF", "TW", "UA", "US", "FL"};
	public Color arrivalSpawnColor, departureSpawnColor, airspaceColor, gateColor;
	
	Random rand;
	
	private Thread thread;
	private boolean running;
	public Handler handler;
	private Grid grid;
	public RadarHand rh;
	public Runway runway;
	public Gate gate;
	public Mouse mouse;
	public KeyInput keyInput;
	
	File audio;
	
	
	
	int degree = 0;
	int r = 500;
	int green = 0;
	int vel = 1;
	
	
	int numOfPlanes = 0;
	double S = 1;
	
	
	Plane selectedPlane;
	
	TestWindow tw;
	//buttonInterface jj;
	ControlWindow gui;
	int timeMutiplier ;
	
	int fps;
	boolean showHUD, showFPS, showPlaneSum, collisionAvoidanceOn, soundOn, showCollisionBounds, showSelectedSum, showQSum;
	
	public Radar()
	{
		audio = new File("alarm.wav");
		//playSound(audio);
		
		running = false;
		rand = new Random();
		handler = new Handler(this);
		
		timeMutiplier = 1;
		
		arrivalSpawnColor =  Color.black;
		airspaceColor = Color.black;
		departureSpawnColor = Color.black;
		gateColor = Color.black;
		
		grid = new Grid(WIDTH, HEIGHT, this);
		handler.addObject(grid);
		
		new Window(WIDTH, HEIGHT, "ATC Display", this);
		
		mouse = new Mouse(handler, this);
		addMouseListener(mouse);
		
		keyInput = new KeyInput(handler, this);
		addKeyListener(keyInput);
		
		runway =new Runway(grid.getOriginX()-4, grid.getOriginY()-50, 45,this);
		handler.addObject(runway);
		
		rh = new RadarHand(grid.getOrigin(), this);
		handler.addObject(rh);
		
		gate = new Gate(grid.getOriginX(), grid.getOriginY(), this);
		handler.addObject(gate);
		
		//selectedPlane = new Plane(grid.getOriginX()-100, grid.getOriginY(), 200, 250, 180, this);
		
		//selectedPlane = new Plane(1, HEIGHT/2, 300, new Vector(S, 45), this);
		//handler.addObject(selectedPlane);
		//handler.addObject(new Plane(200, 1, 200, new Vector(.05, 135), this));
		//handler.addObject(new Plane(90, 1, 200, new Vector(.05, 135), this));
		
		for (int i = 0; i < numOfPlanes; i++)
		{
			handler.addObject(new Plane(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), 10000, new Vector(S, rand.nextInt(360)), this));
			//int randX = rand.nextInt(WIDTH), randY = rand.nextInt(HEIGHT);
			//handler.addObject(new Plane(randX, randY, 200, new Vector(S, Vector.getDegree(grid.getOriginX() - randX, randY - grid.getOriginY())), this));
			//handler.addObject(new Plane(randX, randY, 200, new Vector(S, Vector.getDegree(grid.getOriginX() - randX, grid.getOriginY() - randY*-1)), this));
		}
		
		fps = 0;
		
		showFPS = true;
		showPlaneSum = true;
		showHUD = false;
		collisionAvoidanceOn = false;
		soundOn = true;
		showCollisionBounds = true;
		showSelectedSum = true;
		showQSum = true;
		
		
		/*
		selectedPlane = new Plane(WIDTH/3, HEIGHT-200, ID.Plane, this, Color.yellow);
		handler.addObject(selectedPlane);
		handler.addObject(new Plane(WIDTH/2, HEIGHT/2-40, ID.Plane, this, Color.red));
		handler.addObject(new Plane(WIDTH/4, HEIGHT/3-40, ID.Plane, this, Color.magenta));
		*/
		
		//tw = new TestWindow(WIDTH, HEIGHT, "ATC Controls", this);
		//jj = new buttonInterface(this);
		gui = new ControlWindow(this);
		
		
	}
	
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop()
	{
		try
		{
			thread.join();
			running = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	 //Runtime loop
	public void run()
	{
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while (running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) /ns;
			lastTime = now;
			
			while (delta >= 1)
			{
				update();
				delta--;
			}
			
			if (running) render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer+= 1000;
				//System.out.println("FPS: " + frames);
				fps = frames;
				frames = 0;
			}
		}
	}
	
	private void update()
	{
		handler.update();
		//tw.update();/////////////
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		drawBackground(g);
		
		handler.render(g);
		
		if (showHUD) drawHUD(g);
	
		g.dispose();
		bs.show();
	}
	
	public void displayFPS(Graphics g)
	{
		Color color;
		if (showFPS)
		{
			if (fps >= 60) color = Color.green;
			else if (fps >= 30) color = Color.yellow;
			else color = Color.red;
			g.setColor(color);
			g.drawString("FPS: " + fps, 10, 20);
		}
	}
	
	public void displayPlaneSum(Graphics g)
	{
		if (showPlaneSum) g.drawString("Planes: " + handler.planeList.size(), 10, 30);
	}
	
	public void displayRunwayQ(Graphics g)
	{
		if (showQSum) g.drawString("Queued at Runway: " + runway.getQSize(), 10, 40);
	}
	
	public void displayTotalSelected(Graphics g)
	{
		if (showSelectedSum) g.drawString("Total Selected: " + handler.getTotalSelected(), 10, 50);
	}
	
	
	public Handler getHandler()
	{
		return handler;
	}
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public RadarHand getRH()
	{
		return rh;
	}
	
	public static int polarToX(double r, double degree)
	{
		return (int)(r * Math.cos(Math.toRadians(degree)));
	}
	
	public static int polarToY(double r, double degree)
	{
		return (int)(r * Math.sin(Math.toRadians(degree)));
	}
	
	public void drawBackground(Graphics g)
	{
		g.setColor(arrivalSpawnColor);
		g.fillRect(0, 0,  WIDTH, HEIGHT);
		g.setColor(Color.black);
		g.fillOval(grid.getOriginX()- 500, grid.getOriginY() - 500, 1000, 1000);
		g.setColor(departureSpawnColor);
		g.fillOval(grid.getOriginX()- 100, grid.getOriginY() - 100, 200, 200);
	}
	
	public void drawHUD(Graphics g)
	{
		displayFPS(g);
		displayPlaneSum(g);
		displayRunwayQ(g);
		displayTotalSelected(g); //EXPENSIVE
	}
	
	public void playSound(File sound)
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			clip.start();
			
			Thread.sleep(clip.getMicrosecondLength()/1000);
			System.out.println("SOund Alarm!");
		}
		catch(Exception e)
		{
			System.out.println("FILE ERROR");
		}
	}
	
	 public void addToArrivingList(Plane p)
	 {
		 gui.addToArrivingList(p);
	 }
	
	 public void removeFromArrivingList(Plane p)
	    {
	    	gui.removeFromArrivingList(p);
	    }
	 
	 public void addToDepartingList(Plane p)
	 {
		 gui.addToDepartingList(p);
	 }
	 
	 public void removeFromDepartingList(Plane p)
	 {
		 gui.removeFromDepartingList(p);
	 }
	 
	 public void addToRunwayQueueList(Plane p)
	 {
		 gui.addToRunwayQueueList(p);
	 }
	 
	 public void removeFromRunwayQueueList(Plane p)
	 {
		 gui.removeFromRunwayQueueList(p);
	 }
	 
	 public void popFromRunwayQueueList()
	 {
		 gui.popFromRunwayQueue();
	 }
	
	
	
	
	
	
}
