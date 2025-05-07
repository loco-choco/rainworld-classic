package com.locochoco.gameengine;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import java.lang.Exception;
import java.lang.String;
import java.util.ArrayList;

/**
 * Representation of a Object in game
 */
public class Collider extends Component {
  public boolean physical;

  public String layer;

  private ArrayList<CollisionListener> collision_listeners;
  private ArrayList<Collider> colliders_on_collision;
  private ArrayList<Collider> colliders_this_frame;

  public void OnCreated() {
    physical = true;
    layer = "";
    collision_listeners = new ArrayList<>();
    colliders_on_collision = new ArrayList<>();
    colliders_this_frame = new ArrayList<>();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
  }

  public void PhysicsUpdate(double delta_time) {
    // Check New Colliders
    for (Collider collider : colliders_this_frame) {
      if (!colliders_on_collision.contains(collider)) {
        colliders_on_collision.contains(collider);
        for (CollisionListener listener : collision_listeners)
          listener.OnEnterCollision(collider);
      }
    }
    // Check Removed Colliders
    for (Collider collider : colliders_on_collision) {
      if (!colliders_this_frame.contains(collider)) {
        colliders_on_collision.remove(collider);
        for (CollisionListener listener : collision_listeners)
          listener.OnExitCollision(collider);
      }
    }
    colliders_this_frame.clear();
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

  public void OnCollision(CollisionData data) {
    // Tell listeners about the collision, if there is on
    colliders_this_frame.add(data.getOtherCollider());
    for (CollisionListener listener : collision_listeners)
      listener.OnCollision(data);
  }

  public Collider setPhysical(boolean physical) {
    this.physical = physical;
    return this;
  }

  public Collider setLayer(String layer) {
    this.layer = layer;
    return this;
  }

  public boolean getPhysical() {
    return physical;
  }

  public String getLayer() {
    return layer;
  }

  public void addCollisionListener(CollisionListener listener) {
    collision_listeners.add(listener);
  }

  public void removeCollisionListener(CollisionListener listener) {
    collision_listeners.remove(listener);
  }

  public void clearCollisionListeners() {
    collision_listeners.clear();
  }
}
