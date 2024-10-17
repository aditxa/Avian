package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture background;
    private Texture catapultTexture;
    private Stage stage;
    private boolean isPaused = false;
    private ImageButton pauseButton;
    private int currentLevel; // Store the current level

    // Additional variables for game objects
    private Image bird;
    private Sprite catapultSprite;
    private Sprite pigSprite;
    private Sprite block1Sprite, block2Sprite, block3Sprite;

    public GameScreen(Main game, int level) {
        this.game = game;
        this.currentLevel = level; // Assign the level number
        batch = new SpriteBatch();
        background = new Texture("game.png");
        catapultTexture = new Texture("catapult.png");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Pause button setup
        Texture pauseTexture = new Texture("pause.png");
        TextureRegionDrawable drawable = new TextureRegionDrawable(pauseTexture);
        pauseButton = new ImageButton(drawable);
        pauseButton.setPosition(Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 80);
        pauseButton.setSize(64, 64);

        pauseButton.addListener(event -> {
            if (pauseButton.isPressed() && !isPaused) {
                isPaused = true;
                game.setScreen(new PauseScreen(game, this)); // Go to PauseScreen
            }
            return true;
        });

        stage.addActor(pauseButton);

        // Initialize the catapult sprite
        catapultSprite = new Sprite(catapultTexture);
        catapultSprite.setPosition(100, 100); // Set catapult position

        // Initialize the bird
        bird = new Image(new Texture("bird.png"));
        bird.setPosition(50, 120); // Bird waiting on the left side of catapult
        bird.setSize(50, 50);
        addDragListenerToBird(); // Make bird draggable

        // Initialize the pig and blocks (tower)
        pigSprite = new Sprite(new Texture("pig.png"));
        pigSprite.setPosition(700, 300); // Set pig's position on the tower

        block1Sprite = new Sprite(new Texture("block.png"));
        block1Sprite.setPosition(650, 250); // Bottom block

        block2Sprite = new Sprite(new Texture("block.png"));
        block2Sprite.setPosition(670, 280); // Middle block

        block3Sprite = new Sprite(new Texture("block.png"));
        block3Sprite.setPosition(690, 310); // Top block

        // Add bird to the stage
        stage.addActor(bird);
    }

    private void addDragListenerToBird() {
        bird.addListener(new DragListener() {
            public void dragStart(InputListener event, float x, float y, int pointer) {
                // Optionally handle drag start if needed
            }

            public void drag(InputListener event, float x, float y, int pointer) {
                // Move the bird based on drag
                bird.moveBy(x - bird.getWidth() / 2, y - bird.getHeight() / 2);
            }

            public void dragStop(InputListener event, float x, float y, int pointer) {
                // When drag stops, place bird on catapult
                bird.setPosition(catapultSprite.getX() + 10, catapultSprite.getY() + 40);
            }
        });
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        catapultSprite.draw(batch); // Draw catapult
        pigSprite.draw(batch); // Draw pig
        block1Sprite.draw(batch); // Draw bottom block
        block2Sprite.draw(batch); // Draw middle block
        block3Sprite.draw(batch); // Draw top block
        batch.end();

        if (!isPaused) {
            stage.act(delta);
        }
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
        catapultTexture.dispose();
        stage.dispose();
    }

    // Method to resume the game
    public void resumeGame() {
        isPaused = false;
        Gdx.input.setInputProcessor(stage); // Reset the input processor to handle the stage
    }
}
