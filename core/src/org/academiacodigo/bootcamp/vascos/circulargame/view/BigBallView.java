package org.academiacodigo.bootcamp.vascos.circulargame.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import org.academiacodigo.bootcamp.vascos.circulargame.controller.Controller;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;

/**
 * Created by filipejorge on 20/03/16.
 */
public class BigBallView extends BallView {


    private int PX_TO_METER;

    public BigBallView(World world, BigBall bigBall, float WIDTH, float HEIGHT, int PX_TO_METER) {
        super(world, bigBall);

        super.setBall(bigBall);
        this.PX_TO_METER = PX_TO_METER;

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.KinematicBody;
        // Set its world position
        groundBodyDef.position.set(new Vector2(WIDTH / 2, HEIGHT / 2));
        groundBodyDef.angularVelocity = 0;

        // Create a body from the definition and add it to the world
        Body mainCircle = world.createBody(groundBodyDef);
        // save reference to modelGame's orginal object
        mainCircle.setUserData(this);

        // Create a polygon shape
        ChainShape groundBox = new ChainShape();

        //Calculate the vertices of a circle.
        double radius = bigBall.getRadius();
        int numberOfSegments = 36;
        Vector2[] vertices = new Vector2[numberOfSegments];

        double angleSeg = 360 / numberOfSegments;
        for (int i = 0; i < numberOfSegments; i++) {
            double angle = Math.toRadians(angleSeg * i);
            float x = (float) (radius * Math.cos(angle));
            float y = (float) (radius * Math.sin(angle));
            vertices[i] = new Vector2(x, y);
        }
        //in the end make a edge till center
        //vertices[numberOfSegments] = new Vector2(0, 0);

        groundBox.createLoop(vertices);
        // Create a fixture from our polygon shape and add it to our ground body
        mainCircle.createFixture(groundBox, 0.0f);
        // Clean up after ourselves
        groundBox.dispose();

        super.setBody(mainCircle);

    }

    public void render(ShapeRenderer shapeRenderer, Controller controller) {

        super.render(controller);

        shapeRenderer.setColor(1, 1, 1, 1);


        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(this.getBody().getWorldPoint(new Vector2(0, 0)).x * PX_TO_METER, this.getBody().getWorldPoint(new Vector2(0, 0)).y * PX_TO_METER, (float) ((BigBall) super.getBall()).getRadius() * PX_TO_METER);
        shapeRenderer.end();

    }
}
