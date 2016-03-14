package org.academiacodigo.bootcamp.vascos.circulargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.*;
import org.academiacodigo.bootcamp.vascos.circulargame.GameObjects.CircularMap;
import org.academiacodigo.bootcamp.vascos.circulargame.GameObjects.TcpConnection;

public class circularGame extends ApplicationAdapter {
    private final float WIDTH_PX = 800;
    private final float HEIGHT_PX = 480;

    private final float WIDTH = WIDTH_PX / 10;
    private final float HEIGHT = HEIGHT_PX / 10;

    private final float MAX_VELOCITY = 1;


    private TcpConnection connection;

    private OrthographicCamera camera;
    private World world;


    private CircularMap gameCircularMap;
    private Box2DDebugRenderer debugRenderer;
    private Body groundBody;



    @Override
    public void create() {

        connection = new TcpConnection(55555);

        connection.send("TESTE");
        connection.send("TESTE2");

        System.out.println("RECEBIDO " + connection.receive());
        System.out.println("RECEBIDO " + connection.receive());


        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        //gameCircularMap = new CircularMap(camera);

        Box2D.init();

        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();


        //A BALL
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(WIDTH / 2, HEIGHT / 2);

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);

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

        //A RECTANGLE
        BodyDef rectBodydef = new BodyDef();
        rectBodydef.type = BodyDef.BodyType.DynamicBody;
        rectBodydef.position.set(WIDTH / 2, HEIGHT/3);

        Body rectBody = world.createBody(rectBodydef);
        PolygonShape rect = new PolygonShape();

        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        rect.setAsBox(2.0f, 2.0f);

        // Create a fixture definition to apply our shape to
        FixtureDef rectDef = new FixtureDef();
        rectDef.shape = rect;
        rectDef.density = 0.5f;
        rectDef.friction = 0.4f;
        rectDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixtureRect = rectBody.createFixture(rectDef);

        // Clean up after ourselves
        rect.dispose();


        //GROUND

        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.KinematicBody;
        // Set its world position
        groundBodyDef.position.set(new Vector2(WIDTH / 2, HEIGHT / 2));

        // Create a body from the defintion and add it to the world
        groundBody = world.createBody(groundBodyDef);

        // Create a polygon shape
        ChainShape groundBox = new ChainShape();

        //Calculate the vertices of a circle.
        double radius = 20;
        int numberOfSegments = 20;
        Vector2[] vertices = new Vector2[numberOfSegments];

        double angleSeg = 360/numberOfSegments;
        for (int i = 0; i < numberOfSegments; i++) {
            double angle = Math.toRadians(angleSeg*i);
            float x = (float) (radius *  Math.cos(angle));
            float y = (float) (radius *  Math.sin(angle));
            vertices[i] = new Vector2(x,y);
        }

        groundBox.createLoop(vertices);
        // Create a fixture from our polygon shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f);
        // Clean up after ourselves
        groundBox.dispose();



    }

    @Override
    public void render() {


        Gdx.gl.glClearColor(0.0f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        debugRenderer.render(world, camera.combined);
        world.step(1 / 60f, 6, 2);


        float vel = groundBody.getAngularVelocity();

// apply left impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && vel > -MAX_VELOCITY) {
            groundBody.setAngularVelocity(vel-MAX_VELOCITY/10);
        }

// apply right impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && vel < MAX_VELOCITY) {
            groundBody.setAngularVelocity(vel+MAX_VELOCITY/10);
        }




    }
}

