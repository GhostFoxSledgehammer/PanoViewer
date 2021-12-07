// License: GPL. For details, see LICENSE file.
package org.panoviewer;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

/**
 *
 * @author kshan
 */
public class Settings {

  private static float dragSensitivity = 1f;
  private static int wheelSensitivity = 5;
  private static int precision = 360;//No of slices in spherical mesh.
  private static GLProfile gl;


  /* Whether or not to flip the image when creating texture data. */
  private static boolean invertImage;
  private static GLCapabilities caps;

  public static GLCapabilities getCaps() {
    return caps;
  }

  private Settings() {
    //
  }

  static {
    gl = GLProfile.getMaxProgrammable(true);
    caps = new GLCapabilities(gl);
  }

  public static void invertImage(boolean invert) {
    invertImage = invert;
  }

  public static boolean invertImage() {
    return invertImage;
  }

  public static GLProfile getGL() {
    return gl;
  }

  public static boolean checkMinimumVersion() {
    return GLProfile.isAvailable(GLProfile.GL4);
  }

  public static float getDragSensitivity() {
    return dragSensitivity;
  }

  public static void setDragSensitivity(float newSensi) {
    dragSensitivity = newSensi;
  }

  public static int getWheelSensitivity() {
    return wheelSensitivity;
  }

  public static void setWheelSensitivity(int newSensi) {
    wheelSensitivity = newSensi;
  }

  public static int getPrecision() {
    return precision;
  }

  public static void setPrecision(int newPrecision) {
    precision = newPrecision;
  }
}
