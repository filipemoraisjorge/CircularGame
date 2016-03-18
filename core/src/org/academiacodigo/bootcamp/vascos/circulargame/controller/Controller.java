package org.academiacodigo.bootcamp.vascos.circulargame.controller;

import com.badlogic.gdx.physics.box2d.Body;
import org.academiacodigo.bootcamp.vascos.circulargame.model.ModelGame;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.LilBall;
import org.academiacodigo.bootcamp.vascos.circulargame.view.View;

import java.util.ArrayList;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class Controller {

    private View view;
    private ModelGame modelGame;
    private ArrayList<Gluable> gameObjects = modelGame.getGameObjects();
    private ArrayList<Body> representations = view.getGameObjects();

    public void setView(View view) {
        this.view = view;
    }

    public void setModelGame(ModelGame modelGame) {
        this.modelGame = modelGame;
    }

    public void newGameObject() {

        view.createNewGameObject();
        //o newGameObj manda a view criar qq coisa la dentro dela
        //a View é que dps sabe que tem que criar um body e uma fixture e merdas dessas

    }


    public void deleteGameObject(int gameObjectId) {
        representations.remove(gameObjectId);
        gameObjects.remove(gameObjectId);
    }



    public void touched(Gluable ball1, Gluable ball2) {
        if(ball1 instanceof LilBall) {
            ((LilBall)ball1).crash(ball2);

            if(ball2 instanceof BigBall) {
                ball1.glue();
                view.stopGameObject(ball1);
                return;
            }

            deleteGameObject(((LilBall) ball1).getId());
            deleteGameObject(((LilBall) ball2).getId());
            return;

        }

        ball2.glue();
        view.stopGameObject(ball2);
    }

}
