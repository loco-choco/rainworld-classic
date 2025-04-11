package com.locochoco.gameengine;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

/**
 * Hanldes all the physics logics and calculations
 */
public class Physics {
  private GameEngine game;

  public Physics(GameEngine game) {
    this.game = game;
  }

  public void Update(double delta_time) {
    for (GameObject g : game.getLevel().getGameObjects()) {
      Rigidbody r = g.getRigidbody();
      if (r == null)
        continue;
      Vector2d deltaPos = new Vector2d(r.GetVelocity());
      deltaPos.scale(delta_time);
      g.getTransform().getPosition().add(deltaPos);
    }
    HandleCollision();
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
        if (ga.getRigidbody() == null)
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
        }

      }
    }
  }
}
