package org.academiacodigo.bootcamp.vascos.circulargame.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import org.academiacodigo.bootcamp.vascos.circulargame.Network.TcpConnection;
import org.academiacodigo.bootcamp.vascos.circulargame.controller.Controller;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.GameObjectType;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;

import java.util.*;

/**
 * Created by JVasconcelos on 18/03/16
 */
public class View implements ApplicationListener {
    private final float WIDTH_PX = 800;
    private final float HEIGHT_PX = 480;

    private final int PX_TO_METER = 10;

    private final float WIDTH = WIDTH_PX / PX_TO_METER;
    private final float HEIGHT = HEIGHT_PX / PX_TO_METER;

    private final float BIGBALL_MAX_VELOCITY = 1;
    private final int LILBALL_MAX_VELOCITY = 10;

    private Controller controller;

    private TcpConnection connection;

    private OrthographicCamera cameraBox2d;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body mainCircle;

    private Map<Gluable, BallView> balls;
    private Map<Gluable, BallView> balls_temp;

    private BitmapFont font22;


    private boolean playerTurn;
    private int playerscore;

    private Screen screen;

    public void setController(Controller controller) {
        this.controller = controller;
    }


    @Override
    public void dispose() {
        if (screen != null) screen.hide();
    }

    @Override
    public void pause() {
        if (screen != null) screen.pause();
    }

    @Override
    public void resume() {
        if (screen != null) screen.resume();
    }


    @Override
    public void resize(int width, int height) {
        if (screen != null) screen.resize(width, height);
    }

    /**
     * Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
     * screen, if any.
     *
     * @param screen may be {@code null}
     */
    public void setScreen(Screen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /**
     * @return the currently active {@link Screen}.
     */
    public Screen getScreen() {
        return screen;
    }

    @Override
    public void create() {
        //Create Connection between two players
        //connection = new TcpConnection(55555);

        //Create Fonts
    /*    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/montserrat/Montserrat-Hairline.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 22;
        font22 = generator.generateFont(parameter);
        generator.dispose();*/

        cameraBox2d = new OrthographicCamera();
        cameraBox2d.setToOrtho(false, WIDTH, HEIGHT);

/*        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH_PX, HEIGHT_PX);*/
       /* batch = new SpriteBatch();*/


        Box2D.init();

        world = new World(new Vector2(0, 0), true);

        debugRenderer = new Box2DDebugRenderer();

        shapeRenderer = new ShapeRenderer();

        //START LISTENING FOR COMMANDS
 /*       Thread commandListener = new Thread(new TCPListener());
        commandListener.setName("commandListener");
        commandListener.start();*/

        //DECIDE WHO PLAYS FIRST
        //decideWhoPlaysFirst();


        playerTurn = true;
        //setPlayerTurn(true);
        balls = new HashMap<Gluable, BallView>();
        balls_temp = new HashMap<Gluable, BallView>();
        //set Collision detector
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

/*
                if (!(contact.getFixtureA().getBody().getType() == BodyDef.BodyType.StaticBody) &&
                        !(contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody)) {
                    Gluable ball1 = (Gluable) contact.getFixtureA().getBody().getUserData();
                    Gluable ball2 = (Gluable) contact.getFixtureB().getBody().getUserData();
                    System.out.println(contact.getFixtureA().getBody().getUserData().getClass().getCanonicalName() + " " +
                            contact.getFixtureB().getBody().getUserData().getClass().getCanonicalName());
                    controller.touched(ball1, ball2);
                }*/
                if ((contact.getFixtureA().getBody().getType() != BodyDef.BodyType.StaticBody) &&
                        (contact.getFixtureB().getBody().getType() != BodyDef.BodyType.StaticBody)) {

                    ((BallView) contact.getFixtureA().getBody().getUserData()).startContact((BallView) contact.getFixtureB().getBody().getUserData());
                }
            }

            @Override
            public void endContact(Contact contact) {
                ((BallView) contact.getFixtureA().getBody().getUserData()).endContact();
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }


    @Override
    public void render() {
        if (screen != null) screen.render(Gdx.graphics.getDeltaTime());

        //clear screen
        Gdx.gl.glClearColor(0.0f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update cameras
        cameraBox2d.update();
        //camera.update();

        //render box2D world
        debugRenderer.render(world, cameraBox2d.combined);
        world.step(1 / 60f, 6, 2);


        //my turn info for debugging
/*        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font22.setColor((playerTurn ? Color.GREEN : Color.RED));
        font22.draw(batch, "my turn", 50, 50);
        batch.end();*/

        //draw lilBalls

        Collection<BallView> ballsCollection = balls.values();
        //Iterator<BallView> it = ballsCollection.iterator();
        for (BallView ball : ballsCollection) {

            if (ball.getClass() == BigBallView.class) {
                ((BigBallView) ball).render(shapeRenderer, controller);
            } else {
                ((LilBallView) ball).render(shapeRenderer, controller);
            }
        }

        balls.putAll(balls_temp);


        controlMainCircle();

        //CHECK WHO'S TURN IS IT AND SET IT AND SEND IT
        //checkPlayerTurn();

        //checkCollisions


    }

    private synchronized void controlMainCircle() {
        if (playerTurn) {

            float vel = mainCircle.getAngularVelocity();


            // apply left impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && vel > -BIGBALL_MAX_VELOCITY) {
                float newVel = vel - BIGBALL_MAX_VELOCITY / 10;
                mainCircle.setAngularVelocity(newVel);
                //TcpCmds.MY_VELOCITY.send(connection, newVel);
            }

            // apply right impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && vel < BIGBALL_MAX_VELOCITY) {
                float newVel = vel + BIGBALL_MAX_VELOCITY / 10;
                mainCircle.setAngularVelocity(newVel);
                //TcpCmds.MY_VELOCITY.send(connection, newVel);
            }

/*
            // apply left impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.A) && vel > -MAX_BIGBALL_VELOCITY) {
                float newVel = vel - MAX_BIGBALL_VELOCITY / 10;
                mainCircle.setAngularVelocity(newVel);
                //TcpCmds.MY_VELOCITY.send(connection, newVel);
            }

            // apply right impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.D) && vel < MAX_BIGBALL_VELOCITY) {
                float newVel = vel + MAX_BIGBALL_VELOCITY / 10;
                mainCircle.setAngularVelocity(newVel);
                //TcpCmds.MY_VELOCITY.send(connection, newVel);
            }
            */

        }
    }

    public void createNewGameObject(Gluable ballObject) {
        if (mainCircle == null && ballObject instanceof BigBall) {

            createBigBall((BigBall) ballObject);

        } else if (ballObject instanceof LilBall) {

            createLilBall((LilBall) ballObject);
        }
    }


    private void createBigBall(BigBall bigBall) {

/*        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.KinematicBody;
        // Set its world position
        groundBodyDef.position.set(new Vector2(WIDTH / 2, HEIGHT / 2));
        groundBodyDef.angularVelocity = 0;

        // Create a body from the definition and add it to the world
        mainCircle = world.createBody(groundBodyDef);
        // save reference to modelGame's orginal object
        mainCircle.setUserData(bigBall);

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
        groundBox.dispose();*/
        BigBallView ball = new BigBallView(world, bigBall, WIDTH, HEIGHT, PX_TO_METER);
        mainCircle = ball.getBody();
        balls.put(bigBall, ball);

    }

    private void createLilBall(LilBall lilBall) {
/*        BodyDef lilBallBodyDef = new BodyDef();
        // We set our lilBallBody to dynamic, for something like ground which doesn't move we would set it to StaticBody
        lilBallBodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our lilBallBody's starting position in the world
        // varies with playerID
        lilBallBodyDef.position.set(WIDTH / 2, (HEIGHT / 2) + (lilBall.getPlayerId() == 1 ? -2 : 2));
        //lilBallBodyDef.position.set(WIDTH / 2, HEIGHT / 2);
        // Create our lilBallBody in the world using our lilBallBody definition
        Body lilBallBody = world.createBody(lilBallBodyDef);
        // save reference to modelGame's orginal object
        lilBallBody.setUserData(lilBall);
        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(lilBall.getRadius());


        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.0f; // Make it bounce a little bit

        // Create our fixture and attach it to the lilBallBody
        Fixture fixture = lilBallBody.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

        lilBallBodyMap.put(lilBall, lilBallBody);
        //has to be like a staticBody, it only stays dyanamic when startMoving is called
        lilBallBody.setType(BodyDef.BodyType.StaticBody);*/

        LilBallView ball = new LilBallView(world, lilBall, WIDTH, HEIGHT, PX_TO_METER);
        balls_temp.put(lilBall, ball);

    }

    public void startMoving(LilBall lilBall) {
        BallView ball = balls.get(lilBall);
        if (ball == null) {
            ball = balls_temp.get(lilBall);
        }

        Body lilBallBody = ball.getBody();
        lilBallBody.setType(BodyDef.BodyType.DynamicBody);
        lilBallBody.setLinearVelocity(0, LILBALL_MAX_VELOCITY * (lilBall.getPlayerId() == 1 ? -1 : 1));
    }

    public void stopMoving(LilBall lilBall) {


        //find lilBall body
        BallView ball = balls.get(lilBall);
        if (ball == null) {
            ball = balls_temp.get(lilBall);
        }
        Body lilBallBody = ball.getBody();

        //find otherBall Body
        BallView otherBall = balls.get(lilBall.getAttachedBall());
        System.out.println(otherBall);
        if (otherBall == null) {
            otherBall = balls_temp.get(lilBall.getAttachedBall());
        }
        System.out.println(otherBall);

        Body otherBallBody = otherBall.getBody();


        //do weld joint
        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.bodyA = lilBallBody;
        weldJointDef.bodyB = otherBallBody;
        weldJointDef.initialize(lilBallBody, otherBallBody, lilBallBody.getWorldCenter());

        world.createJoint(weldJointDef);


    }

    public void increaseScore() {
        if(playerTurn) {


        }
    }


}
