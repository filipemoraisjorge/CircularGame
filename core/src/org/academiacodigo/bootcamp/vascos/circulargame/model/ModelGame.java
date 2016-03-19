package org.academiacodigo.bootcamp.vascos.circulargame.model;

import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.GameObjectType;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.Launcher;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;

import java.util.ArrayList;


/**
 * Created by JVasconcelos on 16/03/16
 */
public class ModelGame implements Publisher {


    private Subscriber controller;

    private BigBall bigBall;
    private Player player1;
    private Player player2;

    private Launcher launcher1;
    private Launcher launcher2;

    private ArrayList<Gluable> gameObjects = new ArrayList<Gluable>();


    public ModelGame(Subscriber controller) {
        registerSubscriber(controller);

    }

    public void init() {
        //initialize bigBall and players (and launchers)

        bigBall = new BigBall(this);
        publish(GameObjectType.BIGBALL, bigBall);


        launcher1 = new Launcher(1, this);
        launcher2 = new Launcher(2, this);
        bigBall.put(launcher1.getNextBallToSpit());
        bigBall.put(launcher2.getNextBallToSpit());
        launcher1.spit();
        launcher2.spit();
    }

    public void start() {
        //start game
    }

    public ArrayList<Gluable> getGameObjects() {
        return gameObjects;
    }


    private void spitLilBall(int playerId) {
        switch (playerId) {
            case 1:
                //if player1 ball has stopped:
                launcher1.spit();
                break;
            case 2:
                //if player2 ball has stopped:
                launcher2.spit();
                break;
            default:
                //nothing...
        }
    }

    @Override
    public void registerSubscriber(Subscriber subscriber) {
        this.controller = subscriber;
    }

    @Override
    public void publish(Enum topic, Object obj) {
        controller.update(topic, obj);
    }
}
