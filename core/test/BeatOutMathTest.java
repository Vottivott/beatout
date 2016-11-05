import com.beatout.math.Line;
import com.beatout.math.Vector;
import static com.beatout.math.BeatOutMath.*;
import org.junit.Test;

import static org.junit.Assert.*;


public class BeatOutMathTest {

    Line diagonalLine = new Line(1,1,3,3);
    Line slopedLine = new Line(0,0,2,1);


    @Test
    public void testGetIntersectionBetweenHorizontalLineSegmentAndLine() throws Exception {
        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(0, 1, 2, slopedLine, false));
        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(0, 1, 2, slopedLine, true));
        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(0, -2, -1, slopedLine, false));
        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(0, -2, -1, slopedLine, true));
        assertEquals(new Vector(0,0), getIntersectionBetweenHorizontalLineSegmentAndLine(0, 0, 1, slopedLine, false));
        assertEquals(new Vector(0,0), getIntersectionBetweenHorizontalLineSegmentAndLine(0, 0, 1, slopedLine, true));
        assertEquals(new Vector(0,0), getIntersectionBetweenHorizontalLineSegmentAndLine(0, -1, 0, slopedLine, false));
        assertEquals(new Vector(0,0), getIntersectionBetweenHorizontalLineSegmentAndLine(0, -1, 0, slopedLine, true));

        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(1, 0, 1, slopedLine, false));
        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(1, 0, 1, slopedLine, true));
        assertEquals(new Vector(2,1), getIntersectionBetweenHorizontalLineSegmentAndLine(1, 1, 2, slopedLine, false));
        assertEquals(new Vector(2,1), getIntersectionBetweenHorizontalLineSegmentAndLine(1, 1, 2, slopedLine, true));
        assertEquals(new Vector(2,1), getIntersectionBetweenHorizontalLineSegmentAndLine(1, 2, 3, slopedLine, false));
        assertEquals(new Vector(2,1), getIntersectionBetweenHorizontalLineSegmentAndLine(1, 2, 3, slopedLine, true));
        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(1, 3, 4, slopedLine, false));
        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(1, 3, 4, slopedLine, true));
        assertEquals(new Vector(-2, -1), getIntersectionBetweenHorizontalLineSegmentAndLine(-1, -5, 5, slopedLine, false));
        assertNull(getIntersectionBetweenHorizontalLineSegmentAndLine(-1, -5, 5, slopedLine, true));
    }

    @Test
    public void testGetIntersectionBetweenVerticalLineSegmentAndLine() throws Exception {
        assertNull(getIntersectionBetweenVerticalLineSegmentAndLine(0, 1, 2, diagonalLine, false));
        assertEquals(new Vector(0,0), getIntersectionBetweenVerticalLineSegmentAndLine(0, 0, 1, diagonalLine, false));
        assertEquals(new Vector(0,0), getIntersectionBetweenVerticalLineSegmentAndLine(0, 0, 0.5f, diagonalLine, false));
        assertNull(getIntersectionBetweenVerticalLineSegmentAndLine(0, 0, 0.5f, diagonalLine, true));
        assertNull(getIntersectionBetweenVerticalLineSegmentAndLine(1, 2, 3, diagonalLine, false));
        assertEquals(new Vector(1,1), getIntersectionBetweenVerticalLineSegmentAndLine(1, 1, 3, diagonalLine, false));
        assertNull(getIntersectionBetweenVerticalLineSegmentAndLine(3, 1, 2, diagonalLine, false));
    }

    @Test
    public void testGetIntersectionBetweenVerticalLineAndLine() throws Exception {
        assertEquals(new Vector(0,0), getIntersectionBetweenVerticalLineAndLine(0, diagonalLine, false));
        assertEquals(new Vector(1,1), getIntersectionBetweenVerticalLineAndLine(1, diagonalLine, false));
        assertEquals(new Vector(2,2), getIntersectionBetweenVerticalLineAndLine(2, diagonalLine, false));
        assertEquals(new Vector(3,3), getIntersectionBetweenVerticalLineAndLine(3, diagonalLine, false));
        assertEquals(new Vector(4,4), getIntersectionBetweenVerticalLineAndLine(4, diagonalLine, false));
        assertNull(getIntersectionBetweenVerticalLineAndLine(0, diagonalLine, true));
        assertEquals(new Vector(1,1), getIntersectionBetweenVerticalLineAndLine(1, diagonalLine, true));
    }
}