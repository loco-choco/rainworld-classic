package com.locochoco.gameengine;

/**
 * Representation of a Object in game
 */
public abstract class Component {

  private GameObject owner;

  public Component(GameObject owner) {
    this.owner = owner;
  }

  public abstract void PhysicsUpdate(double delta_time);

  public abstract void GraphicsUpdate(double delta_time);

  public abstract void Update(double delta_time);

  public abstract void LateUpdate(double delta_time);

  public GameObject getGameObject() {return owner; }
}
