package com.locochoco.gameengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.vecmath.Point2d;
import javax.vecmath.Point2i;

/**
 * Handles all the graphic logic and calculations
 */
public class SwingGraphics
    extends JFrame implements MouseListener, KeyListener, GraphicsAPI, InputAPI {

  // GraphicsAPI
  private int window_width;
  private int window_height;
  BufferStrategy buffer;
  Graphics current_frame;
  // InputAPI

  // GraphicsAPI
  public SwingGraphics(String window_name) {
    InitializeWindow(window_name);
    addMouseListener(this);
    addKeyListener(this);
  }

  public void SetWindowSize(int width, int height) {
    window_width = width;
    window_height = height;
    setSize(width, height);
  }

  public void CreateBuffer() {
    current_frame = buffer.getDrawGraphics();
    current_frame.setColor(Color.BLACK);
    current_frame.fillRect(0, 0, window_width, window_height);
  }

  public void DrawRect(Point2i position, int width, int height, Color color) {
    current_frame.setColor(color);
    current_frame.fillRect(position.getX(), position.getY(), width, height);
  }

  public void DrawSprite(Image image, Point2i position) {
    current_frame.drawImage(image, position.getX(), position.getY(), null);
  }

  public void FlushBuffer() {
    current_frame.dispose();
    if (!getBufferStrategy().contentsLost()) {
      getBufferStrategy().show();
    }
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
  // TODO IMPLEMENT
  public Point2d GetMousePos() {
    return new Point2d(0.0, 0.0);
  }

  public boolean GetMouseRightClick() {
    return false;
  }

  public boolean GetKeyPressed(int key_code) {
    return false;
  }

  public void mouseMoved(MouseEvent e) {
  }

  public void mouseClicked(MouseEvent e) {
  }

  public void mouseReleased(MouseEvent e) {
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
  }

  public void keyPressed(KeyEvent e) {
  }

  public void keyTyped(KeyEvent e) {
  }

  public void keyReleased(KeyEvent e) {
  }
}
