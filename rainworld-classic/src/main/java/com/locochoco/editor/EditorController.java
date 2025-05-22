package com.locochoco.editor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.locochoco.game.Room;
import com.locochoco.game.RoomLoader;
import com.locochoco.gameengine.*;
import com.locochoco.serialization.ColorJsonDeserializer;
import com.locochoco.serialization.ColorJsonSerializer;

public class EditorController extends Component {

  private static EditorController instance;
  private String room_prefix = "room";
  private int currently_loaded_room;
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
    currently_loaded_room = 0;
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
    LoadRoom(currently_loaded_room);
  }

  public void LoadNextRoom() {
    currently_loaded_room++;
    UnloadRoom();
    LoadRoom(currently_loaded_room);
  }

  public void LoadPrevRoom() {
    currently_loaded_room = currently_loaded_room == 0 ? 0 : currently_loaded_room - 1;
    UnloadRoom();
    LoadRoom(currently_loaded_room);
  }

  public void LoadRoom(int id) {
    System.out.println("Loading room" + id);
    OpenRoomFromFile(id);
  }

  public void UnloadRoom() {
    System.out.println("Unloading current room");
    for (EditorMode<?> mode : modes.values())
      mode.ClearAllTiles();
  }

  public void ReloadRoom() {
    UnloadRoom();
    LoadRoom(currently_loaded_room);
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

  public int GetCurrentRoom() {
    return currently_loaded_room;
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

  private void SaveRoomToFile(int room_id) {
    String room_name = room_prefix + room_id;
    System.out.printf("Saving room %s to file...\n", room_name);
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule awtModule = new SimpleModule("AWT Module");
    awtModule.addSerializer(Color.class, new ColorJsonSerializer());
    awtModule.addDeserializer(Color.class, new ColorJsonDeserializer());
    mapper.registerModule(awtModule);
    try {
      JsonGenerator generator = mapper.createGenerator(
          new File(String.format("levels/rooms/%s.json", room_name)),
          JsonEncoding.UTF8);
      generator.writeStartObject();
      generator.writeArrayFieldStart("game_objects");
      generator.writeStartObject();
      generator.writePOJOField("name", room_name);
      // Room Component at root to have room info (which room is next)
      generator.writeFieldName("components");
      generator.writeStartObject(); // Components Start
      generator.writeFieldName(Room.class.getName());
      generator.writeStartObject();
      String next_room = String.format("levels/rooms/%s%d.json", room_prefix, room_id + 1);
      generator.writePOJOField("next_room", next_room);
      generator.writeEndObject(); // Room end
      // Load Base Room Stuff
      generator.writeFieldName(RoomLoader.class.getName());
      generator.writeStartObject();
      generator.writePOJOField("file_name", "levels/rooms/base.json");
      generator.writeEndObject(); // Room loader end
      generator.writeEndObject(); // Component End
      // Store editor stuff
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

  private void OpenRoomFromFile(int room_id) {
    String room_name = room_prefix + room_id;
    File room_file = new File(String.format("levels/rooms/%s.json", room_name));
    if (!room_file.exists() || room_file.isDirectory()) {
      System.out.printf("No %s, opening clean project...", room_name);
      return;
    }
    System.out.printf("Opening %s...\n", room_name);
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule awtModule = new SimpleModule("AWT Module");
    awtModule.addSerializer(Color.class, new ColorJsonSerializer());
    awtModule.addDeserializer(Color.class, new ColorJsonDeserializer());
    mapper.registerModule(awtModule);
    FileReader json_file;
    JsonNode root;
    try {
      json_file = new FileReader(room_file);
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
    boolean next_room = inputs.GetKeyPressed(KeyEvent.VK_N);
    boolean prev_room = inputs.GetKeyPressed(KeyEvent.VK_B);
    boolean reload_room = inputs.GetKeyPressed(KeyEvent.VK_R);

    Mode new_mode = current_mode;
    if (!was_any_pressed &&
        (!modes.containsKey(current_mode) || modes.get(current_mode).IsAtDefaultSubmode())) {
      switch (current_mode) {
        case Mode.NONE:
          if (object)
            new_mode = Mode.OBJECT;
          else if (pipe)
            new_mode = Mode.PIPE;
          else if (save) {
            SaveRoomToFile(currently_loaded_room);
          } else if (next_room) {
            LoadNextRoom();
          } else if (prev_room) {
            LoadPrevRoom();
          } else if (reload_room) {
            ReloadRoom();
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
    was_any_pressed = object || pipe || exit || save || next_room || prev_room || reload_room;
    if (new_mode != current_mode) {
      if (modes.containsKey(current_mode))
        modes.get(current_mode).OnExitMode();
      current_mode = new_mode;
      if (modes.containsKey(current_mode))
        modes.get(current_mode).OnEnterMode();
    }
  }
}
