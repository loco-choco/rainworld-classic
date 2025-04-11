package com.locochoco.gameengine;

import java.awt.Color;

import javax.vecmath.Point2d;

/**
 * Representation of a Object in game
 */
public class BoxRenderer extends Renderer {
  private Transform transform;
  private Point2d center;
  private int width;
  private int height;
  private Color color;

  public BoxRenderer(GameObject owner) {
    super(owner);
    transform = owner.getTransform();
    center = new Point2d(0, 0);
    width = 0;
    height = 0;
    color = Color.BLACK;
  }

  public BoxRenderer SetCenter(Point2d center) {
    this.center = center;
    return this;
  }

  public BoxRenderer SetWidth(int width) {
    this.width = width;
    return this;
  }

  public BoxRenderer SetHeight(int height) {
    this.height = height;
    return this;
  }

  public BoxRenderer SetColor(Color color) {
    this.color = color;
    return this;
  }

  public void RenderObject(GraphicsAPI graphics_api) {
    Point2d position = new Point2d(transform.getPosition());
    position.sub(center);
    graphics_api.DrawRect(position, width, height, color);
  }
}
