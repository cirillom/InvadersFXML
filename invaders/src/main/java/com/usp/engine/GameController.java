package com.usp.engine;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.usp.App;
import com.usp.elements.Player;
import com.usp.elements.Sprite;
import com.usp.graphics.LevelDesigner;

import javafx.fxml.FXML;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class GameController{
    private Player player;
    private double t = 0;
    private boolean paused = false;
    private boolean oneKeyPress = false; //this variable helps that paused only runs once at keyPressed
    
    @FXML
    private Pane root;

    @FXML
    private Label points, high_score, life_count, gamePaused;

    @FXML
    public void initialize(){
        start();
    }
    
    int[] alienDir;
    GameEngine gameEngine;
    AnimationTimer timer;
    private void start(){
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        
        timer.start();
        gameEngine = new GameEngine();

        LevelDesigner.nextLevel(root);
        gameEngine.start();
    }
    
    private void update() {
        if(paused){
            gamePaused.setText("JOGO PAUSADO");
            return;
        }
        gamePaused.setText("");
        
        player = (Player) root.getChildren().get(0);
        if(player.life == 0){
            try {
                timer.stop();
                App.setRoot("endscreen");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        t += 0.016;
        life_count.setText("Vidas: " + player.life);
        points.setText(gameEngine.points_value + "pts");
        high_score.setText(GameEngine.high_score_value + "pts");

        List<Sprite> enemies = LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("enemy")).map(n -> (Sprite)n).collect(Collectors.toList());
        
        if(enemies.size() == 0){
            LevelDesigner.nextLevel(root);
        }
        gameEngine.setEnemyMovement(enemies);

        gameEngine.moveAndCheckCollision(root);

        gameEngine.enemyShooting(root, enemies, t);
        
        gameEngine.cleanPane(root);

        if (t > 2) {
            t = 0;
        }
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
                if(!oneKeyPress){
                    if(LevelDesigner.sprites(root).stream().filter(s -> s.type.equals("playerbullet")).count() < 3){
                        root.getChildren().add(player.shoot());
                    }
                    oneKeyPress = true;
                }
                break;
            case ESCAPE:
                if(!oneKeyPress){
                    paused = !paused;
                    oneKeyPress = true;
                }
                break;
                
            default:
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
            case SPACE:
                if(oneKeyPress){
                    oneKeyPress = false;
                }
                break;
            case ESCAPE:
                if(oneKeyPress){
                    oneKeyPress = false;
                }
                break;
            default:
                break;
        }
    }
}