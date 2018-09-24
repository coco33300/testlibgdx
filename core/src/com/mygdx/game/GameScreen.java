package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class GameScreen implements Screen {

    final Drop game;

    private Array<Rectangle> raindrops;

    private Texture dropImage;
    private Music rainMusic;
    private Texture bucketImage;
    private Sound waterDrop;
    private OrthographicCamera camera;
    private Rectangle bucket;
    private long lastDropTime;
    public int dropGathered;

    private Vector3 touchpos;

    public GameScreen(final Drop game) {
        this.game = game;

        //load images
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        //load sound effect and music, set it looping
        waterDrop = Gdx.audio.newSound(Gdx.files.internal("waterDrop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertherain.mp3"));
        rainMusic.setLooping(true);

        //create a camera for the spriteBatch

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,400);

        bucket = new Rectangle(800/2 -64, 20, 64 , 64);

        raindrops = new Array<Rectangle>();
        spawnRaindrop();
        touchpos = new Vector3();

        dropGathered =0;
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle(
                MathUtils.random(0,800-64),
                480,
                64,
                64);
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {

        //Color used to clear the screen, each parameter in [0,1] range
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell camera to update its matrices
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        //Draw
        game.batch.begin();



        game.batch.draw(bucketImage, bucket.x, bucket.y,bucket.width, bucket.height );
        for (Rectangle raindrop : raindrops){
            game.batch.draw(dropImage, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
        }
        game.font.draw(game.batch,("Drops Collected : "+dropGathered), 20,360);
        game.batch.end();

        //process input
        if (Gdx.input.isTouched()) {
            touchpos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpos);
            bucket.x = touchpos.x - 64 / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            bucket.x -= 200*Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            bucket.x += 200*Gdx.graphics.getDeltaTime();
        }

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000){
            spawnRaindrop();
        }

        for (Iterator<Rectangle> it = raindrops.iterator() ; it.hasNext(); ){
            Rectangle raindrop = it.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();

            if (raindrop.y + 64 < 0) {
                it.remove();
            }
            if (raindrop.overlaps(bucket)){
                waterDrop.play();
                it.remove();
                dropGathered++;
            }

        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {
        System.out.println("show ?");
        rainMusic.play();
    }

    @Override
    public void dispose() {
        this.dropImage.dispose();
        this.bucketImage.dispose();
        this.waterDrop.dispose();
        this.rainMusic.dispose();
    }
}
