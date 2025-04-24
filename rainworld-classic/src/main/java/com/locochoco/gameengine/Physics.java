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
        RigidBody gb_rigidbody = gb.getRigidBody();
        if (ga_rigidbody == null || gb_rigidbody == null)
          continue;
        if (data.getCollision()) {
          collider_ga.OnCollision(data, collider_gb);
          Vector2d collision_vector = data.getCollisionVector();

          if (collision_vector.length() == 0)
            continue;

          // Position Correction Handler
          double total_mass = 1 / ga_rigidbody.getMass() + 1 / gb_rigidbody.getMass();
          if (total_mass != 0) {
            double porcentage_to_move_by = total_mass / ga_rigidbody.getMass();
            Vector2d correction = new Vector2d(collision_vector);
            correction.scale(porcentage_to_move_by);
            ga.getTransform().getPosition().add(correction);
          }

          ApplyCollisionImpulse(ga_rigidbody, gb_rigidbody, collision_vector);
          ApplyFriction(ga_rigidbody, gb_rigidbody, collision_vector);
        }

      }
    }
  }

  // Reference: https://youtu.be/1L2g4ZqmFLQ
  private void ApplyCollisionImpulse(RigidBody ra, RigidBody rb, Vector2d collision_direction) {
    Vector2d normal = new Vector2d(collision_direction);
    normal.normalize();
    // RigidBody elastic collision calculations
    Vector2d ra_velocity = ra.GetVelocity();
    Vector2d rb_velocity = new Vector2d(0, 0);
    Vector2d rel_velocity = new Vector2d(ra_velocity);
    rel_velocity.sub(rb_velocity);
    // Vel change from collision
    double velocity_being_lost = Math.min(rel_velocity.dot(normal), 0.0);
    Vector2d vel_diff_from_collision = new Vector2d(normal);
    // The collision vector goes from gb to ga, so we dont need to times by -1
    vel_diff_from_collision.scale(velocity_being_lost);
    Vector2d collision_impulse = new Vector2d(vel_diff_from_collision);
    double sum_of_inverse_mass = 1 / ra.getMass() + 1 / rb.getMass();
    double elastic_influence = 1 + Math.min(ra.getElasticity(), rb.getElasticity());
    collision_impulse.scale(elastic_influence / sum_of_inverse_mass);
    // Add Impulse
    ra.AddForce(collision_impulse);
  }

  private void ApplyFriction(RigidBody ra, RigidBody rb, Vector2d collision_direction) {
    Vector2d normal = new Vector2d(collision_direction);
    Vector2d ra_velocity = ra.GetVelocity();
    Vector2d rb_velocity = new Vector2d(0, 0);
    Vector2d rel_velocity = new Vector2d(ra_velocity);
    rel_velocity.sub(rb_velocity);
    // Rel Vel normal to the collsion
    double velocity_on_the_collision_scale = Math.min(rel_velocity.dot(normal), 0.0);
    Vector2d velocity_on_the_collision_dir = new Vector2d(normal);
    velocity_on_the_collision_dir.scale(velocity_on_the_collision_scale);
    Vector2d vel_normal_to_collision = new Vector2d(rel_velocity);
    vel_normal_to_collision.sub(velocity_on_the_collision_dir);

    Vector2d collision_impulse = new Vector2d(vel_diff_from_collision);
    double sum_of_inverse_mass = 1 / ra.getMass() + 1 / rb.getMass();
    double elastic_influence = 1 + Math.min(ra.getElasticity(), rb.getElasticity());
    collision_impulse.scale(elastic_influence / sum_of_inverse_mass);
    // Add Impulse
    ra.AddForce(collision_impulse);
  }
}
