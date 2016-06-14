/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import tankgame.TankGame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Observable;

/**
 *There are three type of powerup bonus;
 * 2 different shell and 1 life health bonus
 * @author Guoyi Ruan
 */
public class PowerUp extends Unit
{
    private int type;
    private int counter = 0;
    private ArrayList<PlayerParent> tanks;

    public PowerUp(int x, int y, int imgTick, int speed, Image[] img,
                   GameEvents events, int type, ArrayList<PlayerParent> tanks)
    {
        super(x, y, imgTick, speed, img, events, 0, 0);
        this.type = type;
        this.tanks = tanks;
    }

    @Override
    public void move() {
        for (Thing thing : tanks) {
            if (this.collision(thing)) {
                getEvents().setCollision(this, thing);
            }
        }
    }

    @Override
    public void dead() {
        setRDone(true);
    }

    @Override
    public void hitMe(Thing caller) {

    }

    @Override
    public void itHit(Unit u)
    {
        if (type == 0) {
            ((PlayerParent) u).setPower(1);
        } else if (type == 1) {
            ((PlayerParent) u).setPower(2);
        } else {
            ((TankGame.Tank) u).resetHealth();
        }
        setDone(true);
    }

    @Override
    public void draw(Graphics2D g2, ImageObserver obs)
    {
        if (counter < 15) {
            int w = this.getImage().getWidth(obs);
            int h = this.getImage().getHeight(obs);
            AffineTransform trans = new AffineTransform();

            trans.translate(this.getX() - w / 2, this.getY() - h / 2);

            if (!this.getRDone()) {
                g2.drawImage(this.getImage(), trans, obs);
            }
        }

        if (counter > 30) {
            counter = 0;
        } else {
            counter++;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        GameEvents events = (GameEvents) arg;
        if(events.getType() == 1)
        {
            if(events.getCaller() == this)
            {
                itHit((Unit) events.getTarget());
            }
        }
    }
}
