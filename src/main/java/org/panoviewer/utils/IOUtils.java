// License: GPL. For details, see LICENSE file.
package org.panoviewer.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.image.BufferedImage;
import org.panoviewer.FileChooser;
import org.panoviewer.MainScreen;
import org.panoviewer.SwitchModes;

/**
 *
 * @author kshan
 */
public class IOUtils {

   private IOUtils() {
    // private constructor to prevent instantiation
  }
  /*
   * Gets the image selected by the user and sets it on the SwitchMode panel.
   */
  public static void getFile() {
    File file = chooseFile();
    if (file != null) {
      BufferedImage loadImage = loadImage(file);
      if (loadImage == null) {
        return;
      }
      SwitchModes.getInstance().setImage(loadImage);
    }
  }

  /*
   * Loads image from the given file.
   *
   * @param file The file to be opened.
   *
   * @return the opened image, or null when file is invalid or an exception
   * occurs.
   */
  public static BufferedImage loadImage(File file) {
    if (file == null) {
      return null;
    }

    BufferedImage img;
    try {
      img = ImageIO.read(file);
    } catch (IOException e) {
      img = null;
    }
    if (img == null) {
      JOptionPane.showMessageDialog(null, "Load Image <" + file.getName() + "> Failed!", "Error",
              JOptionPane.ERROR_MESSAGE);
    }
    return img;
  }

  /*
   * Displays a file chooser and returns the file selected by the user.
   */
  public static File chooseFile() {
    JFileChooser fileChooser = FileChooser.getFileChooser();
    int returnVal = fileChooser.showOpenDialog(MainScreen.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
      File file = fileChooser.getSelectedFile();
      return file;
    }
    return null;
  }

  public static InputStream getFileFromResourceAsStream(String fileName) {
    Class currentClass = new Object() {
    }.getClass().getEnclosingClass();
    ClassLoader classLoader = currentClass.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(fileName);
    if (inputStream == null) {
      return null;
    } else {
      return inputStream;
    }
  }
}
