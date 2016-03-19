package org.academiacodigo.bootcamp.vascos.circulargame.model;

import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.Launcher;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class Player {

    private int playerId;
    private int score;
    private String name;
    private boolean playerTurn;

    private Launcher launcher;

    public Player(int playerId, ModelGame modelGame) {
        this.playerId = playerId;
        launcher = new Launcher(playerId, modelGame);
    }

    public LilBall getNextBall() {
        return launcher.getNextBallToSpit();
    }

    public void launchBall() {
        launcher.launch();
    }



}
