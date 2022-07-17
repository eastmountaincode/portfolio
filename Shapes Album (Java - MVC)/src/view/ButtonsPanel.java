package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import controller.Features;
import model.Snapshot;

/**
 * A JPanel for buttons.
 */
public class ButtonsPanel extends JPanel implements ActionListener {
  JButton selectButton;
  JButton prevButton;
  JButton nextButton;
  JButton exitButton;

  Features features;
  GraphicalView graphicalView;

  /**
   * Instantiates a new Buttons panel.
   *
   * @param featuresToAdd the features to add
   * @param graphicalView the graphical view
   * @throws IllegalArgumentException the illegal argument exception
   */
  public ButtonsPanel(Features featuresToAdd, GraphicalView graphicalView) throws IllegalArgumentException {
    if (featuresToAdd == null) {
      throw new IllegalArgumentException("Features is null!");
    }
    // Add features (the controller basically)
    this.features = featuresToAdd;

    // Add graphical view so we can communicate with other panels
    this.graphicalView = graphicalView;

    // Set background color and size
    this.setBackground(new Color(148, 172, 209));
    this.setBounds(0, 0, 1375, 98);
    this.setPreferredSize(new Dimension(1375, 98));

    // Add previous snapshot button
    prevButton = new JButton();
    prevButton.addActionListener(this);
    prevButton.setText("PREVIOUS SNAPSHOT");
    add(prevButton);

    // Add select button
    selectButton = new JButton();
    selectButton.addActionListener(this);
    selectButton.setText("SELECT SNAPSHOT");
    add(selectButton);

    // Add next snapshot button
    nextButton = new JButton();
    nextButton.addActionListener(this);
    nextButton.setText("NEXT SNAPSHOT");
    add(nextButton);

    // Add exit button
    exitButton = new JButton();
    exitButton.addActionListener(this);
    exitButton.setText("EXIT");
    add(exitButton);

    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // SELECT BUTTON
    if (e.getSource() == selectButton) {
      // Learn what snapshots are available to select
      List<String> idList = this.features.getAllAvailableSnapshotIDs();
      if (!(idList.size() < 1)) {
        String[] idArray = idList.toArray(new String[0]);

        // Display a popup with the combo box
        // NOTE: selectedValue is null if cancel is chosen.
        Object selectedValue = JOptionPane.showInputDialog(null,
                "Choose a snapshot to display", "SELECT SNAPSHOT",
                JOptionPane.INFORMATION_MESSAGE, null,
                idArray, idArray[0]);
        if (selectedValue != null) {
          Snapshot chosenSnapshot = features.getSnapshotByID(selectedValue.toString());
          graphicalView.displaySnapshot(chosenSnapshot);
        }
        features.setCurrentSnapshotIndex(Arrays.stream(idArray).toList().indexOf(selectedValue));
      }
    }
    // NEXT BUTTON
    if (e.getSource() == nextButton) {
      // Get the current index from the view
      int currentIndex = features.getCurrentSnapshotIndex();
      // Check if there is a next element
      if (currentIndex + 1 < features.getAllAvailableSnapshotIDs().size()) {
        String newSnapName = features.getAllAvailableSnapshotIDs().get(currentIndex + 1);
        features.setCurrentSnapshotIndex(currentIndex + 1);
        graphicalView.displaySnapshot(features.getSnapshotByID(newSnapName));
      }
    }

    // PREV BUTTON
    if (e.getSource() == prevButton) {
      // Get the current index from the view
      int currentIndex = features.getCurrentSnapshotIndex();
      // Check if there is a prev element
      if (currentIndex - 1 >= 0) {
        String newSnapName = features.getAllAvailableSnapshotIDs().get(currentIndex - 1);
        features.setCurrentSnapshotIndex(currentIndex - 1);
        graphicalView.displaySnapshot(features.getSnapshotByID(newSnapName));
      }
    }

    // EXIT BUTTON
    if (e.getSource() == exitButton) {
      int result = JOptionPane.showConfirmDialog(null,
              "Are you sure you want to exit?",
              "Exit confirmation",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE);
      if (result == 0) {
        System.exit(0);
      }
    }

  }
}

