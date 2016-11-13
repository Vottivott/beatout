package com.beatout.core;

import com.badlogic.gdx.Gdx;
import com.beatout.math.BeatOutMath;
import com.beatout.math.Vector;
import com.beatout.math.Line;

import java.util.*;

import com.badlogic.gdx.utils.Clipboard;

import static com.beatout.core.Collision.Direction.*;

public class GameBoard extends Collideable {
    private List<Block> blocks;
    private Paddle paddle;
    private Ball ball;

    private float width;
    private float height;
    private float bottomY;

    public final static int BLOCK_WIDTH = 50;
    public final static int BLOCK_HEIGHT = 25;

    public final static int BAT_WIDTH = 150;
    public final static int BAT_HEIGHT = 25;
    public final static int BAT_DISTANCE_TO_BOTTOM = 50;
    public final static float BAT_SPEED = 2f;

    public static final float BALL_RADIUS = 10;//10//30;

    public GameBoard(float width, float height) {
        this.width = width;
        this.height = height - BAT_DISTANCE_TO_BOTTOM - BAT_HEIGHT; // Test
//        this.height = height;
        createTestLevel();
        float batY = height - BAT_HEIGHT - BAT_DISTANCE_TO_BOTTOM;
        paddle = new Paddle(new Vector(BAT_WIDTH, BAT_HEIGHT), new com.beatout.math.Line(0, batY, width, batY), BAT_SPEED);
        ball = new Ball(BALL_RADIUS, paddle.getPosition().add(paddle.getSize().getX()/2, -2*BALL_RADIUS), new Vector(-1, -1.7f));
        this.bottomY = batY - BALL_RADIUS*2;
        //TEST NON-RANDOM ACTIVATION
        setBlockActivations(Gdx.app.getClipboard().getContents());
    }

    public void createTestLevel() {
        blocks = new ArrayList<Block>();
        Vector size = new Vector(BLOCK_WIDTH, BLOCK_HEIGHT);
        Vector startPos = size.scale(2).add(0,BLOCK_HEIGHT*3);
        for (int i = 0; i < 7; i++) {
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+i*BLOCK_HEIGHT), size));
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+(i+4)*BLOCK_HEIGHT), size));
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+(i+8)*BLOCK_HEIGHT), size));
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+(i+12)*BLOCK_HEIGHT), size));
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+(i-1)*BLOCK_HEIGHT), size));
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+(i-2)*BLOCK_HEIGHT), size));
            blocks.add(new Block(new Vector(startPos.getX()+i*BLOCK_WIDTH,startPos.getY()+(i-3)*BLOCK_HEIGHT), size));
        }

        //TEST RANDOMIZATION
        for (Block block : blocks) {
            if (Math.random() > 0.8) {
                block.setActive(false);
            }
        }

    }

    // FOR DEBUG
    private void setBlockActivations(String activationString) {
        // 1 0 0 1 1 0 ... where 1 means active and 0 means inactive
        String[] blockActivations = activationString.split(" ");
        if (blockActivations.length != blocks.size()) {
            return;
        }
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).setActive(blockActivations[i].equals("1"));
        }
    }

    // FOR DEBUG
    public String getBlockActivations() {
        String a = "";
        for (Block block : blocks) {
            a += block.isActive() ? "1 " : "0 ";
        }
        return a;
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

    public Ball getBall() {
        return ball;
    }

    public Trajectory calculateTrajectory() {
        List<Collision> bounces = new ArrayList<Collision>();
        Ball simulatedBall = new Ball(ball);
        bounces.add(new PaddleCollision(simulatedBall.getPosition()));

        List<Block> modifiedBlocks = new ArrayList<Block>();
        while (true) {
            Collision collision = calculateNextCollision(simulatedBall, modifiedBlocks);
            bounces.add(collision);
            if (collision instanceof BoundaryCollision && collision.getDirection() == BALL_FROM_TOP) {
                break; // The ball returned to the bottom of the screen again
            }
            simulatedBall.setPosition(collision.getPosition());
            simulatedBall.setDirection(collision.getResultingDirection(simulatedBall.getDirection()));
            simulatedBall.calculateHVCPoints();
        }
        for (Block block : modifiedBlocks) { // Restore block activations
            block.setActive(true);
        }

        return new Trajectory(bounces);
    }

    private List<Block> copyBlocks(List<Block> blocks) {
        List<Block> copy = new ArrayList<Block>(blocks);
        for (int i = 0; i < copy.size(); i++) {
            copy.set(i, new Block(copy.get(i)));
        }
        return copy;
    }

    /**
     * Return the soonest upcoming collision for a given ball.
     */
    private Collision calculateNextCollision(final Ball ball, List<Block> modifiedBlocks) {
        List<BlockCollision> possibleBlockCollisions = getPossibleBlockCollisions(ball);
        if (possibleBlockCollisions.size() > 0) {
            BlockCollision blockCollision = Collections.min(possibleBlockCollisions, new Comparator<Collision>() {
                @Override
                public int compare(Collision c1, Collision c2) {
                    return Float.compare(c1.getPosition().subtract(ball.getPosition()).lengthSquared(), c2.getPosition().subtract(ball.getPosition()).lengthSquared());
                }
            });
            blockCollision.getBlock().setActive(false);
            modifiedBlocks.add(blockCollision.getBlock());
            return blockCollision;
        } else { // We only need to check for a boundary collision if there is no possible block collision
            return getPossibleBoundaryCollision(ball);
        }
    }

    private List<BlockCollision> getPossibleBlockCollisions(Ball ball) {
        List<BlockCollision> blockCollisions = new ArrayList<BlockCollision>();
        for (Block block : blocks) {
            if (block.isActive()) {
                BlockCollision collision = (BlockCollision)findCollision(ball, block);
                if (collision != null) {
                    blockCollisions.add(collision);
                }
            }
        }
        return blockCollisions;
    }

    private Collision getPossibleBoundaryCollision(Ball ball) {
        Line H = getPossibleHorizontalCollisionEdgeFromInside(ball, this);
        Line V = getPossibleVerticalCollisionEdgeFromInside(ball, this);
        Vector cCollision = null;
        if (V != null) {
            cCollision = BeatOutMath.getIntersectionBetweenVerticalLineSegmentAndLine(V.getStart().getX(), V.getStart().getY(), V.getEnd().getY(), ball.getCLine(), false);
        }
        if (cCollision == null) {
            if (H != null) {
                cCollision = BeatOutMath.getIntersectionBetweenHorizontalLineSegmentAndLine(H.getStart().getY(), H.getStart().getX(), H.getEnd().getX(), ball.getCLine(), false);
            }
            if (cCollision != null) {
                return collideWith(ball.getPositionCoordinateFromC(cCollision), ball, false);
            }
        } else {
            return collideWith(ball.getPositionCoordinateFromC(cCollision), ball, true);
        }
        return null;
    }

    /**
     * Find the point where the ball's line of movement intersects with the rectangular object, if any.
     * @return null if there is no collision point
     */
    public static Collision findCollision(Ball ball, Collideable collideable) {
        Line H = getPossibleHorizontalCollisionEdge(ball, collideable);
        Line V = getPossibleVerticalCollisionEdge(ball, collideable);
        Vector hCollision = null;
        Vector vCollision = null;
        Vector cCollision = null;
        if (V != null) {
            vCollision = BeatOutMath.getIntersectionBetweenVerticalLineSegmentAndLine(V.getStart().getX(), V.getStart().getY(), V.getEnd().getY(), ball.getVLine(), false);
        }
        if (vCollision != null) {
            return collideable.collideWith(ball.getPositionCoordinateFromV(vCollision), ball, true);
        } else {
            if (H != null) {
                hCollision = BeatOutMath.getIntersectionBetweenHorizontalLineSegmentAndLine(H.getStart().getY(), H.getStart().getX(), H.getEnd().getX(), ball.getHLine(), false);
                if (hCollision != null) {
                    return collideable.collideWith(ball.getPositionCoordinateFromH(hCollision), ball, false);
                }
            }
        }

        if (V != null) {
            cCollision = BeatOutMath.getIntersectionBetweenVerticalLineSegmentAndLine(V.getStart().getX(), V.getStart().getY(), V.getEnd().getY(), ball.getCLine(), false);
        }
        if (cCollision != null) {
            return collideable.collideWith(ball.getPositionCoordinateFromC(cCollision), ball, true);
        } else {
            if (H != null) {
                cCollision = BeatOutMath.getIntersectionBetweenHorizontalLineSegmentAndLine(H.getStart().getY(), H.getStart().getX(), H.getEnd().getX(), ball.getCLine(), false);
                if (cCollision != null) {
                    return collideable.collideWith(ball.getPositionCoordinateFromC(cCollision), ball, false);
                }
            }
        }
        return null;
    }

    public static Line getPossibleHorizontalCollisionEdge(Ball ball, RectBounded rect) {
        if (ball.getTop() > rect.getBottom() && ball.getDirection().getY() < 0) {
            return rect.getBottomLine();
        } else if (ball.getBottom() < rect.getTop() && ball.getDirection().getY() > 0) {
            return rect.getTopLine();
        }
        return null;
    }

    public static Line getPossibleHorizontalCollisionEdgeFromInside(Ball ball, RectBounded rect) {
        if (ball.getDirection().getY() < 0) {
            return rect.getTopLine();
        } else {
            return rect.getBottomLine();
        }
    }

    public static Line getPossibleVerticalCollisionEdge(Ball ball, RectBounded rect) {
        if (ball.getLeft() > rect.getRight() && ball.getDirection().getX() < 0) {
            return rect.getRightLine();
        } else if (ball.getRight() < rect.getLeft() && ball.getDirection().getX() > 0) {
            return rect.getLeftLine();
        }
        return null;
    }

    public static Line getPossibleVerticalCollisionEdgeFromInside(Ball ball, RectBounded rect) {
        if (ball.getDirection().getX() < 0) {
            return rect.getLeftLine();
        } else {
            return rect.getRightLine();
        }
    }

    /**
     * When the ball moves in a line towards a rectangle-shaped object,
     * it is impossible to collide with the back sides of the object before the colliding with the front.
     * Thus, we can filter out the back edges from the list of possible collision lines.
     */
    public static List<Line> getPossibleCollisionEdges(Ball ball, RectBounded rect) {
        List<Line> possibleLines = new ArrayList<Line>();
        if (ball.getLeft() > rect.getRight()) {
            possibleLines.add(rect.getRightLine());
        } else if (ball.getRight() < rect.getLeft()) {
            possibleLines.add(rect.getLeftLine());
        }
        if (ball.getTop() > rect.getBottom()) {
            possibleLines.add(rect.getBottomLine());
        } else if (ball.getBottom() < rect.getTop()) {
            possibleLines.add(rect.getTopLine());
        }
        if (possibleLines.isEmpty()) {
            // Add possible lines for the ball being in the inside of the ball
            if (ball.getDirection().getX() < 0) {
                possibleLines.add(rect.getLeftLine());
            } else {
                possibleLines.add(rect.getRightLine());
            }
            if (ball.getDirection().getY() < 0) {
                possibleLines.add(rect.getTopLine());
            } else {
                possibleLines.add(rect.getBottomLine());
            }
        }
        return possibleLines;
    }


    @Override
    public Collision collideWith(Vector ballPosition, Collision.Direction direction) {
        return new BoundaryCollision(ballPosition, direction);
    }

    @Override
    public Vector getPosition() {
        return new Vector(0,0);
    }

    @Override
    public Vector getSize() {
        return new Vector(width, height);
    }
}
