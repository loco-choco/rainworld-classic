package com.locochoco.gameengine;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

/**
 * Hanldes all the physics logics and calculations
 */
public class Physics {

  private Vector2d gravity;

  private GameEngine game;

  public Physics(GameEngine game) {
    this.game = game;
    gravity = new Vector2d(0, 0);
  }

  public void setGravity(Vector2d gravity) {
    this.gravity = gravity;
  }

  public void Update(double delta_time) {
    HandleCollision();
    for (GameObject g : game.getLevel().getGameObjects()) {
      Rigidbody r = g.getRigidbody();
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
        CollisionData data = collider_ga.CheckCollision(collider_gb);
        Rigidbody ga_rigidbody = ga.getRigidbody();
        if (ga_rigidbody == null)
          continue;
        if (data.getCollision()) {
          // TODO Implement and raise an OnCollision() event on ga
          Vector2d collision_vector = data.getCollisionVector();
          // If collider_gb is physical, we only want to move ga by half the lenght, as
          // when the gameobject of gb is ga, it will also get moved, but to the other
          // direction
          if (gb.getRigidbody() == null)
            collision_vector.scale(0.5);
          ga.getTransform().getPosition().add(collision_vector);
          // Rigidbody elatic collision calculations
          if (collision_vector.length() == 0)
            continue;
          Vector2d ga_velocity = ga_rigidbody.GetVelocity();
          collision_vector.normalize();
          double velocity_being_lost = ga_velocity.dot(collision_vector);
          collision_vector.scale(velocity_being_lost * (1 + collider_gb.getElasticity()));
          ga_velocity.sub(collision_vector);
        }

      }
    }
  }
}
