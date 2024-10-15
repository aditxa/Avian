package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private Texture gameImage;
    private Stage stage;
    private Skin skin;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        gameImage = new Texture("bird.png");  // Example game texture
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create Pause Button
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(200, 150);
        pauseButton.setSize(200, 50);

        // Add a listener for the pause button
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.pauseGame();  // Switch to Pause Screen
            }
        });

        stage.addActor(pauseButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(gameImage, 0, 0);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        gameImage.dispose();
        stage.dispose();
        skin.dispose();
    }
}
