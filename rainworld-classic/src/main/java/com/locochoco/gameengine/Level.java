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
  private Camera main_camera;

  public Level() {
    game_objects = new ArrayList<>();
    main_camera = null;
  }

  public void setMainCamera(Camera camera) {
    if (main_camera != null)
      main_camera.setIsMainCamera(false);
    main_camera = camera;
  }

  public Camera getMainCamera() {
    return main_camera;
  }

  public ArrayList<GameObject> getGameObjects() {
    return game_objects;
  }

  public void AddGameObject(GameObject gameObject) {
    for (GameObject child : gameObject.getChildren()) {
      AddGameObject(child);
    }
    game_objects.add(gameObject);
    gameObject.setEnabled(true);
  }

  public void StartObjects() {
    for (GameObject g : game_objects)
      if (g.isEnabled())
        g.Start();
  }

  public void PhysicsUpdate(double delta_time) {
    for (GameObject g : game_objects)
      if (g.isEnabled())
        g.PhysicsUpdate(delta_time);
  }

  public void GraphicsUpdate(double delta_time) {
    for (GameObject g : game_objects)
      if (g.isEnabled())
        g.GraphicsUpdate(delta_time);
  }

  public void Update(double delta_time) {
    for (GameObject g : game_objects)
      if (g.isEnabled())
        g.Update(delta_time);
  }

  public void LateUpdate(double delta_time) {
    for (GameObject g : game_objects)
      if (g.isEnabled())
        g.LateUpdate(delta_time);
  }

  public void DestroyAllGameObjects() {
    for (GameObject g : game_objects) {
      g.Destroy();
    }
    game_objects.clear();
  }

  public void DestroyMarkedGameObjects() {
    // TODO BY CHANGING THE LIST OF GO's TO A TREE STRUCTURE WE CAN MAKE THIS GO
    // FASTER
    ArrayList<GameObject> gos_to_destroy = new ArrayList<>();
    for (GameObject g : game_objects) {
      if (g.isMarkedToDestruction()) {
        gos_to_destroy.add(g);
        g.Destroy();
      } else {
        g.DestroyMarkedComponents();
      }
    }
    game_objects.removeAll(gos_to_destroy);
  }

  private static ObjectMapper mapper;

  public static Level ReadLevelFromJson(String filename) throws FileNotFoundException, IOException {
    Level new_level = new Level();
    if (mapper == null)
      mapper = new ObjectMapper();
    FileReader json_file = new FileReader(filename);
    JsonNode root = mapper.readTree(json_file);
    JsonNode game_objects = root.get("game_objects");
    for (JsonNode game_object : game_objects) { // Builds the gameobjects in the level
      new_level.AddGameObject(GameObject.CreateGameObjectFromJson(game_object, mapper));
    }
    System.out.printf("Finished building level %s\n", filename);
    return new_level;
  }
}
