package model;

import java.util.List;

/**
 * A mock model for testing the controller..
 */
public class MockModel implements IModel {
  private StringBuilder log;
  private final int uniqueCode;

  /**
   * Instantiates a new Mock model.
   *
   * @param log        the log
   * @param uniqueCode the unique code
   */
  public MockModel(StringBuilder log, int uniqueCode) {
    this.log = log;
    this.uniqueCode = uniqueCode;
  }
  @Override
  public void addShape(IShape shapeToAdd) {
    log.append("Input: " + shapeToAdd.toString() + "\n");
    log.append("Unique Code: " + this.uniqueCode + "\n");

  }

  @Override
  public void removeShapeByName(String nameToRemove) {
    log.append("Input: " + nameToRemove + "\n");
    log.append("Unique Code: " + this.uniqueCode + "\n");

  }

  @Override
  public IShape getShapeByName(String nameToGet) {
    log.append("Input: " + nameToGet + "\n");
    log.append("Unique Code: " + this.uniqueCode + "\n");
    return null;
  }

  @Override
  public void moveShape(String nameToMove, Point2D newPoint) {
    log.append("Input: " + nameToMove + " " + newPoint.toString() + "\n");
    log.append("Unique Code: " + this.uniqueCode + "\n");

  }

  @Override
  public void stretchShapeX(String nameToStretch, int newWidth) {
    log.append("Input: " + nameToStretch + " " + newWidth + "\n");
    log.append("Unique Code: " + this.uniqueCode + "\n");

  }

  @Override
  public void stretchShapeY(String nameToStretch, int newHeight) {
    log.append("Input: " + nameToStretch + " " + newHeight + "\n");
    log.append("Unique Code: " + this.uniqueCode + "\n");

  }

  @Override
  public void changeShapeColor(String nameToChangeColor, ColorRGB newColor) {
    log.append("Input: " + nameToChangeColor + " " + newColor.toString() + "\n");
    log.append("Unique Code: " + this.uniqueCode + "\n");

  }

  @Override
  public void reset() {

  }

  @Override
  public void takeSnapshot(String description) {
    log.append("Input: " + description + "\n");
    log.append("Unique Code: " + this.uniqueCode + "\n");

  }

  @Override
  public List<Snapshot> getSnapshotList() {
    return null;
  }

  @Override
  public String displayShapes() {
    return null;
  }

  /**
   * Gets log.
   *
   * @return the log
   */
  public StringBuilder getLog() {
    return this.log;
  }
}
