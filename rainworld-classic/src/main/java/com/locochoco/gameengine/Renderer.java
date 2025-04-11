package com.locochoco.gameengine;

/**
 * Representation of a Object in game
 */
public abstract class Renderer extends Component {

  public Renderer(GameObject owner) {
    super(owner);
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
