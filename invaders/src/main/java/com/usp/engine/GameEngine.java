package com.usp.engine;

import java.util.List;

import com.usp.elements.Enemy;
import com.usp.elements.Mothership;
import com.usp.elements.Player;
import com.usp.elements.Sprite;
import com.usp.graphics.LevelDesigner;

import javafx.scene.layout.Pane;

/**
 * GameEngine is responsable for running the game, doing movement, collision check and points
 */
public class GameEngine {
    int[] alienDir;
    /**
     * The highest score achieved by the player without losing
     */
    public static int high_score_value;
    /**
     * Current points in the round
     */
    public int points_value;

    /**
     * Initializes some values when the game starts
     */
    public void start(){
        alienDir = new int[] {1,0};
        points_value = 0;
    }

    /**
     * Adds points and check for new high score
     * @param value how many points should be added
     */
    public void addPoints(int value){
        points_value += value;
        if(high_score_value < points_value){
            high_score_value = points_value;
        }
    }

    /**
     * Calculates to where the enemies should move and set's the movement direction
     * @param enemies List with all enemies 
     */
    public void setEnemyMovement(List<Sprite> enemies){
        for(Sprite e : enemies){
            Enemy alien = (Enemy)e;
            
            int ret[] = alien.tryMove();
            if(ret == null){
                //enemies exited at the end of the screen, they'll be removed when movement happens
                e.damage();
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

    /**
     * Makes the enemies shoot
     * @param root where the shots will be spawned
     * @param enemies the list of enemies that could shoot
     */
    public void enemyShooting(Pane root, List<Sprite> enemies){
        for (Sprite s : enemies) {
            if (Math.random() < (0.01 * LevelDesigner.phase)) {
                root.getChildren().add(((Enemy) s).shoot());
            }
        }
    }

    /**
     * Spawns the UFO that gives a lot of points
     * @param root where the UFO will be spawned
     */
    public void UFOAppearance(Pane root){
        if (Math.random() < 0.05 && LevelDesigner.sprites(root).stream().filter(s -> s.type.equals("mothership")).count() < 1) {
            int points = 20 * LevelDesigner.phase + (int) (Math.random() * 250);
            root.getChildren().add(new Mothership(points));
        }
    }

    /**
     * Removes all dead entities and gives the player points from dead enemies
     * @param root where the entities should be removed from
     */
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

    /**
     * Moves all entities and check for collision between than
     * @param root
     */
    public void moveAndCheckCollision(Pane root){
        Player player = (Player) root.getChildren().get(0);
        
        LevelDesigner.sprites(root).forEach(s -> {
            s.move();

            switch (s.type) {
                case "enemybullet":
                    testCollision(s, player, root);

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

    /**
     * Test if there's collision between @param a and @param b , damages them and spawns an explosion
     * @param a first sprite (also where the explosion will happen)
     * @param b second sprite
     * @param root
     */
    void testCollision(Sprite a, Sprite b, Pane root){
        if (a.getBoundsInParent().intersects(b.getBoundsInParent())) {
            a.damage();
            b.damage();
            
            Sprite explosion = new Sprite((int) a.getTranslateX(), (int) a.getTranslateY(), 45, 45, "explosion", "explosion.gif");
            root.getChildren().add(explosion);
        }
    }
}
