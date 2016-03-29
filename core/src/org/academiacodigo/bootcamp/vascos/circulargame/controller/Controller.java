package org.academiacodigo.bootcamp.vascos.circulargame.controller;

import com.badlogic.gdx.physics.box2d.Body;
import org.academiacodigo.bootcamp.vascos.circulargame.model.ModelGame;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Subscriber;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.GameObjectType;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBallTopic;
import org.academiacodigo.bootcamp.vascos.circulargame.view.View;

import java.util.ArrayList;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class Controller implements Subscriber<Gluable> {

    private View view;
    private ModelGame modelGame;
    private ArrayList<Gluable> gameObjects;


    public void setView(View view) {
        this.view = view;
    }

    public void setModelGame(ModelGame modelGame) {
        this.modelGame = modelGame;

    }

    public void newGameObject(Gluable ball) {

        view.createNewGameObject(ball);
        //o newGameObj manda a view criar qq coisa la dentro dela
        //a View é que dps sabe que tem que criar um body e uma fixture e merdas dessas

    }

    public Controller() {
        init();
    }

    private void init() {

    }

    public void updateGameObjects(float deltaTime) {
        //update all game objects


    }


    public void touched(Gluable ball1, Gluable ball2) {
        System.out.println("controller " + ball2 + " touched " + ball1 );
        ball1.touched(ball2);
    }


    @Override
    public void update(Enum topic, Gluable object) {

        if (topic instanceof GameObjectType) {

            switch ((GameObjectType) topic) {
                case BIGBALL:
                    newGameObject(object);
                    break;
                case LILBALL:
                    newGameObject(object);
                    break;
                default:
                    //whatever

            }
        } else if (topic instanceof LilBallTopic && object instanceof LilBall) {
            LilBall lilBall = (LilBall) object;
            switch ((LilBallTopic) topic) {
                case START:
                    view.startMoving(lilBall);
                    break;
                case STOPPED:
                    view.stopMoving(lilBall);
                    modelGame.launchBall(lilBall.getPlayerId()); //next ball
                    break;
                case EXPLODE:
                    view.removeLilBall(lilBall);
                    break;

            }
        }

    }

}
