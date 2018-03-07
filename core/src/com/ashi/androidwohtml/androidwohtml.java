package com.ashi.androidwohtml;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ashi.screens.*;

public class androidwohtml extends Game {
    // Final variables
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_WIDTH = 500;
    public static final int DEFAULT_DIFFICULTY = 1;
    public static final int HIGHEST_STREAK_PLACEHOLDER = 50;
    public static final String PLAYER_PLACEHOLDER = "andy";

    public static int highestStreak;
    public static int difficulty;
    public static String player;
    public static SpriteBatch batch;


    // TODO read player and highestStreak from a file
    // Screen initially set to MenuScreen
    public void create() {
        batch = new SpriteBatch();
        player = PLAYER_PLACEHOLDER;
        difficulty = DEFAULT_DIFFICULTY;
        highestStreak = HIGHEST_STREAK_PLACEHOLDER;
        setScreen(new MenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
    }
}
