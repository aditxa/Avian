package com.aditya.angrybirdsclone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.aditya.angrybirdsclone.screens.HomeScreen;

public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new HomeScreen(this)); // Start with the Home Screen
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
