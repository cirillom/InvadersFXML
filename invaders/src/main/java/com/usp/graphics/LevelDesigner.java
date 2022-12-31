package com.usp.graphics;

import java.util.List;
import java.util.stream.Collectors;

import com.usp.elements.Barrier;
import com.usp.elements.Enemy;
import com.usp.elements.Player;
import com.usp.elements.Sprite;

import javafx.scene.layout.Pane;

/**
 * LevelDesigner is responsible for drawing the game elements life enemies and barriers
 */
public class LevelDesigner {
    /**
     * Keeps track of the number of phases in order to spawn enemies only line below everytime
     */
    public static int phase;

    /**
     * Gets a list of all the sprites in a Pane root
     * @param root 
     * @return List of Sprite
     */
    public static List<Sprite> sprites(Pane root) {
        return root.getChildren().stream().map(n -> (Sprite)n).collect(Collectors.toList());
    }

    /**
     * Initializes the next level in a pane root
     * @param root
     */
    public static void nextLevel(Pane root) {
        root.getChildren().clear();

        root.getChildren().add(new Player(50, 545));

        EnemyLayout(root);
        BarrierLayout(root);
        phase++;
    }

    /**
     * Position the enemies on screen in an especific layout in a Pane root
     * @param root
     */
    private static void EnemyLayout(Pane root){
        final int initialx = 80;
        final int initialy = 40 + 20 * phase;
        final int spacing = 40;
        for (int i = 0; i < 11; i++) {
            Enemy a = new Enemy(initialx + i*spacing, initialy + 0 * spacing, 20, 20, "small_invader.gif", 40);
            Enemy b = new Enemy(initialx + i*spacing, initialy + 1 * spacing, 28, 20, "medium_invader.gif", 20);
            Enemy c = new Enemy(initialx + i*spacing, initialy + 2 * spacing, 28, 20, "medium_invader.gif", 20);
            Enemy d = new Enemy(initialx + i*spacing, initialy + 3 * spacing, 30, 20, "large_invader.gif", 10);
            Enemy e = new Enemy(initialx + i*spacing, initialy + 4 * spacing, 30, 20, "large_invader.gif", 10);

            root.getChildren().add(a);
            root.getChildren().add(b);
            root.getChildren().add(c);
            root.getChildren().add(d);
            root.getChildren().add(e);
        }
    }

    /**
     * Position the barriers on screen in an especific layout in a Pane root
     * @param root
     */
    private static void BarrierLayout(Pane root){
        for (int b = 0; b < 3; b++){
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 2; j++){
                    Barrier barrier = new Barrier( (80 + b*178) + 15*i, 500 - 15*j);
                    root.getChildren().add(barrier);
                }
            }
        }
    }
}
