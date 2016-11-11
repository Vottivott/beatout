package com.beatout.view;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.beatout.core.BlockCollision;
import com.beatout.core.BoundaryCollision;
import com.beatout.core.Collision;
import com.beatout.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class CollisionEffect { // TODO: Improve effect animation
                               // TODO: Perhaps make collision effect larger the more hits you get in a short time,
                                //      like it builds up
    private Vector position;
    private float angle;

    private float duration = 0.05f;
    private float time = 0f;
    private float radius = 200;
    private static final int NUM_RAYS = 150;
    List<ConeLight> coneLights;


    public CollisionEffect(Vector position, float angle, Color color, RayHandler rayHandler) {
        this.position = position;
        this.angle = angle;
        float x = position.getX();
        float y = position.getY();
        float gdxY = Gdx.graphics.getHeight() - y;
        coneLights = new ArrayList<ConeLight>();
        coneLights.add(new ConeLight(rayHandler, NUM_RAYS, color, radius, x, gdxY, angle, 90));
        coneLights.add(new ConeLight(rayHandler, NUM_RAYS, color, radius*.7f, x, gdxY, angle+10, 10));
        coneLights.add(new ConeLight(rayHandler, NUM_RAYS, color, radius*.7f, x, gdxY, angle-10, 10));
    }

    public CollisionEffect(Collision collision, float ballRadius, RayHandler rayHandler) {
        this(calculatePosition(collision, ballRadius), calculateAngle(collision), calculateColor(collision), rayHandler);
    }

    private static Color calculateColor(Collision collision) {
        if (collision instanceof BoundaryCollision) {
            return new Color(1, .5f, 0, 1);
        }
        if (collision instanceof BlockCollision) {
//            BlockCollision b = (BlockCollision) collision;
            return new Color(1, 1f, 0, 1);
        }
        return new Color(1, .5f, 0, 1);
    }

    private static float calculateAngle(Collision collision) {
        switch (collision.getDirection()) {
            case BALL_FROM_RIGHT:
                return 0;
            case BALL_FROM_TOP:
                return 90;
            case BALL_FROM_LEFT:
                return 180;
            case BALL_FROM_BOTTOM:
                return 270;
        }
        return 0;
    }

    private static Vector calculatePosition(Collision collision, float ballRadius) {
        float xOffset, yOffset;
        if (collision.isVertical()) {
            xOffset = (collision.getDirection() == Collision.Direction.BALL_FROM_LEFT) ? 2*ballRadius : 0;
            yOffset = ballRadius;
        } else {
            xOffset = ballRadius;
            yOffset = (collision.getDirection() == Collision.Direction.BALL_FROM_TOP) ? 2*ballRadius : 0;
        }
        return collision.getPosition().add(xOffset, yOffset);
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
