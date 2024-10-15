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

public class PauseScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private Texture pauseScreenImage;
    private Stage stage;
    private Skin skin;

    public PauseScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        pauseScreenImage = new Texture("pause.png");  // Pause screen texture
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create Resume Button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.setPosition(200, 150);
        resumeButton.setSize(200, 50);

        // Add a listener to handle click events
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.startGame(); // Return to the Game Screen
            }
        });

        stage.addActor(resumeButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(pauseScreenImage, 0, 0);
        batch.end();

        // Draw UI (buttons)
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
        pauseScreenImage.dispose();
        stage.dispose();
        skin.dispose();
    }
}
