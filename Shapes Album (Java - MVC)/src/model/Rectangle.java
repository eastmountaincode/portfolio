package model;

/**
 * The type Rectangle.
 */
public class Rectangle extends AbstractShape {

  /**
   * Instantiates a new Rectangle.
   *
   * @param anchorPoint the anchor point
   * @param name        the name
   * @param colorRGB    the color rgb
   * @param width       the width
   * @param height      the height
   */
  public Rectangle(Point2D anchorPoint, String name, ColorRGB colorRGB, int width,
                   int height) {
    super(anchorPoint, name, "rectangle", colorRGB, width, height);
  }

  @Override
  public String toString() {
    String firstLine = "Name: " + this.getName() + "\n";
    String secondLine = "Type: " + this.getType() + "\n";
    String thirdLine = "Min corner: (" + this.getAnchorPoint().getX() + ", " + this.getAnchorPoint().getY()
            + "), Width: " + this.getWidth() + ", Height: " + this.getHeight() + ", Color: ("
            + this.getColor().getR() + ", " + this.getColor().getG() + ", "
            + this.getColor().getB() + ")";
    return firstLine + secondLine + thirdLine;
  }

  public Rectangle copy() {
    return new Rectangle(this.getAnchorPoint(), this.getName(), this.getColor(),
            this.getWidth(), this.getHeight());
  }

}
