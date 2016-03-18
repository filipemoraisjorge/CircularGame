package org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects;

import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBallFactory;


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