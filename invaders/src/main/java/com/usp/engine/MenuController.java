package com.usp.engine;

import java.io.IOException;

import com.usp.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuController {
    @FXML
    void startGame(ActionEvent event) throws IOException {
        App.setRoot("game"); 
    }
}
