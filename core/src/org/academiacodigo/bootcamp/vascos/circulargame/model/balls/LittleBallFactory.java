package org.academiacodigo.bootcamp.vascos.circulargame.model.balls;

import org.academiacodigo.bootcamp.vascos.circulargame.model.RandomGenerator;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class LittleBallFactory {

    public LittleBall getNewLittleBall() {
        int random = RandomGenerator.generateRandomly(2);
        LittleBall littleBall = new LittleBall();

        switch (random) {

            case 0:
                littleBall.setType(LittleBallType.A);
                break;
            case 1:
                littleBall.setType(LittleBallType.B);
                break;
            case 2:
                littleBall.setType(LittleBallType.C);
                break;
        }

        return littleBall;
    }
}
