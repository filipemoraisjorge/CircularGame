package org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects;

import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Publisher;
import org.academiacodigo.bootcamp.vascos.circulargame.model.PublisherTopic;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Subscriber;



/**
 * Created by JVasconcelos on 16/03/16
 */
public class LilBall implements Gluable, Publisher {


    private Subscriber<LilBall> subscriber;

    private int playerId;
    private int id;
    private LilBallType type;
    private boolean attached;
    private float radius;

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

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void explode() {
        //if ball hits attached ball explode this ball
        //explode hit ball and attached ball
        //notify big ball (observer) that I exploded
        ((LilBall)attachedBall).explode();
        publish(PublisherTopic.EXPLODE);
    }

    @Override
    public void glue() {
        publish(PublisherTopic.STOPPED);
        //glue to same type balls or to bigBall walls

        //notify observers of change (stopped)
    }

    public void crash(Gluable otherBall) {
        //lil ball representation touches another ball's representation:
        if(otherBall instanceof BigBall) {
            glue();
            return;
        }

        LilBall otherLilBall = ((LilBall)otherBall);

        if(this.type.equals(otherLilBall.type)) {
            //this ball is attached to other lil ball
            attachedBall = otherLilBall;

             if(otherLilBall.attached) {
                 explode();
                 return;
             }

            this.attached = true;
            otherLilBall.attached = true;
            publish(PublisherTopic.STOPPED);
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
        publish(PublisherTopic.BOUNCE);
        //physics shit - muda a direcção
        //bounce off different typed balls
        //until stopped
    }

    @Override
    public void registerSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void publish(Enum topic) {
        subscriber.update(topic, this);
    }



}
