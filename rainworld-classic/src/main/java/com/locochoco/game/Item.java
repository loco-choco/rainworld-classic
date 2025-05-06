package com.locochoco.game;

import java.util.Vector;

import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public abstract class Item extends Component {

  RigidBody rigidBody;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    rigidBody = getGameObject().getRigidBody();
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
    rigidBody.setEnabled(false);
  }

  public void Throw(Vector2d velocity) {
    rigidBody.SetVelocity(velocity);
    rigidBody.setEnabled(false);
  }

}
