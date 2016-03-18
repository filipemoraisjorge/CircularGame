package org.academiacodigo.bootcamp.vascos.circulargame.model;

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
import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.BigBall;
import org.academiacodigo.bootcamp.vascos.circulargame.model.balls.LilBall;


/**
 * Created by JVasconcelos on 16/03/16
 */
public class Game extends ApplicationAdapter implements Subscriber {

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


    private BigBall bigBall;
    private Player player1;
    private Player player2;
    //private List<Publisher> subjects = new ArrayList<Publisher>();


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


        createBox2dObjects();



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





    public void init() {
        //initialize bigBall and players
    }

    public void start() {
        //start game
    }

    //put the ball in the game
    public void spit(LilBall lilBall) {

    }



    @Override
    public void update() {
        //observer pattern
    }
}
