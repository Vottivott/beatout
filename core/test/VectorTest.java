import com.beatout.math.Vector;
import org.junit.Test;

import static org.junit.Assert.*;


public class VectorTest {

    @Test
    public void testAdd() throws Exception {
        Vector v = new Vector(1,1);
        Vector v2 = v.add(new Vector(2,3)).add(1,2);
        assertTrue(v2.getX() == 4);
        assertTrue(v2.getY() == 6);
        assertFalse(v==v2);
    }

    @Test
    public void testSubtract() throws Exception {
        Vector v = new Vector(10,10);
        Vector v2 = v.subtract(new Vector(4,6)).subtract(1,2);

        assertTrue(v2.getX() == 5);
        assertTrue(v2.getY() == 2);

        assertFalse(v==v2);
    }

    @Test
    public void testLength() throws Exception {
        Vector v = new Vector(3,4);
        float vLength = v.length();

        assertTrue(vLength == 5);
    }

    @Test
    public void testLengthSquared() throws Exception {
        Vector v = new Vector(1,2);
        float lengthSquared = v.lengthSquared();

        assertTrue(lengthSquared == 5);
    }

    @Test
    public void testScale() throws Exception {
        Vector v = new Vector(1,2);
        Vector v2 = v.scale(2);

        assertTrue(v2.getX()==2);
        assertTrue(v2.getY()==4);

        assertFalse(v==v2);
    }

    @Test
    public void testNormalized() throws Exception {
        Vector v = new Vector(1,1);
        Vector v2 = v.normalize();

        assertEquals(1.0f, v2.length(), 0.0001f);

        assertFalse(v==v2);
    }
}