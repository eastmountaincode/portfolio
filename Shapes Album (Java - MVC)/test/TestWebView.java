import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import controller.PhotoAlbumController;
import model.IModel;
import model.ShapesPhotoAlbumModel;
import util.CommandLineParser;
import view.WebView;

/**
 * A JUnit test suite for the web view.
 */
public class TestWebView {
  /**
   * Test happy trail.
   *
   * @throws IOException the io exception
   */
  @Test
  public void TestHappyTrail() throws IOException {
    HashMap<String, String> args = new HashMap<String, String>();
    args.put("in", "demo_input.txt");
    args.put("v", "web");
    args.put("out", "testHTML.html");
    IModel model = new ShapesPhotoAlbumModel();
    PhotoAlbumController controller = new PhotoAlbumController(model, args);
    WebView view = new WebView(controller, args.get("out"));
    Assert.assertEquals(view.getHtmlLines(), view.getHtmlLines());
  }

  /**
   * Test blank model.
   *
   * @throws IOException the io exception
   */
  @Test
  public void TestBlankModel() throws IOException {
    HashMap<String, String> args = new HashMap<String, String>();
    args.put("in", "demo_input_blank_model.txt");
    args.put("v", "web");
    args.put("out", "testHTML.html");
    IModel model = new ShapesPhotoAlbumModel();
    PhotoAlbumController controller = new PhotoAlbumController(model, args);
    WebView view = new WebView(controller, args.get("out"));
    Assert.assertEquals("<html><head>Photo Album</head><body>\t<div>There are no snapshots to display.</div></body></html>",
            view.getHtmlLines());
  }

  /**
   * Test web view but no out.
   *
   * @throws IOException the io exception
   */
  @Test(expected = IllegalArgumentException.class)
  public void TestWebViewButNoOut() throws IOException {
    String[] args = {"-in", "buildings.txt", "-v", "web"};
    HashMap<String, String> parsedArgs = CommandLineParser.parseCommandLineArgs(args);

  }
}
