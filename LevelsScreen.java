package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Texture;

public class LevelsScreen implements Screen {
    private Main game;
    private Stage stage;
    private SpriteBatch batch;
    private Skin skin;
    private int unlockedLevel;
    private Texture background;

    public LevelsScreen(Main game) {
        this.game = game;
        this.unlockedLevel = 1;  // Initially only level 1 is unlocked
        batch = new SpriteBatch();
        background = new Texture("level.png");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        createLevelButtons(table);
    }

    private void createLevelButtons(Table table) {
        for (int i = 1; i <= 10; i++) {
            TextButton levelButton = new TextButton("Level " + i, skin);
            levelButton.setDisabled(i > unlockedLevel);  // Disable button if level is locked
            table.add(levelButton).pad(10).fillX().uniformX();

            final int currentLevel = i;
            levelButton.addListener(event -> {
                if (event.isHandled() && currentLevel <= unlockedLevel) {
                    game.setScreen(new GameScreen(game, currentLevel)); // Start the game at the selected level
                    return true;
                }
                return false;
            });

            if (i % 2 == 0) table.row();  // Arrange in rows of two buttons
        }
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        // Draw the background texture, scaled to fit the window
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
        batch.dispose();
        background.dispose();
        stage.dispose();
    }

    // Call this method after completing a level to unlock the next level
    public void unlockNextLevel() {
        if (unlockedLevel < 10) {
            unlockedLevel++;
            updateLevelButtons(); // Update button states after unlocking
        }
    }

    // Update the buttons' enabled states based on unlocked levels
    private void updateLevelButtons() {
        // Iterate over all actors in the stage
        for (var actor : stage.getActors()) {
            if (actor instanceof TextButton) { // Check if the actor is a TextButton
                TextButton button = (TextButton) actor;
                int levelNumber = Integer.parseInt(button.getText().toString().replace("Level ", ""));
                button.setDisabled(levelNumber > unlockedLevel); // Disable if level is still locked
            }
        }
    }
}
