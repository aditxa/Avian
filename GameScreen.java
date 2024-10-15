package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen implements Screen {
    private final Main game;
    private Texture birdTexture;
    private Texture structureTexture;

    public GameScreen(Main game) {
        this.game = game;
        birdTexture = new Texture("bird.png");  // Add bird image
        structureTexture = new Texture("structure.png");  // Add structure image
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(birdTexture, 100, 100);  // Draw the bird
        game.batch.draw(structureTexture, 400, 100);  // Draw the structure
        game.batch.end();
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
        birdTexture.dispose();
        structureTexture.dispose();
    }
}
