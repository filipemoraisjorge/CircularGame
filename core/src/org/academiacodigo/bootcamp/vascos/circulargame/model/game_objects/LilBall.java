package org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects;

import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Publisher;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Subscriber;

import java.util.ArrayList;
import java.util.List;


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

    private List<LilBall> attachedBallList = new ArrayList<LilBall>();


    public LilBallType getType() {
        return type;
    }

    public void setType(LilBallType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<LilBall> getAttachedBallList() {
        return attachedBallList;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
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
            this.stopped = true;
            //increment attachedBall counter
            if (!attached) {
                this.attached = true;
                this.attachedBallList.add(this);
            }
            publish(LilBallTopic.STOPPED, this);
            System.out.println("lilBall counter " + this + " " + attachedBallList.size());
        }
    }

    @Override
    public void touched(Gluable otherBall) {

        //lil ball representation touches another ball's representation:
        if (otherBall instanceof BigBall) {
            attachedBall = otherBall;
            glue();
            return;
        }

        LilBall otherLilBall = (LilBall) otherBall;

        if (otherLilBall.attachedBallList.size() >= 5) {
            explode();
            return;
        }

        if (!this.attachedBallList.contains(otherLilBall) &&
                this.type.equals(otherLilBall.type)) {

            //this ball is attached to other lil ball
            attachedBall = otherLilBall;
            otherLilBall.attachedBall = this;

            System.out.println("lilBall touched otherball list size " + otherLilBall.attachedBallList.size());


            //glue should be glueTo(otherBall)?
            this.glue();
            otherLilBall.glue();

            System.out.println("before other " + otherLilBall.attachedBallList.size());
            System.out.println("before this  " + this.attachedBallList.size());

            //join both lists, each ball keeps the same list
            //only if they aren't already in each other list
            ArrayList<LilBall> tempList = new ArrayList<LilBall>();
            tempList.addAll(otherLilBall.attachedBallList);

            if (!otherLilBall.attachedBallList.contains(this)) {
                otherLilBall.attachedBallList.addAll(this.attachedBallList);
            }

            if (!this.attachedBallList.contains(otherLilBall)) {
                this.attachedBallList.addAll(tempList);
            }

            //for debug
            System.out.println("after other " + otherLilBall.attachedBallList.size());
            System.out.println("after this  " + this.attachedBallList.size());
            for (LilBall ball : this.attachedBallList) {
                System.out.println(ball);
            }
            System.out.println();
            //end debug

        }

    }


    public void explode() {
        //if ball hits attached ball explode this ball
        //explode hit ball and attached ball
        //notify big ball (observer) that I exploded
        System.out.println("lilBall explode " + this);
        //((LilBall) attachedBall).explode();
        //publish(LilBallTopic.EXPLODE, this);
    }

    @Override
    public void registerSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void publish(Enum topic, Object obj) {
        //System.out.println("LilBall " + topic + " ID:" + this.getId() + " Player:" + this.getPlayerId());
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
