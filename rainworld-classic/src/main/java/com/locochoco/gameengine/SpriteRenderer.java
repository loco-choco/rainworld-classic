package com.locochoco.gameengine;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.vecmath.Point2d;

/**
 * Representation of a Object in game
 */
public class SpriteRenderer extends Renderer {
  private Transform transform;
  public Point2d center;
  public String file_name;
  public boolean horizontal_flip;
  public boolean vertical_flip;
  private BufferedImage sprite;
  private int sprite_width;
  private int sprite_height;

  public void OnCreated() {
    super.OnCreated();
    transform = getGameObject().getTransform();
    center = new Point2d(0, 0);
    file_name = "";
    sprite_width = 0;
    sprite_height = 0;
  }

  public void Start() {
    if (file_name.isEmpty())
      return;
    try {
      sprite = ImageIO.read(new File(file_name));
    } catch (Exception e) {
      System.err.printf("Couldn't open image %s: %s\n", file_name, e.getMessage());
    }
    sprite_width = sprite.getWidth();
    sprite_height = sprite.getHeight();
    if(horizontal_flip) FlipImageHorizontal();
    if(vertical_flip) FlipImageVertical();
  }

  public SpriteRenderer SetCenter(Point2d center) {
    this.center = center;
    return this;
  }

  public SpriteRenderer SetSprite(BufferedImage sprite) {
    this.sprite = sprite;
    sprite_width = sprite.getWidth(null);
    sprite_height = sprite.getHeight(null);
    return this;
  }

  public BufferedImage GetSprite() {
    return sprite;
  }

  public void FlipImageHorizontal() {
    AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
    tx.translate(-sprite.getWidth(), 0);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    sprite = op.filter(sprite, null);
  }

  public void FlipImageVertical() {
    AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
    tx.translate(-sprite.getWidth(), 0);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    sprite = op.filter(sprite, null);
  }

  public void RenderObject(GraphicsAPI graphics_api, Transform camera_transform) {
    if (sprite == null)
      return;
    Point2d position = new Point2d(-sprite_width / 2.0, -sprite_height / 2.0);
    position.add(camera_transform.transformToLocalSpace(transform.getGlobalPosition()));
    position.add(center);
    graphics_api.DrawSprite(sprite, position);
  }
}
