/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import tankgame.TankGame;

import java.awt.Image;
import java.net.URL;
import javax.sound.sampled.*;

/**
 *
 * @author Guoyi Ruan
 */
public class Explosion extends Thing
    {
        private boolean firstTime = true;
        private TankGame.Tank tank;
        
        public Explosion(int x, int y, Image[] img, GameEvents events, URL snd, TankGame.Tank tank)
        {
            super(x, y, 0, 0, img, events);
            this.tank = tank;
            try
            {
                AudioInputStream explSound;
                Clip clip;
                explSound = AudioSystem.getAudioInputStream(snd);
                clip = AudioSystem.getClip();
                clip.open(explSound);
                clip.start();
            }
            catch(Exception e)
            {
                System.out.println("Error in explosion sound: " + e.getMessage());
            }
            
        }
        
        @Override
        public void move()
        {
            if(getImgTick() != 0 || !firstTime)
            {
                firstTime = false;
                increaseImgTick();
            }
            else
            {
                setDone(true);
                if (this.tank != null) {
                    this.tank.reset();
                    this.tank.increaseDead();
                }
            }
        }
        
        @Override
        public void dead()
        {
            setRDone(true);
        }
        
        @Override
        public void itHit(Unit u)
        {
        }
        
    }
