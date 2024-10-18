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

public class HomeScreen implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture background;

    public HomeScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        background = new Texture("homescreen.png"); // Your home screen background

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton startButton = new TextButton("Start", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Handle Start button click
        startButton.addListener(event -> {
            if (event.isHandled()) {
                game.setScreen(new LevelsScreen(game)); // Go to levels screen
                return true;
            }
            return false;
        });

        // Handle Exit button click
        exitButton.addListener(event -> {
            if (event.isHandled()) {
                Gdx.app.exit(); // Exit the application
                return true;
            }
            return false;
        });

        table.add(startButton).fillX().uniformX().minWidth(150).minHeight(50).pad(10); // Make the button bigger with padding
        table.row().pad(1, 0, 10, 0);
        table.add(exitButton).fillX().uniformX().minWidth(150).minHeight(50).pad(10); // Same for the exit button

    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
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
