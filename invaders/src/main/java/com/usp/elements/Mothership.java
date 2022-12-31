package com.usp.elements;

/**
 * Mothership entity
 */
public class Mothership extends Sprite{
    /**
     * How many points the player will gain when destroying this entity
     */
    public int points;

    /**
     * Mothership constructor
     * @param points how many points the player will gain when destroying this entity
     */
    public Mothership(int points){
        super(0, 20, 40, 15, "mothership", "ufo.png");
        this.points = points;
        this.speed = 0.5;
        this.life = 1;
        this.xDir = 1;
    }
    
        public void move(){
        if(xDir == 0) return;
        
        double xPos = getTranslateX();
        
        if(xPos + xDir < 0 || xPos + xDir > (560 - getImage().getWidth())) {
            life--;
            return;
        }
        xPos += (xDir*speed);
        setTranslateX(xPos);
    }
}
