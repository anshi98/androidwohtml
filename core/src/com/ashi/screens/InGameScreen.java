package com.ashi.screens;

import com.ashi.androidwohtml.androidwohtml;
import com.ashi.androidwohtml.blockSet;
import com.ashi.androidwohtml.direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;


/**
 * Created by Andy on 2018-02-24.
 */


public class InGameScreen implements Screen {
    private final int DIFFICULTY = androidwohtml.difficulty;
    private final int INIT_BLOCKS_AMOUNT = 3;
    private final float SPEED_CONSTANT = 0.5f;
    private final float SPEED = SPEED_CONSTANT / DIFFICULTY;

    private androidwohtml mainGame;

    private OrthographicCamera cam;

    private Texture blockTexture;
    private Texture bg;

    private direction currDirection;
    private int blocksLeft;
    private int rows, columns;
    private int currHeight;
    private float moveDelayTime;
    private blockSet currBlockSet;
    private ArrayList<ArrayList<Integer>> placedBlocks;

    public InGameScreen(androidwohtml mainGame) {
        this.mainGame = mainGame;

    }

    @Override
    public void show() {
        placedBlocks = new ArrayList<ArrayList<Integer>>();

        moveDelayTime = 0f;

        bg = new Texture(Gdx.files.internal("background.png"));

        // Init direction to right
        currDirection = direction.RIGHT;

        cam = new OrthographicCamera(androidwohtml.WINDOW_WIDTH, androidwohtml.WINDOW_HEIGHT);
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        cam.update();

        blocksLeft = INIT_BLOCKS_AMOUNT;

        blockTexture = new Texture(Gdx.files.internal("crate.png"));

        rows = androidwohtml.WINDOW_HEIGHT / blockTexture.getHeight();
        columns = androidwohtml.WINDOW_WIDTH / blockTexture.getWidth();

        currBlockSet = new blockSet(0, blocksLeft);
        currHeight = 0;
    }

    public void render(float dt) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        update();

        mainGame.batch.begin();
        // Draw current block set
        mainGame.batch.draw(bg, 0, 0, androidwohtml.WINDOW_WIDTH, androidwohtml.WINDOW_HEIGHT);
        for (int i = currBlockSet.getLeftMostBlock(); i < currBlockSet.getRightMostBlock(); i++) {
            mainGame.batch.draw(blockTexture, i * blockTexture.getWidth(), currHeight * blockTexture.getHeight());
        }
        // Draw placed blocks
        for (int i = 0; i < placedBlocks.size(); i++) {
            for (int j : placedBlocks.get(i)) {
                mainGame.batch.draw(blockTexture, j * blockTexture.getWidth(), i * blockTexture.getHeight());
            }
        }


        mainGame.batch.end();
    }

    // Checks if title ready to be used
    private void update() {
        cam.update();
        mainGame.batch.setProjectionMatrix(cam.combined);

        moveDelayTime += Gdx.graphics.getDeltaTime();

        // Code that shifts the current block set. Once moveDelayTime becomes >= SPEED, it gets reset and move() gets called
        if (moveDelayTime >= SPEED) {
            moveDelayTime = 0;
            move();
        }
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            placeBlock();
        }
    }

    public void move() {
        //If the blockset gets to one end of the screen, the direction changes
        if (currDirection == direction.RIGHT) {
            if (currBlockSet.getRightMostBlock() == columns) {
                currDirection = direction.LEFT;
            }

        } else {
            if (currBlockSet.getLeftMostBlock() == 0) {
                currDirection = direction.RIGHT;
            }

        }

        currBlockSet.moveBlock(currDirection);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    public void placeBlock() {

        // Positions upon placing a blockset
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (int i = currBlockSet.getLeftMostBlock(); i < currBlockSet.getRightMostBlock(); i++) {
            positions.add(i);
        }

        if (placedBlocks.isEmpty()) {
            // currHeight is 0
            placedBlocks.add(positions);
            currHeight++;
            currBlockSet = new blockSet(0, blocksLeft);
        } else {
            ArrayList<Integer> lastLevel = placedBlocks.get(placedBlocks.size() - 1);

            ArrayList<Integer> toRemove = new ArrayList<Integer>();

            for (int curr : positions) {
                if (!lastLevel.contains(curr)) {
                    // Missed a block on the last level
                    toRemove.add(curr);
                    blocksLeft--;
                }
            }

            positions.removeAll(toRemove);

            if (positions.isEmpty()) {
                mainGame.setScreen(new GameOverScreen(mainGame, placedBlocks));
                dispose();
            } else {
                placedBlocks.add(positions);
                currBlockSet = new blockSet(0, blocksLeft);
                currHeight++;
                if (currHeight == rows) {
                    mainGame.setScreen(new GameOverScreen(mainGame, placedBlocks));
                    dispose();
                }
            }
        }


    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bg.dispose();
        blockTexture.dispose();
    }
}
