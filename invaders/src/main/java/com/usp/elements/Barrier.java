package com.usp.elements;

import java.io.IOException;
import java.io.InputStream;

import com.usp.App;

import javafx.scene.image.Image;

public class Barrier extends Sprite{
    String file[];
    public Barrier(int x, int y) {
        super(x, y, 15, 15, "barrier", "b_deg_1.png");
        file = new String[] {"b_dest.png", "b_deg_4.png","b_deg_3.png","b_deg_2.png"};
        this.life = file.length;        
    }

    private void changeImage(String file){
        InputStream is = null;
        try {
            is = App.class.getResource("images/"+ file).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = new Image(is, 15, 15, false, false);
        setImage(image);
    }

    public void damage(){
        System.out.println("Damaging barrier: " + life);
        life--;
        System.out.println("Barrier damaged: " + life);
        if(life >= 0)
            changeImage(file[life]);
        //TODO change sprite
    }
}
