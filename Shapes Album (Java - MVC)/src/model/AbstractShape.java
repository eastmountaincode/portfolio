package model;

/**
 * The type Abstract shape.
 */
public abstract class AbstractShape implements IShape {
  private Point2D anchorPoint;
  private final String name;
  private final String type;
  private ColorRGB colorRGB;
  private int width;
  private int height;

  /**
   * Instantiates a new Abstract shape.
   *
   * @param anchorPoint the anchor point
   * @param name        the name
   * @param type        the type
   * @param colorRGB    the color rgb
   * @throws IllegalArgumentException the illegal argument exception
   */
  public AbstractShape(Point2D anchorPoint, String name, String type, ColorRGB colorRGB,
                       int width, int height)
          throws IllegalArgumentException {
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name can't by empty");
    }
    this.anchorPoint = anchorPoint;
    this.name = name;
    this.type = type;
    this.colorRGB = colorRGB;
    this.width = width;
    this.height = height;
  }

  @Override
  public Point2D getAnchorPoint() {
    return this.anchorPoint;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public ColorRGB getColor() {
    return this.colorRGB;
  }

  @Override
  public void setColor(ColorRGB newColorRGB) {
    this.colorRGB = newColorRGB;
  }

  @Override
  public void move(Point2D newPoint) {
    this.anchorPoint = newPoint;
  }

  /**
   * Get width.
   * @return width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get height.
   * @return height
   */
  public int getHeight() {
    return height;
  }

  @Override
  public void stretchX(int newWidth) throws IllegalArgumentException {
    if (newWidth <= 0) {
      throw new IllegalArgumentException("New value must be greater than zero");
    }
    this.width = newWidth;
  }

  @Override
  public void stretchY(int newHeight) throws IllegalArgumentException {
    if (newHeight <= 0) {
      throw new IllegalArgumentException("New value must be greater than zero");
    }
    this.height = newHeight;
  }
}
