package org.academiacodigo.bootcamp.vascos.circulargame.model.balls;

import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class BigBall implements Gluable{

    private List<LittleBall> littleBallList = new ArrayList<LittleBall>();

    public void rotate(int speed) {
        //receive user input and move accordingly
    }

    public void add(LittleBall littleBall) {
        //add lil ball to array
    }

    @Override
    public boolean glue() {
        //if lil ball touches walls they stop (stopped) and stay glued
        return false;
    }
}
