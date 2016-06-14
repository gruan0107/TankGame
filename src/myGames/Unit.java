/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Image;
import java.util.*;

/**
 *
 * @author Guoyi Ruan
 */
abstract public class Unit extends Thing implements Observer
{

    //damage is the object current damage
    //in general, when the current damage >= to the max damage, the object is dead
    private int damage;
    private int maxdamage;
    
    //damageto is the amount of damage done to an object colliding with this one
    //in general
    private int damageto;


    public Unit(int x, int y, int imgTick, int speed, Image[] img,
            GameEvents events, int maxdamage, int damageto)
    {
        super(x, y, imgTick, speed, img, events);
        this.damage = 0;
        this.maxdamage = maxdamage;
        this.damageto = damageto;
        
        //adds itself to the Observer list
        events.addObserver(this);
    }
    
    //calls when a Thing hits this Unit
    abstract public void hitMe(Thing caller);

    //this is what this Thing does to a Unit that hit it
    @Override
    public void itHit(Unit u)
    {
        u.changeDamage(getDamageTo());
    }


    
    public int getDamage()
    {
        return damage;
    }
    
    public void changeDamage(int change)
    {
        damage += change;
    }
    
    public void setDamage(int damage)
    {
        this.damage = damage;
    }
    
    public int getMax()
    {
        return maxdamage;
    }
    
    public int getDamageTo()
    {
        return damageto;
    }
}
