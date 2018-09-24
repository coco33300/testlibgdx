package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    public void  create(){
        //create a new batch
        batch = new SpriteBatch();

        //use libgdx default arial font
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); //Important ?!
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

        this.getScreen().dispose();
    }
}