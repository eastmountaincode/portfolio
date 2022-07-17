package model;

/**
 * The type Color rgb.
 */
public class ColorRGB {
  private final int R;
  private final int G;
  private final int B;

  /**
   * Instantiates a new Color rgb.
   *
   * @param R the r
   * @param G the g
   * @param B the b
   */
  public ColorRGB(int R, int G, int B) throws IllegalArgumentException {
    if (R > 255 || G > 255 || B > 255 || R < 0 || G < 0 || B < 0) {
      throw new IllegalArgumentException("RBG values must be 0-255 inclusive.");
    }
    this.R = R;
    this.G = G;
    this.B = B;
  }

  /**
   * Gets r.
   *
   * @return the r
   */
  public int getR() {
    return this.R;
  }

  /**
   * Gets g.
   *
   * @return the g
   */
  public int getG() {
    return this.G;
  }

  /**
   * Gets b.
   *
   * @return the b
   */
  public int getB() {
    return this.B;
  }

  @Override
  public String toString() {
    return "R: " + this.R + " G: " + this.G + " B: " + this.B;
  }
}
