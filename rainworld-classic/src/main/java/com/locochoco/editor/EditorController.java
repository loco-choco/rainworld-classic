package com.locochoco.editor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector2d;
import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.locochoco.gameengine.*;
import com.locochoco.serialization.ColorJsonDeserializer;
import com.locochoco.serialization.ColorJsonSerializer;

public class EditorController extends Component {

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
    current_mode = Mode.NONE;
    tile_size = 10;
    modes = new HashMap<>();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
    inputs = GameEngine.getGameEngine().getInputs();
    modes.put(Mode.OBJECT, new ObjectMode(this, inputs));
    modes.put(Mode.PIPE, new PipeMode(this, inputs));
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

  private void SaveRoomToFile() {

    System.out.println("Saving room to file...");
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule awtModule = new SimpleModule("AWT Module");
    awtModule.addSerializer(Color.class, new ColorJsonSerializer());
    awtModule.addDeserializer(Color.class, new ColorJsonDeserializer());
    mapper.registerModule(awtModule);
    try {
      JsonGenerator generator = mapper.createGenerator(new File("levels/level0/room0.json"), JsonEncoding.UTF8);
      generator.writeStartObject();
      generator.writeArrayFieldStart("game_objects");

      for (EditorMode<?> mode : modes.values())
        mode.SerializeTiles(generator, mapper);

      generator.writeEndArray();
      generator.flush();
      generator.close();
      System.out.println("Room saved!");
    } catch (Exception e) {
      System.err.println("Issue writing room to file " + e.getMessage());
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
            SaveRoomToFile();
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
