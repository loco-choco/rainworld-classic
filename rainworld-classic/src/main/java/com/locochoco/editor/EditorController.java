package com.locochoco.editor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector2d;
import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.locochoco.gameengine.*;
import com.locochoco.serialization.ColorJsonDeserializer;
import com.locochoco.serialization.ColorJsonSerializer;

public class EditorController extends Component {

  private static EditorController instance;
  private String currently_loaded_region;
  private String currently_loaded_room;
  private double tile_size;

  private InputAPI inputs;

  public enum Mode {
    NONE,
    OBJECT,
    PIPE
  }

  private HashMap<Mode, EditorMode<?>> modes;

  private Mode current_mode;

  public void OnCreated() {
    if (instance == null)
      instance = this;
    current_mode = Mode.NONE;
    tile_size = 10;
    modes = new HashMap<>();
    currently_loaded_region = "outskirts";
    currently_loaded_room = "room0";
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
    if (instance == this)
      instance = null;
  }

  public void Start() {
    inputs = GameEngine.getGameEngine().getInputs();
    modes.put(Mode.OBJECT, new ObjectMode(this, inputs));
    modes.put(Mode.PIPE, new PipeMode(this, inputs));

    // Load room file
    OpenRoomFromFile(currently_loaded_room, currently_loaded_region);
  }

  public static EditorController GetInstance() {
    return instance;
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
    SwitchMode();
    if (modes.containsKey(current_mode))
      modes.get(current_mode).OnLoopMode();
  }

  public void LateUpdate(double delta_time) {
  }

  public String GetCurrentMode() {
    if (modes.containsKey(current_mode))
      return current_mode.name();
    return "";
  }

  public String GetModeStatus() {
    if (!modes.containsKey(current_mode))
      return "";
    String status = modes.get(current_mode).GetSubmodeStatus();
    String submode = modes.get(current_mode).GetSubmode();
    if (status.isEmpty())
      return submode;
    return String.format("%s : %s", submode, status);
  }

  public Point2d RoundClosestPointDown(Point2d point) {
    double x, y;
    x = Math.floor(point.getX() / tile_size) * tile_size;
    y = Math.floor(point.getY() / tile_size) * tile_size;
    return new Point2d(x, y);
  }

  public Point2d RoundClosestPointUp(Point2d point) {
    double x, y;
    x = Math.ceil(point.getX() / tile_size) * tile_size;
    y = Math.ceil(point.getY() / tile_size) * tile_size;
    return new Point2d(x, y);
  }

  public Point2d RoundClosestPoint(Point2d point) {
    double x, y;
    x = Math.round(point.getX() / tile_size) * tile_size;
    y = Math.round(point.getY() / tile_size) * tile_size;
    return new Point2d(x, y);
  }

  private void SaveRoomToFile(String room_name, String region_name) {
    System.out.printf("Saving room %s to file...\n", room_name);
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule awtModule = new SimpleModule("AWT Module");
    awtModule.addSerializer(Color.class, new ColorJsonSerializer());
    awtModule.addDeserializer(Color.class, new ColorJsonDeserializer());
    mapper.registerModule(awtModule);
    try {
      JsonGenerator generator = mapper.createGenerator(
          new File(String.format("levels/regions/%s/%s.json", region_name, room_name)),
          JsonEncoding.UTF8);
      generator.writeStartObject();
      generator.writeArrayFieldStart("game_objects");
      generator.writeStartObject();
      generator.writePOJOField("name", room_name);
      generator.writeArrayFieldStart("children");

      for (EditorMode<?> mode : modes.values())
        mode.SerializeTiles(generator, mapper);

      generator.writeEndArray();
      generator.writeEndObject();
      generator.writeEndArray();
      generator.flush();
      generator.close();
      System.out.println("Room saved!");
    } catch (Exception e) {
      System.err.println("Issue writing room to file " + e.getMessage());
    }
  }

  private void OpenRoomFromFile(String room_name, String region_name) {
    System.out.printf("Opening room from file %s...\n", room_name);
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule awtModule = new SimpleModule("AWT Module");
    awtModule.addSerializer(Color.class, new ColorJsonSerializer());
    awtModule.addDeserializer(Color.class, new ColorJsonDeserializer());
    mapper.registerModule(awtModule);
    FileReader json_file;
    JsonNode root;
    try {
      json_file = new FileReader(String.format("levels/regions/%s/%s.json", region_name, room_name));
      root = mapper.readTree(json_file);
    } catch (Exception e) {
      System.err.printf("Issues reading room json: %s\n", e.getMessage());
      return;
    }
    JsonNode game_objects = root.get("game_objects");
    for (JsonNode go : game_objects) {
      if (!go.get("name").asText().equals(room_name))
        continue;
      for (EditorMode<?> mode : modes.values())
        mode.DeserializeTiles(go, mapper);
    }
  }

  boolean was_any_pressed = false;

  private void SwitchMode() {
    boolean object = inputs.GetKeyPressed(KeyEvent.VK_O);
    boolean pipe = inputs.GetKeyPressed(KeyEvent.VK_P);
    boolean exit = inputs.GetKeyPressed(KeyEvent.VK_ESCAPE);
    boolean save = inputs.GetKeyPressed(KeyEvent.VK_S);
    boolean new_room = inputs.GetKeyPressed(KeyEvent.VK_N);

    Mode new_mode = current_mode;
    if (!was_any_pressed &&
        (!modes.containsKey(current_mode) || modes.get(current_mode).IsAtDefaultSubmode())) {
      switch (current_mode) {
        case Mode.NONE:
          if (object)
            new_mode = Mode.OBJECT;
          else if (pipe)
            new_mode = Mode.PIPE;
          if (save) {
            SaveRoomToFile(currently_loaded_room, currently_loaded_region);
          }
          break;
        default:
          if (exit) {
            System.out.println("No mode");
            new_mode = Mode.NONE;
          }
          break;
      }
    }
    was_any_pressed = object || pipe || exit || save || new_room;
    if (new_mode != current_mode) {
      if (modes.containsKey(current_mode))
        modes.get(current_mode).OnExitMode();
      current_mode = new_mode;
      if (modes.containsKey(current_mode))
        modes.get(current_mode).OnEnterMode();
    }
  }
}
