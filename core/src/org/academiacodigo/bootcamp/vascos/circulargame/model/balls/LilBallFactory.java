package org.academiacodigo.bootcamp.vascos.circulargame.model.balls;

import org.academiacodigo.bootcamp.vascos.circulargame.model.RandomGenerator;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class LilBallFactory {

    private static int nextId = 1;

    public static LilBall getNewLilBall() {
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
        nextId++;

        return lilBall;
    }


}
