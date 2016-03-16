package org.academiacodigo.bootcamp.vascos.circulargame.model.balls;

import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Observer;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class LittleBall implements Gluable, Subject {

    private List<Observer> listOfObservers = new ArrayList<Observer>();

    private int playerId;
    private LittleBallType type;
    private boolean attached;
    private boolean stopped;



    public void setType(LittleBallType type) {
        this.type = type;
    }

    public void explode() {
        //if ball hits attached ball explode this ball
        //explode hit ball and attached ball
        //remove ball representation and from array
    }

    @Override
    public boolean glue() {
        //glue to same type balls or to bigBall walls
        //notify observers of change (stopped)
        return false;
    }

    public void crash(Gluable gluable) {
        //if ball representation touches another ball's representation
        //if bigBall: glue
        //if same type: set this ball attached and other one as well
        //if already attached: explode()
        //if different type: bounce()
        //notify observers of change (stopped)
    }

    private void bounce() {
        //bounce off different typed balls
        //until stopped
    }

    @Override
    public void registerObserver(Observer observer) {
        listOfObservers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        listOfObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer: listOfObservers) {
            observer.update();
        }
    }
}
