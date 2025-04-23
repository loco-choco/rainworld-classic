package com.locochoco.gameengine;

/**
 * Representation of a Object in game
 */
public interface CollisionListener {

  public void OnEnterCollision();
  public void OnExitCollision();

  public void OnCollision(CollisionData data, Collider collidee);

}
