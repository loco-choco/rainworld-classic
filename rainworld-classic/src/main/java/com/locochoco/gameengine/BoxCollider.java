package com.locochoco.gameengine;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import java.lang.Exception;
import java.lang.String;
import java.util.ArrayList;

/**
 * Representation of a Object in game
 */
public class BoxCollider extends Collider {
  public Point2d center;
  public Point2d corner_a;
  public Point2d corner_b;

  public void OnCreated() {
    super.OnCreated();
    center = new Point2d(0, 0);
    corner_a = new Point2d(0, 0);
    corner_b = new Point2d(0, 0);
  }

  public void Start() {
    super.Start();
    if (center == null) { // Null is a way to show we want the collider to be centered
      center = new Point2d(corner_a);
      center.add(corner_b);
      center.scale(0.5);
    }
  }

  public Collider setCenter(Point2d center) {
    this.center = center;
    return this;
  }

  public Collider setShape(Point2d corner_a, Point2d corner_b) throws Exception {
    if (corner_a.getX() > corner_b.getX() || corner_a.getY() > corner_b.getY())
      throw new Exception("The collider needs to be a convex shape!");
    this.corner_a = corner_a;
    this.corner_b = corner_b;
    return this;
  }

  public Point2d getCenter() {
    return center;
  }

  public Point2d getCornerA() {
    return corner_a;
  }

  public Point2d getGlobalCornerA() {
    Point2d corner_a_local = new Point2d(corner_a);
    corner_a_local.sub(center);
    return getGameObject().getTransform().transformToGlobalSpace(corner_a_local);
  }

  public Point2d getCornerB() {
    return corner_b;
  }

  public Point2d getGlobalCornerB() {
    Point2d corner_b_local = new Point2d(corner_b);
    corner_b_local.sub(center);
    return getGameObject().getTransform().transformToGlobalSpace(corner_b_local);
  }

}
