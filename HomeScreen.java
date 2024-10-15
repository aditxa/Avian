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

public class HomeScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private Texture homeScreenImage;
    private Stage stage;
    private Skin skin;

    public HomeScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        homeScreenImage = new Texture("homescreen.png");
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create Start Button
        TextButton startButton = new TextButton("Start", skin);
        startButton.setPosition(200, 150);
        startButton.setSize(200, 50);

        // Add a listener to handle click events
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.startGame(); // Transition to Game Screen when button is clicked
            }
        });

        stage.addActor(startButton);
        Gdx.input.setInputProcessor(stage); // Set the stage to handle input events
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(homeScreenImage, 0, 0);
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
        homeScreenImage.dispose();
        stage.dispose();
        skin.dispose();
    }
}
