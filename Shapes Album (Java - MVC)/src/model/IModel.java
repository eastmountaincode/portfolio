package model;

import java.util.List;

public interface IModel {
  void addShape(IShape shapeToAdd);
  void removeShapeByName(String nameToRemove);
  IShape getShapeByName(String nameToGet);
  void moveShape(String nameToMove, Point2D newPoint);
  void stretchShapeX(String nameToStretch, int newWidth);
  void stretchShapeY(String nameToStretch, int newHeight);
  void changeShapeColor(String nameToChangeColor, ColorRGB newColor);
  void reset();
  void takeSnapshot(String description);
  List<Snapshot> getSnapshotList();

  String displayShapes();
}
