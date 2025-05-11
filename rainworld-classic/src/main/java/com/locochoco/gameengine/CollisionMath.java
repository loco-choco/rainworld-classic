package com.locochoco.gameengine;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

public class CollisionMath {

  public static boolean CheckPointCollision(Collider c, Point2d point) {
    if (c instanceof BoxCollider b)
      return CheckBox2Point(b, point);

    System.err.printf("Collider (%s) doesn't have a collision checker!\n", c.getClass());
    return false;

  }

  public static Vector2d CheckCollision(Collider a, Collider b) {
    if (a instanceof BoxCollider ba)
      return CheckBox2Smth(ba, b);

    System.err.printf("Collider a (%s) doesn't have a collision checker!\n", a.getClass());
    return null;
  }

  public static Vector2d CheckBox2Smth(BoxCollider box, Collider col) {
    if (col instanceof BoxCollider bb)
      return CheckBox2Box(box, bb);

    System.err.printf("Collider b (%s) doesn't have a collision checker againt BoxCollider!\n", col.getClass());
    return null;
  }

  public static boolean CheckBox2Point(BoxCollider b, Point2d point) {
    Point2d corner_a = b.getGlobalCornerA();
    Point2d corner_b = b.getGlobalCornerB();
    if (point.getX() < corner_a.getX() || point.getX() > corner_b.getX())
      return false;
    if (point.getY() < corner_a.getY() || point.getY() > corner_b.getY())
      return false;
    return true;
  }

  public static Vector2d CheckBox2Box(BoxCollider ba, BoxCollider bb) {
    Point2d ba_corner_a = ba.getGlobalCornerA();
    Point2d ba_corner_b = ba.getGlobalCornerB();

    Point2d bb_corner_a = bb.getGlobalCornerA();
    Point2d bb_corner_b = bb.getGlobalCornerB();

    // bb_corner_b - ba_corner_a
    Vector2d overlap_vector_ba_a_bb_b = new Vector2d(bb_corner_b);
    overlap_vector_ba_a_bb_b.sub(ba_corner_a);

    // bb_corner_a - ba_corner_b
    Vector2d overlap_vector_ba_b_bb_a = new Vector2d(bb_corner_a);
    overlap_vector_ba_b_bb_a.sub(ba_corner_b);

    if (overlap_vector_ba_a_bb_b.getX() < 0 || overlap_vector_ba_a_bb_b.getY() < 0
        || overlap_vector_ba_b_bb_a.getX() > 0 || overlap_vector_ba_b_bb_a.getY() > 0)
      return null; // No Collision

    Vector2d collision_vector = new Vector2d(0, 0);
    // Getting the smalled step to handle the collision from the 2 vectors
    Vector2d collision_handling_vector = new Vector2d();
    if (Math.abs(overlap_vector_ba_a_bb_b.getX()) <= Math.abs(overlap_vector_ba_b_bb_a.getX()))
      collision_handling_vector.setX(overlap_vector_ba_a_bb_b.getX());
    else
      collision_handling_vector.setX(overlap_vector_ba_b_bb_a.getX());

    if (Math.abs(overlap_vector_ba_a_bb_b.getY()) <= Math.abs(overlap_vector_ba_b_bb_a.getY()))
      collision_handling_vector.setY(overlap_vector_ba_a_bb_b.getY());
    else
      collision_handling_vector.setY(overlap_vector_ba_b_bb_a.getY());

    // Get the smallest X,Y or XY difference to handle the collision
    if (Math.abs(collision_handling_vector.getX()) <= Math.abs(collision_handling_vector.getY()))
      collision_vector.setX(collision_handling_vector.getX());
    if (Math.abs(collision_handling_vector.getY()) <= Math.abs(collision_handling_vector.getX()))
      collision_vector.setY(collision_handling_vector.getY());

    return collision_vector;
  }

}
