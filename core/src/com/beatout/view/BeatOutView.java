package com.beatout.view;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Iterator;
import java.util.List;
import com.beatout.core.*;
import com.beatout.math.Line;

import java.util.ArrayList;

public class BeatOutView extends ApplicationAdapter {

    BeatOut beatOut;

	SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    RayHandler rayHandler;
	World world;
	OrthographicCamera camera;
    private List<CollisionEffect> collisionEffects = new ArrayList<CollisionEffect>();

    Trajectory trajectory; // For debugging


    Sound bass;
    Sound hihat;
    Sound hihat2;
    Sound open_hihat;

    @Override
	public void create () {

        // Test sound
        bass = Gdx.audio.newSound(Gdx.files.internal("sounds/bass.wav"));
        hihat = Gdx.audio.newSound(Gdx.files.internal("sounds/hihat.wav"));
        hihat2 = Gdx.audio.newSound(Gdx.files.internal("sounds/hihat2.wav"));
        open_hihat = Gdx.audio.newSound(Gdx.files.internal("sounds/open_hihat.wav"));
        // \test sound

        beatOut = new BeatOut(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        NotificationManager.getDefault().addObserver(BlockCollision.BLOCK_COLLISION_EVENT, new NotificationManager.EventHandler<BlockCollision>() {
            @Override
            public void handleEvent(NotificationManager.Event<BlockCollision> event) {
                System.out.println("Block collision");
                createCollisionEffect(event.data);
                hihat.play();
            }
        });

        NotificationManager.getDefault().addObserver(BoundaryCollision.BOUNDARY_COLLISION_EVENT, new NotificationManager.EventHandler<BlockCollision>() {
            @Override
            public void handleEvent(NotificationManager.Event<BlockCollision> event) {
                System.out.println("Boundary collision");
                createCollisionEffect(event.data);
                hihat2.play();
            }
        });



        NotificationManager.getDefault().addObserver(BeatOut.TEST_BEAT_EVENT, new NotificationManager.EventHandler<BeatOut>() {
            @Override
            public void handleEvent(NotificationManager.Event<BeatOut> event) {
                bass.play();
            }
        });
        NotificationManager.getDefault().addObserver(BeatOut.CYCLE_END, new NotificationManager.EventHandler<BeatOut>() {
            @Override
            public void handleEvent(NotificationManager.Event<BeatOut> event) {
                trajectory = beatOut.getGameBoard().calculateTrajectory();
                open_hihat.play();
            }
        });

        //NotificationManager.getDefault().addObserver(/*4thBeat*/);

		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
		World world = new World(new Vector2(0,0), false);
        // TEST WORLD
//        float h = Gdx.graphics.getHeight();
//        for (Block b : beatOut.getGameBoard().getBlocks()) {
//            PolygonShape polygonShape = new PolygonShape();
//            polygonShape.setAsBox(b.getWidth()/2, b.getHeight()/2, new Vector2(b.getLeft(), h-b.getTop()), 0);
////            polygonShape.setAsBox(b.getWidth(), b.getHeight());
////            ChainShape chainShape = new ChainShape();
////            chainShape.createLoop(new Vector2[] {
////                    new Vector2(b.getLeft(), h-b.getTop()),
////                    new Vector2(b.getRight(), h-b.getTop()),
////                    new Vector2(b.getRight(), h-b.getBottom()),
////                    new Vector2(b.getLeft(), h-b.getBottom()) });
//            BodyDef chainBodyDef = new BodyDef();
//            chainBodyDef.type = BodyDef.BodyType.StaticBody;
//            Body groundBody = world.createBody(chainBodyDef);
////            groundBody.createFixture(chainShape, 0);
////            chainShape.dispose();
//            groundBody.createFixture(polygonShape, 0);
//            polygonShape.dispose();
//        }
        //\WORLD
        shapeRenderer = new ShapeRenderer();
        rayHandler = new RayHandler(world);
		int numRays = 200;
		float distance = 200;
		float x = 100;
		float y = 200;
//		new PointLight(rayHandler, 100, new Color(1,1,1,1), distance, x, y);

//        collisionEffects = new ArrayList<CollisionEffect>();
//        collisionEffects.add(new CollisionEffect(new Vector(x,y), rayHandler));

//        new ConeLight(rayHandler, numRays, new Color(1,.5f,0,1), distance, x, y, 180, 90);
//		new ConeLight(rayHandler, numRays, new Color(1,.5f,0,1), distance*.7f, x, y, 190, 10);
//		new ConeLight(rayHandler, numRays, new Color(1,.5f,0,1), 1000, 600, y, 170, 350);


//        rayHandler.setAmbientLight(.2f);

		float viewportWidth = Gdx.graphics.getWidth();
		float viewportHeight = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		camera.position.set(viewportWidth / 2f, viewportHeight / 2f, 0);
		camera.update();



        trajectory = beatOut.getGameBoard().calculateTrajectory();
	}

    private void createCollisionEffect(Collision collision) {
        CollisionEffect collisionEffect = new CollisionEffect(collision, beatOut.getBallRadius(), rayHandler);
        collisionEffects.add(collisionEffect);
    }

    public void update() {
        float right = Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0;
        float left = Gdx.input.isKeyPressed(Input.Keys.LEFT) ? 1 : 0;
        float direction = right - left;
        if (direction != 0) {
            beatOut.moveBat(direction, Gdx.graphics.getDeltaTime());
        }
        beatOut.update(Gdx.graphics.getDeltaTime());
        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            beatOut = new BeatOut(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            //beatOut.getGameBoard().createTestLevel();
            trajectory = beatOut.getGameBoard().calculateTrajectory();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) { // DEBUGGING: Copy the current level to the clipboard
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
//                Gdx.app.getClipboard().setContents(beatOut.getGameBoard().getBlockActivations());
                Gdx.app.getClipboard().setContents(beatOut.getStateString());
            } else {
                beatOut.setPlayMode(BeatOut.PlayMode.CLIPBOARD);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            beatOut.setPlayMode(BeatOut.PlayMode.REPEAT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            beatOut.setPlayMode(BeatOut.PlayMode.NORMAL);
        }
    }

    @Override
	public void render () {
        update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		camera.update();
        updateCollisionEffects();
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
        shapeRenderer.setProjectionMatrix(camera.combined);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        GameBoard gameBoard = beatOut.getGameBoard();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Block block : gameBoard.getBlocks()) {
            if (block.isActive()) {
                float x = block.getPosition().getX();
                float y = block.getPosition().getY();
                float gdxX = x;
                float gdxY = h - y;
                float gdxWidth = block.getSize().getX();
                float gdxHeight = -block.getSize().getY();
                shapeRenderer.setColor(1,0.8f,0, 1);
                shapeRenderer.rect(gdxX, gdxY, gdxWidth, gdxHeight);
            }
        }
        Paddle paddle = gameBoard.getPaddle();
        shapeRenderer.setColor(1,.95f,0.2f, 1);
        float gdxX = paddle.getPosition().getX();
        float gdxY = h - paddle.getPosition().getY();
        float gdxWidth = paddle.getSize().getX();
        float gdxHeight = -paddle.getSize().getY();
        shapeRenderer.rect(gdxX, gdxY, gdxWidth, gdxHeight);

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            drawTrajectory(trajectory, shapeRenderer);
        }

        Ball ball = gameBoard.getBall();
        shapeRenderer.setColor(1,1,1,1);
        gdxX = ball.getPosition().getX();
        gdxY = h - ball.getPosition().getY();
        gdxWidth = ball.getSize().getX();
        gdxHeight = -ball.getSize().getY();
        shapeRenderer.rect(gdxX, gdxY, gdxWidth, gdxHeight);

        shapeRenderer.end();
	}

    private void drawTrajectory(Trajectory trajectory, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1,0f,0f, 1);
        for (Line line : trajectory.getLinesBetweenBounces()) {
            float gdxX1 = line.getStart().getX()+beatOut.getBallRadius();
            float gdxY1 = Gdx.graphics.getHeight() - line.getStart().getY() - beatOut.getBallRadius();
            float gdxX2 = line.getEnd().getX()+beatOut.getBallRadius();
            float gdxY2 = Gdx.graphics.getHeight() - line.getEnd().getY() - beatOut.getBallRadius();
            shapeRenderer.rectLine(gdxX1, gdxY1, gdxX2, gdxY2, 1.4f);
        }
    }

    private void updateCollisionEffects() {
        Iterator<CollisionEffect> iterator = collisionEffects.iterator();
        while (iterator.hasNext()) {
            CollisionEffect collisionEffect = iterator.next();
            if (!collisionEffect.update()) {
                iterator.remove();
            }
        }
    }

    @Override
	public void dispose () {
		batch.dispose();
        rayHandler.dispose();
	}
}
