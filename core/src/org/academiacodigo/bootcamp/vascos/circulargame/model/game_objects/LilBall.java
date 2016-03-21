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

    private boolean stopped = false;
    private boolean attached = false;
    //tem que passar a ser uma lista de gluables
    private Gluable attachedBall;


    public void setType(LilBallType type) {
        this.type = type;
    }

    public LilBallType getType() {
        return type;
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

    public Gluable getAttachedBall() {
        return attachedBall;
    }

    public void setAttachedBall(Gluable attachedBall) {
        this.attachedBall = attachedBall;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void startMoving() {
        //warn Controller to start this lilBall moving.
        publish(LilBallTopic.START, this);
    }


    @Override
    public void glue() {

        //glue to same type balls or to bigBall walls
        //notify observers of change (stopped)
        if (!this.stopped) {
            System.out.println("lilball Glue " + this);
            this.stopped = true;
            publish(LilBallTopic.STOPPED, this);
        }
    }

    @Override
    public void touched(Gluable otherBall) {
        //lil ball representation touches another ball's representation:
        if (otherBall instanceof BigBall) {
            System.out.println("GLUED TO BIGBALL");
            attachedBall = otherBall;
            glue();

        } else {

            LilBall otherLilBall = ((LilBall) otherBall);

            if (this.type.equals(otherLilBall.type)) {

                //this ball is attached to other lil ball
                attachedBall = otherLilBall;
           /* if (otherLilBall.attached) {
                explode();
                return;
            }*/

                this.attached = true;
                otherLilBall.attached = true;
                this.glue();
                otherBall.glue();
            }

        }
    }


    public void explode() {
        //if ball hits attached ball explode this ball
        //explode hit ball and attached ball
        //notify big ball (observer) that I exploded
        // ((LilBall) attachedBall).explode();
        // publish(LilBallTopic.EXPLODE, this);
    }

    @Override
    public void registerSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void publish(Enum topic, Object obj) {
        System.out.println("LilBall " + topic + " ID:" + this.getId() + " Player:" + this.getPlayerId());
        this.subscriber.update(topic, this);
    }

    @Override
    public String toString() {
        return "LilBall{" +
                "id=" + id +
                ", type=" + type +
                ", playerId=" + playerId +
                '}';
    }
}
