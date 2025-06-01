package com.locochoco.gameengine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.vecmath.Point2d;

/**
 * Handles all the graphic logic and calculations
 */
public class SwingGraphics
    extends JFrame implements MouseListener, KeyListener, DropTargetListener, GraphicsAPI, InputAPI {

  // GraphicsAPI
  private double pixel_to_transform_scale;
  private int window_width;
  private int window_height;
  private BufferStrategy buffer;
  private Graphics current_frame;

  // InputAPI
  private Map<Integer, Boolean> keyboard_map;
  private boolean mouse_left_click;
  private boolean mouse_right_click;
  private DropTarget drop_target;
  private ArrayList<DNDSubscriber> dnd_subscribers;

  // GraphicsAPI
  public SwingGraphics(String window_name) {
    InitializeWindow(window_name);
    addMouseListener(this);
    addKeyListener(this);
    keyboard_map = new HashMap<>();
    mouse_left_click = false;
    mouse_right_click = false;
    dnd_subscribers = new ArrayList<>();
  }

  public void SetWindowSize(int width, int height) {
    window_width = width;
    window_height = height;
    setSize(width, height);
  }

  public int GetWindowWidth() {
    return window_width;
  }

  public int GetWindowHeight() {
    return window_height;
  }

  public void SetPixelToTransformScale(double scale) {
    pixel_to_transform_scale = scale;
  }

  public double GetPixelToTransformScale() {
    return pixel_to_transform_scale;
  }

  public void CreateBuffer() {
    current_frame = buffer.getDrawGraphics();
    current_frame.setColor(Color.BLACK);
    current_frame.fillRect(0, 0, window_width, window_height);
  }

  public void DrawRect(Point2d position, int width, int height, Color color) {
    current_frame.setColor(color);
    current_frame.fillRect((int) Math.round(position.getX() * pixel_to_transform_scale),
        (int) Math.round(position.getY() * pixel_to_transform_scale),
        (int) Math.round(width * pixel_to_transform_scale),
        (int) Math.round(height * pixel_to_transform_scale));
  }

  public void DrawSprite(Image image, Point2d position) {
    Image scaled = image.getScaledInstance((int) Math.round(image.getWidth(null) * pixel_to_transform_scale),
        (int) Math.round(image.getHeight(null) * pixel_to_transform_scale), Image.SCALE_FAST);
    current_frame.drawImage(scaled, (int) Math.round(position.getX() * pixel_to_transform_scale),
        (int) Math.round(position.getY() * pixel_to_transform_scale), null);
  }

  public void DrawText(String text, Point2d position, Color color, Font font) {
    current_frame.setColor(color);
    current_frame.setFont(font);
    current_frame.drawString(text, (int) Math.round(position.getX() * pixel_to_transform_scale),
        (int) Math.round(position.getY() * pixel_to_transform_scale));
  }

  public void FlushBuffer() {
    current_frame.dispose();

    if (!getBufferStrategy().contentsLost()) {
      getBufferStrategy().show();
    }
    Toolkit.getDefaultToolkit().sync();
  }

  @Override
  public void paint(Graphics g) {
  }

  private void InitializeWindow(String window_name) {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle(window_name);
    setAlwaysOnTop(false);
    setAutoRequestFocus(false);
    setResizable(true);
    setVisible(true);

    createBufferStrategy(2);
    buffer = getBufferStrategy();

    drop_target = new DropTarget(this, DnDConstants.ACTION_MOVE, this);
  }

  // InputAPI
  public Point2d GetMousePos() {
    Point pos = MouseInfo.getPointerInfo().getLocation();
    SwingUtilities.convertPointFromScreen(pos, this);
    return new Point2d(pos.getX() / pixel_to_transform_scale, pos.getY() / pixel_to_transform_scale);
  }

  public boolean GetMouseRightClick() {
    return mouse_right_click;
  }

  public boolean GetMouseLeftClick() {
    return mouse_left_click;
  }

  public boolean GetKeyPressed(int key_code) {
    if (!keyboard_map.containsKey(key_code))
      return false;
    return keyboard_map.get(key_code);
  }

  public void SubscribeToDND(DNDSubscriber subscriber) {
    dnd_subscribers.add(subscriber);
  }

  public void UnsubscribeToDND(DNDSubscriber subscriber) {
    dnd_subscribers.remove(subscriber);
  }

  public void mouseClicked(MouseEvent e) {
  }

  public void mouseReleased(MouseEvent e) {
    switch (e.getButton()) {
      case MouseEvent.BUTTON3:
        mouse_right_click = false;
        break;
      case MouseEvent.BUTTON1:
        mouse_left_click = false;
        break;
      default:
        break;
    }
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
    switch (e.getButton()) {
      case MouseEvent.BUTTON3:
        mouse_right_click = true;
        break;
      case MouseEvent.BUTTON1:
        mouse_left_click = true;
        break;
      default:
        break;
    }
  }

  public void keyPressed(KeyEvent e) {
    keyboard_map.put(e.getKeyCode(), true);
  }

  public void keyTyped(KeyEvent e) {
  }

  public void keyReleased(KeyEvent e) {
    keyboard_map.put(e.getKeyCode(), false);
  }

  @Override
  public synchronized void dragEnter(DropTargetDragEvent dtde) {
    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
      dtde.acceptDrag(DnDConstants.ACTION_MOVE);
    else
      dtde.rejectDrag();
  }

  @Override
  public synchronized void dragOver(DropTargetDragEvent dtde) {
    dragEnter(dtde);
  }

  @Override
  public synchronized void dropActionChanged(DropTargetDragEvent dtde) {
  }

  @Override
  public synchronized void dragExit(DropTargetEvent dte) {
  }

  @Override
  public synchronized void drop(DropTargetDropEvent dtde) {
    Transferable t = dtde.getTransferable();
    if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
      dtde.acceptDrop(DnDConstants.ACTION_MOVE);
      try {
        List data = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
        for (Object file : data) {
          for (DNDSubscriber sub : dnd_subscribers)
            sub.ReceiveDNDFile((File) file);
        }
        dtde.dropComplete(true);
      } catch (UnsupportedFlavorException ufe) {
        System.err.printf("Issue with flavor type %s\n", ufe.getMessage());
      } catch (IOException ioe) {
        System.err.println("Issue with IO: " + ioe.getMessage());
      }
    }
    dtde.dropComplete(false);
  }
}
