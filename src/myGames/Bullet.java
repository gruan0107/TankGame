/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Lowell
 */
public class Bullet extends Unit
{

    private ArrayList<ArrayList> everything;
    private Image[] smallExplosionImg;
    private Image[] largeExplosionImg;
    private int type;
    private URL[] soundUrl;

    public Bullet(int x, int y, int imgTick, int speed, Image[] img,
                    GameEvents events, int maxdamage, int damageto,
                    ArrayList<ArrayList> everything, Image[] smallExplosionImg,
                    Image[] largeExplosionImg, int type, URL[] soundUrl)
    {
        super(x, y, imgTick, speed, img, events, maxdamage, damageto);
        this.everything = everything;
        this.smallExplosionImg = smallExplosionImg;
        this.largeExplosionImg = largeExplosionImg;
        this.type = type;
        this.soundUrl = soundUrl;
    }

    @Override
    public void hitMe(Thing caller) {

    }
    // location changes
    @Override
    public void move() {
        int heightChange = (int)(Math.sin((2.0D - this.getImgTick() / 30.0D) * Math.PI) * getSpeed());
        int widthChange = (int)(Math.cos((2.0D - this.getImgTick() / 30.0D) * Math.PI) * getSpeed());
        this.setX(this.getX() + widthChange);
        this.setY(this.getY() + heightChange);

        for (Thing thing : (ArrayList<Thing>) everything.get(2)) {
            if (this.collision(thing)) {
                getEvents().setCollision(this, thing);
            }
        }
        for (Thing thing : (ArrayList<Thing>) everything.get(1)) {
            if (this.collision(thing) && ((PlayerParent)thing).getType() != this.getType()) {
                getEvents().setCollision(this, thing);
            }
        }
    }

    @Override
    public void dead() {
        this.setRDone(true);
    }

    @Override
    public void itHit(Unit u)
    {
        if (u instanceof Block) {
            setDone(true);
            everything.get(0).add(new Explosion(getX(), getY(), smallExplosionImg, this.getEvents(), this.soundUrl[0], null));
        } else if (u instanceof PlayerParent) {
            if (this.getType() != ((PlayerParent) u).getType()) {
                setDone(true);
                everything.get(0).add(new Explosion(getX(), getY(), smallExplosionImg, this.getEvents(), this.soundUrl[0], null));
            }
        }

    }
    //update bullets situation.
    @Override
    public void update(Observable o, Object arg) {
        GameEvents events = (GameEvents) arg;
        if(events.getType() == 1)
        {
            if(events.getTarget() == this)
            {
                hitMe((Thing) events.getTarget());
            }
        }
    }

    public int getType() {
        return type;
    }
}
