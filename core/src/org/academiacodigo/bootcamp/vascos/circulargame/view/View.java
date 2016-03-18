package org.academiacodigo.bootcamp.vascos.circulargame.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiacodigo.bootcamp.vascos.circulargame.Network.TcpConnection;

import java.util.ArrayList;

/**
 * Created by JVasconcelos on 18/03/16
 */
public class View extends ApplicationAdapter {
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
    private Body mainCircle;



    private int gameObjectId = 1;
    private ArrayList<Body> gameObjects = new ArrayList<Body>();


    public ArrayList<Body> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void create() {
        //Create Connection between two players
        //connection = new TcpConnection(55555);

        //Create Fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/montserrat/Montserrat-Hairline.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
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
        debugRenderer = new Box2DDebugRenderer();



        //START LISTENING FOR COMMANDS
 /*       Thread commandListener = new Thread(new TCPListener());
        commandListener.setName("commandListener");
        commandListener.start();*/

        //DECIDE WHO PLAYS FIRST
        //decideWhoPlaysFirst();

        mainCircle.setAngularVelocity(0);
        //playerTurn = true;
        //setPlayerTurn(true);

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

    public void createNewGameObject() {
        if(mainCircle == null) {
            createBigBall();
            return;
        }

        createLilBall();
    }

    private void createBigBall() {

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.KinematicBody;
        // Set its world position
        groundBodyDef.position.set(new Vector2(WIDTH / 2, HEIGHT / 2));
        groundBodyDef.angularVelocity = 0;

        // Create a body from the definition and add it to the world
        mainCircle = world.createBody(groundBodyDef);
        gameObjects.add(0, mainCircle);

        // Create a polygon shape
        ChainShape groundBox = new ChainShape();

        //Calculate the vertices of a circle.
        double radius = 20;
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

    private void createLilBall() {
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(WIDTH / 2, HEIGHT / 2);

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);
        gameObjects.add(gameObjectId, body);
        gameObjectId++;

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(6f);


        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

    }


}
