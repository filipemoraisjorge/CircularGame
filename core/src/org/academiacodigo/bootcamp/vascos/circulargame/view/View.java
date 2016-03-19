package org.academiacodigo.bootcamp.vascos.circulargame.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiacodigo.bootcamp.vascos.circulargame.Network.TcpConnection;
import org.academiacodigo.bootcamp.vascos.circulargame.model.Gluable;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.game_objects.LilBall;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JVasconcelos on 18/03/16
 */
public class View implements ApplicationListener {
    private final float WIDTH_PX = 800;
    private final float HEIGHT_PX = 480;
    private final float WIDTH = WIDTH_PX / 10;
    private final float HEIGHT = HEIGHT_PX / 10;

    private final float BIGBALL_MAX_VELOCITY = 1;
    private final int LILBALL_MAX_VELOCITY = 10;

    private TcpConnection connection;

    private OrthographicCamera cameraBox2d;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body mainCircle;
    private Map<LilBall, Body> lilBallBodyMap;

    private BitmapFont font22;


    private boolean playerTurn;

    private Screen screen;

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


        //START LISTENING FOR COMMANDS
 /*       Thread commandListener = new Thread(new TCPListener());
        commandListener.setName("commandListener");
        commandListener.start();*/

        //DECIDE WHO PLAYS FIRST
        //decideWhoPlaysFirst();


        playerTurn = true;
        //setPlayerTurn(true);
        lilBallBodyMap = new HashMap<LilBall, Body>();
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

        controlMainCircle();

        //CHECK WHO'S TURN IS IT AND SET IT AND SEND IT
        //checkPlayerTurn();

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

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.KinematicBody;
        // Set its world position
        groundBodyDef.position.set(new Vector2(WIDTH / 2, HEIGHT / 2));
        groundBodyDef.angularVelocity = 0;

        // Create a body from the definition and add it to the world
        mainCircle = world.createBody(groundBodyDef);
        mainCircle.setUserData(bigBall.getId()); //save ID on userData

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


    }

    private void createLilBall(LilBall lilBall) {
        BodyDef lilBallBodyDef = new BodyDef();
        // We set our lilBallBody to dynamic, for something like ground which doesn't move we would set it to StaticBody
        lilBallBodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our lilBallBody's starting position in the world
        // varies with playerID
        System.out.println(((lilBall.getPlayerId() == 1 ? -1 : 1)));
        lilBallBodyDef.position.set(WIDTH / 2, (HEIGHT / 2) + (lilBall.getPlayerId() == 1 ? -1 : 1));
        //lilBallBodyDef.position.set(WIDTH / 2, HEIGHT / 2);
        // Create our lilBallBody in the world using our lilBallBody definition
        Body lilBallBody = world.createBody(lilBallBodyDef);

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        System.out.println(lilBall.getRadius());
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
        lilBallBody.setType(BodyDef.BodyType.StaticBody);
        //TODO: has to be like a staticBody, it only stays dyanamic when startMoving is called
    }

    public void startMoving(LilBall lilBall) {
        Body lilBallBody = lilBallBodyMap.get(lilBall);
        lilBallBody.setType(BodyDef.BodyType.DynamicBody);
        lilBallBody.setLinearVelocity(0, LILBALL_MAX_VELOCITY * (lilBall.getPlayerId() == 1 ? -1 : 1));
        System.out.println(lilBallBodyMap.get(lilBall).getLinearVelocity());


    }


}
