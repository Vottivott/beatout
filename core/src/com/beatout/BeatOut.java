package com.beatout;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BeatOut extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	RayHandler rayHandler;
	World world;
	OrthographicCamera camera;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		World world = new World(new Vector2(0,0), false);
		rayHandler = new RayHandler(world);
		int numRays = 200;
		float distance = 200;
		float x = 100;
		float y = 100;
//		new PointLight(rayHandler, 100, new Color(1,1,1,1), distance, x, y);
		new ConeLight(rayHandler, numRays, new Color(1,.5f,0,1), distance, x, y, 180, 90);
//        rayHandler.setAmbientLight(.2f);

		float viewportWidth = Gdx.graphics.getWidth();
		float viewportHeight = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		camera.position.set(0, viewportHeight / 2f, 0);
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
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
