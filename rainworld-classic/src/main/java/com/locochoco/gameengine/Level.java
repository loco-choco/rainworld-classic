package com.locochoco.gameengine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstraction over the loaded objects in game
 */
public class Level {
  private ArrayList<GameObject> game_objects;

  public Level() {
    game_objects = new ArrayList<>();
  }

  public ArrayList<GameObject> getGameObjects() {
    return game_objects;
  }

  public void AddGameObject(GameObject gameObject) {
    game_objects.add(gameObject);
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

  private static ObjectMapper mapper;

  public static Level ReadLevelFromJson(String filename) throws FileNotFoundException, IOException {
    Level new_level = new Level();
    if (mapper == null)
      mapper = new ObjectMapper();
    FileReader json_file = new FileReader(filename);
    JsonNode root = mapper.readTree(json_file);
    JsonNode game_objects = root.get("game_objects");
    for (JsonNode game_object : game_objects) {
      GameObject go = new GameObject();
      new_level.AddGameObject(go);

    }

    return new_level;
  }
}
