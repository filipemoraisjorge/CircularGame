package org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects;

import org.academiacodigo.bootcamp.vascos.circulargame.model.ModelGame;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBallFactory;


public class Launcher {
    private int playerId;
    private int launcherX = 0; // static final?
    private int launcherY = 2; // static?
    private LilBall nextBallToSpit;
    private ModelGame modelGame;
    //TODO: ver se não é melhor ficar no ModelGame.

    public Launcher(int playerId, ModelGame modelGame) {
        this.modelGame = modelGame;
        this.playerId = playerId;
        this.nextBallToSpit = LilBallFactory.getNewLilBall(playerId);
        if (playerId == 2) {
            launcherY = -launcherY;
        }
    }


    public LilBall spit(int velocity) {

        //warn
        //get new lilBall
        LilBall lilBall = LilBallFactory.getNewLilBall(playerId);
        //launch ball -> do your magic Filipe
        modelGame.publish(GameObjectType.LILBALL);
        nextBallToSpit = lilBall;
        return lilBall;
    }

}