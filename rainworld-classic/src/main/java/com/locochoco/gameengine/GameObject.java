package com.locochoco.gameengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Representation of a Object in game
 */
public class GameObject {
  private ArrayList<Component> components;
  private Transform transform;
  private Collider collider;
  private RigidBody rigidbody;
  private Renderer renderer;

  private GameObject parent;
  private ArrayList<GameObject> children;
  private boolean enabled;

  private String name;

  private boolean marked_to_destruction;

  public GameObject() {
    parent = null;
    children = new ArrayList<>();
    enabled = true;
    name = "";
    marked_to_destruction = false;

    components = new ArrayList<>();
    transform = new Transform();
    components.add(transform);
    transform.setGameObject(this);
    transform.OnCreated();
  }

  // TODO Make addComponent receive a Class object and THEN create the components
  // instead of this mess
  public <Comp extends Component> Comp addComponent(Comp component) throws Exception {
    if (component instanceof Transform trans) {
      if (transform != null)
        throw new Exception("There is already a transform in this GameObject");
      transform = trans;
    } else if (component instanceof RigidBody rigid) {
      if (rigidbody != null)
        throw new Exception("There is already a rigidbody in this GameObject");
      rigidbody = rigid;
    } else if (component instanceof Collider col) {
      if (collider != null)
        throw new Exception("There is already a collider in this GameObject");
      collider = col;
    } else if (component instanceof Renderer rend) {
      if (renderer != null)
        throw new Exception("There is already a renderer in this GameObject");
      renderer = rend;
    }
    components.add(component);
    component.setGameObject(this);
    component.OnCreated();
    return component;
  }

  // The enabled condition depends on its parent, so we go up the tree to see who
  // is the first not enabled, or if we reached the end
  public boolean isEnabled() {
    if (!enabled)
      return false;
    if (parent == null)
      return enabled;
    return enabled && parent.isEnabled();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void MarkToDestruction() {
    marked_to_destruction = true;
  }

  public boolean isMarkedToDestruction() {
    if (marked_to_destruction)
      return true;
    if (parent == null)
      return marked_to_destruction;
    return marked_to_destruction || parent.isMarkedToDestruction();
  }

  public Transform getTransform() {
    return transform;
  }

  public Collider getCollider() {
    return collider;
  }

  public RigidBody getRigidBody() {
    return rigidbody;
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public Component getComponent(Class<?> component) {
    if (!Component.class.isAssignableFrom(component))
      return null;
    for (Component attached_component : components) {
      if (component.isAssignableFrom(attached_component.getClass()))
        return attached_component;
    }
    return null;
  }

  public void setEnabled(boolean enable) {
    this.enabled = enable;
  }

  public void Start() {
    for (Component c : components)
      if (c.isEnabled() && !c.getHasStarted()) {
        c.Start();
        c.setHasStarted(true);
      }
  }

  public void PhysicsUpdate(double delta_time) {
    for (Component c : components)
      if (c.isEnabled())
        c.PhysicsUpdate(delta_time);
  }

  public void GraphicsUpdate(double delta_time) {
    for (Component c : components)
      if (c.isEnabled())
        c.GraphicsUpdate(delta_time);
  }

  public void Update(double delta_time) {
    for (Component c : components)
      if (c.isEnabled())
        c.Update(delta_time);
  }

  public void LateUpdate(double delta_time) {
    for (Component c : components)
      if (c.isEnabled())
        c.LateUpdate(delta_time);
  }

  public void DestroyMarkedComponents() {
    Iterator<Component> ittr = components.iterator();
    while (ittr.hasNext()) {
      Component c = ittr.next();
      if (c.isMarkedToDestruction()) {
        c.OnDestroyed();
        ittr.remove();
      }
    }
  }

  public void Destroy() {
    for (Component c : components)
      c.OnDestroyed();

  }

  public GameObject getParent() {
    return parent;
  }

  public void setParent(GameObject parent) throws Exception {
    if (parent == this)
      throw new Exception("GameObject can't parent to itself!");
    if (this.parent != parent) {
      Transform t = getTransform();
      Point2d global_pos = t.getGlobalPosition();
      this.parent.removeChild(this);
      parent.addChild(this);
      t.setGlobalPosition(global_pos);
    }
    this.parent = parent;
  }

  public GameObject findFirstChild(String child_name) {
    for (GameObject child : children) {
      if (child.getName().equals(child_name))
        return child;
    }
    return null;
  }

  public ArrayList<GameObject> getChildren() {
    return children;
  }

  public boolean addChild(GameObject child) throws Exception {
    child.setParent(this);
    return children.add(child);
  }

  public boolean removeChild(GameObject child) {
    return children.remove(child);
  }

  public static GameObject CreateGameObjectFromJson(JsonNode json, ObjectMapper mapper) {

    GameObject go = new GameObject();
    // Enabled when created
    JsonNode enable = json.get("enable");
    if (enable != null)
      go.setEnabled(enable.asBoolean());
    // Name
    JsonNode name = json.get("name");
    String go_name = "default_name";
    if (name != null)
      go_name = name.asText();
    go.setName(go_name);
    System.out.printf("New Game Object %s!\n", go_name);
    // Components
    JsonNode components = json.get("components");
    if (components != null)
      ReadComponents(components, mapper, go);
    JsonNode children = json.get("children");
    if (children != null)
      ReadChildren(children, mapper, go);
    return go;
  }

  private static void ReadComponents(JsonNode components_json, ObjectMapper mapper, GameObject go) {
    // Components
    Iterator<Entry<String, JsonNode>> components = components_json.fields();

    while (components.hasNext()) { // Builds the components of the game object
      Entry<String, JsonNode> component = components.next();
      String component_type = component.getKey();
      System.out.printf("\tNew Component %s!\n", component_type);
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
      Iterator<Entry<String, JsonNode>> fields = component.getValue().fields();
      while (fields.hasNext()) { // Fills the fields of each component
        Entry<String, JsonNode> field = fields.next();
        String field_name = field.getKey();
        Field component_field;
        try {
          component_field = component_class.getField(field_name);
        } catch (NoSuchFieldException exception) {
          System.err.printf("Public field %s not found on component of type %s!\n",
              field_name, component_type);
          continue;
        }
        Object new_field_val = mapper.convertValue(field.getValue(), component_field.getType());
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

  private static void ReadChildren(JsonNode children_json, ObjectMapper mapper, GameObject go) {
    for (JsonNode child_json : children_json) {
      GameObject child = CreateGameObjectFromJson(child_json, mapper);
      try {
        go.addChild(child);
      } catch (Exception e) {
        System.err.printf("Issue adding child to game object!\n");
      }
    }
  }
}
