package com.locochoco.gameengine;

import java.awt.Color;
import java.awt.Image;

import javax.vecmath.Point2i;

/**
 * Handles all the graphic logic and calculations
 */
public interface GraphicsAPI {

  public void SetWindowSize(int width, int height);

  public void CreateBuffer();

  public void DrawRect(Point2i position, int width, int height, Color color);

  public void DrawSprite(Image image, Point2i position);

  public void FlushBuffer();
}
