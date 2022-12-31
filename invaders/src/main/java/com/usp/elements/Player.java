package com.usp.elements;

public class Player extends Sprite{
    int x, y;
    public Player(int x, int y) {
        super(x, y, 33, 20, "player", "tank.png");
        this.life = 4;
        this.speed = 1.5;
        this.x = x;
        this.y = y;
    }

    public Bullet shoot() {
        double middle = getImage().getWidth() / 2;
        Bullet bullet = new Bullet((int) (getTranslateX() + middle), (int) getTranslateY(), type);

        return bullet;
    }

    public void damage(){
        life--;
        setTranslateX(x);
        setTranslateY(y);
    }

    public int getDirX(){
        return xDir;
    }
}
