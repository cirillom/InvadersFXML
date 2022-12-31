package com.usp.engine;

import java.io.IOException;

import com.usp.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameOverController {

    @FXML
    void startGame(ActionEvent event) throws IOException{
        System.out.println("Start game");
        App.setRoot("game");
    }
}

