package com.usp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the starting point of our game and set's up our application
 */
public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("menu"), 560, 640);
        stage.setScene(scene);
        stage.setTitle("Space Invaders");
        stage.show();
    }

    /**
     * Sets the root of the stage, making possible to change between scenes
     * 
     * @param fxml 
     * @throws IOException
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Loads an FXML in order to open a scene
     * 
     * @param fxml
     * @return Parent object to be set as root of a scene
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}