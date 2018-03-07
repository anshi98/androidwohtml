package com.ashi.screens;

import com.ashi.androidwohtml.androidwohtml;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;

public class GameOverScreen implements Screen {
    androidwohtml mainGame;

    private static final int FONT_SIZE = 40;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont textRenderer;

    private OrthographicCamera cam;

    private Texture bg;
    private Texture blockTexture;

    private GlyphLayout gameOverText;

    private ArrayList<ArrayList<Integer>> placedBlocks;

    public GameOverScreen(androidwohtml mainGame, ArrayList<ArrayList<Integer>> placedBlocks) {
        this.mainGame = mainGame;
        this.placedBlocks = placedBlocks;
    }

    @Override
    public void show() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("emulogic.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        textRenderer = generator.generateFont(parameter);

        gameOverText = new GlyphLayout(textRenderer, "game over");

        bg = new Texture(Gdx.files.internal("background.png"));

        blockTexture = new Texture(Gdx.files.internal("crate.png"));

        cam = new OrthographicCamera(androidwohtml.WINDOW_WIDTH, androidwohtml.WINDOW_HEIGHT);
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        cam.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();

        mainGame.batch.begin();
        mainGame.batch.draw(bg, 0, 0, androidwohtml.WINDOW_WIDTH, androidwohtml.WINDOW_HEIGHT);

        for (int i = 0; i < placedBlocks.size(); i++) {
            for (int j : placedBlocks.get(i)) {
                mainGame.batch.draw(blockTexture, j * blockTexture.getWidth(), i * blockTexture.getHeight());
            }
        }

        textRenderer.draw(mainGame.batch, gameOverText, androidwohtml.WINDOW_WIDTH / 2 - gameOverText.width / 2,
                androidwohtml.WINDOW_HEIGHT / 2 + gameOverText.height / 2);

        mainGame.batch.end();

    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            mainGame.setScreen(new PreGameScreen(mainGame));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        generator.dispose();
        textRenderer.dispose();
        bg.dispose();
        blockTexture.dispose();

    }
}