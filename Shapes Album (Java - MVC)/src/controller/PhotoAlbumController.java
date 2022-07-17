package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import model.IModel;
import model.Snapshot;
import util.FileParser;

/**
 * The controller class for the photo album.
 */
public class PhotoAlbumController implements Features {
  private IModel model;
  private int currentSnapshotIndex;

  /**
   * Instantiates a new Photo album controller.
   *
   * @param model      the model
   * @param parsedArgs the parsed args
   * @throws FileNotFoundException the file not found exception
   */
  public PhotoAlbumController(IModel model, HashMap<String, String> parsedArgs) throws FileNotFoundException {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }
    this.model = model;
    this.currentSnapshotIndex = 0;

    // As long as the parsed args are not empty, get the file from the "in" entry key
    if (!parsedArgs.isEmpty()) {
      InputStream file = null;
      try {
        file = new FileInputStream(parsedArgs.get("in"));
      } catch (FileNotFoundException e) {
        System.out.println("File " + parsedArgs.get("in") + " was not found");
        System.exit(1);
      }

      // File parser parses the input and makes adjustments to the model
      FileParser.parseFile(file, this.model);
    }

  }

  @Override
  public List<String> getAllAvailableSnapshotIDs() {
    List<String> idList = model.getSnapshotList()
            .stream().map(snapshot -> snapshot.getID())
            .collect(Collectors.toList());
    return idList;
  }

  @Override
  public Snapshot getSnapshotByID(String idToGet) {
    List<Snapshot> snapshotToGet = model.getSnapshotList()
            .stream().filter(snapshot -> snapshot.getID().equalsIgnoreCase(idToGet))
            .collect(Collectors.toList());
    return snapshotToGet.get(0);
  }

  @Override
  public int getCurrentSnapshotIndex() {
    return this.currentSnapshotIndex;
  }

  @Override
  public void setCurrentSnapshotIndex(int newIndex) {
    this.currentSnapshotIndex = newIndex;
  }
}
