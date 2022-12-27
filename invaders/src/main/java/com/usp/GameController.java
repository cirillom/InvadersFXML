package com.usp;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameController {
    @FXML
    void pauseGame(ActionEvent event)  throws IOException {
        App.setRoot("menu");
    }
}
