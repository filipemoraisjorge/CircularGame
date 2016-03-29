package org.academiacodigo.bootcamp.vascos.circulargame.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiacodigo.bootcamp.vascos.circulargame.controller.Controller;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;

import static org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBallType.A;
import static org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBallType.B;
import static org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBallType.C;

/**
 * Created by filipejorge on 20/03/16.
 */

public class LilBallView extends BallView {


    private int PX_TO_METER;

    public LilBallView(World world, LilBall lilBall, float WIDTH, float HEIGHT, int PX_TO_METER) {

        super(world, lilBall);


        super.setBall(lilBall);
        this.PX_TO_METER = PX_TO_METER;

        BodyDef lilBallBodyDef = new BodyDef();
        // We set our lilBallBody to dynamic, for something like ground which doesn't move we would set it to StaticBody
        lilBallBodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our lilBallBody's starting position in the world
        // varies with playerID
        lilBallBodyDef.position.set(WIDTH / 2, (HEIGHT / 2) + (lilBall.getPlayerId() == 1 ? -1.1f : 1.1f));
        // Create our lilBallBody in the world using our lilBallBody definition
        Body lilBallBody = world.createBody(lilBallBodyDef);
        // save reference to modelGame's orginal object
        lilBallBody.setUserData(this);
        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(lilBall.getRadius());


        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.0f;

        // Create our fixture and attach it to the lilBallBody
        Fixture fixture = lilBallBody.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();


        //has to be like a staticBody, it only stays dyanamic when startMoving is called
        lilBallBody.setType(BodyDef.BodyType.StaticBody);

        super.setBody(lilBallBody);
    }

    public void render(ShapeRenderer shapeRenderer, Controller controller) {

        super.render(controller);

        switch (((LilBall) super.getBall()).getType()) {
            case A:
                shapeRenderer.setColor(0, 1, 1, 1);
                break;
            case B:
                shapeRenderer.setColor(1, 0, 1, 1);
                break;
            case C:
                shapeRenderer.setColor(1, 1, 0, 1);
                break;
            default: // nothing
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(this.getBody().getWorldPoint(new Vector2(0, 0)).x * PX_TO_METER, this.getBody().getWorldPoint(new Vector2(0, 0)).y * PX_TO_METER, ((LilBall) super.getBall()).getRadius() * PX_TO_METER);
        shapeRenderer.end();


    }
}


