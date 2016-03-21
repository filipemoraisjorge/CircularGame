package org.academiacodigo.bootcamp.vascos.circulargame.view;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import org.academiacodigo.bootcamp.vascos.circulargame.controller.Controller;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;

/**
 * Created by filipejorge on 20/03/16.
 */
public class BallView {

    private Body body;
    private Gluable ball;

    private int touching;
    private BallView collidedBall;

    public BallView(World world, Gluable ball) {
        this.ball = ball;


    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Gluable getBall() {
        return ball;
    }

    public void setBall(Gluable ball) {
        this.ball = ball;
    }

    public BallView getCollidedBall() {
        return collidedBall;
    }

    public void startContact(BallView otherBall) {
        touching++;
        this.collidedBall = otherBall;
    }

    public void endContact() {
        // touching--;
        //this.collidedBall = null;
    }

    public void render(Controller controller) {
        if (touching > 0 /*&& collidedBall != null*/) {
            //check what to do
            controller.touched(this.ball, collidedBall.ball);

        }
    }
}
