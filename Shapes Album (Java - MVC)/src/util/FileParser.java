package util;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.ColorRGB;
import model.IModel;
import model.Oval;
import model.Point2D;
import model.Rectangle;

import static java.lang.Integer.parseInt;

/**
 * A file parser to read in commands for the model.
 */
public class FileParser {
  /**
   * Parse file.
   *
   * @param file  the file
   * @param model the model
   * @throws IllegalArgumentException the illegal argument exception
   */
  public static void parseFile(InputStream file, IModel model) throws IllegalArgumentException {
    Scanner scanner = new Scanner(file);
    // While there are more lines...
    while (scanner.hasNext()) {
      String text = scanner.nextLine();
      // if the lin starts with #, it's a comment
      if (text.startsWith("#") || text.isBlank()) {
        continue;
      }
      else {
        List<String> split = Arrays.asList(text.trim().split("\\s+"));
        // COMMANDS
        // Create shape
        if (split.get(0).equalsIgnoreCase("shape")) {
          if (split.size() != 10) {
            throw new IllegalArgumentException("Wrong number of commands");
          }
          int xPos = parseInt(split.get(3));
          int yPos = parseInt(split.get(4));
          int R = parseInt(split.get(7));
          int G = parseInt(split.get(8));
          int B = parseInt(split.get(9));
          int width = parseInt(split.get(5));
          int height = parseInt(split.get(6));
          if (split.get(2).equalsIgnoreCase("rectangle")) {
            try {
              model.addShape(new Rectangle(new Point2D(xPos, yPos),
                      split.get(1), new ColorRGB(R, G, B),
                      width, height));
            }
            catch (Exception e){
              throw new IllegalArgumentException("Bad commands");
            }
          }
          else if (split.get(2).equalsIgnoreCase("oval")) {
            try {
              model.addShape(new Oval(new Point2D(xPos, yPos),
                      split.get(1), new ColorRGB(R, G, B),
                      width, height));
            }
            catch (Exception e) {
              throw new IllegalArgumentException("Bad commands");
            }
          }
        }
        // Take snapshot
        if (split.get(0).equalsIgnoreCase("snapshot")) {
          // If no description
          if (split.size() == 1) {
            model.takeSnapshot("");
          }
          else {
            // If description
            String description = text.replaceAll("(?i)snapshot", "").trim();
            model.takeSnapshot(description);
          }
        }
        // Resize shape
        if (split.get(0).equalsIgnoreCase("resize")) {
          if (split.size() != 4) {
            throw new IllegalArgumentException("Wrong number of commands");
          }
          try {
            model.stretchShapeX(split.get(1), parseInt(split.get(2)));
            model.stretchShapeY(split.get(1), parseInt(split.get(3)));
          }
          catch (Exception e) {
            throw new IllegalArgumentException("Bad commands");
          }
        }
        // Move shape
        if (split.get(0).equalsIgnoreCase("move")) {
          if (split.size() != 4) {
            throw new IllegalArgumentException("Wrong number of commands");
          }
          try {
            model.moveShape(split.get(1),
                    new Point2D(parseInt(split.get(2)),
                            parseInt(split.get(3))));
          }
          catch (Exception e) {
            throw new IllegalArgumentException("Bad commands");
          }
        }
        // Change color
        if (split.get(0).equalsIgnoreCase("color")) {
          if (split.size() != 5) {
            throw new IllegalArgumentException("Wrong number of commands");
          }
          try {
            model.changeShapeColor(split.get(1),
                    new ColorRGB(parseInt(split.get(2)),
                            parseInt(split.get(3)),
                            parseInt(split.get(4))));
          }
          catch (Exception e) {
            throw new IllegalArgumentException("Bad commands");
          }
        }
        // Remove shape
        if (split.get(0).equalsIgnoreCase("remove")) {
          if (split.size() != 2) {
            throw new IllegalArgumentException("Wrong number of commands");
          }
          try {
            model.removeShapeByName(split.get(1));
          }
          catch (Exception e) {
            throw new IllegalArgumentException("Bad commands");
          }
        }

      }
    }
  }
}
