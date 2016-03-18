package org.academiacodigo.bootcamp.vascos.circulargame.model;


import com.badlogic.gdx.Game;
import org.academiacodigo.bootcamp.vascos.circulargame.controller.Controller;
import org.academiacodigo.bootcamp.vascos.circulargame.view.View;

/**
 * Created by JVasconcelos on 18/03/16
 */
public class Main extends Game {

    @Override
    public void create() {
        ModelGame game = new ModelGame();
        View view = new View();
        Controller controller = new Controller();

        controller.setModelGame(game);
        controller.setView(view);

    }
}
