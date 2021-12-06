// License: GPL. For details, see LICENSE file.
package org.panoviewer.gui;

/**
 *
 * @author kshan
 */
public interface Zoomable {

  /**
   * Enables or disables zoom
   *
   * @param enable if true enables zoom else disables it
   */
  void enableZoom(boolean enable);

  /**
   * Checks if zoom is enabled or not.
   *
   * @return {@code true} if the zooming is enabled, {@code false} otherwise.
   */
  boolean isZoomEnabled();

  /**
   * Zooms outwards if zoomBy is positive otherwise inwards.
   * @param zoomBy The amount to zoom by.
   */
  void zoom(float zoomBy);
}
