package com.locochoco.editor;

import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.*;

enum SubMode {
  NONE,
  WALL,
  SPAWNER,
  MOVE,
  DELETE
}

public class ObjectMode extends EditorMode<SubMode> {

  boolean was_any_pressed;

  public ObjectMode(EditorController controller, InputAPI inputs) {
    super(controller, inputs, SubMode.NONE);
    AddSubmode(SubMode.WALL, new WallMode(controller, this, inputs));
    AddSubmode(SubMode.SPAWNER, new ObjectSpawnMode(controller, this, inputs));
    AddSubmode(SubMode.MOVE, new MoveMode(controller, this, inputs));
    AddSubmode(SubMode.DELETE, new DeleteMode(this, inputs));
  }

  public void OnEnterMode() {
    System.out.println("Object Mode");
    SwitchMode(SubMode.NONE);
    was_any_pressed = true;
  }

  public void OnLoopMode() {
    boolean wall = inputs.GetKeyPressed(KeyEvent.VK_W);
    boolean spawner = inputs.GetKeyPressed(KeyEvent.VK_C);
    boolean delete = inputs.GetKeyPressed(KeyEvent.VK_D);
    boolean move = inputs.GetKeyPressed(KeyEvent.VK_M);
    boolean clear = inputs.GetKeyPressed(KeyEvent.VK_DELETE);
    boolean exit = inputs.GetKeyPressed(KeyEvent.VK_ESCAPE);

    SubMode new_mode = GetCurrentSubmode();
    if (!was_any_pressed) {
      switch (GetCurrentSubmode()) {
        case SubMode.NONE:
          if (wall)
            new_mode = SubMode.WALL;
          else if (spawner)
            new_mode = SubMode.SPAWNER;
          else if (move)
            new_mode = SubMode.MOVE;
          else if (delete)
            new_mode = SubMode.DELETE;
          if (clear) {
            ClearAllTiles();
          }
          break;
        default:
          if (exit)
            new_mode = SubMode.NONE;
          break;
      }
    }
    was_any_pressed = wall || spawner || exit || delete || move || clear;
    SwitchMode(new_mode);

    super.OnLoopMode();
  }

  public void OnExitMode() {
  }

  public void SerializeTiles(JsonGenerator generator, ObjectMapper mapper) {
    try {
      generator.writeStartObject();
      generator.writePOJOField("name", "objects");

      generator.writeArrayFieldStart("children");
      for (Tile tile : tiles)
        tile.SaveToJson(generator, mapper);
      generator.writeEndArray();

      generator.writeEndObject();
    } catch (Exception e) {
      System.err.printf("Issues serializing ObjectTiles: %s\n", e.getMessage());
    }
  }

  public void DeserializeTiles(JsonNode room, ObjectMapper mapper) {
    JsonNode rooms_children = room.get("children");
    for (JsonNode child : rooms_children) {
      if (!child.get("name").asText().equals("objects"))
        continue;
      System.out.println("Found Objects gameobject, deserializing it...");
      JsonNode objects = child.get("children");
      for (JsonNode object : objects) {
        Iterator<Entry<String, JsonNode>> itrr = object.get("components").fields();
        while (itrr.hasNext()) {
          Entry<String, JsonNode> component = itrr.next();
          String type = component.getKey();
          JsonNode gen = component.getValue();
          if (type.equals(WallGenerator.class.getName())) {
            WallTile wall = new WallTile();
            wall.ReadFromJson(gen, mapper);
            AddTile(wall);
          } else if (type.equals(ObjectGenerator.class.getName())) {
            ObjectTile obj = new ObjectTile();
            obj.ReadFromJson(gen, mapper);
            AddTile(obj);
          }
        }
      }
      return;
    }

  }
}
