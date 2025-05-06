package com.locochoco.gameengine;

/**
 * Representation of a Object in game
 */
public abstract class Component {

  private GameObject owner;

  private boolean enabled = false;
  private boolean has_started = false;

  public abstract void OnCreated();

  public abstract void OnEnabled();

  public abstract void OnDisabled();

  public abstract void Start();

  public abstract void PhysicsUpdate(double delta_time);

  public abstract void GraphicsUpdate(double delta_time);

  public abstract void Update(double delta_time);

  public abstract void LateUpdate(double delta_time);

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enable) {
    if (enable && !enabled)
      OnEnabled();
    else if (!enable && enabled)
      OnDisabled();
    enabled = enable;
  }

  public void setHasStarted(boolean has_started) {
    this.has_started = has_started;
  }

  public boolean getHasStarted() {
    return has_started;
  }

  public void setGameObject(GameObject owner) {
    this.owner = owner;
  }

  public GameObject getGameObject() {
    return owner;
  }
}
