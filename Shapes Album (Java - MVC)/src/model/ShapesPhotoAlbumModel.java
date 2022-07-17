package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The type Shapes photo album model.
 */
public class ShapesPhotoAlbumModel implements IModel {
  private List<IShape> listOfShapes;
  private List<Snapshot> snapshotList;

  /**
   * Instantiates a new Shapes photo album model.
   */
  public ShapesPhotoAlbumModel() {
    this.listOfShapes = new ArrayList<>();
    this.snapshotList = new ArrayList<>();
  }

  /**
   * Add shape.
   *
   * @param shapeToAdd the shape to add
   * @throws IllegalArgumentException the illegal argument exception
   */
  public void addShape(IShape shapeToAdd) throws IllegalArgumentException {
    // Check if the shape to add has a unique name
    for (IShape shape: this.listOfShapes) {
      if (shape.getName().equalsIgnoreCase(shapeToAdd.getName())) {
        throw new IllegalArgumentException("Shapes must have unique names.");
      }
    }
    // If we get here, we know the shapeToAdd has a unique name
    this.listOfShapes.add(shapeToAdd);
  }

  /**
   * Remove shape by name.
   *
   * @param nameToRemove the name to remove
   * @throws IllegalArgumentException the illegal argument exception
   */
  public void removeShapeByName(String nameToRemove) throws IllegalArgumentException {
    if (this.listOfShapes.stream().filter(iShape -> iShape.getName().equals(nameToRemove))
            .findFirst().isPresent()) {
      this.listOfShapes.removeIf(iShape -> iShape.getName().equalsIgnoreCase(nameToRemove));
    }
    else {
      throw new IllegalArgumentException("Name given not in list");
    }
  }

  /**
   * Gets shape by name.
   *
   * @param nameToGet the name to get
   * @return the shape by name
   * @throws IllegalArgumentException the illegal argument exception
   */
  public IShape getShapeByName(String nameToGet) throws IllegalArgumentException {
    for (IShape shape: this.listOfShapes) {
      if (shape.getName().equalsIgnoreCase(nameToGet)) {
        return shape;
      }
    }
    throw new IllegalArgumentException("Model does not currently have a shape with this name");
  }

  /**
   * Move shape.
   *
   * @param nameToMove the name to move
   * @param newPoint   the new point
   */
  public void moveShape(String nameToMove, Point2D newPoint) {
    // Get the shape based on the name
    IShape shapeToMove = this.getShapeByName(nameToMove);
    shapeToMove.move(newPoint);
  }

  /**
   * Stretch shape x.
   *
   * @param nameToStretch the name to stretch
   * @param newWidth       the new width
   */
  public void stretchShapeX(String nameToStretch, int newWidth) {
    // Get the shape based on the name
    IShape shapeToStretch = this.getShapeByName(nameToStretch);
    shapeToStretch.stretchX(newWidth);
  }

  /**
   * Stretch shape y.
   *
   * @param nameToStretch the name to stretch
   * @param newHeight       the new height
   */
  public void stretchShapeY(String nameToStretch, int newHeight) {
    // Get the shape based on the name
    IShape shapeToStretch = this.getShapeByName(nameToStretch);
    shapeToStretch.stretchY(newHeight);
  }

  /**
   * Change shape color.
   *
   * @param nameToChangeColor the name to change color
   * @param newColor          the new color
   */
  public void changeShapeColor(String nameToChangeColor, ColorRGB newColor) {
    // Get the shape based on the name
    IShape shapeToChangeColor = this.getShapeByName(nameToChangeColor);
    shapeToChangeColor.setColor(newColor);
  }

  /**
   * Reset.
   */
  public void reset() {
    this.listOfShapes = new ArrayList<>();
    this.snapshotList = new ArrayList<>();
  }

  /**
   * Take snapshot.
   *
   * @param description the description
   */
  // SOURCE: https://howtodoinjava.com/java/collections/arraylist/arraylist-clone-deep-copy/
  public void takeSnapshot(String description) {
    List<IShape> snapshotListClone = new ArrayList<>();
    Iterator<IShape> iterator = this.listOfShapes.iterator();
    while(iterator.hasNext()) {
      IShape shape = iterator.next();
      if (shape.getType().equalsIgnoreCase("rectangle")) {
        snapshotListClone.add(shape.copy());
      }
      else if (shape.getType().equalsIgnoreCase("oval")) {
        snapshotListClone.add(shape.copy());
      }
    }
    Snapshot newSnapshot = new Snapshot(description,
            snapshotListClone);
    this.snapshotList.add(newSnapshot);
  }

  /**
   * Display snapshots as a string.
   * Used for testing.
   *
   * @return the string
   */
  public String displaySnapshots() {
    if (this.snapshotList.isEmpty()) {
      return "Snapshot list is empty!";
    }

    String l1 = "Printing Snapshots\n";

    for (int i = 0; i < this.snapshotList.size(); i++) {
      if (i < this.snapshotList.size() - 1) {
        l1 += (this.snapshotList.get(i).toString() + "\n\n");
      }
      else {
        l1 += this.snapshotList.get(i).toString();
      }
    }
    return l1;
  }

  /**
   * Display shapes as a string.
   * Used for testing.
   *
   * @return the string
   */
  public String displayShapes() {
    if (this.listOfShapes.isEmpty()) {
      return "Shape list is empty!";
    }

    String l1 = "Printing Shapes\n";

    for (int i = 0; i < this.listOfShapes.size(); i++) {
      if (i < this.listOfShapes.size() - 1) {
        l1 += (this.listOfShapes.get(i).toString() + "\n\n");
      }
      else {
        l1 += this.listOfShapes.get(i).toString();
      }
    }
    return l1;
  }

  /**
   * Gets snapshot list.
   *
   * @return the snapshot list
   */
  public List<Snapshot> getSnapshotList () {
    return this.snapshotList;
  }








}
