package com.usp;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.usp.elements.Barrier;
import com.usp.elements.Bullet;
import com.usp.elements.Enemy;
import com.usp.elements.Player;
import com.usp.elements.Sprite;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class GameController{
    private Player player;
    private double t = 0;
    
    @FXML
    private Pane root;

    @FXML
    void pauseGame(ActionEvent event)  throws IOException {
        //App.setRoot("menu");
    }

    @FXML
    public void initialize(){
        start();
    }
    
    private void start(){
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        
        timer.start();

        nextLevel();
    }
    
    int[] alienDir;
    private void nextLevel() {
        //this shouldn't be here
        root.getChildren().clear();
        player = new Player(50, 545);
        root.getChildren().add(player);

        alienDir = new int[] {1,0};

        final int initialx = 80;
        final int initialy = 150;
        final int spacing = 40;

        for (int i = 0; i<5; i++){
            Barrier barrier = new Barrier(50 + 15*i, 500);
            root.getChildren().add(barrier);

        }

        for (int i = 0; i < 11; i++) {
            Enemy a = new Enemy(initialx + i*spacing, initialy + 0 * spacing, 20, 20, "small_invader.gif", 10);
            Enemy b = new Enemy(initialx + i*spacing, initialy + 1 * spacing, 28, 20, "medium_invader.gif", 5);
            Enemy c = new Enemy(initialx + i*spacing, initialy + 2 * spacing, 28, 20, "medium_invader.gif", 5);
            Enemy d = new Enemy(initialx + i*spacing, initialy + 3 * spacing, 30, 20, "large_invader.gif", 3);
            Enemy e = new Enemy(initialx + i*spacing, initialy + 4 * spacing, 30, 20, "large_invader.gif", 3);

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
        if(player.life == 0){
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
                        player.life--;
                        s.life--;
                        
                        Sprite explosion = new Sprite((int) s.getTranslateX(), (int) s.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                        root.getChildren().add(explosion);
                    }

                    sprites().stream().filter(e -> e.type.equals("barrier")).forEach(barrier -> {
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
                    sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.life--;
                            s.life--;

                            //TODO GIVE PLAYER POINTS FROM ENEMY
                            
                            Sprite explosion = new Sprite((int) enemy.getTranslateX(), (int) enemy.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                            root.getChildren().add(explosion);
                        }
                    });
                    
                    sprites().stream().filter(e -> e.type.equals("enemybullet")).forEach(bullet -> {
                        if (s.getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                            bullet.life--;
                            s.life--;

                            Sprite explosion = new Sprite((int) bullet.getTranslateX() - 20, (int) bullet.getTranslateY(), 45, 45, "explosion", "explosion.gif");
                            root.getChildren().add(explosion);
                        }
                    });

                    sprites().stream().filter(e -> e.type.equals("barrier")).forEach(barrier -> {
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
                if(sprites().stream().filter(s -> s.type.equals("playerbullet")).count() < 1){
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
