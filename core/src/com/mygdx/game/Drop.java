package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class Drop extends ApplicationAdapter {

    //My textures
    private Texture dropImage;
    private Texture bucketImage;

    //My sounds & music
    private Sound dropSound;
    private Music rainMusic;

    //display var
    private OrthographicCamera camera;
    private SpriteBatch batch;

    //physics
    private Rectangle bucket;
    private Array<Rectangle> raindrops;
    private long lastDropTime;


    @Override
    public void create() {
        //We need to load the images as textures
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        //load the sound effect and the music
        dropSound = Gdx.audio.newSound(Gdx.files.internal("waterDrop.wav"));
        //Shorter than 10s -> Sound
        //else Music
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertherain.mp3"));

        //Start playback loop of the music
        rainMusic.setLooping(true);
        rainMusic.play();

        //Create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
        //Sprite batch
        batch = new SpriteBatch();

        //bucket init
        bucket = new Rectangle();
        //place it in the center at the bottom
        bucket.x = (800 / 2) - (64 / 2); //x position
        bucket.y = 20; //libgdx has y pointing upward
        bucket.width = 64;
        bucket.height = 64;

        //initialize raindrops array and spawn a first raindrop
        raindrops = new Array<Rectangle>();
        spawnRainDrop();
    }

    @Override
    public void render() {
        //clear and put a blue background
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update the camera pos ?
        camera.update();

        //render our bucket
        //use coordinate of the camera
        batch.setProjectionMatrix(camera.combined);
        //start a batch to render as much image possible at one time need to be ended
        batch.begin();
        //draw our bucket texture at the bucket coordinates
        batch.draw(bucketImage,bucket.x,bucket.y);

        //draw the raindrops
        for(Rectangle raindrop : raindrops){
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }

        batch.end();

        //TODO move this around later ?
        //event loop
        if (Gdx.input.isTouched()){ //is screen touched
            Vector3 touchPos = new Vector3(); //create a vector //Bad practice
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0); //tell it where we touched
            camera.unproject(touchPos); //transform it to camera coordinates
            bucket.x = touchPos.x -(64/2); //assign x but center it
        }

        //keyboard move
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

        //Can't go out limit
        if (bucket.x <0) bucket.x = 0;
        if (bucket.x > 800-64) bucket.x = 800 - 64;

        //spawn raindrop after 1s (nano time)
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRainDrop();

        //move the raindrop
        for (Iterator<Rectangle> it = raindrops.iterator(); it.hasNext();){
            Rectangle raindrop = it.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y +64 <0){
                it.remove();
            }

            if (raindrop.overlaps(bucket)){
                dropSound.play();
                it.remove();
            }
        }

    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }
//Omited for clarity ?

    //pause and resume are androids methods

    /**
     * Spawn a raindrop (Rectangle) put it in the raindrops Array
     * update lastDropTime
     */
    private void spawnRainDrop(){
        Rectangle raindrop = new Rectangle();
        //MathUtils is a libgdx math library
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480; //top of the screen
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

}
