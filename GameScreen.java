package com.aditya.angrybirdsclone.screen;

import com.aditya.angrybirdsclone.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
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
    private Array<Image> pigs;
    private Array<Image> blocks;

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
    private int birdsRemaining;  // Track birds left to launch

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
        catapultPos = new Vector2(catapult.getX() + catapult.getWidth() / 2, catapult.getY() + catapult.getHeight() - 20);

        // Setup bird
        bird = new Image(new Texture("bird.png"));
        bird.setSize(50, 50);
        resetBird();

        // Setup pigs and blocks based on level
        pigs = new Array<>();
        blocks = new Array<>();

        if (currentLevel == 1) {
            setupLevel1();
            birdsRemaining = 3;
        } else if (currentLevel == 2) {
            setupLevel2();
            birdsRemaining = 2;
        }

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

    // New method to setup Level 1
    private void setupLevel1() {
        // Setup pigs
        Image pig = new Image(new Texture("pig.png"));
        pig.setPosition(450, 300);
        pig.setSize(70, 70);
        pigs.add(pig);
        stage.addActor(pig);

        // Setup blocks
        for (int i = 0; i < 3; i++) {
            Image block = new Image(new Texture("block.png"));
            block.setPosition(470, 57 + i * 78);
            block.setSize(30, 100);
            blocks.add(block);
            stage.addActor(block);
        }
    }

    // New method to setup Level 2
    private void setupLevel2() {
        // First tower
        for (int i = 0; i < 3; i++) {
            Image block = new Image(new Texture("block.png"));
            block.setPosition(400, 57 + i * 78);
            block.setSize(30, 100);
            blocks.add(block);
            stage.addActor(block);
        }

        Image pig1 = new Image(new Texture("pig.png"));
        pig1.setPosition(380, 300);
        pig1.setSize(70, 70);
        pigs.add(pig1);
        stage.addActor(pig1);

        // Second tower
        for (int i = 0; i < 4; i++) {
            Image block = new Image(new Texture("block.png"));
            block.setPosition(550, 57 + i * 78);
            block.setSize(30, 100);
            blocks.add(block);
            stage.addActor(block);
        }

        Image pig2 = new Image(new Texture("pig.png"));
        pig2.setPosition(530, 378);
        pig2.setSize(70, 70);
        pigs.add(pig2);
        stage.addActor(pig2);
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

            // Reset bird if it goes off screen or hits pig
            if (birdPos.y < 0 || birdPos.x > Gdx.graphics.getWidth()) {
                birdsRemaining--;
                if (birdsRemaining <= 0) {
                    game.setScreen(new EndScreen(game, "Level Failed!", currentLevel, false));
                } else {
                    resetBird();
                }
            }
        }
    }

    private void checkCollisions() {
        Rectangle birdBounds = new Rectangle(birdPos.x, birdPos.y, bird.getWidth(), bird.getHeight());

        // Check collisions with pigs
        for (Image pig : new Array.ArrayIterator<>(pigs)) {
            Rectangle pigBounds = new Rectangle(pig.getX(), pig.getY(), pig.getWidth(), pig.getHeight());
            if (Intersector.overlaps(birdBounds, pigBounds)) {
                pig.remove();
                pigs.removeValue(pig, true);
                resetBird();

                // Check if all pigs are destroyed
                if (pigs.size == 0) {
                    game.setScreen(new EndScreen(game, "Level Complete!", currentLevel, true));
                }
                break;
            }
        }

        // Check collisions with blocks
        for (Image block : new Array.ArrayIterator<>(blocks)) {
            Rectangle blockBounds = new Rectangle(block.getX(), block.getY(), block.getWidth(), block.getHeight());
            if (Intersector.overlaps(birdBounds, blockBounds)) {
                block.remove();
                blocks.removeValue(block, true);
                break;
            }
        }
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
