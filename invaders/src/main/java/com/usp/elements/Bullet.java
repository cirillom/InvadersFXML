package com.usp.elements;

public class Bullet extends Sprite {

    public Bullet(int x, int y, String origin) {
        super(x, y, 3, 18, origin + "bullet", "bullet.png");
        
        if(origin.equals("player")){
            setDir(0, -1);
        }
        if(origin.equals("enemy")){
            setDir(0, 1);
        }

        this.speed = 5;
        this.life = 1;
    }

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
}
