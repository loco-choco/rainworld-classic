package com.locochoco.gameengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Representation of a Object in game
 */
public abstract class Component {

  private GameObject owner;

  public boolean enabled = true;
  private boolean has_started = false;

  private boolean marked_to_destrucion = false;

  public abstract void OnCreated();

  public abstract void OnEnabled();

  public abstract void OnDisabled();

  public abstract void OnDestroyed();

  public abstract void Start();

  public abstract void PhysicsUpdate(double delta_time);

  public abstract void GraphicsUpdate(double delta_time);

  public abstract void Update(double delta_time);

  public abstract void LateUpdate(double delta_time);

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enable) {
    if (enable && !enabled)
      OnEnabled();
    else if (!enable && enabled)
      OnDisabled();
    enabled = enable;
  }

  public void setHasStarted(boolean has_started) {
    this.has_started = has_started;
  }

  public boolean getHasStarted() {
    return has_started;
  }

  public void setGameObject(GameObject owner) {
    this.owner = owner;
  }

  public GameObject getGameObject() {
    return owner;
  }

  public void Destroy() {
    marked_to_destrucion = true;
  }

  public boolean isMarkedToDestruction() {
    return marked_to_destrucion;
  }

  public static void Serialize(Component component, JsonGenerator generator, ObjectMapper mapper) {
    try {
      generator.writeFieldName(component.getClass().getName());
      generator.writeStartObject();
      for (Field field : component.getClass().getFields()) {
        generator.writePOJOField(field.getName(), field.get(component));
      }
      generator.writeEndObject();
    } catch (Exception exception) {
      System.err.printf("Issue serializing component %s\n", component.getClass().getName());
    }

  }

  public static void Deserialize(String type, JsonNode component, ObjectMapper mapper, GameObject go) {
    Class<?> component_class;
    try {
      component_class = Class.forName(type);
      if (!Component.class.isAssignableFrom(component_class)) {
        System.err.printf("Class of type %s is not from Component superclass!\n", type);
        return;
      }
    } catch (ClassNotFoundException exception) {
      System.err.printf("Component of type %s not found!\n", type);
      return;
    }
    Constructor<?> component_constructor;
    try {
      component_constructor = component_class.getConstructor(new Class[] {});
    } catch (NoSuchMethodException exception) {
      System.err.printf("Could not find empty constructor for component of type %s!\n", type);
      return;
    }
    Component component_instance;
    if (component_class != Transform.class) { // Transform is created when the GameObject is created
      try {
        component_instance = (Component) component_constructor.newInstance(new Object[] {});
      } catch (Exception exception) {
        System.err.printf("Issue instantiating component of type %s!\n", type);
        return;
      }
      try {
        go.addComponent(component_instance);
      } catch (Exception exception) {
        System.err.printf("Component of type %s already exists!\n", type);
        return;
      }
    } else
      component_instance = go.getTransform();
    Iterator<Entry<String, JsonNode>> fields = component.fields();
    while (fields.hasNext()) { // Fills the fields on the component
      Entry<String, JsonNode> field = fields.next();
      String field_name = field.getKey();
      Field component_field;
      try {
        component_field = component_class.getField(field_name);
      } catch (NoSuchFieldException exception) {
        System.err.printf("Public field %s not found on component of type %s!\n",
            field_name, type);
        continue;
      }
      Object new_field_val = mapper.convertValue(field.getValue(), component_field.getType());
      try {
        component_field.set(component_instance, new_field_val);
      } catch (IllegalArgumentException exception) {
        System.err.printf("Field %s on component of type %s with wrong value type!\n",
            field_name, type);
        continue;
      } catch (IllegalAccessException exception) {
        System.err.printf("Field %s on component of type %s isn't public (what?)!\n",
            field_name, type);
        continue;
      }
    }
  }

}
