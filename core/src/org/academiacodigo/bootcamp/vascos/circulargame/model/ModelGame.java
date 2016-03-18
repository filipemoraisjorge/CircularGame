package org.academiacodigo.bootcamp.vascos.circulargame.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.javaws.Launcher;
import org.academiacodigo.bootcamp.vascos.circulargame.Network.TcpConnection;
import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.LilBall;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JVasconcelos on 16/03/16
 */
public class ModelGame {

    private BigBall bigBall;
    private Player player1;
    private Player player2;

    private ArrayList<Gluable> gameObjects = new ArrayList<Gluable>();

    public ArrayList<Gluable> getGameObjects() {
        return gameObjects;
    }

    public void init() {
        //initialize bigBall and players (and launchers)
    }

    public void start() {
        //start game
    }




}
