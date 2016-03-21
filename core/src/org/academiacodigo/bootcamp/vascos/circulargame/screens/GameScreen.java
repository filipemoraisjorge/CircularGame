package org.academiacodigo.bootcamp.vascos.circulargame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.academiacodigo.bootcamp.vascos.circulargame.controller.Controller;
import org.academiacodigo.bootcamp.vascos.circulargame.model.ModelGame;
import org.academiacodigo.bootcamp.vascos.circulargame.view.View;

/**
 * @author Filipe Jorge.
 *         At <Academia de Código_> on 18/03/16.
 */

public class GameScreen extends AbstractGameScreen {


    private Controller controller;
    private View view;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {

        // initialize the asset manager
        //AssetManager.instance.init();

    final boolean NETWORK = false;
    final boolean MULTIPLAYER = true;

        // Initialize controller and view
        controller = new Controller();
        ModelGame game = new ModelGame(controller);

        view = new View(NETWORK, MULTIPLAYER);
        view.setController(controller);
        controller.setModelGame(game);
        controller.setView(view);

        view.create();
        game.init(MULTIPLAYER);


    }

    @Override
    public void render(float delta) {

        // Update game world by the time that has passed
        // since last rendered frame.
        controller.updateGameObjects(Gdx.graphics.getDeltaTime());

        // Sets the clear screen color to: Black
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render game world to screen
        view.render();

    }
}
