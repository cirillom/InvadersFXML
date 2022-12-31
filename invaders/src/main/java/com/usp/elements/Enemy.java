package com.usp.elements;

/**
 * Enemy entity
 */
public class Enemy extends Sprite{
    /**
     * How many points the player will gain when destroying this entity
     */
    public int points;

    //the speed in which enemies will descend a line
    final int downSpeed = 40;

    /**
     * Enemy constructor
     * @param x spawn x coordinates
     * @param y spawn y coordinates
     * @param w width of sprite
     * @param h height of sprite
     * @param file enemy image
     * @param points how many points the player will gain when destroying this entity
     */
    public Enemy(int x, int y, int w, int h, String file, int points){
        super(x, y, w, h, "enemy", file);
        this.points = points;
        this.life = 1;
        this.speed = 0.5;
    }

    /**
     * Generate a shot originated from the enemy
     * @return a bullet to be put in a pane
     */
    public Bullet shoot() {
        double middle = getImage().getWidth() / 2;
        Bullet bullet = new Bullet((int) (getTranslateX() + middle), (int) getTranslateY(), type);

        return bullet;
    }

    /**
     * Tries to move this enemy in order to calculate if the pack needs to change movement direction
     * @return the direction this enemy should move next
     */
    public int[] tryMove(){
        double xPos = getTranslateX();
        double yPos = getTranslateY();

        double newXPos = xPos + xDir;
        if(0 >= newXPos || newXPos >= (560 - getImage().getWidth())){
            int dir[] = {0,downSpeed};
            return dir;
        }
        
        if(yPos == (640 - getImage().getHeight())){
            return null;
        }

        if (yDir != 0){
            if(0 <= xPos && xPos <= 5){
                int dir[] = {1,0};
                return dir;
            }else{
                int dir[] = {-1,0};
                return dir;
            }
        }

        int dir[] = {xDir,yDir};
        return dir;
    }
}
