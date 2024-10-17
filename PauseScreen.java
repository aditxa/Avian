package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture background;
    private GameScreen gameScreen;  // Reference to the GameScreen

    public PauseScreen(Main game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen; // Store the current game screen
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        background = new Texture("pausescreen.png"); // Ensure this is the correct pause screen background

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Handle Resume button click
        resumeButton.addListener(event -> {
            if (event.isHandled()) {
                gameScreen.resumeGame(); // Call method to resume the game
                game.setScreen(gameScreen); // Switch back to the game screen
                return true;
            }
            return false;
        });

        // Handle Exit button click
        exitButton.addListener(event -> {
            if (event.isHandled()) {
                game.setScreen(new LevelsScreen(game)); // Return to home screen
                return true;
            }
            return false;
        });

        table.add(resumeButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(exitButton).fillX().uniformX();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();

        stage.act(delta);
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
        stage.dispose();
        background.dispose();
        batch.dispose();
    }
}
