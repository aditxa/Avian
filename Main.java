package com.aditya.angrybirdsclone;

import com.badlogic.gdx.Game;
import com.aditya.angrybirdsclone.screens.HomeScreen;
import com.aditya.angrybirdsclone.screens.GameScreen;
import com.aditya.angrybirdsclone.screens.PauseScreen;

public class Main extends Game {

    @Override
    public void create() {
        // Start the game by displaying the Home Screen.
        this.setScreen(new HomeScreen(this));
    }

    // Method to switch to the Game Screen
    public void startGame() {
        this.setScreen(new GameScreen(this));
    }

    // Method to switch to the Pause Screen
    public void pauseGame() {
        this.setScreen(new PauseScreen(this));
    }

    // Method to return to the Home Screen
    public void returnToHome() {
        this.setScreen(new HomeScreen(this));
    }
}
