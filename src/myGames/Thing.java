/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.ArrayList;

/**
 *
// * @author Guoyi Ruan
 * This class is the root class of every game object
 */
abstract public class Thing
{
    //x and y coordinate from the top right corner
    private int x, y;
    //speed in pixels per frame
    private int speed;
    //the image of the object
    private Image[] img;
    //determines if the object should execute its dead method
    private boolean done;
    //marks the object for deletion
    private boolean reallyDone;
    //an Observable object that every game object needs to access
    private GameEvents events;
    //determines which image is currently showing (for animation)
    private int imgTick;

    
    public Thing(int x, int y, int imgTick, int speed, Image[] img,
            GameEvents events)
    {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.img = img;
        this.events = events;
        done = false;
        reallyDone = false;
        this.imgTick = imgTick;
    }

    //draws the object with the given Graphics2D performimg rotation and
    //translation to get the proper facing and postion
    public void draw(Graphics2D g2, ImageObserver obs)
    {
        int w = img[imgTick].getWidth(obs);
        int h = img[imgTick].getHeight(obs);
        AffineTransform trans = new AffineTransform();

        trans.translate(x - w / 2, y - h / 2);

        if (!done)
        {
            g2.drawImage(img[imgTick], trans, obs);
        }

    }

    //called every frame, calls dead or action and move
    public void update(int w, int h)
    {
        if (done)
        {
            dead();
        } else
        {
            action();

            move();
        }
    }

    //movement type actions
    abstract public void move();

    //action taken when dead
    abstract public void dead();
    
    //what to do when the object hits another object
    abstract public void itHit(Unit u);

    //dummy for action type things (so it does not need to be overridden when
    //not used
    public void action()
    {
    }

    //checks for collision by checking the intersection of the images + eps
    public boolean collision(Thing thing)
    {
        if(getDone())
        {
            return false;
        }

        if (y > thing.getY() - thing.getHeight()/2
                && y < thing.getY() + thing.getHeight()/2
                && x > thing.getX() - thing.getWidth()/2
                && x < thing.getX() + thing.getWidth()/2)
        {
            return true;
        } else
        {
            return false;
        }
    }

    //various get and set type methods for parameters after this point
    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public void changeX(int change)
    {
        this.x += change;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    public void changeY(int change)
    {
        this.y += change;
    }

    public int getSpeed()
    {
        return speed;
    }

    public Image getImage()
    {
        return img[imgTick];
    }

    public void setDone(boolean done)
    {
        this.done = done;
    }
    
    public boolean getDone()
    {
        return done;
    }
    
    public void setRDone(boolean rdone)
    {
        reallyDone = rdone;
    }
    
    public boolean getRDone()
    {
        return reallyDone;
    }
    
    public int getHeight()
    {
        return img[imgTick].getHeight(null);
    }
    
    public int getWidth()
    {
        return img[imgTick].getWidth(null);
    }
    
    public GameEvents getEvents()
    {
        return events;
    }

    public void increaseImgTick() {
        this.imgTick++;
        if (this.imgTick >= this.img.length) {
            this.imgTick = 0;
        }
    }

    public void decreaseImgTick() {
        this.imgTick--;
        if (this.imgTick < 0) {
            this.imgTick = this.img.length-1;
        }
    }

    public int getImgTick() {
        return imgTick;
    }

    public boolean isOverlap(Thing thing, int x, int y) {

        Rectangle rectangle = new Rectangle(this.x - this.getWidth()/2 - thing.getWidth()/2, this.y-this.getHeight()/2-thing.getHeight()/2, this.getWidth()+thing.getWidth(), this.getHeight()+thing.getHeight());
        return rectangle.contains(x, y);

    }

}