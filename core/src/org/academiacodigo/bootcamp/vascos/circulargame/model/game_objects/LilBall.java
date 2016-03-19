package org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects;

import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Publisher;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Subscriber;


/**
 * Created by JVasconcelos on 16/03/16
 */
public class LilBall implements Gluable, Publisher {


    private Subscriber<LilBall> subscriber;

    private int id;
    private LilBallType type;
    private int playerId;

    private float radius;

    private boolean attached;
    //tem que passar a ser uma lista de gluables
    private Gluable attachedBall;


    public void setType(LilBallType type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void startMoving() {
        //warn Controller to start this lilBall moving.
        publish(LilBallTopic.START, this);
    }


    @Override
    public void glue() {
        publish(LilBallTopic.STOPPED, this);
        //glue to same type balls or to bigBall walls

        //notify observers of change (stopped)
    }


    public void crash(Gluable otherBall) {
        //lil ball representation touches another ball's representation:
        if (otherBall instanceof BigBall) {
            glue();
            return;
        }

        LilBall otherLilBall = ((LilBall) otherBall);

        if (this.type.equals(otherLilBall.type)) {
            //this ball is attached to other lil ball
            attachedBall = otherLilBall;

            if (otherLilBall.attached) {
                explode();
                return;
            }

            this.attached = true;
            otherLilBall.attached = true;
            glue();
            return;
        }

        //if not same type bounce
        bounce();

        //if it touches bigBall become glued - stopped = true
        //if same type: set this ball attached (and stopped)
        // and set the other attached as well
        //if already attached: explode()
        //if different type: bounce()

        //notify observers of change (stopped)

    }

    private void bounce() {
        publish(LilBallTopic.BOUNCE, this);
        //physics shit - muda a direcção
        //bounce off different typed balls
        //until stopped
    }

    public void explode() {
        //if ball hits attached ball explode this ball
        //explode hit ball and attached ball
        //notify big ball (observer) that I exploded
        ((LilBall) attachedBall).explode();
        publish(LilBallTopic.EXPLODE, this);
    }

    @Override
    public void registerSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void publish(Enum topic, Object obj) {
        this.subscriber.update(topic, this);
    }


}
