package com.usp.elements;

public class Enemy extends Sprite{
    public int points;
    public Enemy(int x, int y, int w, int h, String file, int points){
        super(x, y, w, h, "enemy", file);
        this.points = points;
        this.life = 1;
        this.speed = 0.5;
    }

    public Bullet shoot() {
        double middle = getImage().getWidth() / 2;
        Bullet bullet = new Bullet((int) (getTranslateX() + middle), (int) getTranslateY(), type);

        return bullet;
    }

    public int[] tryMove(){
        double xPos = getTranslateX();
        double yPos = getTranslateY();

        double newXPos = xPos + xDir;
        if(0 >= newXPos || newXPos >= (560 - getImage().getWidth())){
            int dir[] = {0,40};
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
