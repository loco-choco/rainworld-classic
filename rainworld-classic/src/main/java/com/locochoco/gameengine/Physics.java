package com.locochoco.gameengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.lang.String;
import java.security.KeyException;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.vecmath.Vector2d;

/**
 * Hanldes all the physics logics and calculations
 */
public class Physics {

  private Vector2d gravity;

  private HashMap<String, HashSet<String>> collision_matrix;

  private GameEngine game;

  public Physics(GameEngine game) {
    this.game = game;
    gravity = new Vector2d(0, 0);
    collision_matrix = new HashMap<>();
  }

  public void setGravity(Vector2d gravity) {
    this.gravity = gravity;
  }

  public void addCollisionLayer(String layer, String... collides_with) {
    for (String other_layer : collides_with) {
      makeLayersCollidable(layer, other_layer);
    }
  }

  private void makeLayersCollidable(String layerA, String layerB) {
    HashSet<String> layer_collision_set_a;
    HashSet<String> layer_collision_set_b;
    if (!collision_matrix.containsKey(layerA)) {
      layer_collision_set_a = new HashSet<>();
      layer_collision_set_a.add(layerA);
      collision_matrix.put(layerA, layer_collision_set_a);
    } else
      layer_collision_set_a = collision_matrix.get(layerA);
    if (!collision_matrix.containsKey(layerB)) {
      layer_collision_set_b = new HashSet<>();
      layer_collision_set_b.add(layerB);
      collision_matrix.put(layerB, layer_collision_set_b);
    } else
      layer_collision_set_b = collision_matrix.get(layerB);

    layer_collision_set_a.add(layerB);
    layer_collision_set_b.add(layerA);
  }

  public void Update(double delta_time) {
    HandleCollision();
    for (GameObject g : game.getLevel().getGameObjects()) {
      RigidBody r = g.getRigidBody();
      if (r == null)
        continue;
      Vector2d gravity_force = new Vector2d(gravity);
      gravity_force.scale(r.getMass());
      r.AddForce(gravity_force);
      Vector2d deltaPos = new Vector2d(r.GetVelocity());
      deltaPos.scale(delta_time);
      g.getTransform().getPosition().add(deltaPos);
    }
  }

  private void HandleCollision() {
    ArrayList<GameObject> game_objects = game.getLevel().getGameObjects();
    for (GameObject ga : game_objects) {
      Collider collider_ga = ga.getCollider();
      if (collider_ga == null)
        continue;
      for (GameObject gb : game_objects) {
        Collider collider_gb = gb.getCollider();
        if (collider_gb == null || collider_ga == collider_gb)
          continue;
        // If the layer field is either null or an empty string, collide either way
        if (!collider_ga.layer.isEmpty() && !collider_gb.layer.isEmpty()
            && collision_matrix.containsKey(collider_ga.layer)
            && !collision_matrix.get(collider_ga.layer).contains(collider_gb.layer))
          continue;

        CollisionData data = collider_ga.CheckCollision(collider_gb);
        RigidBody ga_rigidbody = ga.getRigidBody();
        if (ga_rigidbody == null)
          continue;
        if (data.getCollision()) {
          collider_ga.OnCollision(data, collider_gb);
          Vector2d collision_vector = data.getCollisionVector();
          // If collider_gb is physical, we only want to move ga by half the lenght, as
          // when the gameobject of gb is ga, it will also get moved, but to the other
          // direction
          if (gb.getRigidBody() == null)
            collision_vector.scale(0.5);
          ga.getTransform().getPosition().add(collision_vector);
          // RigidBody elastic collision calculations
          if (collision_vector.length() == 0)
            continue;
          Vector2d ga_velocity = ga_rigidbody.GetVelocity();
          collision_vector.normalize();
          double velocity_being_lost = Math.min(ga_velocity.dot(collision_vector), 0.0);
          collision_vector.scale(velocity_being_lost * (1 + collider_gb.getElasticity()));
          ga_velocity.sub(collision_vector);
        }

      }
    }
  }
}
