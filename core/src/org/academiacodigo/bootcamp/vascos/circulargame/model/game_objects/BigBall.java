package org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects;

import org.academiacodigo.bootcamp.vascos.circulargame.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class BigBall implements Gluable, Subscriber<LilBall> {

    private Map<Integer, LilBall> lilBallMap;

    private ModelGame modelGame;

    private double radius;
    private int id;

    public BigBall(ModelGame modelGame) {
        this.modelGame = modelGame;
        this.lilBallMap = new HashMap<Integer, LilBall>();
        this.radius = 20;
        this.id = 0;
    }

    public double getRadius() {
        return radius;
    }

    public int getId() {
        return id;
    }

    public void put(LilBall lilBall) {
        //subscribe this BigBall to receive publishes from lilBall
        lilBall.registerSubscriber(this);
        //add lil ball to map
        lilBallMap.put(lilBall.getId(), lilBall);

    }

    public void remove(LilBall lilBall) {
        lilBallMap.remove(lilBall.getId());
    }


    @Override
    public void glueTo(Gluable other) {

        LilBall otherLilBall = (LilBall) other;
        otherLilBall.glueTo(this);
    }

    @Override
    public void touched(Gluable otherBall) {
        //lil ball representation touches another ball's representation:
        LilBall lilBall = (LilBall) otherBall;
        this.glueTo(lilBall);
    }


    @Override
    public void update(Enum topic, LilBall lilBall) {

        switch ((LilBallTopic) topic) {
            case START:
                modelGame.publish(LilBallTopic.START, lilBall);
                break;
            case STOPPED:
                //stop balls, set position
                modelGame.publish(LilBallTopic.STOPPED, lilBall);
                break;
            case EXPLODE:
                //erase ball representation and remove from ball list
                lilBallMap.remove(lilBall.getId());
                modelGame.publish(LilBallTopic.EXPLODE, lilBall);
                break;

            default:
                //never going to occur but whatever
        }


    }

    @Override
    public String toString() {
        return "BigBall{" +
                "id=" + id +
                '}';
    }

}
