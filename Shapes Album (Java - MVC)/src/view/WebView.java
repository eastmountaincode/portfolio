package view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import controller.Features;
import model.IShape;
import model.Oval;
import model.Rectangle;
import model.Snapshot;

/**
 * A web view that generates an html file.
 */
public class WebView implements IView {
  private Features features;
  private FileWriter html;
  private File textFile;


  /**
   * Instantiates a new Web view.
   *
   * @param features      the features
   * @param nameOfOutFile the name of out file
   * @throws IOException the io exception
   */
  // SOURCE (for file writing)
  // https://stackoverflow.com/questions/25291770/why-is-filewriter-not-creating-a-new-file-filenotfoundexception
  // https://stackoverflow.com/questions/1525060/file-createnewfile-thowing-ioexception-no-such-file-or-directory
  public WebView(Features features, String nameOfOutFile) throws IOException {
    this.features = features;
    List<String> snapshotIDs = features.getAllAvailableSnapshotIDs();
    Path filePath = Paths.get(".." + File.separator + "resources" + File.separator + nameOfOutFile);
    this.textFile = new File(filePath.toString());
    this.html = new FileWriter(textFile);

    // If snapshot list is empty
    if (snapshotIDs.size() < 1) {
      html.write("<html>\n" +
              "<head>Photo Album</head>\n" +
              "<body>\n" +
              "\t<div>There are no snapshots to display.</div>\n" +
              "</body>\n" +
              "</html>");
      html.close();
    }
    // If snapshot list has at least one element...
    else {
      html.write("<html>\n" +
              "<h1>Photo Album</h1>\n" +
              "<body>");
      for (String snapshotID: snapshotIDs) {
        Snapshot snapshot = features.getSnapshotByID(snapshotID);
        List<IShape> shapeList = snapshot.getListOfShapes();
        // Display ID and description
        html.write("<h2>ID: " + snapshot.getID() +"</h2>");
        html.write("<h3>Description: " + snapshot.getDescription() +"</h3>");
        // draw view-box and blue delimiter line
        html.write("<div>");
        html.write("<svg width=\"15cm\" height=\"15cm\" viewBox=\"0 0 1000 1000\"" +
                " xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
        html.write("<rect x=\"1\" y=\"1\" width=\"998\" height=\"998\"\n" +
                "        \t\tfill=\"none\" stroke=\"blue\" stroke-width=\"2\"/>");
        if (!shapeList.isEmpty()) {
          for (IShape shape: shapeList) {
            // Rectangle
            if (shape instanceof Rectangle) {
              html.write("<rect x=\""+ shape.getAnchorPoint().getX()
                      +"\" y=\"" + shape.getAnchorPoint().getY()
                      + "\" width=\""+ shape.getWidth()
                      + "\" height=\""+ shape.getHeight()
                      +"\"\n" +
                      "\tfill=\"rgb(" + shape.getColor().getR() +
                      ", " + shape.getColor().getG() +
                      ", " + shape.getColor().getB() +
                      ")\" />");
            }
            // Circle
            if (shape instanceof Oval) {
              int xRadius = shape.getWidth()/2;
              int yRadius = shape.getHeight()/2;

              html.write("<ellipse cx=\""+ (shape.getAnchorPoint().getX() + xRadius)
                      +"\" cy=\"" + (shape.getAnchorPoint().getY() + yRadius)
                      + "\" rx=\""+ xRadius
                      + "\" ry=\""+ yRadius
                      +"\"\n" +
                      "\tfill=\"rgb(" + shape.getColor().getR() +
                      ", " + shape.getColor().getG() +
                      ", " + shape.getColor().getB() +
                      ")\" />");
            }
          }
        }
        html.write("</svg>");
        html.write("</div><br>");
      }

      html.write("</body>\n" +
              "</html>");
      html.close();
    }
  }

  @Override
  public void displaySnapshot(Snapshot snapshot) {}

  /**
   * Gets html lines.
   *
   * @return the html lines
   * @throws IOException the io exception
   */
  // SOURCE: https://mkyong.com/java/java-how-to-read-a-file/
  public String getHtmlLines() throws IOException {
    Stream<String> lines = Files.lines(Paths.get(textFile.toString()));

    String accum = "";
    List<String> list1 = lines.collect(Collectors.toList());
    for (String s: list1) {
      accum += s;
    }
    return accum;
  }

}
