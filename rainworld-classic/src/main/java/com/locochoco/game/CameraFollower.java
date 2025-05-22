package com.locochoco.game;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class CameraFollower extends Component {
  private static CameraFollower instance;
  private Transform target;
  private Transform transform;

  public double width_step;
  public double height_step;

  public void OnCreated() {
    instance = this;
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    transform = getGameObject().getTransform();
  }

  public void OnDestroyed() {
    instance = null;
  }

  public static void SetFollower(Transform target) {
    instance.target = target;
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
    if (target == null)
      return;
    Point2d diff = new Point2d(target.getGlobalPosition());
    diff.sub(transform.getGlobalPosition());
    double x_diff = (long) (diff.getX() / (width_step / 2)) * width_step;
    double y_diff = (long) (diff.getY() / (height_step / 2)) * height_step;
    Point2d new_pos = new Point2d(transform.getGlobalPosition());
    new_pos.setX(new_pos.getX() + x_diff);
    new_pos.setY(new_pos.getY() + y_diff);
    transform.setPosition(new_pos);
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }
}
