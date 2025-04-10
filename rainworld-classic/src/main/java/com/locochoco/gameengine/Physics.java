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
    HandleCollision();
    for (GameObject g : game.getLevel().getGameObjects()) {
      if (g == null)
        continue;
      Vector2d deltaPos = new Vector2d(g.getRigidbody().GetVelocity());
      deltaPos.scale(delta_time);
      g.getTransform().getPosition().add(deltaPos);
    }
  }

  private void HandleCollision() {
    ArrayList<GameObject> game_objects = game.getLevel().getGameObjects();
    for (GameObject ga : game_objects) {
      if (ga == null)
        continue;
      for (GameObject gb : game_objects) {
        if (gb == null || ga == gb)
          continue;
        Collider collider_ga = ga.getCollider();
        Collider collider_gb = gb.getCollider();
        CollisionData data = collider_ga.CheckCollision(collider_gb);
        if (!collider_ga.getPhysical())
          continue;
        if (data.getCollision()) {
          // TODO Implement and raise an OnCollision() event on ga
          Vector2d collision_vector = data.getCollisionVector();
          // If collider_gb is physical, we only want to move ga by half the lenght, as
          // when the gameobject of gb is ga, it will also get moved, but to the other
          // direction
          if (collider_gb.getPhysical())
            collision_vector.scale(0.5);
          ga.getTransform().getPosition().add(collision_vector);
        }

      }
    }
  }
}
