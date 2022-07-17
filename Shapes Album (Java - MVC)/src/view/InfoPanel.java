package view;

import java.awt.*;

import javax.swing.*;

/**
 * A JPanel for information about the snapshot.
 */
public class InfoPanel extends JPanel {
  /**
   * Instantiates a new Info panel.
   */
  public InfoPanel() {
    this.setBackground(new Color(148, 172, 209));
    this.setBounds(0, 0, 1375, 50);
    this.setPreferredSize(new Dimension(1375, 50));
    this.setVisible(true);

  }
}
