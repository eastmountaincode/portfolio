import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

import controller.PhotoAlbumController;
import model.MockModel;

/**
 * A JUnit test suite for PhotoAlbumController.
 */
public class TestController {

  /**
   * Test that the mock model's log is correct after passing args to controller.
   *
   * @throws FileNotFoundException the file not found exception
   */
  @Test
  public void testLogBasic() throws FileNotFoundException {
    MockModel model = new MockModel(new StringBuilder(), 123456);
    HashMap<String, String> args = new HashMap<String, String>();
    args.put("in", "demo_input.txt");
    args.put("v", "graphical");
    PhotoAlbumController controller = new PhotoAlbumController(model, args);
    StringBuilder log = model.getLog();
    String expected = "Input: Name: myrect\n" +
            "Type: rectangle\n" +
            "Min corner: (200, 200), Width: 50, Height: 100, Color: (255, 0, 0)\n" +
            "Unique Code: 123456\n" +
            "Input: Name: myoval\n" +
            "Type: oval\n" +
            "Center: (500, 100), X radius: 60, Y radius: 30, Color: (0, 255, 1)\n" +
            "Unique Code: 123456\n" +
            "Input: After first selfie\n" +
            "Unique Code: 123456\n" +
            "Input: myrect X: 300 Y: 200\n" +
            "Unique Code: 123456\n" +
            "Input: myrect 25\n" +
            "Unique Code: 123456\n" +
            "Input: myrect 100\n" +
            "Unique Code: 123456\n" +
            "Input: myrect X: 100 Y: 300\n" +
            "Unique Code: 123456\n" +
            "Input: 2nd selfie\n" +
            "Unique Code: 123456\n" +
            "Input: myrect R: 0 G: 0 B: 255\n" +
            "Unique Code: 123456\n" +
            "Input: myoval X: 500 Y: 400\n" +
            "Unique Code: 123456\n" +
            "Input: \n" +
            "Unique Code: 123456\n" +
            "Input: myrect\n" +
            "Unique Code: 123456\n" +
            "Input: Selfie after removing the rectangle from the picture\n" +
            "Unique Code: 123456\n";
    Assert.assertEquals(expected, log.toString());
  }

  /**
   * Test that the mock model's log is correct when args are passed to controller with blank file.
   *
   * @throws FileNotFoundException the file not found exception
   */
  @Test
  public void testLogBlank() throws FileNotFoundException {
    MockModel model = new MockModel(new StringBuilder(), 123456);
    HashMap<String, String> args = new HashMap<String, String>();
    args.put("in", "demo_input_blank_model.txt");
    args.put("v", "graphical");
    PhotoAlbumController controller = new PhotoAlbumController(model, args);
    StringBuilder log = model.getLog();
    String expected = "Input: Name: myrect\n" +
            "Type: rectangle\n" +
            "Min corner: (200, 200), Width: 50, Height: 100, Color: (255, 0, 0)\n" +
            "Unique Code: 123456\n" +
            "Input: Name: myoval\n" +
            "Type: oval\n" +
            "Center: (500, 100), X radius: 60, Y radius: 30, Color: (0, 255, 1)\n" +
            "Unique Code: 123456\n" +
            "Input: myrect X: 300 Y: 200\n" +
            "Unique Code: 123456\n" +
            "Input: myrect 25\n" +
            "Unique Code: 123456\n" +
            "Input: myrect 100\n" +
            "Unique Code: 123456\n" +
            "Input: myrect X: 100 Y: 300\n" +
            "Unique Code: 123456\n" +
            "Input: myrect R: 0 G: 0 B: 255\n" +
            "Unique Code: 123456\n" +
            "Input: myoval X: 500 Y: 400\n" +
            "Unique Code: 123456\n" +
            "Input: myrect\n" +
            "Unique Code: 123456\n";
    Assert.assertEquals(expected, log.toString());

  }
}
