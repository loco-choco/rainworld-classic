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
    WALL,
    PIPE,
    MOVE,
    DELETE
  }

  private HashMap<Mode, EditorMode> modes;

  private ArrayList<Tile> tile_list;

  private Mode current_mode;

  public void OnCreated() {
    tile_list = new ArrayList<>();
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
    modes.put(Mode.MOVE, new MoveMode(this, inputs));
    modes.put(Mode.WALL, new WallMode(this, inputs));
    modes.put(Mode.DELETE, new DeleteMode(this, inputs));
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

  public void AddTile(Tile tile) {
    tile_list.add(tile);
  }

  public void RemoveTile(Tile tile) {
    tile_list.remove(tile);
    tile.Destroy();
  }

  public void ClearAllTiles() {
    for (Tile tile : tile_list)
      tile.Destroy();
    tile_list.clear();
  }

  public Tile GetTileUnderCursor() {
    Point2d cursor = inputs.GetMousePos();
    for (Tile tile : tile_list) {
      if (CollisionMath.CheckPointCollision(tile.GetGameObject().getCollider(), cursor))
        return tile;
    }
    return null;
  }

  private void SaveRoomToFile() {
    System.out.println("Saving room to file...");
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule awtModule = new SimpleModule("AWT Module");
    awtModule.addSerializer(Color.class, new ColorJsonSerializer());
    awtModule.addDeserializer(Color.class, new ColorJsonDeserializer());
    mapper.registerModule(awtModule);
    try {
      JsonGenerator generator = mapper.createGenerator(new File("room0.json"), JsonEncoding.UTF8);
      generator.writeStartObject();
      generator.writeArrayFieldStart("game_objects");
      for (Tile tile : tile_list)
        tile.SaveToJson(generator, mapper);
      generator.writeEndArray();
      generator.flush();
      generator.close();
      System.out.println("Room saved!");
    } catch (Exception e) {
      System.err.println("Issue writing room to file " + e.getMessage());
    }
  }

  private void SwitchMode() {
    boolean wall_mode = inputs.GetKeyPressed(KeyEvent.VK_W);
    boolean pipe_mode = inputs.GetKeyPressed(KeyEvent.VK_P);
    boolean exit_mode = inputs.GetKeyPressed(KeyEvent.VK_ESCAPE);
    boolean delete_mode = inputs.GetKeyPressed(KeyEvent.VK_D);
    boolean move_mode = inputs.GetKeyPressed(KeyEvent.VK_M);
    boolean save = inputs.GetKeyPressed(KeyEvent.VK_S);
    boolean clear = inputs.GetKeyPressed(KeyEvent.VK_DELETE);
    boolean new_room = inputs.GetKeyPressed(KeyEvent.VK_N);

    Mode new_mode = current_mode;
    switch (current_mode) {
      case Mode.NONE:
        if (wall_mode) {
          new_mode = Mode.WALL;
        } else if (pipe_mode)
          new_mode = Mode.PIPE;
        else if (delete_mode)
          new_mode = Mode.DELETE;
        else if (move_mode)
          new_mode = Mode.MOVE;
        else if (clear) {
          ClearAllTiles();
        } else if (save) {
          SaveRoomToFile();
        }
        break;
      case Mode.WALL:
      case Mode.PIPE:
      case Mode.MOVE:
      case Mode.DELETE:
        if (exit_mode) {
          System.out.println("No mode");
          new_mode = Mode.NONE;
        }
      default:
        break;
    }
    if (new_mode != current_mode) {
      if (modes.containsKey(current_mode))
        modes.get(current_mode).OnExitMode();
      current_mode = new_mode;
      if (modes.containsKey(current_mode))
        modes.get(current_mode).OnEnterMode();
    }
  }
}
