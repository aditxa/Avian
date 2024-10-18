package com.aditya.angrybirdsclone.screens;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture background;
    private Stage stage;
    private boolean isPaused = false;
    private ImageButton pauseButton;
    private int currentLevel;

    private Image bird;
    private Image catapult;
    private Image pig;
    private Image block1, block2, block3;

    private Vector2 catapultPos;
    private Vector2 birdPos;
    private Vector2 dragStart;
    private boolean isDragging = false;
    private boolean isFlying = false;
    private Vector2 velocity;
    private ShapeRenderer shapeRenderer;

    private static final float MAX_DRAG_DISTANCE = 100f;
    private static final float LAUNCH_SPEED_FACTOR = 5f;
    private static final float GRAVITY = -9.8f;

    private Array<Vector2> trajectoryPoints;

    public GameScreen(Main game, int level) {
        this.game = game;
        this.currentLevel = level;
        batch = new SpriteBatch();
        background = new Texture("game.png");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        shapeRenderer = new ShapeRenderer();
        trajectoryPoints = new Array<>();

        setupGameElements();
    }

    private void setupGameElements() {
        // Setup catapult
        catapult = new Image(new Texture("catapult.png"));
        catapult.setPosition(50, 50);
        catapultPos = new Vector2(catapult.getX() + catapult.getWidth() / 2,
            catapult.getY() + catapult.getHeight() - 20);

        // Setup bird
        bird = new Image(new Texture("bird.png"));
        bird.setSize(50, 50);
        resetBird();

        // Setup obstacles
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

        // Setup pause button
        Texture pauseTexture = new Texture("pause.png");
        pauseButton = new ImageButton(new TextureRegionDrawable(pauseTexture));
        pauseButton.setPosition(Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 80);
        pauseButton.setSize(64, 64);

        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isPaused = true;
                game.setScreen(new PauseScreen(game, GameScreen.this));
                return true;
            }
        });

        // Add elements to stage
        stage.addActor(catapult);
        stage.addActor(bird);
        stage.addActor(pig);
        stage.addActor(block1);
        stage.addActor(block2);
        stage.addActor(block3);
        stage.addActor(pauseButton);

        // Setup bird input listener
        bird.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!isFlying) {
                    isDragging = true;
                    dragStart = new Vector2(x, y);
                }
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isDragging) {
                    Vector2 dragCurrent = new Vector2(x, y);
                    Vector2 dragVector = dragStart.cpy().sub(dragCurrent);
                    if (dragVector.len() > MAX_DRAG_DISTANCE) {
                        dragVector.setLength(MAX_DRAG_DISTANCE);
                    }
                    birdPos.set(catapultPos.x - dragVector.x, catapultPos.y - dragVector.y);
                    bird.setPosition(birdPos.x - bird.getWidth() / 2, birdPos.y - bird.getHeight() / 2);
                    calculateTrajectory(dragVector);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDragging) {
                    isDragging = false;
                    isFlying = true;
                    Vector2 dragVector = new Vector2(catapultPos.x - birdPos.x, catapultPos.y - birdPos.y);
                    velocity = dragVector.scl(LAUNCH_SPEED_FACTOR);
                }
            }
        });
    }

    private void resetBird() {
        birdPos = new Vector2(catapultPos.x, catapultPos.y);
        bird.setPosition(birdPos.x - bird.getWidth() / 2, birdPos.y - bird.getHeight() / 2);
        isFlying = false;
        velocity = new Vector2();
    }

    private void calculateTrajectory(Vector2 dragVector) {
        trajectoryPoints.clear();
        Vector2 velocityForTrajectory = dragVector.cpy().scl(LAUNCH_SPEED_FACTOR);
        Vector2 pos = new Vector2(birdPos);

        for (float t = 0; t < 2; t += 0.1f) {
            float x = pos.x + velocityForTrajectory.x * t;
            float y = pos.y + velocityForTrajectory.y * t + 0.5f * GRAVITY * t * t;
            trajectoryPoints.add(new Vector2(x, y));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (!isPaused) {
            updateBird(delta);
            checkCollisions();
            stage.act(delta);
        }

        stage.draw();

        if (isDragging) {
            renderTrajectory();
        }
    }

    private void updateBird(float delta) {
        if (isFlying) {
            velocity.y += GRAVITY * delta;
            birdPos.add(velocity.x * delta, velocity.y * delta);
            bird.setPosition(birdPos.x - bird.getWidth() / 2, birdPos.y - bird.getHeight() / 2);

            // Reset bird if it goes off screen
            if (birdPos.y < 0 || birdPos.x > Gdx.graphics.getWidth()) {
                resetBird();
            }
        }
    }

    private void checkCollisions() {
        // Implement collision detection logic here
        // For simplicity, we're not implementing it in this example
    }

    private void renderTrajectory() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 0.5f);
        for (int i = 1; i < trajectoryPoints.size; i++) {
            Vector2 point1 = trajectoryPoints.get(i - 1);
            Vector2 point2 = trajectoryPoints.get(i);
            shapeRenderer.line(point1, point2);
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {}

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
        shapeRenderer.dispose();
    }

    public void resumeGame() {
        isPaused = false;
        Gdx.input.setInputProcessor(stage);
    }
}
