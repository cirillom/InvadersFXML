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
                if (Math.random() < 0.5) {
                    root.getChildren().add(((Enemy) s).shoot());
                }
            }
        }
    }

    void testCollision(Sprite a, Sprite b, Pane root){
        if (a.getBoundsInParent().intersects(b.getBoundsInParent())) {
            a.damage();
            b.damage();
            
            Sprite explosion = new Sprite((int) a.getTranslateX(), (int) a.getTranslateY(), 45, 45, "explosion", "explosion.gif");
            root.getChildren().add(explosion);
        }
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
}
