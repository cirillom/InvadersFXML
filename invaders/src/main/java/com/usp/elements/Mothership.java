package com.usp.elements;

public class Mothership extends Sprite{
    public int points;
    public Mothership(int y, int w, int h, String file, int points){
        super(0, y, w, h, "mothership", file);
        this.points = points;
        this.speed = 0.5;
        this.life = 1;
        this.xDir = 1;
    }
}
