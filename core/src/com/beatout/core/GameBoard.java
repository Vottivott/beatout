package com.beatout.core;

import com.beatout.math.BeatOutMath;
import com.beatout.math.Vector;
import com.beatout.math.Line;

import java.util.*;

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
    public final static float BAT_SPEED = 1.4f;

    public static final float BALL_RADIUS = 30;

    public GameBoard(float width, float height) {
        this.width = width;
        this.height = height;
        createTestLevel();
        float batY = height - BAT_HEIGHT - BAT_DISTANCE_TO_BOTTOM;
        paddle = new Paddle(new Vector(BAT_WIDTH, BAT_HEIGHT), new com.beatout.math.Line(0, batY, width, batY), BAT_SPEED);
        ball = new Ball(BALL_RADIUS, paddle.getPosition().add(paddle.getSize().getX()/2, -2*BALL_RADIUS), new Vector(0, -1));
        this.bottomY = batY - BALL_RADIUS*2;
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

    public Ball getBall() {
        return ball;
    }

    public Trajectory calculateTrajectory() {
        List<Collision> bounces = new ArrayList<Collision>();
        Ball simulatedBall = ball;

        boolean finished = false;
        while (!finished) {
            Collision collision = calculateNextCollision(simulatedBall);
            bounces.add(collision);
            simulatedBall = new Ball(simulatedBall.getRadius(), simulatedBall.getPosition(), collision.getResultingDirection(simulatedBall.getDirection()));
            if (simulatedBall.getPosition().getY() >= bottomY) { // If the ball returns to the bottom, the trajectory is finished
                finished = true;
            }
        }

        Trajectory trajectory = new Trajectory(bounces);
        return trajectory;
    }

    /**
     * Return the soonest upcoming collision for a given ball.
     */
    private Collision calculateNextCollision(final Ball ball) {
        List<Collision> possibleCollisions = new ArrayList<Collision>();
        possibleCollisions.addAll(getPossibleBlockCollisions(ball));
        possibleCollisions.addAll(getPossibleBoundaryCollisions(ball));
        // Find the collision position closest to the ball, that is, the collision position for the next object the ball will collide with
        Collision nextCollision = Collections.min(possibleCollisions, new Comparator<Collision>() {
            @Override
            public int compare(Collision c1, Collision c2) {
                return Float.compare(c1.getPosition().subtract(ball.getPosition()).lengthSquared(), c2.getPosition().subtract(ball.getPosition()).lengthSquared());
            }
        });
        return nextCollision;
    }

    private List<Collision> getPossibleBlockCollisions(Ball ball) {
        List<Collision> blockCollisions = new ArrayList<Collision>();
        for (Block block : blocks) {
            if (block.isActive()) {
                Collision collision = findCollision(ball, block);
                if (collision != null) {
                    blockCollisions.add(collision);
                }
            }
        }
        return blockCollisions;
    }

    private List<Collision> getPossibleBoundaryCollisions(Ball ball) {
        List<Collision> boundaryCollisions = new ArrayList<Collision>();
        Collision boundaryCollision = findCollision(ball, this);
        if (boundaryCollision != null) {
            boundaryCollisions.add(boundaryCollision);
        }
        return boundaryCollisions;
    }


    /**
     * Find the point where the ball's line of movement intersects with the rectangular object, if any.
     * @return null if there is no collision point
     */
    public static Collision findCollision(Ball ball, Collideable collideable) {
        List<Line> edges = getPossibleCollisionEdges(ball, collideable);
        for (Line edge : edges) {
            if (edge.isVertical()) {
                Vector position = BeatOutMath.getIntersectionBetweenVerticalLineSegmentAndLine(edge.getStart().getX(), edge.getStart().getY(), edge.getEnd().getY(), ball.getDirectionLine(), true);
                return collideable.collideWith(position, true);
            } else if (edge.isHorizontal()) {
                Vector position = BeatOutMath.getIntersectionBetweenHorizontalLineSegmentAndLine(edge.getStart().getY(), edge.getStart().getX(), edge.getEnd().getX(), ball.getDirectionLine(), true);
                return collideable.collideWith(position, false);
            }
        }
        return null;
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
    public Collision collideWith(Vector collisionPoint, boolean verticalEdge) {
        return new BoundaryCollision(collisionPoint, verticalEdge);
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
