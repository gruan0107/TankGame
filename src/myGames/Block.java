package myGames;

import java.awt.*;
import java.util.Observable;

//
//Design for breakable and unbreakable walls;
//@author Guoyi Ruan
public class Block extends Unit {

    int health = 20;
    int type = 0;

    public Block(int x, int y, int imgTick, int speed, Image[] img, GameEvents events, int type) {
        super(x, y, imgTick, speed, img, events, 1 , 0);
        this.type = type;
    }

    //movement type actions
    @Override
    public void move() {

    }

    @Override
    public void dead() {
        this.setRDone(true);
    }

    //if the wall is breakable, get damage; else return;
    @Override
    public void hitMe(Thing caller) {
        if (caller instanceof Bullet) {
            if (type == 1) {
                return;
            }
            health -= ((Bullet) caller).getDamageTo();
            if (health <= 0) {
                this.setDone(true);
            }
        }
    }

    @Override
    public void itHit(Unit u) {

    }
    //update the situation of breakable and unbreakable walls.
    @Override
    public void update(Observable o, Object arg) {
        GameEvents events = (GameEvents) arg;
        if(events.getType() == 1)
        {
            if(events.getTarget() == this)
            {
                if (events.getCaller() instanceof Unit) {
                    ((Unit) events.getCaller()).itHit(this);
                }
                hitMe((Thing)events.getCaller());
            }
        }
    }
}
