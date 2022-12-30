package com.usp.elements;

import java.io.IOException;
import java.io.InputStream;

import com.usp.App;

import javafx.scene.image.Image;

public class Barrier extends Sprite{
    String file[];
    public Barrier(int x, int y, String file[]) {
        super(x, y, 15, 15, "barrier", file[file.length - 1]);
        changeImage(type);
        this.life = file.length;        
        this.file = file;
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
        life--;
        changeImage(file[life]);
        //TODO change sprite
    }
}
