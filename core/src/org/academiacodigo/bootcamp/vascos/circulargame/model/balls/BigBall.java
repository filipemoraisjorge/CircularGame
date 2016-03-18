package org.academiacodigo.bootcamp.vascos.circulargame.model.balls;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.PublisherTopic;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JVasconcelos on 16/03/16
 */
public class BigBall implements Gluable, Subscriber<LilBall> {

    private List<LilBall> lilBallList = new ArrayList<LilBall>();


    private Body mainCircle;
    private double radius = 20;


    public void rotate(int speed) {
        //receive user input and move accordingly
    }

    public void add(LilBall lilBall) {
        //add lil ball to array
    }

    @Override
    public boolean glue() {
        //if lil ball touches walls they stop (stopped) and stay glued
        return false;
    }


    //PROVISÓRIO!!!!!!!!
    public void createBigBall(World world, float WIDTH, float HEIGHT) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.KinematicBody;
        // Set its world position
        groundBodyDef.position.set(new Vector2(WIDTH / 2, HEIGHT / 2));
        groundBodyDef.angularVelocity = 0;

        // Create a body from the definition and add it to the world
        mainCircle = world.createBody(groundBodyDef);

        // Create a polygon shape
        ChainShape groundBox = new ChainShape();

        //Calculate the vertices of a circle.
        //double radius = 20;
        int numberOfSegments = 36;
        Vector2[] vertices = new Vector2[numberOfSegments + 2];

        double angleSeg = 360 / numberOfSegments;

        for (int i = 0; i < numberOfSegments+1; i++) {
            double angle = Math.toRadians(angleSeg * i);
            float x = (float) (radius * Math.cos(angle));
            float y = (float) (radius * Math.sin(angle));
            vertices[i] = new Vector2(x, y);
        }

        //in the end make a edge till center
        vertices[numberOfSegments + 1] = new Vector2(0, 0);

        groundBox.createLoop(vertices);
        // Create a fixture from our polygon shape and add it to our ground body
        mainCircle.createFixture(groundBox, 0.0f);
        // Clean up after ourselves
        groundBox.dispose();
    }


    @Override
    public void update() {
        for (LilBall lilball : lilBallList) {
            //if(lilball exploded)
            //lilBallList.remove(lilball);
        }
    }


    @Override
    public void update(PublisherTopic topic, LilBall object) {

    }
}
