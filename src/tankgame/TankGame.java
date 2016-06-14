/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.*;
import javax.sound.midi.*;
import javax.swing.*;
import myGames.*;

/**
 * Tank game is a 2 players game. The goal of the game is to destroy each other.
 * For left screen player the controls are: wasd for movement and space to fire
 * For right screen player:  arrow keys control movement, enter is fire
 * The mini map shows everything on the map including 2 tanks, life bonus, bullets, wall...
 * Life bar is on the top of the tank.
 *
 * @author Guoyi Ruan
 */
public class TankGame extends Game
{
    private GameSpace screen;
    private ArrayList<ArrayList> everything;
    private ArrayList<Thing> things;
    private ArrayList<PlayerParent> players;
    private int score;
    private Image[]  playerimg[],pickup[], smallexpl, largeexpl, mybullet[];
    private GameController gcontroller;
    private TankEvents events;
    private boolean gameover;
    private boolean destroy = false;
    private boolean twoplayers = true;
    private int lives;
    private URL[] explsoundurl;

    //creates and adds all the game panel to the applet
    //also sets up images, sounds, and creates and initializes state for most
    //variables and objects.
    @Override
    public void init()
    {      
        super.init();
        events = new TankEvents();
        Image[] blockers = new Image[2];
        blockers[0] = getSprite("Resources/tank/wall1.gif");
        blockers[1] = getSprite("Resources/tank/wall2.gif");

        screen = new GameSpace(getSprite("Resources/tank/background.png"), new DrawAbs(), blockers, 640, 480, events);
        add(screen, BorderLayout.CENTER);
        setBackground(Color.white);


        everything = new ArrayList<ArrayList>();
        things = new ArrayList<Thing>();
        everything.add(things);
        players = new ArrayList<PlayerParent>();
        everything.add(players);
        ArrayList<Thing> blocks = screen.getBlocks();
        everything.add(blocks);

        KeyControl keys = new KeyControl(events);
        addKeyListener(keys);
        
        gcontroller = new GameController();
        score = 0;

        gameover = false;

    }
    
    //getting all image files
    @Override
    public void initImages()
    {
        try
        {
            playerimg = new Image[2][60];
            BufferedImage bufferedImage = getBufferedSprite("Resources/tank/tank1.png");
            int width = bufferedImage.getWidth()/60;
            for (int i = 0; i < 60; i++)
            {
                playerimg[0][i] = bufferedImage.getSubimage(i * width, 0, width, bufferedImage.getHeight());
            }
            bufferedImage = getBufferedSprite("Resources/tank/tank2.png");
            for(int i = 0; i < 60; i++)
            {
                playerimg[1][i] = bufferedImage.getSubimage(i * width, 0, width, bufferedImage.getHeight());
            }

            smallexpl = new Image[6];
            bufferedImage = getBufferedSprite("Resources/tank/Explosion_small_strip6.png");
            int smallExplWidth = bufferedImage.getWidth()/6;
            for(int i = 0; i < 6; i++)
            {
                smallexpl[i] = bufferedImage.getSubimage(i * smallExplWidth, 0, smallExplWidth, bufferedImage.getHeight());
            }
            largeexpl = new Image[7];
            bufferedImage = getBufferedSprite("Resources/tank/Explosion_large_strip7.png");
            int largeExplWidth = bufferedImage.getWidth()/7;
            for (int i = 0; i < 7; i++)
            {
                largeexpl[i] = bufferedImage.getSubimage(i * largeExplWidth, 0, largeExplWidth, bufferedImage.getHeight());
            }

            mybullet = new Image[3][60];
            bufferedImage = getBufferedSprite("Resources/tank/weapon0.png");
            int weaponWith = bufferedImage.getWidth()/60;
            for (int i = 0; i < 60; i++)
            {
                mybullet[0][i] = bufferedImage.getSubimage(i * weaponWith, 0, weaponWith, bufferedImage.getHeight());
            }
            bufferedImage = getBufferedSprite("Resources/tank/weapon1.png");
            weaponWith = bufferedImage.getWidth()/60;
            for (int i = 0; i < 60; i++)
            {
                mybullet[1][i] = bufferedImage.getSubimage(i * weaponWith, 0, weaponWith, bufferedImage.getHeight());
            }
            bufferedImage = getBufferedSprite("Resources/tank/weapon2.png");
            weaponWith = bufferedImage.getWidth()/60;
            for (int i = 0; i < 60; i++)
            {
                mybullet[2][i] = bufferedImage.getSubimage(i * weaponWith, 0, weaponWith, bufferedImage.getHeight());
            }

            pickup = new Image[4][1];
            bufferedImage = getBufferedSprite("Resources/tank/pickup.png");
            int pickupWith = bufferedImage.getWidth()/4;
            for (int i = 0; i < 4; i++)
            {
                pickup[i][0] = bufferedImage.getSubimage(i * pickupWith, 0, pickupWith, bufferedImage.getHeight());
            }

        } catch (Exception e)
        {
            System.out.println("Error in getting images: " + e.getMessage());
        }
    }
    
    //getting all sound files
    @Override
    public void initSound()
    {
        try
        {
        Sequence music;
        Sequencer seq;
        URL musicu = TankGame.class.getResource("Resources/tank/Music.mid");
        explsoundurl = new URL[2];
        explsoundurl[0] = TankGame.class.getResource("Resources/snd_explosion1.wav");
        explsoundurl[1] = TankGame.class.getResource("Resources/snd_explosion2.wav");
        
           music =  MidiSystem.getSequence(musicu);
           seq = MidiSystem.getSequencer();
           seq.open();
           seq.setSequence(music);
           seq.start();
           seq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        }
        catch(Exception e)
        {
            System.out.println("Error in midi: " + e.getMessage());
        }
    }

    //this creates Things when needed to make the gameplay pattern
    public class GameController
    {

        private int timer;

        public GameController()
        {
            timer = 0;
        }

        public void timeline()
        {
            switch (timer)
            {
                case 0:
                        players.add(new Tank(300, 400, 15, 6, playerimg[0],
                                events, 30, 10, KeyEvent.VK_A, KeyEvent.VK_D,
                                KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_SPACE,
                                KeyEvent.VK_SHIFT, 10, 5, largeexpl.length, screen, 0));

                        players.add(new Tank(1000, 800, 15, 6, playerimg[1],
                            events, 30, 20, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER,
                            KeyEvent.VK_DELETE, 10, 5, largeexpl.length, screen, 1));

                        screen.setLeftPlayerParent(players.get(0));
                        screen.setRightPlayerParent(players.get(1));
                        players.get(0).setOtherPlayerParent(players.get(1));
                        players.get(1).setOtherPlayerParent(players.get(0));

                    break;

                case 30:

                    things.add(new PowerUp(100, 100, 0, 0, pickup[0], events, 0, players));
                    break;


                case 100:

                    things.add(new PowerUp(600, 700, 0, 0, pickup[1], events, 1, players));
                    break;

                case 200:

                    things.add(new PowerUp(900, 900, 0, 0, pickup[3], events, 3, players));
                    break;

            }

            if (timer == Integer.MAX_VALUE) {
                timer = 1;
            } else {
                timer++;
            }
        }

    }

    //This is the player's tank
    public class Tank extends PlayerParent
    {
        private int startx, starty, spawnDelay;
        private int health = 100;
        private int deathTime = 0;


        public Tank(int x, int y, int imgTick, int speed, Image[] img,
                GameEvents events, int maxdamage, int damageto,
                int left, int right, int up, int down, int fire, int spfire,
                int shotTime, int fastShotTime, int deadTime, GameSpace gameSpace, int type)
        {
            super(x, y, imgTick, speed, img, events, maxdamage, damageto,
                    left, right, up, down, fire, spfire, shotTime, fastShotTime,
                    deadTime, gameSpace, type);

            startx = x;
            starty = y;
            spawnDelay = 30;

        }

        //moves based on the keys pressed, but only with the basic update
        @Override
        public void move()
        {
            if(getMvLeft())
            {
                this.increaseImgTick();
            }

            if(getMvRight())
            {
                this.decreaseImgTick();
            }

            if(getMvUp())
            {
                int heightChange = (int)(Math.sin((2.0D - this.getImgTick() / 30.0D) * Math.PI) * getSpeed());
                int widthChange = (int)(Math.cos((2.0D - this.getImgTick() / 30.0D) * Math.PI) * getSpeed());
                int x1 = this.getX() + widthChange;
                int y1 = this.getY() + heightChange;
                if (!this.gameSpace().valid(this, x1, y1)
                        || (this.getOtherPlayerParent() != null
                        && this.getOtherPlayerParent().isOverlap(this, x1, y1))) {
                    return;
                }
                changeY(heightChange);
                changeX(widthChange);

            }

            if(getMvDown())
            {
                int heightChange = (int)(Math.sin((2.0D - this.getImgTick() / 30.0D) * Math.PI) * getSpeed());
                int widthChange = (int)(Math.cos((2.0D - this.getImgTick() / 30.0D) * Math.PI) * getSpeed());
                int x1 = this.getX() - widthChange;
                int y1 = this.getY() - heightChange;
                Rectangle rectangle = new Rectangle(x1 - this.getWidth()/2, y1 - this.getHeight() / 2, this.getWidth(), this.getHeight());
                if (!this.gameSpace().valid(this, x1, y1)
                        || (this.getOtherPlayerParent() != null
                        && this.getOtherPlayerParent().isOverlap(this, x1, y1))) {
                    return;
                }

                changeY(-heightChange);
                changeX(-widthChange);

            }

            if (getDamage() >= getMax())
            {
                lives--;
                if(getPower() > 0)
                {
                    setPower(getPower() - 1);
                }

                setDone(true);
            }

            if(getShotDelay() > 0)
            {
                changeShotDelay(-1);
            }
        }

        //shoots with button pressed, but only with the basic update
        @Override
        public void action()
        {
            if (getIsSp()) {
                if(getShotDelay() == 0)
                {
                    if(getPower() == 1)
                    {
                        things.add(new Bullet(getX(),getY(), this.getImgTick(), 8, mybullet[1], events, 1, 10, everything, smallexpl, largeexpl, getType(), explsoundurl));
                    } else if (getPower() == 2) {
                        things.add(new Bullet(getX(),getY(), this.getImgTick(), 8, mybullet[2], events, 1, 20, everything, smallexpl, largeexpl, getType(), explsoundurl));
                    }
                    setShotDelay(getShotTime());
                }
            }
            else if(getIsFiring())
            {
                if(getShotDelay() == 0)
                {

                    things.add(new Bullet(getX(),getY(), this.getImgTick(), 8, mybullet[0], events, 1, 5, everything, smallexpl, largeexpl, getType(), explsoundurl));

                    setShotDelay(getShotTime());
                }
            }
            else
            {
                if(getShotDelay() > getFastShotTime())
                {
                    setShotDelay(getFastShotTime());
                }
            }
        }

        //explodes, then set up the scoretable or respawns
        @Override
        public void dead()
        {

            things.add(new Explosion(getX(), getY(), largeexpl, events, explsoundurl[1], this));

        }

        //this makes the plane flash during mercy invincibility
        @Override
        public void draw(Graphics2D g2, ImageObserver obs)
        {
            if (!this.getDone()) {
                super.draw(g2, obs);
                g2.setStroke(new BasicStroke(7));
                g2.setColor(Color.green);
                int startx = this.getX() - this.getWidth() / 2;
                int starty = this.getY() - this.getHeight() / 2 - 20;
                int endx = startx + this.getWidth() * this.health / 100;
                g2.drawLine(startx, starty, endx, starty);
            }
        }

        //starting mercy timer
        @Override
        public void hitMe(Thing t)
        {
            if (t instanceof Bullet && ((Bullet) t).getType() != this.getType()) {
                this.health -= ((Bullet) t).getDamageTo();
                t.itHit(this);
                if (this.health <= 0) {
                    setDone(true);
                }

            }
        }

        //this is what this Thing does to a Unit that hit it
        @Override
        public void itHit(Unit u)
        {

        }
        @Override
        public int getWidth(){
            return super.getWidth() - 20;
        }

        @Override
        public int getHeight(){
            return super.getHeight() - 20;
        }

        public void resetHealth() {
            this.health = 100;
        }

        public void increaseDead() {
            this.deathTime++;
        }

        public int getDeathTime() {
            return this.deathTime;
        }

        public void reset() {
            this.setDone(false);
            this.setX(startx);
            this.setY(starty);
            this.resetHealth();
        }
    }
    
    //adds an event type to GameEvents
    public class TankEvents extends GameEvents
    {


    }

    //Updates all Things and then draws everything
    //when the game is resetting, this method will also 
    @Override
    public void drawAll(int w, int h, Graphics2D g2)
    {
        Thing temp;

        Iterator<ArrayList> it = everything.listIterator();

        try {
        while (it.hasNext())
        {
            ArrayList<Thing> list = it.next();
            Iterator<Thing> it2 = list.listIterator();
            while (it2.hasNext())
            {
                if (gameover)
                {
                    break;
                }
                temp = it2.next();
                temp.update(w, h);

                if (temp.getRDone())
                {
                    it2.remove();
                }
            }
            if (gameover)
            {
                break;
            }
        }
        }
        catch (Exception e) {

        }
        screen.updateBackground();
        screen.drawHere(everything, g2);
        screen.drawBackground(g2);
        screen.drawMiniBackground(g2);
        if (players != null && players.size() > 0) {
            Font font = new Font("Serif", Font.PLAIN, 50);
            g2.setFont(font);
            g2.setColor(Color.RED);
            g2.drawString(String.valueOf(((Tank)players.get(1)).getDeathTime()), 280, 40);
            g2.setColor(Color.BLUE);
            g2.drawString(String.valueOf(((Tank)players.get(0)).getDeathTime()), 340, 40);
        }

        if(destroy)
        {
            it = everything.listIterator();
            while(it.hasNext())
            {
                Iterator<Thing> it2 = it.next().listIterator();
                while(it2.hasNext())
                {
                    it2.next();
                    it2.remove();
                }
                
            }
            
            gcontroller = new GameController();
            destroy = false;
            gameover = false;
            score = 0;
            events.deleteObservers();
            this.requestFocus();
        }
        
        gcontroller.timeline();
    }

   
    public static void main(String[] args)
    {
        final TankGame game = new TankGame();
        game.init();
        final JFrame f = new JFrame("Tank Wars");
        f.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                f.dispose();
                System.exit(0);
            }
        });
        f.getContentPane().add("Center", game);
        f.pack();
        f.setSize(new Dimension(640, 502));
        f.setVisible(true);
        f.setResizable(false);
        game.start();
    }
}