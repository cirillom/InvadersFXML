package com.usp.elements;

import com.usp.engine.GameController;

/**
 * Player entity
 */
public class Player extends Sprite{
    int x, y;
    /**
     * Player constructor, fills the super for the sprite
     * @param x
     * @param y
     */
    public Player(int x, int y) {
        super(x, y, 33, 20, "player", "tank.png");
        this.life = 4;
        this.speed = 1.5;
        this.x = x;
        this.y = y;
    }

    /**
     * Generate a shot originated from the player
     * @return a bullet to be put in a pane
     */
    public Bullet shoot() {
        double middle = getImage().getWidth() / 2;
        Bullet bullet = new Bullet((int) (getTranslateX() + middle), (int) getTranslateY(), type);

        return bullet;
    }

    /**
     * Overrides the damage in order to restart player position and pause the game
     */
    @Override
    public void damage(){
        life--;
        GameController.paused = true;
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Get the x direction of the player (left or right)
     * @return int
     */
    public int getDirX(){
        return xDir;
    }
}
