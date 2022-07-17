import org.junit.Assert;
import org.junit.Test;

import model.Point2D;

/**
 * JUnit4 test suite for Point2D.
 */
public class TestPoint2D {
  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    Point2D p1 = new Point2D(1, 2);
    Assert.assertEquals(1, p1.getX(), 0.01);
    Assert.assertEquals(2, p1.getY(), 0.01);

    Point2D p2 = new Point2D(-59, 0);
    Assert.assertEquals(-59, p2.getX(), 0.01);
    Assert.assertEquals(0, p2.getY(), 0.01);
  }
}
