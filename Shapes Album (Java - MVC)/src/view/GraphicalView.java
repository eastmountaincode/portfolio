package view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import controller.Features;
import model.IShape;
import model.Snapshot;

/**
 * A graphical view.
 */
public class GraphicalView extends JFrame implements IView {
  private JLabel snapshotID;
  private JLabel snapshotDesc;

  private JPanel infoPanel;
  private ShapeZonePanel shapesPanel;
  private JPanel buttonPanel;
  private Features features;

  private final int BUFFER = 200;

  /**
   * Instantiates a new Graphical view.
   *
   * @param features the features
   */
  public GraphicalView(Features features) {
    // The Graphical View IS the JFrame

    // Set features
    this.features = features;

    // Exit program on frame close
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Set size
    this.setSize(1000, 1000);

    // Set background to a nice calm blue
    this.getContentPane().setBackground(new Color(185, 213,250));

    // Position the frame at the middle of the screen
    this.setLocationRelativeTo(null);

    // The FlowLayout class puts components in a row, sized at their preferred size.
    // If the horizontal space in the container is too small to put all the components
    // in one row, the FlowLayout class uses multiple rows.
    // https://stackoverflow.com/questions/5769032/how-to-make-flowlayout-add-components-to-the-top-of-a-frame-instead-of-the-cente
    this.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Prevent the main JFrame from being resized
    this.setResizable(false);

    // Create a panel in which to display the snapshot ID and description
    this.infoPanel = new InfoPanel();
    add(infoPanel);

    // Create a panel on which to put shapes
    this.shapesPanel = new ShapeZonePanel(800, 800);
    add(shapesPanel);

    // Create a panel for buttons
    this.buttonPanel = new ButtonsPanel(this.features, this);
    add(buttonPanel);

    // Labels for description and ID
    this.snapshotID = new JLabel("If you're reading this," +
            " there are no snapshot in the model! Try adding some snapshots, then" +
            " running the program again.");
    this.snapshotDesc = new JLabel();
    infoPanel.add(snapshotID);
    infoPanel.add(snapshotDesc);

    // Display first snapshot (assumes the model has at least one snapshot)
    List<String> snapshotIDs = features.getAllAvailableSnapshotIDs();
    if (!snapshotIDs.isEmpty()) {
      displaySnapshot(features.getSnapshotByID(
              features.getAllAvailableSnapshotIDs().get(
                      features.getCurrentSnapshotIndex())));
    }

    // Display the window.
    this.setVisible(true);

  }

  /**
   * Instantiates a new Graphical view, but this constructor is used when
   * we are given a max x and y dimension in the arguments.
   *
   * @param features the features
   * @param maxX     the max x
   * @param maxY     the max y
   */
  public GraphicalView(Features features, int maxX, int maxY) {
    // The Graphical View IS the JFrame

    // Set features
    this.features = features;

    // Exit program on frame close
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Set size
    this.setSize(maxX+BUFFER, maxY+BUFFER);

    // Set background to a nice calm blue
    this.getContentPane().setBackground(new Color(185, 213,250));

    // Position the frame at the middle of the screen
    this.setLocationRelativeTo(null);

    // The FlowLayout class puts components in a row, sized at their preferred size.
    // If the horizontal space in the container is too small to put all the components
    // in one row, the FlowLayout class uses multiple rows.
    // https://stackoverflow.com/questions/5769032/how-to-make-flowlayout-add-components-to-the-top-of-a-frame-instead-of-the-cente
    this.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Prevent the main JFrame from being resized
    this.setResizable(false);

    // Create a panel in which to display the snapshot ID and description
    this.infoPanel = new InfoPanel();
    add(infoPanel);

    // Create a panel on which to put shapes
    this.shapesPanel = new ShapeZonePanel(maxX, maxY);
    add(shapesPanel);

    // Create a panel for buttons
    this.buttonPanel = new ButtonsPanel(this.features, this);
    add(buttonPanel);

    // Labels for description and ID
    this.snapshotID = new JLabel("If you're reading this," +
            " there are no snapshot in the model! Try adding some snapshots, then" +
            " running the program again.");
    this.snapshotDesc = new JLabel();
    infoPanel.add(snapshotID);
    infoPanel.add(snapshotDesc);

    // Display first snapshot (assumes the model has at least one snapshot
    List<String> snapshotIDs = features.getAllAvailableSnapshotIDs();
    if (!snapshotIDs.isEmpty()) {
      displaySnapshot(features.getSnapshotByID(
              features.getAllAvailableSnapshotIDs().get(
                      features.getCurrentSnapshotIndex())));
    }

    // Display the window.
    this.setVisible(true);
  }


  @Override
  public void displaySnapshot(Snapshot snapshot) {
    snapshotID.setText("Snapshot ID: " + snapshot.getID());
    if (snapshot.getDescription().isBlank()) {
      snapshotDesc.setText("");
    }
    else {
      snapshotDesc.setText("Snapshot description: " + snapshot.getDescription());
    }
    List<IShape> shapeList = snapshot.getListOfShapes();
    shapesPanel.displayShapes(shapeList);

  }


}
