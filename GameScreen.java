package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture background;
    private Texture catapult;
    private Texture pauseTexture;
    private Stage stage;
    private Skin skin;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("gamebackground.png"); // Load your game background
        catapult = new Texture("catapult.png"); // Load your catapult image
        pauseTexture = new Texture("pause.png"); // Load your pause button image
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create pause button
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(pauseTexture));
        pauseButton.setPosition(Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 80);

        pauseButton.addListener(event -> {
            if (event.isHandled()) {
                game.setScreen(new PauseScreen(game)); // Go to pause screen
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
        batch.draw(background, 0, 0);
        batch.draw(catapult, 100, 100); // Position the catapult
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
        batch.dispose();
        background.dispose();
        catapult.dispose();
        pauseTexture.dispose();
        stage.dispose();
    }
}
