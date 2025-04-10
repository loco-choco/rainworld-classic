package com.locochoco.gameengine;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstraction over the loaded objects in game
 */
public class Level implements Serializable {
  private ArrayList<GameObject> game_objects;

  public Level() {
    game_objects = new ArrayList<>();
  }

  public ArrayList<GameObject> getGameObjects() {
    return game_objects;
  }

  public void PhysicsUpdate(double delta_time) {
    for (GameObject g : game_objects)
      g.PhysicsUpdate(delta_time);
  }

  public void GraphicsUpdate(double delta_time) {
    for (GameObject g : game_objects)
      g.GraphicsUpdate(delta_time);
  }

  public void Update(double delta_time) {
    for (GameObject g : game_objects)
      g.Update(delta_time);
  }

  public void LateUpdate(double delta_time) {
    for (GameObject g : game_objects)
      g.LateUpdate(delta_time);
  }
}
