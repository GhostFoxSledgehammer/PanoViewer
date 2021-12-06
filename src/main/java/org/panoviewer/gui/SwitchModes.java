// License: GPL. For details, see LICENSE file.
package org.panoviewer.gui;

import org.panoviewer.gui.jogl.FlatPanel;
import org.panoviewer.gui.jogl.PanoramicPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.panoviewer.Mode;
import org.panoviewer.ModeRecorder;

import static org.panoviewer.utils.imageutils.isRatio;

/**
 * JPanel to switch modes between Flat and Panoramic.
 * @author - Rohan Babbar
*/

public class SwitchModes extends JPanel implements PropertyChangeListener {

  private final CardLayout cardLayout;
  private final FlatPanel flatPanel;
  private final PanoramicPanel panoramicPanel;

  private static SwitchModes instance;
  public static SwitchModes getInstance() {
    if (instance == null) {
      instance = new SwitchModes();
    }
    return instance;
  }

  private SwitchModes() {
    this.panoramicPanel = PanoramicPanel.getInstance();
    this.flatPanel = FlatPanel.getInstance();
    ModeRecorder.getInstance().addPropertyChangeListener(this);
    setBounds(50,50,400,400);
    setLayout(new CardLayout());
    add(Mode.Flat.toString(),flatPanel);
    add(Mode.Panoramic.toString(),panoramicPanel);
    cardLayout = (CardLayout)getLayout();
    cardLayout.show(this,Mode.Panoramic.toString());
  }

  /**
   * Sets the image to be displayed.
   *
   * @param image the image to be set.
   */
  public void setImage(BufferedImage image) {
    flatPanel.setImage(image);
    panoramicPanel.setImage(image);
    if(isRatio(image))
    {
      ModeRecorder.getInstance().setCurrentMode(Mode.Panoramic);
    }
    else {
      ModeRecorder.getInstance().setCurrentMode(Mode.Flat);

    }
  }

  public void switchingModes(Mode mode) {
    cardLayout.show(this,mode.toString());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("mode")) {
      Mode mode = (Mode) evt.getNewValue();
      switchingModes(mode);
    }
  }
}
