package com.locochoco.gameengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.vecmath.Point2d;

/**
 * Handles all the graphic logic and calculations
 */
public class SwingGraphics
    extends JFrame implements MouseListener, KeyListener, GraphicsAPI, InputAPI {

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

  // GraphicsAPI
  public SwingGraphics(String window_name) {
    InitializeWindow(window_name);
    addMouseListener(this);
    addKeyListener(this);
    keyboard_map = new HashMap<>();
    mouse_left_click = false;
    mouse_right_click = false;
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
  }

  // InputAPI
  public Point2d GetMousePos() {
    Point pos = MouseInfo.getPointerInfo().getLocation();
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
}
