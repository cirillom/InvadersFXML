package com.usp.elements;

import java.io.IOException;
import java.io.InputStream;

import com.usp.App;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Main gameobject, all entities are derived from a sprite
 */
public class Sprite extends ImageView {
    /**
     * The life of the sprite, if it's equal to 0, the entitiy is dead
     */
    public int life = 1;
    /**
     * The type of entity (player, enemy, bullet...)
     */
    public String type;
    int xDir, yDir;
    double speed = 1;

    /**
     * Constructor of a sprite
     * @param x coordinates X in a pane
     * @param y coordinates Y in a pane
     * @param w width of the sprite
     * @param h height of the sprite
     * @param type of the sprite (player, enemy, bullet, mothership, barrier)
     * @param file image that will represend the sprite
     */
    public Sprite(int x, int y, int w, int h, String type, String file) {
        InputStream is = null;
        try {
            is = App.class.getResource("images/"+ file).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = new Image(is, w, h, false, false);
        setImage(image);

        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Set the movement direction of the sprite
     * @param x
     * @param y
     */
    public void setDir(int x, int y){
        xDir = x;
        yDir = y;
    }
    
    /**
     * Moves the sprite
     */
    public void move(){
        if(xDir + yDir == 0) return;
        
        double xPos = getTranslateX();
        double yPos = getTranslateY();
        
        if(xPos + xDir < 0 || xPos + xDir > (560 - getImage().getWidth())) {
            life--;
            return;
        }
        xPos += (xDir*speed);
        setTranslateX(xPos);
        
        if(yPos + yDir < 0 || yPos + yDir > (640 - getImage().getHeight())) {
            life--;
            return;
        }
        
        yPos += (yDir*speed);
        setTranslateY(yPos);
    }

    /**
     * Set sprite speed
     * @param speed
     */
    public void setSpeed(double speed){
        this.speed = speed;
    }

    /**
     * Damages the sprite, this can be overwritten in order to make things happen when sprite damaged
     */
    public void damage(){
        life--;
    }
}
