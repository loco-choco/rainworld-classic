package com.locochoco.gameengine;

import javax.vecmath.Point2d;

/**
 * Representation of a Object in game
 */
public final class Transform extends Component {
  private Point2d position;

  public void OnCreated() {
    position = new Point2d(0.0, 0.0);
  }

  public void Start() {
  }

  public void setPosition(Point2d position) {
    this.position = position;
  }

  public Point2d getPosition() {
    return position;
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

}
