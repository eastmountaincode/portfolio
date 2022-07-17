package model;

/**
 * The type Oval.
 */
public class Oval extends AbstractShape {
  private int width;
  private int height;

  /**
   * Instantiates a new Oval.
   *
   * @param anchorPoint the anchor point
   * @param name        the name
   * @param colorRGB    the color rgb
   * @param width     the width
   * @param height     the height
   */
  public Oval(Point2D anchorPoint, String name, ColorRGB colorRGB,
              int width, int height) {
    super(anchorPoint, name, "oval", colorRGB, width, height);
  }

  @Override
  public String toString() {
    String firstLine = "Name: " + this.getName() + "\n";
    String secondLine = "Type: " + this.getType() + "\n";
    String thirdLine = "Center: (" + this.getAnchorPoint().getX() + ", " + this.getAnchorPoint().getY()
            + "), X radius: " + this.getWidth() + ", Y radius: " + this.getHeight() + ", Color: ("
            + this.getColor().getR() + ", " + this.getColor().getG() + ", "
            + this.getColor().getB() + ")";
    return firstLine + secondLine + thirdLine;
  }

  public Oval copy() {
    return new Oval(this.getAnchorPoint(), this.getName(), this.getColor(),
            this.getWidth(), this.getHeight());
  }

}
