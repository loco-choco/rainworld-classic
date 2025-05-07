package com.locochoco.gameengine;

import javax.vecmath.Vector2d;

/**
 * Representation of a Object in game
 */
public final class CollisionData {
  Collider our_collider;
  Collider other_collider;
  private Vector2d collision_vector;
  private boolean real_collision;

  public CollisionData() {
    collision_vector = new Vector2d(0.0, 0.0);
    real_collision = false;
    our_collider = null;
    other_collider = null;
  }

  public boolean getCollision() {
    return real_collision;
  }

  public CollisionData setCollision(boolean collision) {
    real_collision = collision;
    return this;
  }

  public Vector2d getCollisionVector() {
    return collision_vector;
  }

  public CollisionData setCollisionVector(Vector2d vector) {
    collision_vector = vector;
    return this;
  }

  public Collider getOurCollider() {
    return our_collider;
  }

  public CollisionData setOurCollider(Collider our_collider) {
    this.our_collider = our_collider;
    return this;
  }

  public Collider getOtherCollider() {
    return other_collider;
  }

  public CollisionData setOtherCollider(Collider other_collider) {
    this.other_collider = other_collider;
    return this;
  }
}
