package org.academiacodigo.bootcamp.vascos.circulargame.model;

import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.GameObjectType;


/**
 * Created by JVasconcelos on 16/03/16
 */
public class ModelGame implements Publisher {


    private Subscriber controller;

    private BigBall bigBall;
    private Player player1;
    private Player player2;


    public ModelGame(Subscriber controller) {
        registerSubscriber(controller);

    }

    public BigBall getBigBall() {
        return bigBall;
    }

    public void init(boolean multiPlayer) {
        //initialize bigBall and players (and launchers)

        bigBall = new BigBall(this);
        publish(GameObjectType.BIGBALL, bigBall);

        player1 = new Player(1, this);
        bigBall.put(player1.getNextBall());
        player1.launchBall();


        if (multiPlayer) {
            player2 = new Player(2, this);
            bigBall.put(player2.getNextBall());
            player2.launchBall();
        }


  /*      launcher1 = new Launcher(1, this);
        launcher2 = new Launcher(2, this);
        launcher1.spit();
        launcher2.spit();*/
    }

    public void start() {
        //start game
    }

    public void launchBall(int playerId) {
        switch (playerId) {
            case 1:
                //bigBall.put(player1.getNextBall());
                player1.launchBall();
                break;
            case 2:
                //bigBall.put(player2.getNextBall());
                player2.launchBall();
                break;
            default:
                //nothing;

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
