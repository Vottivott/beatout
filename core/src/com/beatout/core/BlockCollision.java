package com.beatout.core;

import com.beatout.math.Vector;

public class BlockCollision extends Collision {

    private final Block block;
    public static final String BLOCK_COLLISION_EVENT = "BlockCollisionEvent";

    public BlockCollision(Vector ballPosition, Direction direction, Block block) {
        super(ballPosition, direction);
        this.block = block;
    }

    @Override
    public void performCollision() {
        block.setActive(false);
        NotificationManager.getDefault().registerEvent(BLOCK_COLLISION_EVENT, this);
    }

    @Override
    public boolean isBlockCollision() {
        return true;
    }

    public Block getBlock() {
        return block;
    }
}
