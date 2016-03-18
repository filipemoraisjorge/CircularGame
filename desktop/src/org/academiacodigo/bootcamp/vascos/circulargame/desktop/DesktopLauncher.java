package org.academiacodigo.bootcamp.vascos.circulargame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Main;
import org.academiacodigo.bootcamp.vascos.circulargame.view.View;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CircularGame";
		config.width = 800;
		config.height = 480;
		//new LwjglApplication(new circularGame(), config);
		new LwjglApplication(new Main(), config);
		//new LwjglApplication(new View(), config);
	}
}
