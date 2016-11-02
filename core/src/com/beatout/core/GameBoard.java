package com.beatout.core;

import com.beatout.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private List<Block> blocks;
    private Paddle paddle;
    private float width;
    private float height;

    public final static int BLOCK_WIDTH = 50;
    public final static int BLOCK_HEIGHT = 25;
    public final static int BAT_WIDTH = 150;
    public final static int BAT_HEIGHT = 25;
    public final static int BAT_DISTANCE_TO_BOTTOM = 50;
    public final static float BAT_SPEED = 1.4f;


    public GameBoard(float width, float height) {
        this.width = width;
        this.height = height;
        createTestLevel();
        float batY = height - BAT_HEIGHT - BAT_DISTANCE_TO_BOTTOM;
        paddle = new Paddle(new Vector(BAT_WIDTH, BAT_HEIGHT), new Line(0, batY, width, batY), BAT_SPEED);
    }

    private void createTestLevel() {
        blocks = new ArrayList<Block>();
        Vector size = new Vector(BLOCK_WIDTH, BLOCK_HEIGHT);
        Vector startPos = size.scale(2).add(0,BLOCK_HEIGHT*3);
        for (int i = 0; i < 7; i++) {
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+i*BLOCK_HEIGHT), size));
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+(i+4)*BLOCK_HEIGHT), size));
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void moveBat(float direction, float deltaTime) {
        paddle.move(direction, deltaTime);
    }

}
