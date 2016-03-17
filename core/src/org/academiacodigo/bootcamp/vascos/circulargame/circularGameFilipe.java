package org.academiacodigo.bootcamp.vascos.circulargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;
import org.academiacodigo.bootcamp.vascos.circulargame.Network.TcpCmds;
import org.academiacodigo.bootcamp.vascos.circulargame.Network.TcpConnection;

public class circularGameFilipe extends ApplicationAdapter {
    private final float WIDTH_PX = 800;
    private final float HEIGHT_PX = 480;

    private final float WIDTH = WIDTH_PX / 10;
    private final float HEIGHT = HEIGHT_PX / 10;

    private final float MAX_VELOCITY = 1;


    private TcpConnection connection;

    private OrthographicCamera cameraBox2d;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;

    private BitmapFont font22;

    private Box2DDebugRenderer debugRenderer;
    private Body bigBall;
    private Body player1Motor;
    private Body player2Motor;

    private long playerTimeToControl = 500;
    private long lastPlayerTime;
    private boolean playerTurn;


    @Override
    public void create() {
        //Create Connection between two players
        // connection = new TcpConnection(55555);


        //Create Fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/montserrat/Montserrat-Hairline.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 22;
        font22 = generator.generateFont(parameter);
        generator.dispose();


        cameraBox2d = new OrthographicCamera();
        cameraBox2d.setToOrtho(false, WIDTH, HEIGHT);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH_PX, HEIGHT_PX);
        batch = new SpriteBatch();

        Box2D.init();

        world = new World(new Vector2(0, -10), true);
        //world.setGravity(new Vector2(0, 0));
        debugRenderer = new Box2DDebugRenderer();
        createBox2dObjects();

        //START LISTENING FOR COMMANDS
      /*  Thread commandListener = new Thread(new TCPListener());
        commandListener.setName("commandListener");
        commandListener.start();*/

        //DECIDE WHO PLAYS FIRST
        //decideWhoPlaysFirst();
        playerTurn = true;
        bigBall.setAngularVelocity(0);


    }


    @Override
    public void render() {
        //clear screen
        Gdx.gl.glClearColor(0.0f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //update cameras
        cameraBox2d.update();
        camera.update();

        //render box2D world
        debugRenderer.render(world, cameraBox2d.combined);
        world.step(1 / 60f, 6, 2);

        //my turn info for debugging
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font22.setColor((playerTurn ? Color.GREEN : Color.RED));
        font22.draw(batch, "my turn", 50, 50);
        batch.end();

        controlMainCircle();

        //CHECK WHO'S TURN IS IT AND SET IT AND SEND IT


        //checkPlayerTurn();

    }


    private synchronized void decideWhoPlaysFirst() {

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
        //If is it my turn, save currentTime
        if (playerTurn) {
            lastPlayerTime = TimeUtils.millis();
        }
    }

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


    private synchronized void controlMainCircle() {
        if (playerTurn) {


            float vel = bigBall.getAngularVelocity();

            //Player1
            // apply left impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.A) && vel > -MAX_VELOCITY) {
                moveLeft(player1Motor, vel);
            }

            // apply right impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.D) && vel < MAX_VELOCITY) {
                moveRight(player1Motor, vel);
            }

            //Player2
            // apply left impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && vel > -MAX_VELOCITY) {
                moveLeft(player2Motor, vel);
            }

            // apply right impulse, but only if max velocity is not reached yet
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && vel < MAX_VELOCITY) {
                moveRight(player2Motor, vel);
            }


        }
    }

    private void moveLeft(Body player, float vel) {
        float newVel = vel - MAX_VELOCITY;
        player.setAngularVelocity(newVel);
        //bigBall.applyForce(new Vector2(0.0f, 1.0f), bigBall.getLocalCenter().add(20, 0), true);
        //TcpCmds.MY_VELOCITY.send(connection, newVel);
    }

    private void moveRight(Body player, float vel) {
        float newVel = vel + MAX_VELOCITY;
        player.setAngularVelocity(newVel);
        //TcpCmds.MY_VELOCITY.send(connection, newVel);
    }


    private synchronized void checkPlayerTurn() {
        if (playerTurn && TimeUtils.timeSinceMillis(lastPlayerTime) >= playerTimeToControl) {
            playerTurn = false;


            TcpCmds.YOUR_TURN.send(connection, true);
        }
    }

    private void createBox2dObjects() {

        //BIG BALL
        // Create our body definition
        BodyDef bigCircleBodyDef = new BodyDef();
        bigCircleBodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set its world position
        bigCircleBodyDef.position.set(WIDTH / 2, HEIGHT / 2);
        bigCircleBodyDef.angularVelocity = 0;
        // Create a body from the definition and add it to the world
        bigBall = world.createBody(bigCircleBodyDef);

        // Create a polygon shape
        ChainShape bigCircleChain = new ChainShape();
        //Calculate the vertices of a circle.
        double radius = 20;
        int numberOfSegments = 36;
        Vector2[] vertices = new Vector2[numberOfSegments + 2];

        double angleSeg = 360 / numberOfSegments;
        for (int i = 0; i < numberOfSegments + 1; i++) {
            double angle = Math.toRadians(angleSeg * i);
            float x = (float) (radius * Math.cos(angle));
            float y = (float) (radius * Math.sin(angle));
            vertices[i] = new Vector2(x, y);
        }
        vertices[numberOfSegments + 1] = new Vector2(0, 0);

        bigCircleChain.createLoop(vertices);

        // Create FixtureDef
        FixtureDef bigCircleFixtureDef = new FixtureDef();
        bigCircleFixtureDef.shape = bigCircleChain;
        bigCircleFixtureDef.density = 0.00001f;
        bigCircleFixtureDef.friction = 1f;
        bigCircleFixtureDef.restitution = 0.0f;
        // Create a fixture from our polygon shape and add it to our ground body
        bigBall.createFixture(bigCircleFixtureDef);


        //The Motors
        // Create a circle shape
        CircleShape motorCircleShape = new CircleShape();
        motorCircleShape.setRadius(3f);

        // Create a fixture definition to apply our shape to
        FixtureDef motorFixtureDef = new FixtureDef();
        motorFixtureDef.shape = motorCircleShape;
        motorFixtureDef.density = 0.5f;
        motorFixtureDef.friction = 1f;
        motorFixtureDef.restitution = 0.0f;


        //PLAYER1 MOTOR
        // First we create a body definition
        BodyDef motor1 = new BodyDef();
        motor1.type = BodyDef.BodyType.KinematicBody;

        float radiusM = 20 + motorCircleShape.getRadius();
        float xM = (float) (radiusM * Math.cos(Math.toRadians(30)));
        float yM = (float) (radiusM * Math.sin(Math.toRadians(30)));
        // Set our body's starting position in the world
        motor1.position.set((WIDTH / 2) - xM, (HEIGHT / 2) - yM);
        // Create our body in the world using our body definition
        player1Motor = world.createBody(motor1);
        // Create our fixture and attach it to the body
        Fixture player1MotorFixture = player1Motor.createFixture(motorFixtureDef);


        //PLAYER2 MOTOR
        // First we create a body definition
        BodyDef motor2 = new BodyDef();
        motor2.type = BodyDef.BodyType.KinematicBody;
        // Set our body's starting position in the world
        motor2.position.set((WIDTH / 2) + xM, (HEIGHT / 2) - yM);
        // Create our body in the world using our body definition
        player2Motor = world.createBody(motor2);
        // Create our fixture and attach it to the body
        player2Motor.createFixture(motorFixtureDef);

        //a test square
        BodyDef squareBodyDef = new BodyDef();
        squareBodyDef.type = BodyDef.BodyType.DynamicBody;
        squareBodyDef.position.set(WIDTH / 2 + 23, HEIGHT / 2 + 1);
        Body square = world.createBody(squareBodyDef);
        PolygonShape squareShape = new PolygonShape();
        squareShape.setAsBox(1, 1);
        square.createFixture(squareShape, 0.5f);

        // Clean up after ourselves
          bigCircleChain.dispose();
          motorCircleShape.dispose();
          squareShape.dispose();


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
                    float vel = bigBall.getAngularVelocity();
                    System.out.println("vel " + vel);
                    if (vel <= -MAX_VELOCITY) {
                        vel = -MAX_VELOCITY;
                    }
                    if (vel >= MAX_VELOCITY) {
                        vel = MAX_VELOCITY;
                    }

                    float calcVelocity = vel + Float.parseFloat(value);
                    bigBall.setAngularVelocity(calcVelocity);
                    break;

                default:
                    //nothing
            }

        }
    }


}


