import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.ColorRGB;
import model.IShape;
import model.Oval;
import model.Point2D;
import model.Rectangle;
import model.ShapesPhotoAlbumModel;

/**
 * JUnit4 test suite for ShapesPhotoAlbumModel.
 */
public class TestShapesPhotoAlbumModel {
  ShapesPhotoAlbumModel model;

  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    model = new ShapesPhotoAlbumModel();
  }

  /**
   * Test add shape.
   */
  @Test
  public void testAddShape() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "blueOval", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    Rectangle testRectangle = (Rectangle) model.getShapeByName("redRectangle");
    String expectedString = "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (10, 0, 0)";
    Assert.assertEquals(expectedString, testRectangle.toString());
    Oval testOval = (Oval) model.getShapeByName("blueOval");
    expectedString = "Name: blueOval\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)";
    Assert.assertEquals(expectedString, testOval.toString());
  }

  /**
   * Test add shape with a blank name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddShapeBlankName() {
    Oval o1 = new Oval(new Point2D(1, 2), "",
            new ColorRGB(1, 2, 3), 10 ,20);
  }

  /**
   * Test remove shape by name.
   */
  @Test
  public void testRemoveShapeByName() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "blueOval", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    String expectedString = "Printing Shapes\n" +
            "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (10, 0, 0)\n" +
            "\n" +
            "Name: blueOval\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)";
    Assert.assertEquals(expectedString, model.displayShapes());
    model.removeShapeByName("redRectangle");
    expectedString = "Printing Shapes\n" +
            "Name: blueOval\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)";
    Assert.assertEquals(expectedString, model.displayShapes());
    model.removeShapeByName("blueOval");
    Assert.assertEquals("Shape list is empty!", model.displayShapes());
  }

  /**
   * Test get shape by name.
   */
  @Test
  public void testGetShapeByName() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    Rectangle testRectangle = (Rectangle) model.getShapeByName("redRectangle");
    String expectedString = "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (10, 0, 0)";
    Assert.assertEquals(expectedString, testRectangle.toString());
    Oval testOval = (Oval) model.getShapeByName("$$$");
    expectedString = "Name: $$$\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)";
    Assert.assertEquals(expectedString, testOval.toString());
  }

  /**
   * Test get shape by name shape after that shape has been removed.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testGetShapeByNameShapeAfterRemoval() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.removeShapeByName("redRectangle");
    model.removeShapeByName("$$$");
    IShape obj1 = model.getShapeByName("$$$");
  }

  /**
   * Test get shape by name, where it is try to get a non-existant shape.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testGetShapeByNameNonexistantShape() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);

    IShape obj1 = model.getShapeByName("asdfasoief");
  }

  /**
   * Test move shape.
   */
  @Test
  public void testMoveShape() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.moveShape("redRectangle", new Point2D(50, 500));
    String expectedString = "Printing Shapes\n" +
            "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (50, 500), Width: 30, Height: 40, Color: (10, 0, 0)\n" +
            "\n" +
            "Name: $$$\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)";
    Assert.assertEquals(expectedString, model.displayShapes());
  }

  /**
   * Test move non-existant shape.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testMoveNonexistantShape() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.moveShape("redSpectangle", new Point2D(50, 500));

  }

  /**
   * Test stretch shape x.
   */
  @Test
  public void testStretchShapeX() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.stretchShapeX("redRectangle", 200);
    model.stretchShapeX("$$$", 50);
    String expectedString = "Printing Shapes\n" +
            "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 200, Height: 40, Color: (10, 0, 0)\n" +
            "\n" +
            "Name: $$$\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 50, Y radius: 13, Color: (0, 0, 10)";
    Assert.assertEquals(expectedString, model.displayShapes());
  }

  /**
   * Test stretch shape y.
   */
  @Test
  public void testStretchShapeY() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.stretchShapeY("redRectangle", 200);
    model.stretchShapeY("$$$", 50);
    String expectedString = "Printing Shapes\n" +
            "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 200, Color: (10, 0, 0)\n" +
            "\n" +
            "Name: $$$\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 50, Color: (0, 0, 10)";
    Assert.assertEquals(expectedString, model.displayShapes());
  }

  /**
   * Test change shape color.
   */
  @Test
  public void testChangeShapeColor() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.changeShapeColor("redRectangle", new ColorRGB(5, 5, 5));
    String expectedString = "Printing Shapes\n" +
            "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (5, 5, 5)\n" +
            "\n" +
            "Name: $$$\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)";
    Assert.assertEquals(expectedString, model.displayShapes());
  }

  /**
   * Test reset.
   */
  @Test
  public void testReset() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.changeShapeColor("redRectangle", new ColorRGB(5, 5, 5));
    model.takeSnapshot("First snap");
    model.reset();
    Assert.assertEquals("Snapshot list is empty!", model.displaySnapshots());
    Assert.assertEquals("Shape list is empty!", model.displayShapes());

  }

  /**
   * Test take snapshot.
   */
  @Test
  public void testTakeSnapshot() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.changeShapeColor("redRectangle", new ColorRGB(5, 5, 5));
    model.takeSnapshot("First snap");
    Rectangle greenRectangle = new Rectangle(new Point2D(10, 20),
            "greenRectangle", new ColorRGB(0, 10, 0), 30, 40);
    model.addShape(greenRectangle);
    model.takeSnapshot("Second snap");
    String expectedString = "Printing Snapshots\n" +
            "Snapshot ID: " + model.getSnapshotList().get(0).getID() + "\n" +
            "Timestamp: " + model.getSnapshotList().get(0).getTimestamp() + "\n" +
            "Description: First snap\n" +
            "Shape Information:\n" +
            "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (5, 5, 5)\n" +
            "\n" +
            "Name: $$$\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)\n" +
            "\n" +
            "Snapshot ID: " + model.getSnapshotList().get(1).getID() + "\n" +
            "Timestamp: " + model.getSnapshotList().get(1).getTimestamp() + "\n" +
            "Description: Second snap\n" +
            "Shape Information:\n" +
            "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (5, 5, 5)\n" +
            "\n" +
            "Name: $$$\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)\n" +
            "\n" +
            "Name: greenRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (0, 10, 0)";
    Assert.assertEquals(expectedString, model.displaySnapshots());
  }

  /**
   * Test display shapes.
   */
  @Test
  public void testDisplayShapes() {
    Rectangle redRectangle = new Rectangle(new Point2D(10, 20),
            "redRectangle", new ColorRGB(10, 0, 0), 30, 40);
    Oval blueOval = new Oval(new Point2D(10, 20),
            "$$$", new ColorRGB(0, 0, 10), 12, 13);
    model.addShape(redRectangle);
    model.addShape(blueOval);
    model.changeShapeColor("redRectangle", new ColorRGB(5, 5, 5));
    Rectangle greenRectangle = new Rectangle(new Point2D(10, 20),
            "greenRectangle", new ColorRGB(0, 10, 0), 30, 40);
    model.addShape(greenRectangle);
    String expectedString = "Printing Shapes\n" +
            "Name: redRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (5, 5, 5)\n" +
            "\n" +
            "Name: $$$\n" +
            "Type: oval\n" +
            "Center: (10, 20), X radius: 12, Y radius: 13, Color: (0, 0, 10)\n" +
            "\n" +
            "Name: greenRectangle\n" +
            "Type: rectangle\n" +
            "Min corner: (10, 20), Width: 30, Height: 40, Color: (0, 10, 0)";
    Assert.assertEquals(expectedString, model.displayShapes());

  }

}
