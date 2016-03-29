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

    private List<Gluable> attachedBallList = new ArrayList<Gluable>();
    private int attachedCounter;

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

    public void setAttachedCounter(int attachedCounter) {
        this.attachedCounter = attachedCounter;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public List<Gluable> getAttachedBallList() {
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
    public void glueTo(Gluable otherBall) {

        //glueTo to same type lilBalls or to bigBall walls
        //notify observers of change (stopped)
        if (!this.stopped) {
            this.stopped = true;
            //increment attachedBall counter
            //if (!attached) {
            this.attached = true;
            this.attachedCounter++;
            //this.attachedBallList.add(otherBall);
            this.attachedBall = otherBall;
            //}
            publish(LilBallTopic.STOPPED, this);

            //for debug
            // System.out.println("lilBall glueTo. This " + this + " AttachedBall " + this.attachedBall + " this.counter " + this.attachedCounter);
           /*
            System.out.println("lilBall glueTo. This " + this + " list size " + attachedBallList.size() + ". list: ");
            for (Gluable ball : this.attachedBallList) {
                System.out.println(ball);
            }
            System.out.println();
            */
            //end debug
        }
    }

    @Override
    public void touched(Gluable otherBall) {
        //lil ball representation touches another ball's representation:

        //BigBall exception case
   /*     if (otherBall instanceof BigBall && !this.attached) {
            glueTo(otherBall);
            return;
        }*/


        LilBall otherLilBall = (LilBall) otherBall;
        //don't do this if this ball already is attached to otherBall
        // && if both have the same type

        if (this.type.equals(otherLilBall.type)) {

            //this ball is attached to other lil ball
            //attachedBall = otherLilBall;
            // otherLilBall.attachedBall = this;

            //System.out.println("lilBall touched otherball list size " + otherLilBall.attachedBallList.size());

            otherLilBall.attachedCounter += this.attachedCounter;
            otherLilBall.glueTo(this);
            //this.glueTo(otherLilBall);

            //join both lists, each ball keeps the same list
            //only if they aren't already in each other list
/*                ArrayList<LilBall> tempList = new ArrayList<LilBall>();
                tempList.addAll(otherLilBall.attachedBallList);

                if (!otherLilBall.attachedBallList.contains(this)) {
                    otherLilBall.attachedBallList.addAll(this.attachedBallList);
                }

                if (!this.attachedBallList.contains(otherLilBall)) {
                    this.attachedBallList.addAll(tempList);
                }*/

            //check if its time to explode
            if (otherLilBall.attachedCounter >= 5) {
                otherLilBall.attachedCounter = 0;
                this.attachedCounter = 0;
                this.explode();

            }

            /*
                if (otherLilBall.attachedBallList.size() >= 5) {
                    explode();
                }*/


        }

    }


    public void explode() {
        //if ball hits attached ball explode this ball
        //call explode method from attachedBall
        if (attachedBall instanceof LilBall) {
            ((LilBall) attachedBall).explode();
        }

        //explode this ball
        //notify big ball (observer) that I exploded
        this.attached = false;
        this.attachedBall = null;
        this.stopped = false;
        publish(LilBallTopic.EXPLODE, this);
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
