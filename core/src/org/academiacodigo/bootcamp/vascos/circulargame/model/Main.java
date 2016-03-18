package org.academiacodigo.bootcamp.vascos.circulargame.model;


import com.badlogic.gdx.Game;
import org.academiacodigo.bootcamp.vascos.circulargame.controller.Controller;
import org.academiacodigo.bootcamp.vascos.circulargame.screens.GameScreen;
import org.academiacodigo.bootcamp.vascos.circulargame.view.View;
import com.badlogic.gdx.Gdx;
import org.academiacodigo.bootcamp.vascos.circulargame.screens.MenuScreen;

/**
 * Created by JVasconcelos on 18/03/16
 */
public class Main extends Game {

/*    @Override
    public void create() {
        ModelGame game = new ModelGame();
        View view = new View();
        Controller controller = new Controller();

        controller.setModelGame(game);
        controller.setView(view);

    }*/




    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static int SCREEN_MARGIN = 10;

    @Override
    public void create () {

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        setScreen(new MenuScreen(this));
    }
}