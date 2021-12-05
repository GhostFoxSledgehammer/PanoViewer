// License: GPL. For details, see LICENSE file.
package PanoViewer.gui.jogl;

import static PanoViewer.Settings.getWheelSensitivity;
import static PanoViewer.Utils.joglUtils.getTextureData;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureCoords;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * The Flat Image panel which will display Flat Images
 *
 * @author - Rohan Babbar
 * @author - Kishan Tripathi
 */
public class FlatPanel extends JOGLImageViewer {

  private static FlatPanel instance;

  private boolean updateImage;
  private boolean panEnabled;
  private boolean zoomEnabled;
  private TextureData textureData;
  private Texture texture;
  private BufferedImage baseImage;
  private final float maxZoom = 5f;
  private final float minZoom = 1f;
  private float zoom = 1f;
//  private TextureCoords imageCoord;
  private Rectangle visibleRect;
  private final HandleMouseEvent mouseListener;

  private FlatPanel() {
    this.panEnabled = true;
    this.zoomEnabled = true;
    mouseListener = new HandleMouseEvent();
    addMouseListener(mouseListener);
    addMouseMotionListener(mouseListener);
    addMouseWheelListener(mouseListener);
  }

  @Override
  public void setImage(BufferedImage image) {
    if (image == null) {
      return;
    }
    baseImage = image;
    updateImage = true;
    textureData = getTextureData(image);
    zoom = minZoom;
    repaint();
  }

  @Override
  public boolean isPanningEnabled() {
    return panEnabled;
  }

  @Override
  public void enablePanning(boolean enable) {
    panEnabled = enable;
  }

  @Override
  public void pan(float panX, float panY) {
    if (!panEnabled) {
      return;
    }
    Rectangle rect;
    BufferedImage image;
    synchronized (this) {
      rect = this.visibleRect;
      image = this.baseImage;
    }
    rect.x += panX * image.getWidth() / (getWidth() * zoom);
    rect.y += panY * image.getWidth() / (getWidth() * zoom);
    checkVisibleRectPos(image, rect);
    synchronized (this) {
      visibleRect = rect;
    }
    repaint();
  }

  @Override
  public void enableZoom(boolean enable) {
    zoomEnabled = enable;
  }

  @Override
  public boolean isZoomEnabled() {
    return zoomEnabled;
  }

  @Override
  public void zoom(float zoomBy) {
    if (!zoomEnabled) {
      return;
    }
    Rectangle rect;
    BufferedImage image;
    synchronized (this) {
      rect = this.visibleRect;
      image = this.baseImage;
    }
    if (zoomBy < 0) {
      zoom = zoom * 1.1f;
    } else {
      zoom = zoom * 0.9f;
    }
    zoom = Math.min(zoom, maxZoom);
    zoom = Math.max(zoom, minZoom);
    float[] center = new float[]{rect.x + (rect.width / 2), rect.y + (rect.height / 2)};
    float width = image.getWidth() / zoom;
    float height = image.getHeight() / zoom;
    // Set the same ratio for the visible rectangle and the display area
    float hFact = height * getSize().width;
    float wFact = width * getSize().height;
    if (hFact > wFact) {
      width = hFact / getSize().height;
    } else {
      height = wFact / getSize().width;
    }
    rect.x = (int) (center[0] - width / 2);
    rect.y = (int) (center[1] - height / 2);
    rect.width = (int) width;
    rect.height = (int) height;
    checkVisibleRectSize(image, visibleRect);
    checkVisibleRectPos(image, rect);
    synchronized (this) {
      visibleRect = rect;
    }
    repaint();
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    texture = new Texture(GL_TEXTURE_2D);
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    texture.disable(gl);
    texture.destroy(gl);
  }

  @Override
  public void display(GLAutoDrawable drawable) {
    if (baseImage == null) {
      return;
    }
    Rectangle target;
    TextureCoords texCoords;
    GL2 gl;
    gl = drawable.getGL().getGL2();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    if (updateImage) {
      updateTexture(gl);
      updateImage = false;
    }
    texture.enable(gl);
    texture.bind(gl);
    texCoords = imageToTex(visibleRect, baseImage);
    target = calculateDrawImageRectangle(visibleRect);
    float[] scaledCoords = componentToOpengl(target);
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(texCoords.left(), texCoords.bottom());
    gl.glVertex3f(scaledCoords[0], scaledCoords[1], 0);
    gl.glTexCoord2f(texCoords.right(), texCoords.bottom());
    gl.glVertex3f(scaledCoords[2], scaledCoords[1], 0);
    gl.glTexCoord2f(texCoords.right(), texCoords.top());
    gl.glVertex3f(scaledCoords[2], scaledCoords[3], 0);
    gl.glTexCoord2f(texCoords.left(), texCoords.top());
    gl.glVertex3f(scaledCoords[0], scaledCoords[3], 0);
    gl.glEnd();
    texture.disable(gl);
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL2 gl = drawable.getGL().getGL2();
    gl.glViewport(0, 0, width, height);
    gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
  }

  public static FlatPanel getInstance() {
    if (instance == null) {
      instance = new FlatPanel();
    }
    return instance;
  }

  private void updateTexture(GL2 gl) {
    texture.updateImage(gl, textureData);
    texture.enable(gl);
    texture.bind(gl);
    visibleRect = new Rectangle(0, 0, textureData.getWidth(), textureData.getHeight());
  }

  private Rectangle calculateDrawImageRectangle(Rectangle visibleRect) {
    return calculateDrawImageRectangle(visibleRect, new Rectangle(0, 0, getSize().width, getSize().height));
  }

  /**
   * calculateDrawImageRectangle
   *
   * @param imgRect the part of the image that should be drawn (in image coordinates)
   * @param compRect the part of the component where the image should be drawn (in component coordinates)
   * @return the part of compRect with the same width/height ratio as the image
   */
  private static Rectangle calculateDrawImageRectangle(Rectangle imgRect, Rectangle compRect) {
    int x = 0;
    int y = 0;
    int w = compRect.width;
    int h = compRect.height;
    int wFact = w * imgRect.height;
    int hFact = h * imgRect.width;
    if (wFact != hFact) {
      if (wFact > hFact) {
        w = hFact / imgRect.height;
        x = (compRect.width - w) / 2;
      } else {
        h = wFact / imgRect.width;
        y = (compRect.height - h) / 2;
      }
    }
    return new Rectangle(x + compRect.x, y + compRect.y, w, h);
  }

  /**
   * Coverts java image coordinates to opengl texture coordinates.
   * @param imageCoords Rectangle in image coordinates
   * @param baseImage the original image
   * @return the rectangle in texture coordinates
   */
  private TextureCoords imageToTex(Rectangle imageCoords, BufferedImage baseImage) {
    float w = baseImage.getWidth();
    float h = baseImage.getHeight();
    return new TextureCoords((1f * imageCoords.x) / w,
            (h - imageCoords.y) / h,
            (1f * imageCoords.x + imageCoords.width) / w,
            (h - imageCoords.y - imageCoords.height) / h);
  }

  /**
   * Converts component coordinates to OpenGL screen coordinates.
   * @param target The component coordinates.
   * @return
   */
  private float[] componentToOpengl(Rectangle target) {
    float w = getWidth();
    float h = getHeight();
    return new float[]{(2 * (1f * target.x) / w) - 1, (2 * (1f * target.y) / h) - 1,
      (2 * (1f * target.x + target.width) / w) - 1, (2 * (1f * target.y + target.height) / h) - 1};
  }

  /**
   * Makes sure the visible part of images is not out of image bounds.
   * @param image The original image.
   * @param visibleRect The visible part of the image.
   */
  public static void checkVisibleRectPos(BufferedImage image, Rectangle visibleRect) {
    if (visibleRect.x < 0) {
      visibleRect.x = 0;
    }
    if (visibleRect.y < 0) {
      visibleRect.y = 0;
    }
    if (visibleRect.x + visibleRect.width > image.getWidth()) {
      visibleRect.x = image.getWidth() - visibleRect.width;
    }
    if (visibleRect.y + visibleRect.height > image.getHeight()) {
      visibleRect.y = image.getHeight() - visibleRect.height;
    }
  }

  /**
   * Checks whether visible part of image is not more than the image size.
   * @param image The original image.
   * @param visibleRect The visible part of the image.
   */
  private static void checkVisibleRectSize(BufferedImage image, Rectangle visibleRect) {
    if (visibleRect.width > image.getWidth(null)) {
      visibleRect.width = image.getWidth(null);
    }
    if (visibleRect.height > image.getHeight(null)) {
      visibleRect.height = image.getHeight(null);
    }
  }

  /**
   *
   * Handles Mouse Events for Panning and Zooming
   */
  private class HandleMouseEvent extends MouseAdapter {

    private int finalX;
    private int finalY;

    public HandleMouseEvent() {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      zoom(e.getWheelRotation() * getWheelSensitivity());
    }

    @Override
    public void mousePressed(MouseEvent e) {
      finalX = e.getX();
      finalY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      int newX = e.getX();
      int newY = e.getY();
      pan((float) finalX - newX, (float) -finalY + newY);
      finalX = newX;
      finalY = newY;
    }
  }
}
