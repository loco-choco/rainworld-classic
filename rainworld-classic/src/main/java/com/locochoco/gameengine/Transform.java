package com.locochoco.gameengine;

import javax.vecmath.Point2d;

/**
 * Representation of a Object in game
 */
public final class Transform extends Component {
  public Point2d position;

  public void OnCreated() {
    position = new Point2d(0.0, 0.0);
  }

  public void Start() {
  }

  public Point2d transformToLocalSpace(Point2d global_position) {
    GameObject go = getGameObject();
    GameObject parent = go.getParent();
    Point2d transformed_position = new Point2d(global_position);
    if (parent != null)
      transformed_position = parent.getTransform().transformToLocalSpace(global_position);

    transformed_position.sub(position);
    return transformed_position;
  }

  public Point2d transformToGlobalSpace(Point2d local_position) {
    GameObject go = getGameObject();
    GameObject parent = go.getParent();
    Point2d transformed_position = new Point2d(local_position);
    transformed_position.add(position);
    if (parent != null)
      transformed_position = parent.getTransform().transformToGlobalSpace(local_position);

    return transformed_position;
  }

  public void setPosition(Point2d position) {
    this.position = position;
  }

  public Point2d getPosition() {
    return position;
  }

  public void setGlobalPosition(Point2d global_position) {
    this.position = transformToLocalSpace(global_position);
  }

  public Point2d getGlobalPosition() {
    return transformToGlobalSpace(new Point2d(0, 0));
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
