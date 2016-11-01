package com.beatout.core;

import com.beatout.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private List<Block> blocks;
    private float width;
    private float height;

    public GameBoard() {
        blocks = new ArrayList<Block>();
        blocks.add(new Block(new Vector(200,200)));
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
