package com.usp;

import javafx.fxml.FXMLLoader;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.usp.elements.Bullet;
import com.usp.elements.Sprite;

/**
 * JavaFX App
 */
public class App extends Application {

    private Pane root = new Pane();

    private double t = 0;

    private Sprite player = new Sprite(50, 545, 33, 20, "player", "tank.png");

    private Parent createContent() {
        root.setPrefSize(560, 640);
        root.setStyle("-fx-background-color: black;");

        root.getChildren().add(player);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();

        nextLevel();

        return root;
    }

    private void nextLevel() {
        final int initialx = 80;
        final int initialy = 150;
        final int spacing = 40;

        for (int i = 0; i < 11; i++) {
            Sprite a = new Sprite(initialx + i*spacing, initialy + 0 * spacing, 20, 20, "enemy", "small_invader_a.png");
            Sprite b = new Sprite(initialx + i*spacing, initialy + 1 * spacing, 28, 20, "enemy", "medium_invader_a.png");
            Sprite c = new Sprite(initialx + i*spacing, initialy + 2 * spacing, 28, 20, "enemy", "medium_invader_a.png");
            Sprite d = new Sprite(initialx + i*spacing, initialy + 3 * spacing, 30, 20, "enemy", "large_invader_a.png");
            Sprite e = new Sprite(initialx + i*spacing, initialy + 4 * spacing, 30, 20, "enemy", "large_invader_a.png");

            root.getChildren().add(a);
            root.getChildren().add(b);
            root.getChildren().add(c);
            root.getChildren().add(d);
            root.getChildren().add(e);
        }
    }

    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n -> (Sprite)n).collect(Collectors.toList());
    }

    private void update() {
        t += 0.016;

        sprites().forEach(s -> {
            switch (s.type) {

                case "enemybullet":
                    s.moveDown();

                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        player.dead = true;
                        s.dead = true;

                        Sprite explosion = new Sprite((int) s.getTranslateX(), (int) s.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                        root.getChildren().add(explosion);
                    }
                    break;

                case "playerbullet":
                    s.moveUp();

                    sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.dead = true;
                            s.dead = true;

                            Sprite explosion = new Sprite((int) enemy.getTranslateX(), (int) enemy.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                            root.getChildren().add(explosion);
                        }
                    });

                    sprites().stream().filter(e -> e.type.equals("enemybullet")).forEach(bullet -> {
                        if (s.getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                            bullet.dead = true;
                            s.dead = true;

                            Sprite explosion = new Sprite((int) bullet.getTranslateX() - 20, (int) bullet.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                            root.getChildren().add(explosion);
                        }
                    });

                    break;

                case "enemy":

                    if (t > 2) {
                        if (Math.random() < 0.01) {
                            shoot(s);
                        }
                    }

                    break;
            }
        });

        root.getChildren().removeIf(n -> {
            Sprite s = (Sprite) n;
            return s.dead;
        });

        if (t > 2) {
            t = 0;
        }
    }

    private void shoot(Sprite who) {
        //Sprite s = new Sprite((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 5, 20, who.type + "bullet", "bullet.png");
        Bullet b = new Bullet((int) who.getTranslateX() + 20, (int) who.getTranslateY(), who.type);

        root.getChildren().add(b);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    player.moveLeft();
                    break;
                case D:
                    player.moveRight();
                    break;
                case SPACE:
                    if(sprites().stream().filter(s -> s.type.equals("playerbullet")).count() < 3){
                        shoot(player);
                    }
                    break;
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}