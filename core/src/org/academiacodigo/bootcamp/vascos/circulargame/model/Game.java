package org.academiacodigo.bootcamp.vascos.circulargame.model;

import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.LittleBall;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Created by JVasconcelos on 16/03/16
 */
public class Game implements Observer {

    private BigBall bigBall;
    private Player player1;
    private Player player2;
    //private List<Subject> subjects = new ArrayList<Subject>();


    public void init() {
        //initialize bigBall and players
    }

    public void start() {
        //start game
    }

    //put the ball in the game
    public void spit(LittleBall littleBall) {

    }


    @Override
    public void update() {

    }
}
