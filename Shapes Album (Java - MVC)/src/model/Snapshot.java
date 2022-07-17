package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The type Snapshot.
 */
public class Snapshot {
  private final String ID;
  private final String timestamp;
  private final String description;
  private final List<IShape> listOfShapes;
  // Source: https://www.javatpoint.com/java-get-current-date
  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * Instantiates a new Snapshot.
   *
   * @param description  the description
   * @param listOfShapes the list of shapes
   */
  public Snapshot(String description, List<IShape> listOfShapes) {
    LocalDateTime now = LocalDateTime.now();
    this.timestamp = dtf.format(now);
    this.ID = now.toString();
    this.description = description;
    this.listOfShapes = listOfShapes;
  }

  @Override
  public String toString() {
    String l1 = "Snapshot ID: " + this.ID + "\n";
    String l2 = "Timestamp: " + this.timestamp + "\n";
    String l3 = "Description: " + this.description + "\n";
    String l4 = "Shape Information:\n";
    String l5 = "";

    for (int i = 0; i < this.listOfShapes.size(); i++) {
      if (i < this.listOfShapes.size() - 1) {
        l5 += this.listOfShapes.get(i).toString() + "\n\n";
      }
      else {
        l5 += this.listOfShapes.get(i).toString();
      }
    }
    return l1 + l2 + l3 + l4 + l5;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public String getID() {
    return this.ID;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public String getTimestamp() {
    return this.timestamp;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() { return this.description; }

  /**
   * Get list of shapes.
   * @return list of shapes
   */
  public List<IShape> getListOfShapes() { return this.listOfShapes; }

}
