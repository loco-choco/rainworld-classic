package com.locochoco.gameengine;

import java.awt.Color;

import javax.vecmath.Point2d;

/**
 * Representation of a Object in game
 */
public class BoxRenderer extends Renderer {
  private Transform transform;
  public Point2d center;
  public int width;
  public int height;
  public Color color;

  public void OnCreated() {
    super.OnCreated();
    transform = getGameObject().getTransform();
    center = new Point2d(0, 0);
    width = 0;
    height = 0;
    color = Color.BLACK;
  }

  public void Start() {
    if (center == null) {
      center = new Point2d(width / 2.0, height / 2.0);
    }
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

  public void RenderObject(GraphicsAPI graphics_api, Transform camera_transform) {
    Point2d position = new Point2d(camera_transform.transformToLocalSpace(transform.getGlobalPosition()));
    position.sub(center);
    graphics_api.DrawRect(position, width, height, color);
  }
}
