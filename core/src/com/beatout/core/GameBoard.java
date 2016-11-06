package com.beatout.core;

import com.beatout.math.BeatOutMath;
import com.beatout.math.Vector;
import com.beatout.math.Line;

import java.util.*;

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
    public final static float BAT_SPEED = 1.4f;

    public static final float BALL_RADIUS = 10;//30;

    public GameBoard(float width, float height) {
        this.width = width;
        this.height = height;
        createTestLevel();
        float batY = height - BAT_HEIGHT - BAT_DISTANCE_TO_BOTTOM;
        paddle = new Paddle(new Vector(BAT_WIDTH, BAT_HEIGHT), new com.beatout.math.Line(0, batY, width, batY), BAT_SPEED);
        ball = new Ball(BALL_RADIUS, paddle.getPosition().add(paddle.getSize().getX()/2, -2*BALL_RADIUS), new Vector(-1, -1.7f));
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
        bounces.add(new PaddleCollision(simulatedBall.getPosition()));

        while (true) {
            Collision collision = calculateNextCollision(simulatedBall);
            bounces.add(collision);
            if (collision instanceof BoundaryCollision && collision.getDirection() == BALL_FROM_TOP) {
                break; // The ball returned to the bottom of the screen again
            }
            simulatedBall = new Ball(simulatedBall.getRadius(), collision.getPosition(), collision.getResultingDirection(simulatedBall.getDirection()));
        }

        Trajectory trajectory = new Trajectory(bounces);
        return trajectory;
    }

    /**
     * Return the soonest upcoming collision for a given ball.
     */ // TODO: Only calculate boundaryCollision if there are no block collisions (boundary is always further away than any block)
    private Collision calculateNextCollision(final Ball ball) {
        List<Collision> possibleCollisions = new ArrayList<Collision>();
        possibleCollisions.addAll(getPossibleBlockCollisions(ball));
        Collision boundaryCollision = getPossibleBoundaryCollision(ball);
        if (boundaryCollision != null) {
            possibleCollisions.add(boundaryCollision);
        }
        // Find the collision position closest to the ball, that is, the collision position for the next object the ball will collide with

        if (possibleCollisions.size() == 0) {
            return null; //TEST
        }

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

    private Collision getPossibleBoundaryCollision(Ball ball) {
        Line H = getPossibleHorizontalCollisionEdge(ball, this);
        Line V = getPossibleVerticalCollisionEdge(ball, this);
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
        // Handle bouncing inside rect
        if (ball.getRight() <= rect.getRight() && ball.getLeft() >= rect.getLeft() && ball.getTop() >= rect.getTop() && ball.getBottom() <= rect.getBottom()) {
            if (ball.getDirection().getY() < 0) {
                return rect.getTopLine();
            } else {
                return rect.getBottomLine();
            }
        }
        return null;
    }

    public static Line getPossibleVerticalCollisionEdge(Ball ball, RectBounded rect) {
        if (ball.getLeft() > rect.getRight() && ball.getDirection().getX() < 0) {
            return rect.getRightLine();
        } else if (ball.getRight() < rect.getLeft() && ball.getDirection().getX() > 0) {
            return rect.getLeftLine();
        }
        // Handle bouncing inside rect
        if (ball.getBottom() <= rect.getBottom() && ball.getTop() >= rect.getTop() && ball.getLeft() >= rect.getLeft() && ball.getRight() <= rect.getRight()) {
            if (ball.getDirection().getX() < 0) {
                return rect.getLeftLine();
            } else {
                return rect.getRightLine();
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
