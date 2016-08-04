package com.diegoalves.flappybrid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Circle;
import com.sun.org.apache.xpath.internal.operations.String;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    private SpriteBatch mBatch;
    private Texture[] birds;
    private Texture bDied;
    private Texture birdFear;
    private Texture scene;
    private Texture tubeTop;
    private Texture tubeBottom;
    private Texture life;
    private BitmapFont font;
    private Texture gameOver;

    private Circle birdCircle;
    private Rectangle tubeTopRec;
    private Rectangle tubeBottomRec;
    //private ShapeRenderer shapeRenderer;

    // configs
    private int deviceWidth;
    private int deviceHeight;
    private Random randomNumber;
    private int normalizer = 300;
    private int gameState = 0;
    private int score = 0;
    private int lifeCount = 3;
    private int illegalAction = 0;

    private float initPositionBird;
    private float variation = 0;
    private float fallDown = 0;
    private float tubePositionH;
    private float tubePositionW;
    private float spaceBetweenTubes;
    private float spaceBetweenTubesRandom;

    private boolean scorePlus;


    @Override
    public void create() {

        mBatch = new SpriteBatch();
        randomNumber = new Random();
        birdCircle = new Circle();
        //tubeTopRec = new Rectangle();
        // tubeBottomRec = new Rectangle();
        // shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(8);
        birds = new Texture[3];
        birds[0] = new Texture("b1.png");
        birds[1] = new Texture("b2.png");
        birds[2] = new Texture("b3.png");
        bDied = new Texture("b_died.png");
        scene = new Texture("scene.png");
        tubeTop = new Texture("tube-up.png");
        tubeBottom = new Texture("tube-bottom.png");
        gameOver = new Texture("gameOver.png");
        life = new Texture("life.png");

        deviceWidth = Gdx.graphics.getWidth();
        deviceHeight = Gdx.graphics.getHeight();
        initPositionBird = deviceHeight / 2;
        tubePositionH = deviceWidth;
        tubePositionW = deviceHeight / 2;
        spaceBetweenTubes = 650;
    }

    @Override
    public void render() {
        // bird movement
        variation += Gdx.graphics.getDeltaTime() * 7;
        if (variation > 2)
            variation = 0;

        // init
        if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else {

            fallDown++;
            if (initPositionBird > 0 || fallDown < 0)
                initPositionBird -= fallDown;

            if (gameState == 1) {

                tubePositionH -= Gdx.graphics.getDeltaTime() * 250;

                if (Gdx.input.justTouched()) {
                    fallDown = -20;
                }

                // verify tube screen out
                if (tubePositionH < -tubeTop.getWidth() * 6) {
                    tubePositionH = deviceWidth;
                    spaceBetweenTubesRandom = randomNumber.nextInt(400) - 200;
                    scorePlus = false;
                }

                // verify points
                if (tubePositionH < 120) {
                    if (!scorePlus) {
                        score++;
                        scorePlus = true;
                    }
                }
            } else {
                if (Gdx.input.justTouched()) {
                    gameState = 0;
                    score = 0;
                    fallDown = 0;
                    initPositionBird = deviceHeight / 2;
                    tubePositionH = deviceWidth;
                }
            }

        }

        mBatch.begin();
        mBatch.draw(scene, 0, 0, deviceWidth, deviceHeight);
        mBatch.draw(tubeTop, tubePositionH, tubePositionW + spaceBetweenTubes + spaceBetweenTubesRandom - normalizer, 150, deviceHeight);
        mBatch.draw(tubeBottom, tubePositionH, 0 - spaceBetweenTubes + spaceBetweenTubesRandom - normalizer, 150, deviceHeight);

        if(gameState == 2) {
            mBatch.draw(bDied, 120, 0, 150, 150);
        } else {
            mBatch.draw(birds[(int) variation], 120, initPositionBird, 150, 150);
        }

        if (lifeCount == 3 && gameState != 2) {
            mBatch.draw(life, 30, deviceHeight - 100);
            mBatch.draw(life, 100, deviceHeight - 100);
            mBatch.draw(life, 170, deviceHeight - 100);
        } else if (lifeCount == 2) {
            mBatch.draw(life, 30, deviceHeight - 100);
            mBatch.draw(life, 100, deviceHeight - 100);
        } else if (lifeCount == 1) {
            mBatch.draw(life, 30, deviceHeight - 100);
        }

        font.draw(mBatch, java.lang.String.valueOf(score), deviceWidth / 2, deviceHeight - 150);

        if (gameState == 2) {
            mBatch.draw(gameOver, deviceHeight / 3 - gameOver.getWidth() -100, deviceWidth, 650, 250);
        }

        mBatch.end();

        birdCircle.set(120 + birds[0].getWidth() / 1 + 10, initPositionBird + 65, birds[0].getWidth() / 2 + birds[0].getHeight() / 2);
        tubeTopRec = new Rectangle(tubePositionH,
                0 - spaceBetweenTubes + spaceBetweenTubesRandom - normalizer,
                150, deviceHeight);
        tubeBottomRec = new Rectangle(tubePositionH,
                tubePositionW + spaceBetweenTubes + spaceBetweenTubesRandom - normalizer,
                150, deviceHeight);

        // designer forms
        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
        shapeRenderer.rect(tubeTopRec.x, tubeTopRec.y, tubeTopRec.width, tubeTopRec.height);
        shapeRenderer.rect(tubeBottomRec.x, tubeBottomRec.y, tubeBottomRec.width, tubeBottomRec.height);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.end();*/

        // intercepts
        if (Intersector.overlaps(birdCircle, tubeTopRec) || Intersector.overlaps(birdCircle, tubeBottomRec)
                || initPositionBird <= 0 || initPositionBird >= deviceHeight) {
            if (gameState != 2) {
                illegalAction = 1;
                if (illegalAction == 1) {
                    lifeCount--;
                    gameState = 0;
                    fallDown = 0;
                    initPositionBird = deviceHeight / 2;
                    illegalAction = 0;
                    tubePositionH = deviceWidth;
                }
                if (lifeCount < 0) {
                    gameState = 2;
                    lifeCount = 3;
                }
            }

        }

    }
}
