package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture background;
    private Texture catapult;
    private Texture pauseTexture;
    private Stage stage;
    private Skin skin;
    private int currentLevel;
    private boolean isPaused = false; // Track pause state

    public GameScreen(Main game, int level) {
        this.game = game;
        this.currentLevel = level;
        batch = new SpriteBatch();
        background = new Texture("game.png");
        catapult = new Texture("catapult.png");
        pauseTexture = new Texture("pause.png");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create a drawable with a black thick outline for the pause button
        Drawable drawable = new TextureRegionDrawable(pauseTexture);
        ImageButton pauseButton = new ImageButton(drawable);
        pauseButton.setPosition(Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 80);
        pauseButton.setSize(64, 64); // Set size of the button

        // Add a thick black border to the button
        pauseButton.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
        pauseButton.getStyle().up = drawable;
        pauseButton.getStyle().down = drawable; // Keep the same drawable for pressed state
        pauseButton.getStyle().over = drawable; // Keep the same drawable for hovered state

        pauseButton.addListener(event -> {
            if (event.isHandled()) {
                if (!isPaused) {
                    isPaused = true;
                    game.setScreen(new PauseScreen(game, this)); // Switch to PauseScreen
                }
                return true;
            }
            return false;
        });

        stage.addActor(pauseButton);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Adjusted to draw background correctly
        batch.draw(catapult, 100, 100); // Position the catapult
        batch.end();

        if (!isPaused) { // Only update the stage if not paused
            stage.act(delta);
        }
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        catapult.dispose();
        pauseTexture.dispose();
        stage.dispose();
    }

    // Call this method when the level is completed to unlock the next level
    public void completeLevel() {
        if (currentLevel < 10) {
            game.getLevelsScreen().unlockNextLevel(); // Unlock next level
        }
    }

//Method to resume the game
    public void resumeGame() {
        isPaused = false; // Set the pause state to false
    }
}
