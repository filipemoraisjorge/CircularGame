package org.academiacodigo.bootcamp.vascos.circulargame.model;

import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.LilBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.LilBallFactory;


public class Launcher {
    private int playerId;
    private int launcherX; // static final?
    private int launcherY; // static?

    public Launcher(int playerId) {
        this.playerId = playerId;
        if(playerId==2){
            launcherY = -launcherY;
        }
    }

    LilBall nextBalltoSpit = LilBallFactory.getNewLilBall();

    public void spit(int velocity){
        //warn controller
        //launch ball -> do your magic Filipe
        nextBalltoSpit = LilBallFactory.getNewLilBall();

    }

}