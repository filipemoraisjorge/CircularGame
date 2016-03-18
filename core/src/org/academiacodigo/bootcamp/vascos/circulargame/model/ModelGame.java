package org.academiacodigo.bootcamp.vascos.circulargame.model;

import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.BigBall;

import java.util.ArrayList;


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
