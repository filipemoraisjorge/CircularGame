package org.academiacodigo.bootcamp.vascos.circulargame.model;

import com.sun.javaws.Launcher;
import org.academiacodigo.bootcamp.vascos.circulargame.controller.Controller;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.GameObjectType;

import java.util.ArrayList;


/**
 * Created by JVasconcelos on 16/03/16
 */
public class ModelGame implements Publisher{


    private Subscriber controller;

    private BigBall bigBall;
    private Player player1;
    private Player player2;


    private Launcher launcher1;
    private Launcher launcher2;

    private ArrayList<Gluable> gameObjects = new ArrayList<Gluable>();

    public ArrayList<Gluable> getGameObjects() {
        return gameObjects;
    }

    public ModelGame(Subscriber controller) {
        registerSubscriber(controller);

    }

    public void init() {

        bigBall = new BigBall(this);
        publish(GameObjectType.BIGBALL);

        //initialize bigBall and players (and launchers)
    }

    public void start() {
        //start game
    }


    public void createLilBall(int velocity) {
        //if player1 ball has stopped:
        launcher1.spit();

        //if player2 ball has stopped:
        launcher2.spit();
    }

    @Override
    public void registerSubscriber(Subscriber subscriber) {
        this.controller = subscriber;
    }

    @Override
    public void publish(Enum topic) {
        controller.update(topic, this);
    }
}
