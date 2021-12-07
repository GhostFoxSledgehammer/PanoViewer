// License: GPL. For details, see LICENSE file.
package org.panoviewer.gui.jogl;

import static com.jogamp.opengl.GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import java.awt.image.BufferedImage;
import static org.panoviewer.Settings.getCaps;
import static org.panoviewer.Settings.getGL;
import static org.panoviewer.utils.joglUtils.getTextureData;

/**
 * Contains a single shared texture.
 * @author kshan
 */
public class TextureShare {

  private static TextureShare instance;

  private final GLOffscreenAutoDrawable sharedDrawable;
  private Texture texture;
  private TextureData textureData;
  private boolean updateImage;

  private TextureShare() {

    sharedDrawable = GLDrawableFactory.getFactory(getGL()).createOffscreenAutoDrawable(null, getCaps(), null, 256, 256);
    sharedDrawable.addGLEventListener(new GLEventListener() {
      @Override
      public void init(GLAutoDrawable drawable) {
        texture = new Texture(GL_TEXTURE_2D);
      }

      @Override
      public void dispose(GLAutoDrawable drawable) {
      }

      @Override
      public void display(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        if (updateImage) {
          updateTexture(gl);
        }
      }

      @Override
      public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      }

      private void updateTexture(GL4 gl) {
        texture.updateImage(gl, textureData);
        textureData = null;
        if (gl.isExtensionAvailable("GL_EXT_texture_filter_anisotropic")) {
          float max[] = new float[1];
          gl.glGetFloatv(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, max, 0);
          gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, max[0]);
        }
        updateImage = false;
      }

    });
    System.err.println("Showing init frame1");
    sharedDrawable.display();
  }

  public Texture getTexture() {
    return texture;
  }

  public static TextureShare getInstance() {
    if (instance == null) {
      instance = new TextureShare();
    }
    return instance;
  }

  public void setImage(BufferedImage image) {
    textureData = getTextureData(image);
    updateImage = true;
    sharedDrawable.display();
  }



  public GLOffscreenAutoDrawable getSharedDrawable() {
    return sharedDrawable;
  }
}
