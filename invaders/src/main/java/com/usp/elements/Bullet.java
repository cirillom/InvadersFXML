package com.usp.elements;

public class Bullet extends Sprite {

    public Bullet(int x, int y, String origin) {
        super(x, y, 3, 18, origin + "bullet", "bullet.png");
    }

    public void moveUp() {
        double next_pos = getTranslateY() - 5;
        if(next_pos < 0){
            dead = true;
            return;
        }
        setTranslateY(next_pos);
    }
    
    public void moveDown() {
        double next_pos = getTranslateY() + 5;
        if(next_pos > (640 - getImage().getWidth())){
            dead = true;
            return;
        }
        setTranslateY(next_pos);
    }
}
