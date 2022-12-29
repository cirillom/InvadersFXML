package com.usp.elements;

import java.io.IOException;
import java.io.InputStream;

import com.usp.App;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView {
    public boolean dead = false;
    public final String type;
    int xDir, yDir;
    double speed = 1;

    public Sprite(int x, int y, int w, int h, String type, String file) {
        InputStream is = null;
        try {
            is = App.class.getResource("images/"+ file).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = new Image(is, w, h, false, false);
        setImage(image);

        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }

    public void setDir(int x, int y){
        xDir = x;
        yDir = y;
        //if(type.equals("enemy")) System.out.println("Alien sprite dir: (" + xDir + ", " + yDir + ")");
    }
    
    public void move(){
        if(xDir + yDir == 0) return;
        
        double xPos = getTranslateX();
        double yPos = getTranslateY();
        
        if(xPos + xDir < 0 || xPos + xDir > (560 - getImage().getWidth())) return;
        xPos += (xDir*speed);
        setTranslateX(xPos);
        
        if(yPos + yDir < 0 || yPos + yDir > (640 - getImage().getHeight())) return;
        
        yPos += (yDir*speed);
        setTranslateY(yPos);
    }

    public void moveLeft() {
        double next_pos = getTranslateX() - 5;
        if(next_pos >= 0){
            setTranslateX(next_pos);
        }
    }

    public void moveRight() {
        double next_pos = getTranslateX() + 5;
        if(next_pos <= (560 - getImage().getWidth())){
            setTranslateX(next_pos);
        }
    }

    public void moveUp() {
        double next_pos = getTranslateY() - 5;
        if(next_pos >= 0){
            setTranslateY(next_pos);
        }
    }

    public void moveDown() {
        double next_pos = getTranslateY() + 5;
        if(next_pos <= (640 - getImage().getHeight())){
            setTranslateY(next_pos);
        }
    }
}
