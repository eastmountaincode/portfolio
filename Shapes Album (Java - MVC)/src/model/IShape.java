package model;

/**
 * The interface Shape.
 */
public interface IShape {
  /**
   * Gets anchor point.
   *
   * @return the anchor point
   */
  Point2D getAnchorPoint();

  /**
   * Gets name.
   *
   * @return the name
   */
  String getName();

  /**
   * Gets type.
   *
   * @return the type
   */
  String getType();

  /**
   * Gets color.
   *
   * @return the color
   */
  ColorRGB getColor();

  /**
   * Sets color.
   *
   * @param newColorRGB the new color rgb
   */
  void setColor(ColorRGB newColorRGB);

  /**
   * Set new width.
   *
   * @param newWidth the percent
   */
  void stretchX(int newWidth);

  /**
   * Set new height.
   *
   * @param newHeight the percent
   */
  void stretchY(int newHeight);

  /**
   * Move a shape to a new anchor point.
   *
   * @param newPoint the new point
   */
  void move(Point2D newPoint);

  /**
   * Get width.
   * @return width
   */
  int getWidth();

  /**
   * Get height.
   * @return height
   */
  int getHeight();

  IShape copy();
}
