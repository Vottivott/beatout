package com.beatout.view;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Iterator;
import java.util.List;
import com.beatout.core.*;
import com.beatout.math.Vector;

import java.util.ArrayList;

public class BeatOutView extends ApplicationAdapter {

    BeatOut beatOut;

	SpriteBatch batch;
	Texture img;
    ShapeRenderer shapeRenderer;
    RayHandler rayHandler;
	World world;
	OrthographicCamera camera;
    private List<CollisionEffect> collisionEffects;


    @Override
	public void create () {

        beatOut = new BeatOut();

        NotificationManager.getDefault().addObserver(BlockCollision.BLOCK_COLLISION_EVENT, new NotificationManager.EventHandler<BlockCollision>() {
            @Override
            public void handleEvent(NotificationManager.Event<BlockCollision> event) {
                System.out.println("Event test");
            }
        });

		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
		World world = new World(new Vector2(0,0), false);
        shapeRenderer = new ShapeRenderer();
        rayHandler = new RayHandler(world);
		int numRays = 200;
		float distance = 200;
		float x = 100;
		float y = 200;
//		new PointLight(rayHandler, 100, new Color(1,1,1,1), distance, x, y);

        collisionEffects = new ArrayList<CollisionEffect>();
        collisionEffects.add(new CollisionEffect(new Vector(x,y), rayHandler));

//        new ConeLight(rayHandler, numRays, new Color(1,.5f,0,1), distance, x, y, 180, 90);
//		new ConeLight(rayHandler, numRays, new Color(1,.5f,0,1), distance*.7f, x, y, 190, 10);
//		new ConeLight(rayHandler, numRays, new Color(1,.5f,0,1), distance*.7f, x, y, 170, 10);


//        rayHandler.setAmbientLight(.2f);

		float viewportWidth = Gdx.graphics.getWidth();
		float viewportHeight = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		camera.position.set(viewportWidth / 2f, viewportHeight / 2f, 0);
		camera.update();
	}

	@Override
	public void render () {
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

        GameBoard gameBoard = beatOut.getGameBoard();
        for (Block block : gameBoard.getBlocks()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();
            float x = block.getPosition().getX();
            float y = block.getPosition().getY();
            shapeRenderer.setColor(1,0.8f,0, 1);
            shapeRenderer.rect(x, y, 40, 27);
//            shapeRenderer.rect(10, 10, w - 20, h - 20);
            shapeRenderer.end();
//            float x1=w/3;
//            float y1=h/3;
//            float cc=w/2;
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(290/255f, 190/255f,190/255f, 1);
//            shapeRenderer.rect(x1,y1, cc,cc);
//            shapeRenderer.setColor(107/255f, 107/255f,207/255f, 1);
//            cc=cc/2;
//            shapeRenderer.triangle(x1, y1, x1+cc, y1, x1, y1+cc);
//            shapeRenderer.setColor(290/255f, 290/255f,290/255f, 1);
//            shapeRenderer.circle(x1+cc,y1+cc, (cc*(2^1/2))/2);
//            shapeRenderer.end();
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
		img.dispose();
        rayHandler.dispose();
	}
}
