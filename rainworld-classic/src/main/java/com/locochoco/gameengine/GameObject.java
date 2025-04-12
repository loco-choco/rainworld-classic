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
  private Renderer renderer;

  public GameObject() {
    components = new ArrayList<>();
    transform = new Transform(this);
    components.add(transform);
  }

  public <Comp extends Component> Comp addComponent(Comp component) throws Exception {
    if (component instanceof Rigidbody rigid) {
      if (rigidbody != null)
        throw new Exception("There is already a rigidbody in this GameObject");
      rigidbody = rigid;
    } else if (component instanceof Collider col) {
      if (collider != null)
        throw new Exception("There is already a collider in this GameObject");
      collider = col;
    } else if (component instanceof Renderer rend) {
      if (renderer != null)
        throw new Exception("There is already a renderer in this GameObject");
      renderer = rend;
    }
    components.add(component);
    return component;
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

  public Renderer getRenderer() {
    return renderer;
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
