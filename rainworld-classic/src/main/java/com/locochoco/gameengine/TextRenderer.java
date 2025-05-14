package com.locochoco.gameengine;

import java.awt.Color;
import java.awt.Font;

import javax.vecmath.Point2d;

/**
 * Representation of a Object in game
 */
public class TextRenderer extends Renderer {
  private Transform transform;
  public Point2d position;
  public String font_name;
  public int text_size;
  public String text;
  private Font font;
  private Color color;

  public void OnCreated() {
    super.OnCreated();
    transform = getGameObject().getTransform();
    color = Color.WHITE;
  }

  public void Start() {
    font = new Font(font_name, Font.PLAIN, text_size);
  }

  public void SetText(String text) {
    this.text = text;
  }

  public void RenderObject(GraphicsAPI graphics_api, Transform camera_transform) {
    Point2d position = new Point2d(camera_transform.transformToLocalSpace(transform.getGlobalPosition()));
    graphics_api.DrawText(text, position, color, font);
  }
}
