package com.locochoco.gameengine;

import java.util.ArrayList;
import javax.vecmath.Point2d;

/**
 * Representation of a Object in game
 */
public class GameObject {
  private ArrayList<Component> components;
  private Transform transform;
  private Collider collider;
  private Rigidbody rigidbody;

  public GameObject() {
    components = new ArrayList<>();
    transform = new Transform(this);
    components.add(transform);
  }

  public void addCollider(Point2d center, Point2d corner_a, Point2d corner_b, boolean physical) throws Exception {
    if (collider != null)
      throw new Exception("There is already a collider in this GameObject");
    collider = new Collider(this);
    collider.setShape(corner_a, corner_b);
    collider.setCenter(center);
    collider.setPhysical(physical);
    components.add(collider);
  }

  public void addRigidbody() throws Exception {
    if (rigidbody != null)
      throw new Exception("There is already a rigidbody in this GameObject");
    rigidbody = new Rigidbody(this);
    components.add(rigidbody);
  }

  public Transform getTransform() {
    return transform;
  }

  public Collider getCollider() {
    return collider;
  }

  public Rigidbody getRigidbody() {
    return rigidbody;
  }

  public void PhysicsUpdate(double delta_time) {
    for (Component c : components)
      c.PhysicsUpdate(delta_time);
  }

  public void GraphicsUpdate(double delta_time) {
    for (Component c : components)
      c.GraphicsUpdate(delta_time);
  }

  public void Update(double delta_time) {
    for (Component c : components)
      c.Update(delta_time);
  }

  public void LateUpdate(double delta_time) {
    for (Component c : components)
      c.LateUpdate(delta_time);
  }

}
