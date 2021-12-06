// License: GPL. For details, see LICENSE file.
package org.panoviewer;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Tests {@link Camera}
 */
public class CameraTest extends TestCase {

  public CameraTest() {
  }

  @Test
  public void Camera_test() {
    Camera camera_new = new Camera();
    double epsilon = 0.00001;
    assertEquals(true, camera_new.getYaw() - (0) < epsilon);
  }

  @Test
  public void rotate_test() {
    Camera rotate_test = new Camera();
    double epsilon = 1e-5;

    rotate_test.rotate((float) (Math.PI / 4), (float) (Math.PI / 4));
    assertTrue(rotate_test.getYaw() <= 2 * Math.PI + epsilon);
    assertTrue(rotate_test.getYaw() >= 0 - epsilon);
    assertEquals((float) (Math.PI / 4), rotate_test.getYaw(), epsilon);
    assertTrue(Math.abs(rotate_test.getPitch() - Math.PI / 4) < epsilon);

    rotate_test.rotate((float) (3 * Math.PI / 4), (float) (Math.PI / 2));
    assertTrue(rotate_test.getYaw() <= 2 * Math.PI + epsilon);
    assertTrue(rotate_test.getYaw() >= 0 - epsilon);
    assertTrue(rotate_test.getPitch() <= Math.PI / 2 + epsilon);
    assertTrue(rotate_test.getPitch() >= -Math.PI / 2 - epsilon);
    assertTrue(rotate_test.getYaw() + epsilon >= (float) (3 / 2 * Math.PI));
    assertTrue(rotate_test.getYaw() - epsilon <= (float) (3 / 2 * Math.PI));
    assertTrue(rotate_test.getPitch() + epsilon >= (float) (Math.PI / 2));
    assertTrue(rotate_test.getPitch() - epsilon <= (float) (Math.PI / 2));

    rotate_test.rotate((float) (4 / 3 * Math.PI), (float) (7 / 4 * Math.PI));
    assertTrue(rotate_test.getYaw() <= 2 * Math.PI + epsilon);
    assertTrue(rotate_test.getYaw() >= 0 - epsilon);
    assertTrue(rotate_test.getYaw() + epsilon >= (float) (4 / 6 * Math.PI));
    assertTrue(rotate_test.getYaw() - epsilon <= (float) (4 / 6 * Math.PI));
    assertTrue(rotate_test.getPitch() <= Math.PI / 2 + epsilon);
    assertTrue(rotate_test.getPitch() >= -Math.PI / 2 - epsilon);
  }
}
