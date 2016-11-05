import com.beatout.core.Ball;
import com.beatout.core.Block;
import com.beatout.core.GameBoard;
import com.beatout.math.Line;
import com.beatout.math.Vector;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


public class GameBoardTest {

    @Test
    public void testGetPossibleCollisionEdges() throws Exception {
        Ball ball = new Ball(10, new Vector(0, 0), new Vector(10, 10));
        Block block = new Block(new Vector(50, 50), new Vector(20,20));
        Set<Line> edges = new HashSet<Line>(GameBoard.getPossibleCollisionEdges(ball, block));
        Set<Line> expected = new HashSet<Line>();
        expected.add(block.getTopLine());
        expected.add(block.getLeftLine());
        assertEquals(edges, expected); // From top-left

        ball = new Ball(ball.getRadius(), new Vector(90, 60), new Vector(-1, -0.2f));
        edges = new HashSet<Line>(GameBoard.getPossibleCollisionEdges(ball, block));
        expected.clear();
        expected.add(block.getRightLine());
//        printEdges(edges, block);
        assertEquals(edges, expected); // From the right
    }

    private void printEdges(Collection<Line> edges, Block block) {
        for (Line l : edges) {
            if (l.equals(block.getTopLine())) {
                System.out.println("t");
            }
            if (l.equals(block.getBottomLine())) {
                System.out.println("b");
            }
            if (l.equals(block.getLeftLine())) {
                System.out.println("l");
            }
            if (l.equals(block.getRightLine())) {
                System.out.println("r");
            }
        }
    }
}