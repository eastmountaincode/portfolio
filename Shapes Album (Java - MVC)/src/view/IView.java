package view;

import model.Snapshot;

/**
 * An interface for the view component of the MVC architecture.
 */
public interface IView {
  /**
   * Display an individual snapshot.
   */
  void displaySnapshot(Snapshot snapshot);

}
