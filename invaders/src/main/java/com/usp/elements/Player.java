package com.usp.elements;

public class Player extends Sprite{
    public Player(int x, int y) {
        super(x, y, 33, 20, "player", "tank.png");
    }

    public int getDirX(){
        return xDir;
    }

    public int getDirY(){
        return yDir;
    }

}
