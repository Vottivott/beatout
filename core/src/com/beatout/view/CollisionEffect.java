package com.beatout.view;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.beatout.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class CollisionEffect {
    private Vector position;
    private float duration = 0.05f;
    private float time = 0f;
    private float radius = 200;
    private float angle = 0;
    private static final int NUM_RAYS = 150;
    List<ConeLight> coneLights;


    public CollisionEffect(Vector position, RayHandler rayHandler) {
        this.position = position;
        float x = position.getX();
        float y = position.getY();
        coneLights = new ArrayList<ConeLight>();
        coneLights.add(new ConeLight(rayHandler, NUM_RAYS, new Color(1,.5f,0,1), radius, x, y, angle, 90));
        coneLights.add(new ConeLight(rayHandler, NUM_RAYS, new Color(1,.5f,0,1), radius*.7f, x, y, angle+10, 10));
        coneLights.add(new ConeLight(rayHandler, NUM_RAYS, new Color(1,.5f,0,1), radius*.7f, x, y, angle-10, 10));
    }

    /**
     * Returns false if the collision effect is finished
     */
    public boolean update() {
        time += Gdx.graphics.getDeltaTime();
        if (time > duration) {
            System.out.println("!");
            disposeConeLights();
            return false;
        }

        float radiusScale = 0.85f + (1 - time/duration) * 0.3f;
        for (ConeLight coneLight : coneLights) {
            coneLight.setDistance(radius * radiusScale);
        }

        return true;
    }

    private void disposeConeLights() {
        for (ConeLight coneLight : coneLights) {
            coneLight.remove(true);
        }
    }
}
