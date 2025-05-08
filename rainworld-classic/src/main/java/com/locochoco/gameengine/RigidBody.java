package com.locochoco.gameengine;

import javax.vecmath.Vector2d;

/**
 * Representation of a Object in game
 */
public final class RigidBody extends Component {
  private Vector2d force;
  public Vector2d velocity;
  public boolean immovable;
  public double mass;
  public double elasticity;
  public double friction;

  public void OnCreated() {
    velocity = new Vector2d(0.0, 0.0);
    force = new Vector2d(0.0, 0.0);
    mass = 1;
    immovable = false;
    elasticity = 0;
    friction = 0;
  }

  public void OnEnabled() {
    force = new Vector2d(0, 0);
  }

  public void OnDisabled() {
  }

  public void Start() {
  }

  public double getMass() {
    if (immovable)
      return Double.POSITIVE_INFINITY;
    return mass;
  }

  public double getElasticity() {
    return elasticity;
  }

  public double getFriction() {
    return friction;
  }

  public RigidBody SetMass(double mass) {
    this.mass = mass;
    return this;
  }

  public RigidBody setElasticity(double elasticity) {
    this.elasticity = elasticity;
    return this;
  }

  public RigidBody setFriction(double friction) {
    this.friction = friction;
    return this;
  }

  public void PhysicsUpdate(double delta_time) {
    if (!immovable) {
      Vector2d deltaVel = new Vector2d(force);
      deltaVel.scale(delta_time / mass);
      velocity.add(deltaVel);
    }
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
