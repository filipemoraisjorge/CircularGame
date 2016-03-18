package org.academiacodigo.bootcamp.vascos.circulargame.model.balls;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.javaws.Launcher;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Publisher;
import org.academiacodigo.bootcamp.vascos.circulargame.model.PublisherTopic;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class BigBall implements Gluable, Subscriber<LilBall> {

    private List<Publisher> lilBallList = new ArrayList<Publisher>();
    private Launcher launcher1;
    private Launcher launcher2;


    private Body mainCircle;
    private double radius = 20;
    private int id = 0;


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
    public void update(PublisherTopic topic, LilBall object) {

        switch (topic) {
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
