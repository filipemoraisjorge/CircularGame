package org.academiacodigo.bootcamp.vascos.circulargame.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Main;

/**
 * @author Filipe Jorge.
 *         At <Academia de Código_> on 18/03/16.
 */

public class MenuScreen extends AbstractGameScreen{

    private Stage stage;
    private Skin skin;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {

        //skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        stage = new Stage();

/*        Image logo = new Image(new Texture(Gdx.files.internal("images/logo.png")));
        logo.setPosition(Main.SCREEN_WIDTH / 2 - logo.getWidth() / 2, Main.SCREEN_HEIGHT / 2 - 100);
        stage.addActor(logo);

        Label spaceToStart = new Label("PRESS START TO PLAY", skin);
        spaceToStart.setAlignment(1);
        spaceToStart.setWidth(200f);
        spaceToStart.setHeight(20f);
        spaceToStart.setPosition(Main.SCREEN_WIDTH / 2 - 100f, logo.getY() - 40);
        stage.addActor(spaceToStart);*/

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }

        stage.draw();
    }
}
