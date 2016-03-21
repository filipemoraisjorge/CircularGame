package org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects;

import org.academiacodigo.bootcamp.vascos.circulargame.model.RandomGenerator;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class LilBallFactory {

    private static int nextId = 1;

    public static LilBall getNewLilBall(int playerId) {
        int random = RandomGenerator.generateRandomly(2);
        LilBall lilBall = new LilBall();


        switch (random) {
            case 0:
                lilBall.setType(LilBallType.A);
                break;
            case 1:
                lilBall.setType(LilBallType.B);
                break;
            case 2:
                lilBall.setType(LilBallType.C);
                break;
        }

        lilBall.setId(nextId);
        lilBall.setPlayerId(playerId);
        lilBall.setRadius(1f);

        nextId++;

        return lilBall;
    }


}
