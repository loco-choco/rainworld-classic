package com.locochoco.gameengine;

import java.util.ArrayList;
import javax.vecmath.Vector2d;

/**
 * Representation of a Object in game
 */
public final class Rigidbody extends Component {
  private Vector2d force;
  private Vector2d velocity;
  private double mass;

  public Rigidbody(GameObject owner) {
    super(owner);
    velocity = new Vector2d(0.0, 0.0);
    force = new Vector2d(0.0, 0.0);
    mass = 1;
  }

  public double getMass() {
    return mass;
  }

  public Rigidbody SetMass(double mass) {
    this.mass = mass;
    return this;
  }

  public void PhysicsUpdate(double delta_time) {
    Vector2d deltaVel = new Vector2d(force);
    deltaVel.scale(delta_time / mass);
    velocity.add(deltaVel);
    force = new Vector2d(0.0, 0.0);
  }

  public Vector2d GetVelocity() {
    return velocity;
  }

  public void SetVelocity(Vector2d velocity) {
    this.velocity = velocity;
  }

  public void AddForce(Vector2d force) {
    this.force.add(force);
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

}
