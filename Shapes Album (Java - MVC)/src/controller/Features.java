package controller;

import java.util.List;

import model.Snapshot;

/**
 * The interface Features.
 */
public interface Features {
  /**
   * Gets all available snapshot IDs.
   *
   * @return a list of all available snapshot IDs
   */
  List<String> getAllAvailableSnapshotIDs();

  /**
   * Gets snapshot by id.
   *
   * @param idToGet the id of the snapshot to get
   * @return a snapshot
   */
  Snapshot getSnapshotByID(String idToGet);

  /**
   * Gets current snapshot index.
   *
   * @return the current snapshot index
   */
  int getCurrentSnapshotIndex();

  /**
   * Set current snapshot index.
   *
   * @param newIndex the new index
   */
  void setCurrentSnapshotIndex(int newIndex);
}
