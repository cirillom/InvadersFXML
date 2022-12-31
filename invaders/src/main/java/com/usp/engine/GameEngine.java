package com.usp.engine;

import java.util.List;

import com.usp.elements.Enemy;
import com.usp.elements.Mothership;
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
    }

    public void addPoints(int value){
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
            e.setSpeed((enemies.size()/-108.0) + (109.0/108.0));
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

    public void cleanPane(Pane root){
        root.getChildren().removeIf(n -> {
            Sprite s = (Sprite) n;
            if(s.life <= 0) {
                if(s.type == "player"){
                    return false;
                } else if(s.type == "enemy"){
                    addPoints(((Enemy)s).points);
                } else if(s.type == "mothership"){
                    addPoints(((Mothership)s).points);
                }
                return true;
            }
            return false;
        });
    }

    public void moveAndCheckCollision(Pane root){
        Player player = (Player) root.getChildren().get(0);
        
        LevelDesigner.sprites(root).forEach(s -> {
            s.move();

            switch (s.type) {
                case "enemybullet":
                    testCollision(player, s, root);

                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("barrier")).forEach(barrier -> {
                        testCollision(barrier, s, root);
                    });
                    break;
                    
                case "playerbullet":
                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        testCollision(enemy, s, root);
                    });

                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("mothership")).forEach(mothership -> {
                        testCollision(mothership, s, root);
                    });
                    
                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("enemybullet")).forEach(bullet -> {
                        testCollision(bullet, s, root);
                    });

                    LevelDesigner.sprites(root).stream().filter(e -> e.type.equals("barrier")).forEach(barrier -> {
                        testCollision(barrier, s, root);
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

    void testCollision(Sprite a, Sprite b, Pane root){
        if (a.getBoundsInParent().intersects(b.getBoundsInParent())) {
            a.damage();
            b.damage();
            
            Sprite explosion = new Sprite((int) a.getTranslateX(), (int) a.getTranslateY(), 45, 45, "explosion", "explosion.gif");
            root.getChildren().add(explosion);
        }
    }
}
