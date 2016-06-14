/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 * This is the space in which the game object live.
 * It draws the background and all objects.
 * This exact way in which the objects are drawn is determined by the DrawType
 * class
 * @author Guoyi
 */
public class GameSpace extends JPanel
{
    private int width = 1280;
    private int height = 1280;
    private int leftXOffset = 0;
    private int leftYOffset = 0;
    private int rightXOffset = 0;
    private int rightYOffset = 0;
    final private int screenWidth;
    final private int screenHeight;
    private BufferedImage background;
    private Image miniMap;
    private PlayerParent leftPlayerParent;
    private PlayerParent rightPlayerParent;
    private ArrayList<Thing> blocks = new ArrayList<>();
    private Image[][] blockImage;
    private GameEvents events;
    private Image backgroundImg;
    private DrawType drawer;
    private int miniWidth;
    private int miniHeight;

    public GameSpace(Image background, DrawType drawer, Image[] blockImage, int screenWidth, int screenHeight, GameEvents events)
    {
        super();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.drawer = drawer;
        this.blockImage = new Image[2][1];
        this.blockImage[0][0] = blockImage[0];
        this.blockImage[1][0] = blockImage[1];
        this.events = events;
        this.backgroundImg = background;
        this.background = toBufferedImage(background);
        this.miniWidth = this.background.getWidth() / 10;
        this.miniHeight = this.background.getHeight() / 10;
    }

    public void drawMiniBackground(Graphics2D g2) {
        miniMap = this.background.getScaledInstance(this.miniWidth, this.miniHeight, Image.SCALE_FAST);
        g2.drawImage(miniMap, (this.screenWidth - this.miniWidth)/2, this.screenHeight-this.miniHeight, null);
    }
    public void drawBackground(Graphics2D g2)
    {
        if (leftPlayerParent != null) {
            int newX = leftPlayerParent.getX() - screenWidth / 4;
            if (newX < 0) {
                leftXOffset = 0;
            } else if (newX > this.width - screenWidth / 2) {
                leftXOffset = this.width - screenWidth / 2;
            } else {
                leftXOffset = newX;
            }

            int newY = leftPlayerParent.getY() - screenHeight / 2;
            if (newY < 0) {
                leftYOffset = 0;
            } else if (newY > this.height - screenHeight) {
                leftYOffset = this.height - screenHeight;
            } else {
                leftYOffset = newY;
            }
        }

        if (rightPlayerParent != null) {
            int newX = rightPlayerParent.getX() - screenWidth / 4;
            if (newX < 0) {
                rightXOffset = 0;
            } else if (newX > this.width - screenWidth / 2) {
                rightXOffset = this.width - screenWidth / 2;
            } else {
                rightXOffset = newX;
            }

            int newY = rightPlayerParent.getY() - screenHeight / 2;
            if (newY < 0) {
                rightYOffset = 0;
            } else if (newY > this.height - screenHeight) {
                rightYOffset = this.height - screenHeight;
            } else {
                rightYOffset = newY;
            }
        }
        g2.drawImage(background.getSubimage(leftXOffset, leftYOffset, screenWidth / 2, screenHeight), 0, 0, this);
        g2.drawImage(background.getSubimage(rightXOffset, rightYOffset, screenWidth / 2, screenHeight), screenWidth / 2, 0, this);
        g2.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);

    }


    //goes through and draws everything
    public void drawHere(ArrayList<ArrayList> everything, Graphics2D g2)
    {
        Graphics2D bGr = this.background.createGraphics();
        Thing temp;
        Iterator<ArrayList> it = everything.subList(0,2).listIterator();
        while (it.hasNext())
        {
            Iterator<Thing> it2 = it.next().listIterator();
            while (it2.hasNext())
            {
                temp = it2.next();
                temp.draw(bGr, null);
            }
        }
        bGr.dispose();
    }

    public void setDrawType(DrawType drawer)
    {
        this.drawer = drawer;
    }
    
    public DrawType getDrawType()
    {
        return drawer;
    }

    private BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();

        for (int i = 0; i < this.width; i+=img.getWidth(null)) {
            for (int j = 0; j < this.height; j += img.getHeight(null)) {
                int width = Math.min(img.getWidth(null), this.width - i);
                int height = Math.min(img.getHeight(null), this.height - j);
                bGr.drawImage(img, i, j, width, height, null);
            }
        }
        int blockWidth =blockImage[0][0].getWidth(null);

        for (int i = 0; i < this.width; i+=blockWidth) {
            blocks.add(new Block(i + blockWidth / 2, blockWidth / 2, 0, 0, blockImage[1], this.events, 1));
            blocks.add(new Block(i + blockWidth / 2, this.height-blockWidth/2, 0, 0, blockImage[1], this.events, 1));
        }
        for (int j = 0; j < this.height; j+=blockImage[0][0].getHeight(null)) {
            blocks.add(new Block(blockWidth / 2, j + blockWidth / 2, 0, 0, blockImage[1], this.events, 1));
            blocks.add(new Block(this.height-blockWidth/2, j + blockWidth / 2, 0, 0, blockImage[1], this.events, 1));

        }

        for(int j = 0; j < 384; j+=blockImage[1][0].getHeight(null)) {
            blocks.add(new Block(608 + blockWidth / 2, j + 3 * blockWidth / 2, 0, 0, blockImage[1], this.events, 1));
            blocks.add(new Block(640 + blockWidth / 2, j + 3 * blockWidth / 2, 0, 0, blockImage[1], this.events, 1));
            blocks.add(new Block(608 + blockWidth / 2, 864+j + blockWidth / 2, 0, 0, blockImage[1], this.events, 1));
            blocks.add(new Block(640 + blockWidth / 2, 864+j + blockWidth / 2, 0, 0, blockImage[1], this.events, 1));
        }
        for (int i = 0; i < 224; i+=blockImage[1][0].getWidth(null)) {
            blocks.add(new Block(this.width - 3 * blockWidth / 2 - i, 160 + blockWidth / 2, 0, 0, blockImage[1], this.events, 1));
            blocks.add(new Block(i+ 3 * blockWidth / 2, 1120 + blockWidth / 2 ,0, 0, blockImage[1], this.events, 1));

        }

        for (int i = 0; i < 128; i+=blockImage[0][0].getWidth(null)) {
            blocks.add(new Block(192+i + blockWidth / 2,160 + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
            blocks.add(new Block(192+i + blockWidth / 2,256 + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
            blocks.add(new Block(this.width - 3 * blockWidth / 2-160-i,1120 + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
            blocks.add(new Block(this.width - 3 * blockWidth / 2-160-i,1024 + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));

        }

        for(int j = 0; j < 128; j+=blockImage[0][0].getHeight(null)) {
            blocks.add(new Block(192 + blockWidth / 2, 160+j + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
            blocks.add(new Block(960 + blockWidth / 2, this.width - 3 * blockWidth / 2-96-j, 0, 0, blockImage[0], this.events, 0));
            blocks.add(new Block(864 + blockWidth / 2, 288+j + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
            blocks.add(new Block(384 + blockWidth / 2, 864+j + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
        }

        for (int i = 0; i < 512; i+=blockImage[0][0].getWidth(null)) {
            blocks.add(new Block(384+i + blockWidth / 2,416 + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
            blocks.add(new Block(384+i + blockWidth / 2,832 + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
        }

        for (int i = 0; i < 192; i+=blockImage[0][0].getWidth(null)) {
            for(int j = 0; j < 128; j+=blockImage[0][0].getHeight(null)){
                blocks.add(new Block(blockWidth / 2 + i , 576 + j + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
                blocks.add(new Block(this.width - blockWidth / 2 - i , 576 + j + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
                blocks.add(new Block(384 + blockWidth / 2 + i , 576 + j + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
                blocks.add(new Block(this.width - 384 + blockWidth / 2 - i , 576 + j + blockWidth / 2, 0, 0, blockImage[0], this.events, 0));
            }
        }

        for (Thing block : blocks) {
            block.draw(bGr, null);
        }
        bGr.dispose();
        return bimage;
    }

    public void updateBackground() {

        BufferedImage bimage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();

        for (int i = 0; i < this.width; i+=backgroundImg.getWidth(null)) {
            for (int j = 0; j < this.height; j += backgroundImg.getHeight(null)) {
                int width = Math.min(backgroundImg.getWidth(null), this.width - i);
                int height = Math.min(backgroundImg.getHeight(null), this.height - j);
                bGr.drawImage(backgroundImg, i, j, width, height, null);
            }
        }

        Iterator<Thing> it = blocks.listIterator();
        while (it.hasNext()) {
            Thing block = it.next();
            if (!block.getRDone()) {
                block.draw(bGr, null);
            }
        }
        bGr.dispose();
        this.background = bimage;
    }

    public void setLeftPlayerParent(PlayerParent leftPlayerParent) {
        this.leftPlayerParent = leftPlayerParent;
        if (leftPlayerParent != null) {
            int newX = leftPlayerParent.getX() - screenWidth / 2;
            if (newX < 0) {
                leftXOffset = 0;
            } else if (newX > this.width - screenWidth) {
                leftXOffset = this.width - screenWidth;
            } else {
                leftXOffset = newX;
            }

            int newY = leftPlayerParent.getY() - screenHeight / 2;
            if (newY < 0) {
                leftYOffset = 0;
            } else if (newY > this.height - screenHeight) {
                leftYOffset = this.height - screenHeight;
            } else {
                leftYOffset = newY;
            }
        }
    }

    public void setRightPlayerParent(PlayerParent rightPlayerParent) {
        this.rightPlayerParent = rightPlayerParent;
        if (rightPlayerParent != null) {
            int newX = rightPlayerParent.getX() - screenWidth / 2;
            if (newX < 0) {
                rightXOffset = 0;
            } else if (newX > this.width - screenWidth) {
                rightXOffset = this.width - screenWidth;
            } else {
                rightXOffset = newX;
            }

            int newY = rightPlayerParent.getY() - screenHeight / 2;
            if (newY < 0) {
                rightYOffset = 0;
            } else if (newY > this.height - screenHeight) {
                rightYOffset = this.height - screenHeight;
            } else {
                rightYOffset = newY;
            }
        }
    }

    public int getLeftXOffset() {
        return leftXOffset;
    }

    public int getLeftYOffset() {
        return leftYOffset;
    }

    public int getRightXOffset() {
        return rightXOffset;
    }

    public int getRightYOffset() {
        return rightYOffset;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public boolean valid(Thing thing, int x, int y ) {
        for (Thing block : blocks) {
            if (block.isOverlap(thing, x, y)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Thing> getBlocks() {
        return blocks;
    }
}
