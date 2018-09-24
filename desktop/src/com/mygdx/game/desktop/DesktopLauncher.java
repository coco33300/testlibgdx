package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Drop;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		//create a config object
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//Window name
		config.title = "Drop"; //ik, not legit java ^^"

		//display size
		config.width = 800;
		config.height = 400;

		//launch Drop
		new LwjglApplication(new Drop(), config);
	}
}
