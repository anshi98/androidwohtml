package com.ashi.screens;

import com.ashi.androidwohtml.androidwohtml;

import java.util.ArrayList;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


/**
 * Created by Andy on 2018-02-22.
 */

public class MenuScreen implements Screen {
    // Final variables
    private static final int LOGO_SCROLL_SPEED = 3;
    private static final int START_TEXT_BLINK_DELAY = 1;
    private static final int FONT_SIZE = 20;
    private static final int HUD_ELEM_Y_OFFSET = 20;
    private static final float START_TEXT_HEIGHT_PERCENT = 0.6f;
    private static final float COPYRIGHT_TEXT_HEIGHT_PERCENT = 0.5f;
    private static final float PERCENT_OF_WINDOW_HEIGHT_FOR_LOGO = 0.7f;
    // Main game
    private androidwohtml mainGame;
    // Images
    private Sprite logo;
    private Texture bg;
    // Camera
    private OrthographicCamera cam;
    // Text rendering
    private FreeTypeFontGenerator generator;
    private FreeTypeFontParameter parameter;
    private BitmapFont textRenderer;
    private GlyphLayout startGameText;
    private GlyphLayout copyrightText;
    // Misc. variables
    private float elapsedTime;
    private boolean canControl;
    // HUD
    private ArrayList<GlyphLayout> labels, values;


    public MenuScreen(androidwohtml mainGame) {
        this.mainGame = mainGame;

    }

    @Override
    public void show() {
        // Generate font renderer
        generator = new FreeTypeFontGenerator(Gdx.files.internal("emulogic.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        textRenderer = generator.generateFont(parameter);
        // Init elapsedTime
        elapsedTime = 0f;
        // Init textures and sprites
        bg = new Texture(Gdx.files.internal("background.png"));
        logo = new Sprite(new Texture(Gdx.files.internal("logo.png")));
        logo.setPosition(androidwohtml.WINDOW_WIDTH, androidwohtml.WINDOW_HEIGHT * PERCENT_OF_WINDOW_HEIGHT_FOR_LOGO);
        // Init GlyphLayouts
        startGameText = new GlyphLayout(textRenderer, "click or touch to start");
        copyrightText = new GlyphLayout(textRenderer, "©2018 indiebox games");
        // HUD elements init
        labels = new ArrayList<GlyphLayout>();
        values = new ArrayList<GlyphLayout>();
        labels.add(new GlyphLayout(textRenderer, "hi-score"));
        labels.add(new GlyphLayout(textRenderer, "player"));
        values.add(new GlyphLayout(textRenderer, androidwohtml.highestStreak + ""));
        values.add(new GlyphLayout(textRenderer, androidwohtml.player + ""));
        // Cam init
        cam = new OrthographicCamera(bg.getWidth(), bg.getHeight());
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        cam.update();

        canControl = false;
    }

    public void render(float delta) {
        // Clear screen and call helper methods
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        update();

        mainGame.batch.begin();
        // Since logo and bg are always drawn, draw them first
        mainGame.batch.draw(bg, 0, 0, androidwohtml.WINDOW_WIDTH, androidwohtml.WINDOW_HEIGHT);
        mainGame.batch.draw(logo, logo.getX(), logo.getY());

        if (canControl) {
            // Blink start text. If blink factor is even, then draw start text
            if ((int) (elapsedTime / START_TEXT_BLINK_DELAY) % 2 == 0)
                textRenderer.draw(mainGame.batch, startGameText, androidwohtml.WINDOW_WIDTH / 2 - startGameText.width / 2,
                        androidwohtml.WINDOW_HEIGHT * START_TEXT_HEIGHT_PERCENT);
            // Draw copyrightText
            textRenderer.draw(mainGame.batch, copyrightText, androidwohtml.WINDOW_WIDTH / 2 - copyrightText.width / 2,
                    androidwohtml.WINDOW_HEIGHT * COPYRIGHT_TEXT_HEIGHT_PERCENT);
            // Draw HUD
            for (int i = 0; i < values.size(); i++) {
                textRenderer.draw(mainGame.batch, labels.get(i), i * (androidwohtml.WINDOW_WIDTH / labels.size())
                        + (androidwohtml.WINDOW_WIDTH / labels.size() / 2 - labels.get(i).width / 2), androidwohtml.WINDOW_HEIGHT - HUD_ELEM_Y_OFFSET);
                textRenderer.draw(mainGame.batch, values.get(i),
                        i * (androidwohtml.WINDOW_WIDTH / values.size())
                                + (androidwohtml.WINDOW_WIDTH / values.size() / 2 - values.get(i).width / 2),
                        androidwohtml.WINDOW_HEIGHT - HUD_ELEM_Y_OFFSET * 2);
            }
        }
        mainGame.batch.end();
    }

    // Checks if title ready to be used
    private void update() {


        if (logo.getX() > androidwohtml.WINDOW_WIDTH / 2 - logo.getWidth() / 2) {
            // Still loading logo
            logo.setX(logo.getX() - LOGO_SCROLL_SPEED);
        } else {
            // in control
            canControl = true;

        }

        cam.update();
        mainGame.batch.setProjectionMatrix(cam.combined);

        elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public void handleInput() {
        if (canControl && Gdx.input.justTouched()) {
            mainGame.setScreen(new PreGameScreen(mainGame));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        logo.getTexture().dispose();
        generator.dispose();
        bg.dispose();
        textRenderer.dispose();

    }
}
