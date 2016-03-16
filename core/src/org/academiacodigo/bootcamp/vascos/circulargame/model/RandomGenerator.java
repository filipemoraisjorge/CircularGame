package org.academiacodigo.bootcamp.vascos.circulargame.model;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class RandomGenerator {

    public static int generateRandomly(int max) {
        return (int)(Math.random() * (max + 1));
    }
}
