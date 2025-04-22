package com.locochoco.gameengine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    for (JsonNode game_object : game_objects) { // Builds the gameobjects in the level
      GameObject go = new GameObject();
      new_level.AddGameObject(go);
      JsonNode components = game_object.get("components");
      for (JsonNode component : components) { // Builds the components for each gameobject
        String component_type = component.get("type").asText();
        Class<?> component_class;
        try {
          component_class = Class.forName(component_type);
          if (!Component.class.isAssignableFrom(component_class)) {
            System.err.printf("Class of type %s is not from Component superclass!\n", component_type);
            continue;
          }
        } catch (ClassNotFoundException exception) {
          System.err.printf("Component of type %s not found!\n", component_type);
          continue;
        }
        Constructor<?> component_constructor;
        try {
          component_constructor = component_class.getConstructor(new Class[] {});
        } catch (NoSuchMethodException exception) {
          System.err.printf("Could not find empty constructor for component of type %s!\n", component_type);
          continue;
        }
        Component component_instance;
        if (component_class != Transform.class) { // Transform is created when the GameObject is created
          try {
            component_instance = (Component) component_constructor.newInstance(new Object[] {});
          } catch (Exception exception) {
            System.err.printf("Issue instantiating component of type %s!\n", component_type);
            continue;
          }
          try {
            go.addComponent(component_instance);
          } catch (Exception exception) {
            System.err.printf("Component of type %s already exists!\n", component_type);
            continue;
          }
        } else
          component_instance = go.getTransform();
        JsonNode component_fields = component.get("fields");
        for (JsonNode field : component_fields) { // Fills the fields of each component
          String field_name = field.get("name").asText();
          Field component_field;
          try {
            component_field = component_class.getField(field_name);
          } catch (NoSuchFieldException exception) {
            System.err.printf("Public field %s not found on component of type %s!\n",
                field_name, component_type);
            continue;
          }
          Object new_field_val = mapper.convertValue(field.get("value"), component_field.getType());
          try {
            component_field.set(component_instance, new_field_val);
          } catch (IllegalArgumentException exception) {
            System.err.printf("Field %s on component of type %s with wrong value type!\n",
                field_name, component_type);
            continue;
          } catch (IllegalAccessException exception) {
            System.err.printf("Field %s on component of type %s isn't public (what?)!\n",
                field_name, component_type);
            continue;
          }
        }
      }
    }

    return new_level;
  }
}
