package PanoViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {

  private static JFileChooser fileChooser;
  private static FileFilter fileFilter;
  private static UIManager.LookAndFeelInfo laf;

  /*
   * Returns a fileChooser with default filter for images.
   */
  public static JFileChooser getFileChooser() {
    if (fileChooser == null || !laf.equals(LookFeel.getInstance().getCurrentLook())) {
      fileChooser = new JFileChooser();
      laf = LookFeel.getInstance().getCurrentLook();
    }
    if (fileFilter == null) {
      fileFilter = new FileNameExtensionFilter("Images Files", ImageIO.getReaderFileSuffixes());

    }
    fileChooser.setFileFilter(fileFilter);
    return fileChooser;
  }
}
