package com.locochoco.gameengine;

/**
 * Representation of a Object in game
 */
public interface CollisionListener {

  public void OnEnterCollision(Collider collider);

  public void OnExitCollision(Collider collider);

  public void OnCollision(CollisionData data);

}
