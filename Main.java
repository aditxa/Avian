package com.aditya.angrybirdsclone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.aditya.angrybirdsclone.screens.HomeScreen;

public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new HomeScreen(this));  // Set the home screen as the default screen
    }

    @Override
    public void render() {
        super.render();  // Render the active screen
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
