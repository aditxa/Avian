package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EndScreen implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture background;
    private String message;
    private boolean levelCompleted;
    private int currentLevel;

    public EndScreen(Main game, String message, int currentLevel, boolean levelCompleted) {
        this.game = game;
        this.message = message;
        this.levelCompleted = levelCompleted;
        this.currentLevel = currentLevel;

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        background = new Texture("endscreen.png"); // Make sure you have this image in your assets

        setupUI();
    }

    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create border texture
        Texture borderTexture = createBorderTexture(2, Color.WHITE);

        TextButton messageButton = new TextButton(message, skin);
        TextButton restartButton = new TextButton("Retry", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Set border drawable and text colors for all buttons
        for (TextButton button : new TextButton[]{messageButton, restartButton, exitButton}) {
            button.getStyle().up = new TextureRegionDrawable(borderTexture);
            button.getStyle().fontColor = Color.BLACK;
            button.getStyle().overFontColor = Color.WHITE;
        }

        // Restart button listener
        restartButton.addListener(event -> {
            if (restartButton.isPressed()) {
                game.setScreen(new GameScreen(game, currentLevel));
                return true;
            }
            return false;
        });

        // Home button listener
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                game.setScreen(new LevelsScreen(game));
                return true;
            }
            return false;
        });

        table.add(messageButton).fillX().uniformX().pad(10);
        table.row();
        table.add(restartButton).fillX().uniformX().pad(10);
        table.row();
        table.add(exitButton).fillX().uniformX().pad(10);

        if (levelCompleted) {
            TextButton nextLevelButton = new TextButton("Next Level", skin);
            nextLevelButton.getStyle().up = new TextureRegionDrawable(borderTexture);
            nextLevelButton.getStyle().fontColor = Color.BLACK;
            nextLevelButton.getStyle().overFontColor = Color.WHITE;

            nextLevelButton.addListener(event -> {
                if (nextLevelButton.isPressed()) {
                    game.setScreen(new GameScreen(game, currentLevel + 1));
                    return true;
                }
                return false;
            });

            table.row();
            table.add(nextLevelButton).fillX().uniformX().pad(10);
        }
    }

    private Texture createBorderTexture(int borderWidth, Color borderColor) {
        Pixmap pixmap = new Pixmap(200, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(borderColor);
        pixmap.fill();

        // Draw border
        pixmap.setColor(Color.ORANGE);
        pixmap.drawRectangle(0, 0, 200, 100);
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(borderWidth, borderWidth, 200 - 2 * borderWidth, 100 - 2 * borderWidth);

        Texture borderTexture = new Texture(pixmap);
        pixmap.dispose();
        return borderTexture;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        background.dispose();
        batch.dispose();
    }
}
