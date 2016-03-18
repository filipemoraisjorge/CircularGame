package org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects;

import com.badlogic.gdx.physics.box2d.Body;

import org.academiacodigo.bootcamp.vascos.circulargame.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class BigBall implements Gluable, Subscriber<LilBall> {

    private List<Publisher> lilBallList = new ArrayList<Publisher>();
    private Launcher launcher1;
    private Launcher launcher2;

    private ModelGame modelGame;


    private Body mainCircle;
    private double radius = 20;
    private int id = 0;

    public BigBall(ModelGame modelGame) {
        this.modelGame = modelGame;

        launcher1 = new Launcher(1, modelGame);
        launcher2 = new Launcher(2, modelGame);
        launcher1.spit(1);
        launcher2.spit(1);

    }

    public void rotate(int speed) {
        //receive user input and move accordingly
    }

    public void add(LilBall lilBall) {
        //add lil ball to array
    }

    @Override
    public void glue() {
        //not implemented because it is not needed
    }


    @Override
    public void update(Enum topic, LilBall object) {

        switch ((PublisherTopic) topic) {
            case STOPPED:
                //stop balls, set position
                break;
            case EXPLODE:
                //erase ball representation and remove from ball list
                break;
            case BOUNCE:
                //change ball to opposite direction
                break;
            default:
                //never going to occur but whatevs
        }
    }

}
