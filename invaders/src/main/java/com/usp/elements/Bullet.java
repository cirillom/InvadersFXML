package com.usp.elements;

/**
 * Bullet entity
 */
public class Bullet extends Sprite {

    /**
     * Bullet constructor
     * @param x spawn x coordinates
     * @param y spawn y coordinates
     * @param origin owner of this bullet (player or enemy)
     */
    public Bullet(int x, int y, String origin) {
        super(x, y, 3, 18, origin + "bullet", "bullet.png");
        
        //sets the direction the bullet should move
        if(origin.equals("player")){
            setDir(0, -1);
        } else if(origin.equals("enemy")){
            setDir(0, 1);
        }

        this.speed = 5;
        this.life = 1;
    }
}
