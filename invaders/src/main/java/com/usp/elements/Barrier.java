package com.usp.elements;

import java.io.IOException;
import java.io.InputStream;

import com.usp.App;

import javafx.scene.image.Image;

/**
 * Barrier entity
 */
public class Barrier extends Sprite{
    String file[];
    /**
     * Barrier constructor
     * @param x spawn x coordinates
     * @param y spawn y coordinates
     */
    public Barrier(int x, int y) {
        super(x, y, 15, 15, "barrier", "b_deg_1.png");
        file = new String[] {"b_dest.png", "b_deg_4.png","b_deg_3.png","b_deg_2.png"};
        this.life = file.length;        
    }

    /**
     * Changes the image of the barrier to show destruction
     * @param file
     */
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

    /**
     * Damage override in order to change barrier image
     */
    @Override
    public void damage(){
        life--;
        if(life >= 0)
            changeImage(file[life]);
    }
}
