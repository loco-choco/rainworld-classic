package com.locochoco.gameengine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.vecmath.Point2d;

/**
 * Handles all the graphic logic and calculations
 */
public interface GraphicsAPI {

  public void SetWindowSize(int width, int height);

  public int GetWindowWidth();

  public int GetWindowHeight();

  public void SetPixelToTransformScale(double scale);

  public double GetPixelToTransformScale();

  public void CreateBuffer();

  public void DrawRect(Point2d position, int width, int height, Color color);

  public void DrawSprite(Image image, Point2d position);

  public void DrawText(String text, Point2d position, Color color, Font font);

  public void FlushBuffer();
}
