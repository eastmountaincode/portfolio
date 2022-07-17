import org.junit.Assert;
import org.junit.Test;

import model.ColorRGB;

/**
 * The type Test color rgb.
 */
public class TestColorRGB {
  /**
   * Test constructor.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testBadConstructor() {
    ColorRGB c1 = new ColorRGB(-1, 0, 3);
    Assert.assertEquals(-1, c1.getR(), 0.001);
    Assert.assertEquals(0, c1.getG(), 0.001);
    Assert.assertEquals(3, c1.getB(), 0.001);
  }

  @Test
  public void testGoodConstructor() {
    ColorRGB c1 = new ColorRGB(255, 0, 3);
    Assert.assertEquals(255, c1.getR(), 0.001);
    Assert.assertEquals(0, c1.getG(), 0.001);
    Assert.assertEquals(3, c1.getB(), 0.001);
  }

}
