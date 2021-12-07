// License: GPL. For details, see LICENSE file.
package org.panoviewer.gui.jogl;

import org.panoviewer.gui.ImageViewer;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import static org.panoviewer.Settings.getCaps;

/**
 * JOGL based image viewer.
 * @author kshan
 */
public abstract class JOGLImageViewer extends GLJPanel implements GLEventListener, ImageViewer {
  public JOGLImageViewer() {
    super(getCaps());
    setSharedAutoDrawable(TextureShare.getInstance().getSharedDrawable());
    addGLEventListener(this);
  }
}
