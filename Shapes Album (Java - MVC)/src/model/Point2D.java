package model;

/**
 * The type Point 2D.
 */
public class Point2D {
  private final int xCoord;
  private final int yCoord;

  /**
   * Instantiates a new Point 2 d.
   *
   * @param xCoord the x coordinate
   * @param yCoord the y coordinate
   */
  public Point2D(int xCoord, int yCoord) {
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  /**
   * Gets x.
   *
   * @return the x
   */
  public int getX() {
    return this.xCoord;
  }

  /**
   * Gets y.
   *
   * @return the y
   */
  public int getY() {
    return this.yCoord;
  }

  @Override
  public String toString() {
    return "X: " + this.xCoord + " Y: " + this.yCoord;
  }
}
