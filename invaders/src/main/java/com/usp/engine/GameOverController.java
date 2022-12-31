package com.usp.engine;

import java.io.IOException;

import com.usp.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * The menu controller only purpose is to initialize the actual game
 */
public class GameOverController {
    @FXML
    void startGame(ActionEvent event) throws IOException{
        App.setRoot("game");
    }
}

