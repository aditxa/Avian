package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture background;
    private Texture catapultTexture;
    private Stage stage;
    private boolean isPaused = false;
    private ImageButton pauseButton;
    private int currentLevel;

    private Image bird;
    private Group catapultGroup;
    private Image catapultImage;
    private Image pig;
    private Image block1, block2, block3;

    // Initial position for the bird
    private float initialBirdX = 50;
    private float initialBirdY = 67;

    public GameScreen(Main game, int level) {
        this.game = game;
        this.currentLevel = level;
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
                game.setScreen(new PauseScreen(game, this));
            }
            return true;
        });

        stage.addActor(pauseButton);

        // Initialize the catapult and bird
        catapultGroup = new Group();
        catapultImage = new Image(catapultTexture);
        catapultImage.setPosition(70, 60);
        catapultGroup.addActor(catapultImage);

        // Initialize the bird
        bird = new Image(new Texture("bird.png"));
        bird.setPosition(initialBirdX, initialBirdY);
        bird.setSize(50, 50);
        addClickListenerToBird();
        stage.addActor(bird);  // Add bird directly to stage

        // Initialize the pig and blocks
        pig = new Image(new Texture("pig.png"));
        pig.setPosition(450, 300);
        pig.setSize(70, 70);

        block1 = new Image(new Texture("block.png"));
        block1.setPosition(470, 213);
        block1.setSize(30, 100);
        block2 = new Image(new Texture("block.png"));
        block2.setPosition(470, 135);
        block2.setSize(30, 100);
        block3 = new Image(new Texture("block.png"));
        block3.setPosition(470, 57);
        block3.setSize(30, 100);

        // Add to stage
        stage.addActor(catapultGroup);
        stage.addActor(pig);
        stage.addActor(block1);
        stage.addActor(block2);
        stage.addActor(block3);
    }

    private void addClickListenerToBird() {
        bird.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (bird.getX() == initialBirdX && bird.getY() == initialBirdY) {
                    // Move bird to catapult
                    bird.setPosition((catapultImage.getX() + (catapultImage.getWidth() - bird.getWidth()) / 2)-9,
                        catapultImage.getY() + catapultImage.getHeight() - bird.getHeight() / 2);
                } else {
                    // Return bird to initial position
                    bird.setPosition(initialBirdX, initialBirdY);
                }
                return true;
            }
        });
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

    public void resumeGame() {
        isPaused = false;
        Gdx.input.setInputProcessor(stage);
    }
}
