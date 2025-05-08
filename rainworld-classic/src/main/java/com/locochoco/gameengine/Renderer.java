package com.locochoco.gameengine;

/**
 * Representation of a Object in game
 */
public abstract class Renderer extends Component {
  public String layer;

  public void OnCreated() {
    layer = "";
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

  public abstract void RenderObject(GraphicsAPI graphics_api);
}
