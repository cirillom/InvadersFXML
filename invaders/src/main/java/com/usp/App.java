package com.usp;

import javafx.fxml.FXMLLoader;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

import com.usp.elements.Bullet;
import com.usp.elements.Enemy;
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
    
    int[] alienDir;
    private void nextLevel() {
        alienDir = new int[] {1,0};
        final int initialx = 80;
        final int initialy = 150;
        final int spacing = 40;

        for (int i = 0; i < 11; i++) {
            /* Sprite a = new Sprite(initialx + i*spacing, initialy + 0 * spacing, 20, 20, "enemy", "small_invader_a.png");
            Sprite b = new Sprite(initialx + i*spacing, initialy + 1 * spacing, 28, 20, "enemy", "medium_invader_a.png");
            Sprite c = new Sprite(initialx + i*spacing, initialy + 2 * spacing, 28, 20, "enemy", "medium_invader_a.png");
            Sprite d = new Sprite(initialx + i*spacing, initialy + 3 * spacing, 30, 20, "enemy", "large_invader_a.png");
            Sprite e = new Sprite(initialx + i*spacing, initialy + 4 * spacing, 30, 20, "enemy", "large_invader_a.png"); */
            Enemy a = new Enemy(initialx + i*spacing, initialy + 0 * spacing, 20, 20, "small_invader_a.png", 10);
            Enemy b = new Enemy(initialx + i*spacing, initialy + 1 * spacing, 28, 20, "medium_invader_a.png", 5);
            Enemy c = new Enemy(initialx + i*spacing, initialy + 2 * spacing, 28, 20, "medium_invader_a.png", 5);
            Enemy d = new Enemy(initialx + i*spacing, initialy + 3 * spacing, 30, 20, "large_invader_a.png", 3);
            Enemy e = new Enemy(initialx + i*spacing, initialy + 4 * spacing, 30, 20, "large_invader_a.png", 3);

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

        //check if player is dead
        if(player.dead){
            System.out.println("GAME OVER");
            return;
            //GAME OVER, LOAD LAST SCENE
        }

        //set player movement direction

        //find enemy movement direction
        List<Sprite> enemies = sprites().stream().filter(e -> e.type.equals("enemy")).map(n -> (Sprite)n).collect(Collectors.toList());
        for(Sprite e : enemies){
            Enemy alien = (Enemy)e;
            
            int ret[] = alien.tryMove();
            if(ret == null){
                //gameOver = true;
                break;
            }
            if(ret[0] != alienDir[0] && ret[1] != alienDir[1]){
                alienDir = ret;
                break;
            }
        }

        for(Sprite e : enemies){
            e.setDir(alienDir[0], alienDir[1]);
        }

        //move all entities 
        //(and check for collision) (check for out of bounds)     
        sprites().forEach(s -> {
            s.move();

            switch (s.type) {
                case "enemybullet":
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        player.dead = true;
                        s.dead = true;
                        
                        Sprite explosion = new Sprite((int) s.getTranslateX(), (int) s.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                        root.getChildren().add(explosion);
                    }
                    break;
                    
                case "playerbullet":
                    sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.dead = true;
                            s.dead = true;

                            //TODO GIVE PLAYER POINTS FROM ENEMY
                            
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

                        if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                            player.dead = true;
                            s.dead = true;
                            
                            Sprite explosion = new Sprite((int) s.getTranslateX(), (int) s.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                            root.getChildren().add(explosion);
                        }
                    break;
                }
        });
        
        //remove dead entities
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
        double middle = who.getImage().getWidth() / 2;
        Bullet b = new Bullet((int) (who.getTranslateX() + middle), (int) who.getTranslateY(), who.type);

        root.getChildren().add(b);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    player.setDir(-1, 0);
                    break;
                case D:
                    player.setDir(1,0);
                    break;
                case SPACE:
                    if(sprites().stream().filter(s -> s.type.equals("playerbullet")).count() < 3){
                        shoot(player);
                    }
                    break;
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case A:
                    player.setDir(0, 0);
                    break;
                case D:
                    player.setDir(0,0);
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