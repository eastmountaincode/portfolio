import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.ColorRGB;
import model.Oval;
import model.Point2D;
import model.Rectangle;

/**
 * JUnit4 test suite for IShape.
 */
public class TestIShape {
  Point2D anchorPoint1;
  Point2D anchorPoint2;
  ColorRGB colorBlue;
  ColorRGB colorRed;
  Rectangle rectangle1;
  Oval oval1;

  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    anchorPoint1 = new Point2D(50, 100);
    anchorPoint2 = new Point2D(10, 20);
    colorBlue = new ColorRGB(0, 0, 1);
    colorRed = new ColorRGB(1, 0, 0);
    rectangle1 = new Rectangle(anchorPoint1, "blueRectangle",
            colorBlue, 10, 20);
    oval1 = new Oval(anchorPoint2, "redOval",
            colorRed, 25, 30);

  }

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    String expectedString = "Name: blueRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (50, 100), Width: 10, Height: 20, Color: (0, 0, 1)";
    Assert.assertEquals(expectedString, rectangle1.toString());
    expectedString = "Name: redOval\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 25, Y radius: 30, Color: (1, 0, 0)";
    Assert.assertEquals(expectedString, oval1.toString());
  }

  /**
   * Test get anchor point.
   */
  @Test
  public void testGetAnchorPoint() {
    Assert.assertEquals(50.0, rectangle1.getAnchorPoint().getX(), 0.001);
    Assert.assertEquals(100.0, rectangle1.getAnchorPoint().getY(), 0.001);
    Assert.assertEquals(10.0, oval1.getAnchorPoint().getX(), 0.001);
    Assert.assertEquals(20.0, oval1.getAnchorPoint().getY(), 0.001);
  }

  /**
   * Test get name.
   */
  @Test
  public void testGetName() {
    Assert.assertEquals("blueRectangle", rectangle1.getName());
    Assert.assertEquals("redOval", oval1.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlankName() {
    // test blank name
    Rectangle r1 = new Rectangle(anchorPoint1, "", colorRed, 10, 10);
    Assert.assertEquals("", r1.getName());
  }

  /**
   * Test get type.
   */
  @Test
  public void testGetType() {
    Assert.assertEquals("rectangle", rectangle1.getType());
    Assert.assertEquals("oval", oval1.getType());
  }

  /**
   * Test get color.
   */
  @Test
  public void testGetColor() {
    Assert.assertEquals(colorBlue, rectangle1.getColor());
    Assert.assertEquals(colorRed, oval1.getColor());
    ColorRGB weirdColor = new ColorRGB(1, 2, 3);
    Rectangle r1 = new Rectangle(anchorPoint1, "asf", weirdColor, 1, 1);
    Assert.assertEquals(1, r1.getColor().getR(), 0.001);
    Assert.assertEquals(2, r1.getColor().getG(), 0.001);
    Assert.assertEquals(3, r1.getColor().getB(), 0.001);
  }

  /**
   * Test set color.
   */
  @Test
  public void testSetColor() {
    Assert.assertEquals(colorBlue, rectangle1.getColor());
    Assert.assertEquals(colorRed, oval1.getColor());
    ColorRGB weirdColor = new ColorRGB(1, 2, 3);
    rectangle1.setColor(weirdColor);
    oval1.setColor(weirdColor);
    oval1.setColor(colorRed);
    Assert.assertEquals(weirdColor, rectangle1.getColor());
    Assert.assertEquals(colorRed, oval1.getColor());
  }

  /**
   * Test move.
   */
  @Test
  public void testMove() {
    Assert.assertEquals(50.0, rectangle1.getAnchorPoint().getX(), 0.001);
    Assert.assertEquals(100.0, rectangle1.getAnchorPoint().getY(), 0.001);
    Assert.assertEquals(10.0, oval1.getAnchorPoint().getX(), 0.001);
    Assert.assertEquals(20.0, oval1.getAnchorPoint().getY(), 0.001);
    rectangle1.move(new Point2D(500, 510));
    Assert.assertEquals(500.0, rectangle1.getAnchorPoint().getX(), 0.001);
    Assert.assertEquals(510.0, rectangle1.getAnchorPoint().getY(), 0.001);
    oval1.move(new Point2D(-10, 0));
    Assert.assertEquals(-10, oval1.getAnchorPoint().getX(), 0.001);
    Assert.assertEquals(0, oval1.getAnchorPoint().getY(), 0.001);
  }

  /**
   * Test stretch.
   */
  @Test
  public void testStretch() {
    String expectedString = "Name: blueRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (50, 100), Width: 10, Height: 20, Color: (0, 0, 1)";
    Assert.assertEquals(expectedString, rectangle1.toString());
    rectangle1.stretchX(200);
    rectangle1.stretchY(50);
    expectedString = "Name: blueRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (50, 100), Width: 200, Height: 50, Color: (0, 0, 1)";
    Assert.assertEquals(expectedString, rectangle1.toString());

    expectedString = "Name: redOval\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 25, Y radius: 30, Color: (1, 0, 0)";
    Assert.assertEquals(expectedString, oval1.toString());
    oval1.stretchX(200);
    oval1.stretchY(50);
    expectedString = "Name: redOval\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 200, Y radius: 50, Color: (1, 0, 0)";
    Assert.assertEquals(expectedString, oval1.toString());
  }

  /**
   * Test negative stretch percentage rectangle.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testNegativeStretchPercentageRectangle() {
    rectangle1.stretchY(-10);
  }

  /**
   * Test negative stretch percentage oval.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testNegativeStretchPercentageOval() {
    oval1.stretchX(-10);
  }

  /**
   * Test zero stretch percentage rectangle.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testZeroStretchPercentageRectangle() {
    rectangle1.stretchY(0);
  }

  /**
   * Test zero stretch percentage oval.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testZeroStretchPercentageOval() {
    oval1.stretchX(0);
  }
}
