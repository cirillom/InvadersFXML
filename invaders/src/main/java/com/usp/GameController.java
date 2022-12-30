package com.usp;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.usp.elements.Barrier;
import com.usp.elements.Bullet;
import com.usp.elements.Enemy;
import com.usp.elements.Player;
import com.usp.elements.Sprite;
import com.usp.graphics.LevelDesigner;

import javafx.fxml.FXML;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class GameController{
    private Player player;
    private double t = 0;
    
    @FXML
    private Pane root;

    @FXML
    private Label points, high_score, life_count;
    private int points_value, high_score_value;

    @FXML
    void pauseGame(ActionEvent event)  throws IOException {
        //App.setRoot("menu");
    }

    @FXML
    public void initialize(){
        start();
    }
    
    int[] alienDir;
    private void start(){
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        
        timer.start();
    
        LevelDesigner.nextLevel(root);
        alienDir = new int[] {1,0};
        addPoints(0);
        player = (Player) root.getChildren().get(0);
    }

    private void update() {
        t += 0.016;

        //check if player is dead
        if(player.life == 0){
            System.out.println("GAME OVER");
            return;
            //GAME OVER, LOAD LAST SCENE
        }
        life_count.setText("Vidas: " + player.life);

        //set player movement direction

        //find enemy movement direction
        List<Sprite> enemies = LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("enemy")).map(n -> (Sprite)n).collect(Collectors.toList());
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
        LevelDesigner.sprites(root).forEach(s -> {
            s.move();

            switch (s.type) {
                case "enemybullet":
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        player.life--;
                        s.life--;
                        
                        Sprite explosion = new Sprite((int) s.getTranslateX(), (int) s.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                        root.getChildren().add(explosion);
                    }

                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("barrier")).forEach(barrier -> {
                        if (s.getBoundsInParent().intersects(barrier.getBoundsInParent())) {
                            Barrier b = (Barrier) barrier;
                            b.damage();
                            s.life--;

                            Sprite explosion = new Sprite((int) barrier.getTranslateX() - 20, (int) barrier.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                            root.getChildren().add(explosion);
                        }
                    });
                    break;
                    
                case "playerbullet":
                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.life--;
                            s.life--;

                            //TODO GIVE PLAYER POINTS FROM ENEMY
                            Enemy alien = (Enemy) enemy;
                            addPoints(alien.points);
                            
                            Sprite explosion = new Sprite((int) enemy.getTranslateX(), (int) enemy.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                            root.getChildren().add(explosion);
                        }
                    });
                    
                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("enemybullet")).forEach(bullet -> {
                        if (s.getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                            bullet.life--;
                            s.life--;

                            Sprite explosion = new Sprite((int) bullet.getTranslateX() - 20, (int) bullet.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                            root.getChildren().add(explosion);
                        }
                    });

                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("barrier")).forEach(barrier -> {
                        if (s.getBoundsInParent().intersects(barrier.getBoundsInParent())) {
                            System.out.println("Player bullet collided with barrier");
                            Barrier b = (Barrier) barrier;
                            b.damage();
                            s.life--;

                            Sprite explosion = new Sprite((int) barrier.getTranslateX() - 20, (int) barrier.getTranslateY(), 45, 45, "explosion", "explosion.gif");
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
                            //TODO LOSES GAME
                            player.life = 0;
                        }
                    break;
                }
        });
        
        //remove dead entities
        root.getChildren().removeIf(n -> {
            Sprite s = (Sprite) n;
            if(s.life <= 0) {
                return true;
            }
            return false;
        });

        if (t > 2) {
            t = 0;
        }
    }

    private void addPoints(int value){
        points_value += value;
        if(high_score_value < points_value){
            high_score_value = points_value;
        }
        points.setText(points_value + "pts");
        high_score.setText(high_score_value + "pts");
    }

    private void shoot(Sprite who) {
        //Sprite s = new Sprite((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 5, 20, who.type + "bullet", "bullet.png");
        double middle = who.getImage().getWidth() / 2;
        Bullet b = new Bullet((int) (who.getTranslateX() + middle), (int) who.getTranslateY(), who.type);

        root.getChildren().add(b);
    }

    @FXML
    public void handleKeyPressed(KeyEvent e){
        switch (e.getCode()) {
            case A:
                player.setDir(-1, 0);
                break;
            case D:
                player.setDir(1,0);
                break;
            case SPACE:
                if(LevelDesigner.sprites(root).stream().filter(s -> s.type.equals("playerbullet")).count() < 1){
                    shoot(player);
                }
                break;
        }
    }
    @FXML
    public void handleKeyReleased(KeyEvent e){
        switch (e.getCode()) {
            case A:
                if(player.getDirX() == -1)
                player.setDir(0, 0);
                break;
            case D:
                if(player.getDirX() == 1)
                    player.setDir(0,0);
                break;
        }
    }
}
