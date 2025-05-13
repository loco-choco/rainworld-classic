package com.locochoco.game;

import javax.vecmath.Point2d;

public class CreaturePipeable extends Pipeable {

  public void OnCreated() {
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

  public void EnterPipe() {
    getGameObject().setEnabled(false);
  }

  public void ExitPipe(Point2d exit_position) {
    getGameObject().setEnabled(true);
    getGameObject().getTransform().setGlobalPosition(exit_position);
  }

}
