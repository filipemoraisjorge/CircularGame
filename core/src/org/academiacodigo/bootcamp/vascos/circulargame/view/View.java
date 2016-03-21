package org.academiacodigo.bootcamp.vascos.circulargame.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.TimeUtils;
import org.academiacodigo.bootcamp.vascos.circulargame.Network.TcpCmds;
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
    private boolean NETWORK_ON;
    private boolean MULTIPLAYER_ON;
    private Controller controller;


    private OrthographicCamera cameraBox2d;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body mainCircle;

    private Map<Gluable, BallView> balls;
    private Map<Gluable, BallView> balls_temp;


    private TcpConnection connection;
    private BitmapFont font22;
    private long playerTimeToControl = 500;
    private long lastPlayerTime;


    private boolean playerTurn;

    private Screen screen;


    public View(boolean isNetworking, boolean isMultiplayer) {
        this.NETWORK_ON = isNetworking;
        this.MULTIPLAYER_ON = isMultiplayer;
    }

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
     * @return the currently active {@link Screen}.
     */
    public Screen getScreen() {
        return screen;
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


    @Override
    public void create() {

        if (NETWORK_ON) {
            //Create Connection between two players
            connection = new TcpConnection(55555);
        }

        if(MULTIPLAYER_ON) {

            //Create Fonts
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/montserrat/Montserrat-Hairline.otf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 22;
            font22 = generator.generateFont(parameter);
            generator.dispose();


            camera = new OrthographicCamera();
            camera.setToOrtho(false, WIDTH_PX, HEIGHT_PX);
            batch = new SpriteBatch();
        }

        cameraBox2d = new OrthographicCamera();
        cameraBox2d.setToOrtho(false, WIDTH, HEIGHT);


        Box2D.init();

        world = new World(new Vector2(0, 0), true);

        //debugRenderer = new Box2DDebugRenderer();

        shapeRenderer = new ShapeRenderer();

        if (NETWORK_ON) {
            //START LISTENING FOR COMMANDS
            Thread commandListener = new Thread(new TCPListener());
            commandListener.setName("commandListener");
            commandListener.start();
        }
        //DECIDE WHO PLAYS FIRST
        decideWhoPlaysFirst();


        //setPlayerTurn(true);
        balls = new HashMap<Gluable, BallView>();
        balls_temp = new HashMap<Gluable, BallView>();
        //set Collision detector
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                if (!(contact.getFixtureA().getBody().getType() == BodyDef.BodyType.StaticBody) &&
                        !(contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody)) {

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

    private synchronized void decideWhoPlaysFirst() {

        if (NETWORK_ON) {
            //the quickest to get where starts.
            long myTime = sendTimeToOther();
            long otherTime = 0;
            while (otherTime == 0) {
                otherTime = receiveTimeFromOther();
            }
            System.out.println("MINE  " + myTime);
            System.out.println("OTHER " + otherTime);

            if (myTime > otherTime) {
                playerTurn = true;
                TcpCmds.YOUR_TURN.send(connection, !playerTurn);
            }
        } else if (MULTIPLAYER_ON) {
            playerTurn = (Math.random() > 0.5);
        } else {
            playerTurn = true;
        }
        //If is it my turn, save currentTime
        if (playerTurn) {
            lastPlayerTime = TimeUtils.millis();
        }
    }


    @Override
    public void render() {
        if (screen != null) screen.render(Gdx.graphics.getDeltaTime());

        //clear screen
        Gdx.gl.glClearColor(0.0f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update cameras
        cameraBox2d.update();
        if (MULTIPLAYER_ON) {
            camera.update();
        }

        //render box2D world
        //debugRenderer.render(world, cameraBox2d.combined);
        world.step(1 / 60f, 6, 2);

        if (MULTIPLAYER_ON) {
            //my turn info for debugging
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            font22.setColor((playerTurn ? Color.GREEN : Color.RED));
            font22.draw(batch, "player1", 50, 50);
            font22.setColor((!playerTurn ? Color.GREEN : Color.RED));
            font22.draw(batch, "player2", 50, 25);
            batch.end();
        }

        //draw Balls
        Collection<BallView> ballsCollection = balls.values();
        for (BallView ball : ballsCollection) {

            if (ball.getClass() == BigBallView.class) {
                ((BigBallView) ball).render(shapeRenderer, controller);
            } else {
                ((LilBallView) ball).render(shapeRenderer, controller);
            }
        }
        balls.putAll(balls_temp);


        controlMainCircle();
        if (MULTIPLAYER_ON) {
            //CHECK WHO'S TURN IS IT AND SET IT AND SEND IT
            checkPlayerTurn();
        }

    }

    private synchronized void controlMainCircle() {
        float vel = mainCircle.getAngularVelocity();

            //PLAYER1
        if (playerTurn) {

            // apply left impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && vel > -BIGBALL_MAX_VELOCITY) {
                float newVel = vel - BIGBALL_MAX_VELOCITY / 10;
                mainCircle.setAngularVelocity(newVel);
                if (NETWORK_ON) {
                    TcpCmds.MY_VELOCITY.send(connection, newVel);
                }
            }

            // apply right impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && vel < BIGBALL_MAX_VELOCITY) {
                float newVel = vel + BIGBALL_MAX_VELOCITY / 10;
                mainCircle.setAngularVelocity(newVel);
                if (NETWORK_ON) {
                    TcpCmds.MY_VELOCITY.send(connection, newVel);
                }
            }

            //PLAYER 2
        } else if (MULTIPLAYER_ON) {
            // apply left impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.A) && vel > -BIGBALL_MAX_VELOCITY) {
                float newVel = vel - BIGBALL_MAX_VELOCITY / 10;
                mainCircle.setAngularVelocity(newVel);
                if (NETWORK_ON) {
                    TcpCmds.MY_VELOCITY.send(connection, newVel);
                }
            }

            // apply right impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.D) && vel < BIGBALL_MAX_VELOCITY) {
                float newVel = vel + BIGBALL_MAX_VELOCITY / 10;
                mainCircle.setAngularVelocity(newVel);
                if (NETWORK_ON) {
                    TcpCmds.MY_VELOCITY.send(connection, newVel);
                }
            }
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

        BigBallView ball = new BigBallView(world, bigBall, WIDTH, HEIGHT, PX_TO_METER);
        mainCircle = ball.getBody();
        balls.put(bigBall, ball);

    }

    private void createLilBall(LilBall lilBall) {

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

        //do weld joint, the glue!
        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.bodyA = lilBallBody;
        weldJointDef.bodyB = otherBallBody;
        weldJointDef.initialize(lilBallBody, otherBallBody, lilBallBody.getWorldCenter());

        world.createJoint(weldJointDef);

    }

    /**
     * Networking
     *
     * @return
     */

    private synchronized long sendTimeToOther() {
        //send time to other
        long myTime = TimeUtils.millis();
        TcpCmds.MY_TIME.send(connection, myTime);
        return myTime;

    }

    private synchronized long receiveTimeFromOther() {
        //receive other time (will be stored lastPlayerTime)
        return lastPlayerTime;
    }

    private synchronized void checkPlayerTurn() {
        if (MULTIPLAYER_ON) {
            System.out.println(lastPlayerTime);
            if (playerTurn && TimeUtils.timeSinceMillis(lastPlayerTime) >= playerTimeToControl) {
                playerTurn = false;

                if (NETWORK_ON) {
                    TcpCmds.YOUR_TURN.send(connection, true);
                }
            }
        }

    }


    private class TCPListener implements Runnable {
        @Override
        public void run() {
            while (true) {
                receiveCommand(connection);
            }

        }

        private void receiveCommand(TcpConnection connection) {

            String[] msg = connection.receive().split(" ");

            TcpCmds thisCmds = TcpCmds.valueOf(msg[0]);
            String value = msg[1];
            switch (thisCmds) {
                case YOUR_TURN:
                    playerTurn = Boolean.parseBoolean(value);
                    if (playerTurn) {
                        lastPlayerTime = TimeUtils.millis();
                    }
                    break;
                case MY_TIME:
                    lastPlayerTime = Long.parseLong(value);
                case MY_VELOCITY:
                    float vel = mainCircle.getAngularVelocity();
                    System.out.println("vel " + vel);
                    if (vel <= -BIGBALL_MAX_VELOCITY) {
                        vel = -BIGBALL_MAX_VELOCITY;
                    }
                    if (vel >= BIGBALL_MAX_VELOCITY) {
                        vel = BIGBALL_MAX_VELOCITY;
                    }

                    float calcVelocity = vel + Float.parseFloat(value);
                    mainCircle.setAngularVelocity(calcVelocity);
                    break;

                default:
                    //nothing
            }

        }

    }

}
