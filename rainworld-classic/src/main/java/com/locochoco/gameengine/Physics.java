package com.locochoco.gameengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.lang.String;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    makeLayersCollidable(layer, layer);
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
      if (r == null || !g.isEnabled() || !r.isEnabled())
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
    // System.out.printf("--------------------------------\n");
    ArrayList<GameObject> game_objects = game.getLevel().getGameObjects();
    int i, j;
    // This way, we only check for collision on different objects, and we don't
    // repeat checks
    for (i = 0; i < game_objects.size(); i++) {
      GameObject gameObject_a = game_objects.get(i);
      Collider collider_a = gameObject_a.getCollider();
      if (collider_a == null || !gameObject_a.isEnabled() || !collider_a.isEnabled())
        continue;
      HashSet<String> collision_layers = collision_matrix.getOrDefault(collider_a.getLayer(), new HashSet<String>());
      // System.out.printf("Obj: %s (%s)\n", gameObject_a.getName(),
      // collider_a.getLayer());

      for (j = i + 1; j < game_objects.size(); j++) {
        GameObject gameObject_b = game_objects.get(j);
        Collider collider_b = gameObject_b.getCollider();
        if (collider_b == null || !gameObject_b.isEnabled() || !collider_b.isEnabled())
          continue;
        // System.out.printf("Obj: %s (%s)\n", gameObject_a.getName(),
        // collider_a.getLayer());
        // Only collide if they are in the same collision list
        if (!collision_layers.contains(collider_b.getLayer()))
          continue;

        // -------------------- SOLVE COLLISION -----------------------
        Vector2d collision_vector = CollisionMath.CheckCollision(collider_a, collider_b);
        // System.out.printf("\t\t- Col: %s\n", collision_vector);

        if (collision_vector == null)
          continue; // If null, then no collision
                    //
        // If either collider is non physical, skip applying physics and report
        // collision
        if (!collider_a.getPhysical() || !collider_b.getPhysical()) {
          ReportCollision(collider_a, collider_b, collision_vector);
          continue;
        }
        // -------------------- APPLY PHYSICS -------------------------
        RigidBody rigidBody_a = gameObject_a.getRigidBody();
        RigidBody rigidBody_b = gameObject_b.getRigidBody();
        // If either doesn't have a rigidbody, we don't even try
        if (rigidBody_a == null || rigidBody_b == null)
          continue;
        // If the collision vector is of size 0, then we don't need to apply any physics
        // (both are touching but that's it)
        if (collision_vector.length() == 0)
          continue;
        // ------- 1) Position Fix
        // Move based on the mass distribution between both of the objects
        Transform transform_a = gameObject_a.getTransform();
        Transform transform_b = gameObject_b.getTransform();

        double mass_a = rigidBody_a.getMass();
        double mass_b = rigidBody_b.getMass();

        double mass_sum = Double.isInfinite(mass_b) ? mass_a : mass_a + mass_b;
        double porcent_to_move_a = Double.isInfinite(mass_a) ? 0 : 1.0 - mass_a / mass_sum;
        double porcent_to_move_b = Double.isInfinite(mass_b) ? 0 : 1.0 - porcent_to_move_a;
        if (Double.isInfinite(mass_b))
          porcent_to_move_a = 1.0;

        Vector2d correction_a = new Vector2d(collision_vector);
        correction_a.scale(porcent_to_move_a);
        Vector2d correction_b = new Vector2d(collision_vector);
        correction_b.scale(-porcent_to_move_b); // A '-' because the collision_vector is on the POV is A

        Point2d new_pos_a = new Point2d(transform_a.getGlobalPosition());
        new_pos_a.add(correction_a);
        transform_a.setGlobalPosition(new_pos_a);
        Point2d new_pos_b = new Point2d(transform_b.getGlobalPosition());
        new_pos_b.add(correction_b);
        transform_b.setGlobalPosition(new_pos_b);
        // ------- 2) Force Application
        ApplyCollisionImpulses(rigidBody_a, rigidBody_b, collision_vector);
        // ------- 3) Report Collision after solved
        ReportCollision(collider_a, collider_b, collision_vector);
      }
    }
  }

  private void ReportCollision(Collider collider_a, Collider collider_b, Vector2d collision_vector) {
    collider_a.OnCollision(new CollisionData()
        .setCollisionVector(collision_vector)
        .setOurCollider(collider_a)
        .setOtherCollider(collider_b));

    collider_b.OnCollision(new CollisionData()
        .setCollisionVector(collision_vector)
        .setOurCollider(collider_b)
        .setOtherCollider(collider_a));
  }

  // Reference: https://youtu.be/1L2g4ZqmFLQ
  private void ApplyCollisionImpulses(RigidBody ra, RigidBody rb, Vector2d collision_direction) {
    Vector2d normal = new Vector2d(collision_direction);
    normal.normalize();
    // RigidBody elastic collision calculations
    Vector2d ra_velocity = ra.GetVelocity();
    Vector2d rb_velocity = rb.GetVelocity();
    Vector2d rel_velocity = new Vector2d(ra_velocity);
    rel_velocity.sub(rb_velocity);
    // Vel change from collision
    double velocity_being_lost = Math.min(rel_velocity.dot(normal), 0.0);
    Vector2d vel_diff_from_collision = new Vector2d(normal);
    // The collision vector goes from gb to ga, so we dont need to times by -1
    vel_diff_from_collision.scale(velocity_being_lost);
    Vector2d normal_impulse = new Vector2d(vel_diff_from_collision);
    double sum_of_inverse_mass = 1 / ra.getMass() + 1 / rb.getMass();
    double elastic_influence = 1 + Math.min(ra.getElasticity(), rb.getElasticity());
    double normal_impulse_val = sum_of_inverse_mass == 0 ? 0
        : elastic_influence / sum_of_inverse_mass;
    normal_impulse.scale(normal_impulse_val);
    // Add Normal Impulse
    Vector2d ra_normal_vel_change = new Vector2d(normal_impulse);
    ra_normal_vel_change.scale(1.0 / ra.getMass());
    ra.velocity.sub(ra_normal_vel_change);
    Vector2d rb_normal_vel_change = new Vector2d(normal_impulse);
    rb_normal_vel_change.scale(1.0 / rb.getMass());
    rb.velocity.add(rb_normal_vel_change);

    // https://github.com/gszauer/GamePhysicsCookbook/blob/master/Code/RigidbodyVolume.cpp#L174
    // Calculate Friction Force
    Vector2d rel_vel_ortogonal = new Vector2d(normal);
    rel_vel_ortogonal.scale(-velocity_being_lost);
    rel_vel_ortogonal.add(rel_velocity);

    double ortogonal_vel = rel_vel_ortogonal.length();
    if (ortogonal_vel == 0)
      return;

    Vector2d ortogonal = new Vector2d(rel_vel_ortogonal);
    ortogonal.normalize();
    // Add Friction Impulse
    double friction_impulse_val = sum_of_inverse_mass == 0 ? 0
        : ortogonal_vel / sum_of_inverse_mass;
    double friction_coeficient = Math.sqrt(ra.getFriction() * rb.getFriction());
    double friction_impulse_val_from_normal = normal_impulse_val * friction_coeficient;
    if (friction_impulse_val > friction_impulse_val_from_normal) {
      friction_impulse_val = friction_impulse_val_from_normal;
    } else if (friction_impulse_val < -friction_impulse_val_from_normal) {
      friction_impulse_val = -friction_impulse_val_from_normal;
    }
    Vector2d friction_impulse = new Vector2d(ortogonal);
    friction_impulse.scale(friction_impulse_val);

    Vector2d ra_orto_vel_change = new Vector2d(friction_impulse);
    ra_orto_vel_change.scale(1.0 / ra.getMass());
    ra.velocity.sub(ra_orto_vel_change);
    Vector2d rb_orto_vel_change = new Vector2d(friction_impulse);
    rb_orto_vel_change.scale(1.0 / rb.getMass());
    rb.velocity.add(rb_orto_vel_change);
  }

  public void ReadSettingsFromJson(JsonNode json, ObjectMapper mapper) {
    // Collision Matrix
    JsonNode collision_matrix = json.get("collision_matrix");
    Iterator<Entry<String, JsonNode>> fields = collision_matrix.fields();
    while (fields.hasNext()) { // Fills the fields of each component
      Entry<String, JsonNode> layer = fields.next();
      ArrayList<String> colliding_layers_list = new ArrayList<>();
      for (JsonNode other_layer : layer.getValue()) {
        colliding_layers_list.add(other_layer.asText());
      }
      String[] colliding_layers = new String[colliding_layers_list.size()];
      colliding_layers = colliding_layers_list.toArray(colliding_layers);
      addCollisionLayer(layer.getKey(), colliding_layers);
    }
    // Gravity
    Vector2d gravity = new Vector2d();
    gravity = mapper.convertValue(json.get("gravity"), gravity.getClass());
    setGravity(gravity);
  }
}
