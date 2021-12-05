// License: GPL. For details, see LICENSE file.
package PanoViewer;

import java.awt.Window;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Look and Feel Class
 * @author Rohan Babbar
 */
public class LookFeel {

  private LookAndFeelInfo currentLook;
  private static LookFeel instance = new LookFeel();

  /**
   * Sets the look and feel to Metal and caches the first LaF returned by UIManager.
   */
  private LookFeel() {
    currentLook = getAllLookAndFeel()[0];
  }

  /**
   * Get All the look and feels
   *
   * @return all the LookAndFeels
   */
  public LookAndFeelInfo[] getAllLookAndFeel() {
    return UIManager.getInstalledLookAndFeels();
  }

  /**
   * Get Current LAF
   *
   * @return the current LAF
   */
  public LookAndFeelInfo getCurrentLook() {
    return currentLook;
  }

  /**
   * Set Current LAF to LAF returned by the UIManager
   *
   * @param currentLook The look and feel Info
   */
  public void setCurrentLook(LookAndFeelInfo currentLook) {
    try {
      UIManager.setLookAndFeel(currentLook.getClassName());
      this.currentLook = currentLook;
    } catch (Exception e) {
    }
    SwingUtilities.invokeLater(() -> {
      for (Window window : Window.getWindows()) {
        SwingUtilities.updateComponentTreeUI(window);
      }
    });
  }

  public static LookFeel getInstance() {
    return instance;
  }
}
