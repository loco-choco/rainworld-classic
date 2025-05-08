package com.locochoco.game;

import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public abstract class Item extends Component {

  RigidBody rigidBody;
  Collider collider;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
    rigidBody = getGameObject().getRigidBody();
    collider = getGameObject().getCollider();
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

  public boolean TryToBeGrabbed() {
    GetGrabbed();
    return true;
  }

  protected void GetGrabbed() {
    rigidBody.SetVelocity(new Vector2d(0, 0));
    rigidBody.setEnabled(false);
    collider.setEnabled(false);
  }

  public void GetReleased() {
    rigidBody.setEnabled(true);
    collider.setEnabled(true);
  }

}
