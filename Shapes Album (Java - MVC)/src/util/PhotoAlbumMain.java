package util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import controller.PhotoAlbumController;

import model.IModel;

import model.ShapesPhotoAlbumModel;
import view.GraphicalView;
import view.IView;
import view.WebView;

import static java.lang.Integer.parseInt;

/**
 * Main entry point for the Photo Album.
 */
public class PhotoAlbumMain {
  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws IOException              the io exception
   * @throws IllegalArgumentException the illegal argument exception
   */
  public static void main(String[] args) throws IOException, IllegalArgumentException {
    // Parse the arguments in a hashmap
    HashMap<String, String> parsedArgs = CommandLineParser.parseCommandLineArgs(args);

    // Create model, and create a controller with model and args
    IModel model = new ShapesPhotoAlbumModel();
    PhotoAlbumController controller = new PhotoAlbumController(model, parsedArgs);

    // Create a view based on the args
    if (parsedArgs.get("viewType").equalsIgnoreCase("graphical")) {
      if (parsedArgs.containsKey("maxViewSize")) {
        List<String> maxSize = List.of(parsedArgs.get("maxViewSize").split("\\s+"));
        try {
          IView view = new GraphicalView(controller,
                  parseInt(maxSize.get(0)), parseInt(maxSize.get(1)));
        } catch (Exception e) {
          throw new IllegalArgumentException("Bad arguments for max view size");
        }
      } else {
        IView view = new GraphicalView(controller);
      }

    } else if (parsedArgs.get("viewType").equalsIgnoreCase("web")) {
      IView view = new WebView(controller, parsedArgs.get("out"));
    } else {
      throw new IllegalArgumentException("unknown view type");
    }
  }
}
