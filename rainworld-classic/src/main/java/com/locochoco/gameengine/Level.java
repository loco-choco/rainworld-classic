package com.locochoco.gameengine;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.locochoco.serialization.ColorJsonDeserializer;
import com.locochoco.serialization.ColorJsonSerializer;

/**
 * Abstraction over the loaded objects in game
 */
public class Level {
  private ArrayList<GameObject> game_objects;
  private ArrayList<GameObject> objects_to_be_added;
  private Camera main_camera;

  public Level() {
    game_objects = new ArrayList<>();
    objects_to_be_added = new ArrayList<>();
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
    objects_to_be_added.add(gameObject);
    gameObject.setEnabled(true);
  }

  public void AwakeObjects() {
    for (GameObject go : objects_to_be_added) {
      game_objects.add(go);
    }
    objects_to_be_added.clear();
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

  private static ObjectMapper GetMapper() {
    if (mapper == null) {
      mapper = new ObjectMapper();
      SimpleModule awtModule = new SimpleModule("AWT Module");
      awtModule.addSerializer(Color.class, new ColorJsonSerializer());
      awtModule.addDeserializer(Color.class, new ColorJsonDeserializer());
      mapper.registerModule(awtModule);
      mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
      mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
    }
    return mapper;
  }

  public void SaveLevelToJson(String filename) throws FileNotFoundException, IOException {
    System.out.printf("Saving level to %s\n", filename);
    ObjectMapper mapper = GetMapper();
    JsonGenerator generator = mapper.createGenerator(
        new File(filename),
        JsonEncoding.UTF8);
    generator.writeStartObject();

    ArrayList<GameObject> root_objects = new ArrayList<>();
    for (GameObject go : game_objects) {
      if (go.getParent() == null)
        root_objects.add(go);
    }

    generator.writeArrayFieldStart("game_objects");
    for (GameObject game_object : root_objects) { // Starts deserialization from root
      game_object.Serialize(generator, mapper);
    }
    generator.writeEndArray();
    generator.writeEndObject();
    generator.flush();
    generator.close();
    System.out.printf("Finished saving level to %s\n", filename);
  }

  public static Level ReadLevelFromJson(String filename) throws FileNotFoundException, IOException {
    System.out.printf("Loading level %s\n", filename);
    Level new_level = new Level();
    ObjectMapper mapper = GetMapper();
    FileReader json_file = new FileReader(filename);
    JsonNode root = mapper.readTree(json_file);
    JsonNode game_objects = root.get("game_objects");
    for (JsonNode game_object : game_objects) { // Builds the gameobjects in the level
      new_level.AddGameObject(GameObject.Deserialize(game_object, mapper));
    }
    System.out.printf("Finished building level %s\n", filename);
    return new_level;
  }

  public GameObject LoadGameObjectFromJson(String filename, GameObject go_root) {
    System.out.printf("Loading gameobject from %s\n", filename);
    ObjectMapper mapper = GetMapper();
    try {
      FileReader json_file = new FileReader(filename);
      JsonNode root = mapper.readTree(json_file);
      GameObject go = GameObject.Deserialize(root, mapper);
      go.setParent(go_root);
      AddGameObject(go);
      System.out.printf("Finished loading gameobject in %s\n", filename);
      return go;
    } catch (Exception e) {
      System.err.printf("Issues loading gameobject from %s: %s\n", filename, e.getMessage());
    }
    return null;
  }

  public void AdditiveLevelLoad(String filename, GameObject go_root) {
    System.out.printf("Additively loading level %s\n", filename);
    try {
      ObjectMapper mapper = GetMapper();
      FileReader json_file = new FileReader(filename);
      JsonNode root = mapper.readTree(json_file);
      JsonNode game_objects = root.get("game_objects");
      for (JsonNode game_object : game_objects) { // Builds the gameobjects in the level
        GameObject go = GameObject.Deserialize(game_object, mapper);
        go.setParent(go_root);
        AddGameObject(go);
      }
      System.out.printf("Finished loading additively level %s\n", filename);
    } catch (Exception e) {
      System.err.printf("Issues loading additively level %s\n", filename);
    }

  }
}
