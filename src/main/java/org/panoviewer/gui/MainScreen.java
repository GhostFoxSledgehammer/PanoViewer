// License: GPL. For details, see LICENSE file.
package org.panoviewer.gui;

import org.panoviewer.utils.IOUtils;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import static java.awt.dnd.DnDConstants.ACTION_COPY;
import java.io.IOException;

public class MainScreen extends JFrame implements DropTargetListener {

  private final JMenuBar menuBar;
  private final JPanel jPanel;
  private static MainScreen instance;

  public static MainScreen getInstance() {
    if (instance == null) {
      instance = new MainScreen();
    }
    return instance;
  }

  private MainScreen() {
    this.menuBar = Menu.getInstance();
    setSize(600, 600);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    setJMenuBar(menuBar);
    jPanel = SwitchModes.getInstance();
    add(jPanel);
    new DropTarget(this, this);
  }

//  public static void main(String args[]) {
//    MainScreen.getInstance();
//  }
  @Override
  public void dragEnter(DropTargetDragEvent event) {

  }

  @Override
  public void dragOver(DropTargetDragEvent event) {

  }

  @Override
  public void dropActionChanged(DropTargetDragEvent event) {

  }

  @Override
  public void dragExit(DropTargetEvent event) {

  }

  @Override
  public void drop(DropTargetDropEvent event) {
    event.acceptDrop(ACTION_COPY);
    Transferable transferable = event.getTransferable();

    DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();

    for (DataFlavor df : dataFlavors) {
      try {
        if (df.isFlavorJavaFileListType()) {
          List<File> files = (List<File>) transferable.getTransferData(df);
          displayImage(files.get(0));
        }
      } catch (UnsupportedFlavorException | IOException e) {
      }
    }
  }

  private void displayImage(File file) {
    BufferedImage image = IOUtils.loadImage(file);
    if (image != null) {
      SwitchModes.getInstance().setImage(image);
    }
  }
}
