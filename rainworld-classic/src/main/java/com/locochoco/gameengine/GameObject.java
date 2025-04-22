package com.locochoco.gameengine;

import java.util.ArrayList;

/**
 * Representation of a Object in game
 */
public class GameObject {
  private ArrayList<Component> components;
  private Transform transform;
  private Collider collider;
  private RigidBody rigidbody;
  private Renderer renderer;

  private GameObject parent;
  private ArrayList<GameObject> children;

  public GameObject() {
    parent = null;
    children = new ArrayList<>();

    components = new ArrayList<>();
    transform = new Transform();
    components.add(transform);
    transform.setGameObject(this);
    transform.OnCreated();
  }

  // TODO Make addComponent receive a Class object and THEN create the components
  // instead of this mess
  public <Comp extends Component> Comp addComponent(Comp component) throws Exception {
    if (component instanceof Transform trans) {
      if (transform != null)
        throw new Exception("There is already a transform in this GameObject");
      transform = trans;
    } else if (component instanceof RigidBody rigid) {
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
    component.setGameObject(this);
    component.OnCreated();
    return component;
  }

  public Transform getTransform() {
    return transform;
  }

  public Collider getCollider() {
    return collider;
  }

  public RigidBody getRigidBody() {
    return rigidbody;
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public void Start() {
    for (Component c : components)
      if (!c.getHasStarted()) {
        c.Start();
        c.setHasStarted(true);
      }
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

  public GameObject getParent() {
    return parent;
  }

  public void setParent(GameObject parent) throws Exception {
    if (parent == this)
      throw new Exception("GameObject can't parent to itself!");
    this.parent = parent;
  }

  public ArrayList<GameObject> getChildren() {
    return children;
  }

  public boolean addChild(GameObject child) throws Exception {
    child.setParent(this);
    return children.add(child);
  }

  public boolean removeChild(GameObject child) {
    return children.remove(child);
  }

}
