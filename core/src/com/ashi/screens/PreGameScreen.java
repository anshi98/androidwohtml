package com.ashi.screens;

import com.badlogic.gdx.Screen;
import com.ashi.androidwohtml.androidwohtml;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Andy on 2018-02-22.
 */

public class PreGameScreen implements Screen {
    private static final int FONT_SIZE = 20;
    private static final int MAX_DIFF = 30;
    private static final float SPACING = 80;
    private static final float SHIFT_TIME = 0.5f;

    private androidwohtml mainGame;
    private OrthographicCamera cam;
    private float elapsedTime;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontParameter parameter;
    private BitmapFont textRenderer;

    private Sprite frontArrow, backArrow, playButton;
    private Texture bg;

    private GlyphLayout difficultyLabel, difficultyValue;

    public PreGameScreen(androidwohtml mainGame) {
        this.mainGame = mainGame;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        // Init text renderers
        generator = new FreeTypeFontGenerator(Gdx.files.internal("emulogic.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        textRenderer = generator.generateFont(parameter);
        // Init GlyphLayouts
        difficultyLabel = new GlyphLayout(textRenderer, "difficulty select");
        difficultyValue = new GlyphLayout(textRenderer, androidwohtml.difficulty + "");
        // Init textures and sprites and sets their positions
        bg = new Texture(Gdx.files.internal("background.png"));
        frontArrow = new Sprite(new Texture(Gdx.files.internal("arrowFront.png")));
        backArrow = new Sprite(new Texture(Gdx.files.internal("arrowBack.png")));
        // (androidwohtml.WINDOW_WIDTH - arrow.getWidth())/2 centers the arrows
        // Adding or subtracting by difficultyValue makes sure the arrow doesn't overlap with the difficultyValue text
        // SPACING used to space arrows and difficultyValue more
        frontArrow.setPosition(
                (androidwohtml.WINDOW_WIDTH - frontArrow.getWidth()) / 2 + SPACING + difficultyValue.width / 2,
                (androidwohtml.WINDOW_HEIGHT - frontArrow.getHeight()) / 2);
        backArrow.setPosition(
                (androidwohtml.WINDOW_WIDTH - backArrow.getWidth()) / 2 - SPACING - difficultyValue.width / 2,
                (androidwohtml.WINDOW_HEIGHT - backArrow.getHeight()) / 2);
        // Centers the button and then changes the height by SPACING
        playButton = new Sprite(new Texture(Gdx.files.internal("playButton.png")));
        playButton.setPosition((androidwohtml.WINDOW_WIDTH - playButton.getWidth()) / 2,
                (androidwohtml.WINDOW_HEIGHT - playButton.getHeight()) / 2 - SPACING);
        // Init cam
        cam = new OrthographicCamera(androidwohtml.WINDOW_WIDTH, androidwohtml.WINDOW_HEIGHT);
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        cam.update();
        // Init elapsedTime
        elapsedTime = 0f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        update();

        mainGame.batch.begin();
        mainGame.batch.draw(bg, 0, 0, androidwohtml.WINDOW_WIDTH, androidwohtml.WINDOW_HEIGHT);

        textRenderer.draw(mainGame.batch, difficultyLabel, (androidwohtml.WINDOW_WIDTH - difficultyLabel.width) / 2,
                (androidwohtml.WINDOW_HEIGHT - difficultyLabel.height) / 2 + SPACING);

        textRenderer.draw(mainGame.batch, difficultyValue, androidwohtml.WINDOW_WIDTH / 2 - difficultyValue.width / 2,
                androidwohtml.WINDOW_HEIGHT / 2 + difficultyValue.height / 2);

        frontArrow.draw(mainGame.batch);

        backArrow.draw(mainGame.batch);

        playButton.draw(mainGame.batch);

        mainGame.batch.end();
    }

    // Checks if button has been clicked (any button of the 3)
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            if (textureClicked(frontArrow) && androidwohtml.difficulty < MAX_DIFF) {
                androidwohtml.difficulty++;

                // Change difficulty value
                difficultyValue = new GlyphLayout(textRenderer, androidwohtml.difficulty + "");

            }
            if (textureClicked(backArrow) && androidwohtml.difficulty > 1) {
                androidwohtml.difficulty--;

                // Change difficulty value
                difficultyValue = new GlyphLayout(textRenderer, androidwohtml.difficulty + "");

            }
            if (textureClicked(playButton)) {
                mainGame.setScreen(new InGameScreen(mainGame));
                dispose();
            }
        }
    }

    // self exp.
    private boolean textureClicked(Sprite texture) {

        Vector3 mousepos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mousepos);
        int x = (int) mousepos.x;
        int y = (int) mousepos.y;

        return x > texture.getX() && x < texture.getX() + texture.getWidth() && y > texture.getY()
                && y < texture.getY() + texture.getHeight();
    }

    private void update() {
        cam.update();
        mainGame.batch.setProjectionMatrix(cam.combined);

        elapsedTime += Gdx.graphics.getDeltaTime();

        // Shifting arrow effect. Alternates between 1 and -1 depending on parity of elapsedTime/SHIFT_TIME
        frontArrow.setPosition(frontArrow.getX() + (((int) (elapsedTime / SHIFT_TIME % 2) == 0) ? (1) : (-1)),
                frontArrow.getY());
        backArrow.setPosition(backArrow.getX() + (((int) (elapsedTime / SHIFT_TIME % 2) == 0) ? (-1) : (1)),
                backArrow.getY());
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
        bg.dispose();
        generator.dispose();
        frontArrow.getTexture().dispose();
        backArrow.getTexture().dispose();
        playButton.getTexture().dispose();
        textRenderer.dispose();
    }
}
