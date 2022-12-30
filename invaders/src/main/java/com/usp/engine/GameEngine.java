package com.usp.engine;

import java.util.List;

import com.usp.elements.Barrier;
import com.usp.elements.Enemy;
import com.usp.elements.Player;
import com.usp.elements.Sprite;
import com.usp.graphics.LevelDesigner;

import javafx.scene.layout.Pane;

public class GameEngine {
    int[] alienDir;
    public static int high_score_value;
    public int points_value;

    public void start(){
        alienDir = new int[] {1,0};
        points_value = 0;
        //setPoints(0);
    }

    private void addPoints(int value){
        points_value += value;
        if(high_score_value < points_value){
            high_score_value = points_value;
        }
    }

    public void setEnemyMovement(List<Sprite> enemies){
        for(Sprite e : enemies){
            Enemy alien = (Enemy)e;
            
            int ret[] = alien.tryMove();
            if(ret == null){
                //gameOver = true;
                //TODO enemies exited at the end of the screen, do something
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
    }

    public void enemyShooting(Pane root, List<Sprite> enemies, double t){
        for (Sprite s : enemies) {
            if (t > 2) {
                if (Math.random() < 0.01) {
                    root.getChildren().add(((Enemy) s).shoot());
                }
            }
        }
    }

    public void moveAndCheckCollision(Pane root){
        Player player = (Player) root.getChildren().get(0);
        
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
                        if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                            player.life = 0;
                        }
                    break;
                }
        });
    }
}
