package com.locochoco.gameengine;

import javax.vecmath.Vector2d;

/**
 * Representation of a Object in game
 */
public final class CollisionData {
  private Vector2d collision_vector;
  private boolean real_collision;

  public CollisionData() {
    collision_vector = new Vector2d(0.0, 0.0);
    real_collision = false;
  }

  public boolean getCollision() {
    return real_collision;
  }

  public void setCollision(boolean collision) {
    real_collision = collision;
  }

  public Vector2d getCollisionVector() {
    return collision_vector;
  }

  public void setCollisionVector(Vector2d vector) {
    collision_vector = vector;
  }

}
