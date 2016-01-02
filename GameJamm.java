//DO CS PREP ASSIGNMENT
//do not replace asteroids for all hits; only asteroid self his and out of bounds //DONE
//remove missile if it is out of bounds
//different asteroid colours
//levels??
//score edit
//menu
//change ship sprite? like change of explosion sprite ayyy
//life reduction on explosion impact

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Random;

public class GameJamm extends JFrame implements ActionListener{ //window settings
	Timer myTimer;   
	GamePanel game;
		
    public GameJamm() {
		super("NOT YOUR AVERAGE SPACE GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,650);

		myTimer = new Timer(10, this);	 // trigger every 10 ms
		
		game = new GamePanel(this);
		add(game);

		setResizable(false);
		setVisible(true);
    }
	
	public void start(){
		myTimer.start();
	}

	public void actionPerformed(ActionEvent evt){
		game.move();
		game.repaint();
	}

    public static void main(String[] arguments) {
		GameJamm frame = new GameJamm();		
    }
}

class GamePanel extends JPanel implements KeyListener{ //stuff that happens in the game
	public Ship player; //keeps track of player's location
	private boolean []keys; //keyboard keys
	private Image back; //background picture for the game
	private GameJamm mainFrame; //mainframe that allows communication between it and gamepanel
	public static Image ship, redAsteroid, greyAsteroid, greenAsteroid, missilePic;
	
	public static ArrayList<Asteroid> activeAsteroids; //list of asteroids on screen
	public static ArrayList<Missile> missiles; //your missiles on board
	public static ArrayList<Explosion> explosions; //explosions created
	
	public static ArrayList<Image> explosionPics;
	
	public static int movetimer; //keeps track of how many millisecens has passed 
	public static int missiletimer; //timer that controls when to create a new missile if you continuously press spacebar
	public static int score; //your score
	public static int life; //how many lives you have left
	public static int level; //what level (out of 3) you are on
	
	
	public GamePanel(GameJamm m){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		//back = new ImageIcon("OuterSpace.jpg").getImage();
		
		missilePic = new ImageIcon("missile.png").getImage();
			
		mainFrame = m;
	    score = 0;
		life = 3;
		//level = 1;
		movetimer = 0;
		missiletimer = 0;
		activeAsteroids = new ArrayList<Asteroid>();
		missiles = new ArrayList<Missile>();
		explosionPics = new ArrayList<Image>();
		explosions = new ArrayList<Explosion>();
		player = (new Ship(40,200, 0));
		
		setSize(800,570);
        addKeyListener(this);
        
        
       	ship = new ImageIcon("whiteship2.png").getImage(); 
       	for (int i = 1; i < 17; i++){
			explosionPics.add(new ImageIcon("explosion"+i+".png").getImage());
		}
		
       	loadAsteroids();
	}
	
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    
    public void loadAsteroids(){
		Random randomGenerator = new Random();
		
		ArrayList<Point> asteroidPoints = new ArrayList<Point>();
		for (int i = 0; i <3; i++){
			asteroidPoints.add(new Point(1+100*i, 0));
		}
		
		for (Point aP : asteroidPoints){ 
			activeAsteroids.add(new Asteroid(aP.x, aP.y, randomGenerator.nextInt(10)-5, randomGenerator.nextInt(10), randomGenerator.nextInt(3)));
		}
		/**activeAsteroids.add(new Asteroid(10, 0, 1, 1, c));
		activeAsteroids.add(new Asteroid(400, 0, -1, 1, c));**/
	}
	
	public void move(){
		Random randomGenerator = new Random();
		int moreAsteroids = 0;
		
		if(keys[KeyEvent.VK_RIGHT] ){
			player.playerx += 5;
		}
		if(keys[KeyEvent.VK_LEFT] ){
			player.playerx -= 5;
		}
		if(keys[KeyEvent.VK_UP] ){
			player.playery -= 5;
		}
		if(keys[KeyEvent.VK_DOWN] ){
			player.playery += 5;
		}
		
		if(keys[KeyEvent.VK_SPACE] ){ //fire baguettes when you press space
			if (missiletimer%50== 0 ){ //when you have space bar continuously pressed you can only fire one missile every 50 ms
				missiles.add(new Missile(player.playerx+2, player.playery)); //creates new missile at your (x,y), adds to list
				missiles.add(new Missile(player.playerx+48, player.playery)); //creates new missile at your (x,y), adds to list  
			}
			missiletimer+=1; //to make sure you cant fire missiles continously if you press spacebar continuously
		}
		
		else{
			missiletimer = 0; //resets missiletimer at 0 so that when space is pressed a missile is fired immediately
		}
		
		for (int m = 0; m < missiles.size();m++){ //if there are missiles flying on screen
			missiles.get(m).posy-=3; //increases y coordinate of your missiles by 5
		}
		
		if (movetimer%5==0){
			for (Asteroid a : activeAsteroids){
				a.ax += a.directionx;
	        	a.ay += a.directiony;
	        }
		}
		ArrayList<Asteroid> fallenAsteroids= new ArrayList<Asteroid>();
		if (activeAsteroids.size()>=1){ //if there are asteroids flying on screen
			for (Asteroid a:activeAsteroids){ 
				for (Asteroid b : activeAsteroids){ //checks if asteroid hits another asteroid
					if ((a!=b) && (a.ax>=b.ax && a.ax<=b.ax+10
					&& a.ay>=b.ay && a.ay<=b.ay+10)){
						fallenAsteroids.add(a);
						fallenAsteroids.add(b);
						explosions.add(new Explosion(a.ax, a.ay, 1));
						moreAsteroids +=2;
					}	
				}
				if (a.ax <= 0 || a.ax>=getWidth() || a.ay >= getHeight()){
					fallenAsteroids.add(a);
					moreAsteroids +=1;
				}
				
				if (a.ax>=player.playerx && a.ax<=player.playerx+70 //collide with ship
					&& a.ay>=player.playery && a.ay<=player.playery+74){ 
					fallenAsteroids.add(a);
					explosions.add(new Explosion(a.ax, a.ay, 1));
					life -= 1;
					System.out.print("You got hit! Current lives: "+life+"\n");
					moreAsteroids +=1;
				}
			}
		}
		
		ArrayList<Missile> shrapnel = new ArrayList<Missile>(); //missiles to be removed if they hit something
		
		if (missiles.size()>=1){ //if there are missiles flying on screen
			for (Missile mi:missiles){ //checks for every missile flying
				for (Asteroid a : activeAsteroids){ 
					//if the missile touches the asteroid, that missile and asteroid will be removed
					for (int i = 0; i < 18; i++){ //*********
						if (mi.posx+i>=a.ax && mi.posx+i<=a.ax+10
						&& mi.posy>=a.ay && mi.posy<=a.ay+10){
							fallenAsteroids.add(a);
							shrapnel.add(mi);
							explosions.add(new Explosion(a.ax, a.ay, 1));
							score +=10;
							System.out.print("+10 to score! Current score: "+score+"\n");
							i = 18;
						}
					}	
				}
			}
		}
		
		missiles.removeAll(shrapnel);
		
		ArrayList<Explosion> finishedExplosions = new ArrayList<Explosion>();
		
		if (movetimer%5==0){
			for (Explosion e : explosions){
				if (e.frame+1 < 17){
					e.frame += 1;
				}
				else{
					finishedExplosions.add(e);
				} 		
			}
		}
		
		explosions.removeAll(finishedExplosions);
		activeAsteroids.removeAll(fallenAsteroids);
		
		for (int i= 0; i < moreAsteroids; i++){
			activeAsteroids.add(new Asteroid (randomGenerator.nextInt(801), 0, randomGenerator.nextInt(10)-5, randomGenerator.nextInt(10), randomGenerator.nextInt(3)));
		}
		
		/**for (Asteroid f : fallenAsteroids){
			activeAsteroids.remove(f);
			Image c = null;
			activeAsteroids.add(new Asteroid (randomGenerator.nextInt(801), 0, randomGenerator.nextInt(10)-5, randomGenerator.nextInt(10), c));
		}**/
			
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		Point offset = getLocationOnScreen();
		//System.out.println("("+(mouse.x-offset.x)+", "+(mouse.y-offset.y)+")");
		movetimer+=1; 
	}
	
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    
    public void paintComponent(Graphics g){ 	
    	g.setColor(new Color(222,222,255));  
        g.fillRect(0,0,getWidth(),getHeight());   
    		
        for (Asteroid a : activeAsteroids){
        	if (a.type==0){
        		g.setColor(new Color(255,111,111)); 
        	}
        	else if (a.type==1){
        		g.setColor(new Color(255, 111, 183)); 
        	}
        	else if (a.type==2){
        		g.setColor(new Color(111,111,255)); 
        	}
        	g.fillOval(a.ax, a.ay, 10, 10);
        }
        
        for (int i = 0; i<missiles.size();i++){ //draws all missiles
    		g.drawImage(missilePic,missiles.get(i).posx, missiles.get(i).posy,this);
    	}
        
        g.drawImage(ship, player.playerx, player.playery, this);
        
        if (explosions.size()>0){ //draws explosions
        	for(Explosion e: explosions){
        		g.drawImage(explosionPics.get(e.frame-1), e.ex, e.ey, this);
        	}
        }
    }
}