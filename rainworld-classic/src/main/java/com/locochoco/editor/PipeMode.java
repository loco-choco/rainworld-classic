package com.locochoco.editor;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.game.EntrancePipe;
import com.locochoco.gameengine.*;

enum PipeSubMode {
  NONE,
  SPAWNER,
  CONNECTOR,
  MOVE,
  DELETE
}

public class PipeMode extends EditorMode<PipeSubMode> {

  boolean was_any_pressed;
  private int last_id;

  private HashMap<Integer, PipeTile> pipes;

  private String pipe_entrance_file_name;
  private String pipe_connector_file_name;

  public PipeMode(EditorController controller, InputAPI inputs) {
    super(controller, inputs, PipeSubMode.NONE);
    AddSubmode(PipeSubMode.SPAWNER, new PipeSpawnMode(controller, this, inputs));
    AddSubmode(PipeSubMode.CONNECTOR, new PipeConnectorMode(this, inputs));
    AddSubmode(PipeSubMode.MOVE, new MoveMode(controller, this, inputs));
    AddSubmode(PipeSubMode.DELETE, new DeleteMode(this, inputs));
    last_id = 0;
    pipes = new HashMap<>();
    pipe_entrance_file_name = "objects/enviroments/pipe/entrance.json";
    pipe_connector_file_name = "objects/enviroments/pipe/connection.json";
  }

  public void OnEnterMode() {
    System.out.println("Pipe Mode");
    SwitchMode(PipeSubMode.NONE);
    was_any_pressed = true;
  }

  public void OnLoopMode() {
    boolean connector = inputs.GetKeyPressed(KeyEvent.VK_C);
    boolean spawner = inputs.GetKeyPressed(KeyEvent.VK_S);
    boolean delete = inputs.GetKeyPressed(KeyEvent.VK_D);
    boolean move = inputs.GetKeyPressed(KeyEvent.VK_M);
    boolean clear = inputs.GetKeyPressed(KeyEvent.VK_DELETE);
    boolean exit = inputs.GetKeyPressed(KeyEvent.VK_ESCAPE);

    PipeSubMode new_mode = GetCurrentSubmode();
    if (!was_any_pressed) {
      switch (GetCurrentSubmode()) {
        case PipeSubMode.NONE:
          if (connector)
            new_mode = PipeSubMode.CONNECTOR;
          else if (spawner)
            new_mode = PipeSubMode.SPAWNER;
          else if (move)
            new_mode = PipeSubMode.MOVE;
          else if (delete)
            new_mode = PipeSubMode.DELETE;
          if (clear) {
            ClearAllTiles();
          }
          break;
        default:
          if (exit)
            new_mode = PipeSubMode.NONE;
          break;
      }
    }
    was_any_pressed = connector || spawner || exit || delete || move || clear;
    SwitchMode(new_mode);

    super.OnLoopMode();
  }

  public void OnExitMode() {
  }

  public PipeEntranceTile AddEntrance(Point2d position) {
    last_id++;
    PipeEntranceTile pipe = new PipeEntranceTile(controller, pipe_entrance_file_name, position, last_id);
    AddTile(pipe);
    pipes.put(last_id, pipe);
    System.out.printf("Added entrance %s\n", last_id);
    return pipe;
  }

  public PipeConnectorTile AddConnector(Point2d position) {
    last_id++;
    PipeConnectorTile pipe = new PipeConnectorTile(controller, pipe_connector_file_name, position, last_id);
    AddTile(pipe);
    pipes.put(last_id, pipe);
    System.out.printf("Added entrance %s\n", last_id);
    return pipe;
  }

  public PipeTile GetPipeTile(int id) {
    return pipes.get(id);
  }

  public void SerializeTiles(JsonGenerator generator, ObjectMapper mapper) {
    try {
      generator.writeStartObject();

      generator.writePOJOField("name", "pipes");

      generator.writeFieldName("components");
      generator.writeStartObject();

      generator.writeFieldName(PipeGenerator.class.getName());
      generator.writeStartObject();
      generator.writeArrayFieldStart("pipes");
      for (Tile tile : tiles) {
        PipeTile pipe = (PipeTile) tile;
        PipeInfo info = new PipeInfo();
        info.id = pipe.GetId();
        ArrayList<Integer> connections = pipe.GetConnectionsIds();
        info.connections = connections.toArray(new Integer[connections.size()]);
        info.position = pipe.GetGameObject().getTransform().getGlobalPosition();
        info.file_name = pipe.GetFileName();
        generator.writePOJO(info);
      }
      generator.writeEndArray();
      generator.writeEndObject();

      generator.writeEndObject();

      generator.writeEndObject();
    } catch (Exception e) {
      System.err.println("Issue serializing pipes: " + e.getMessage());
    }
  }

  public void DeserializeTiles(JsonNode room, ObjectMapper mapper) {
    JsonNode rooms_children = room.get("children");
    for (JsonNode child : rooms_children) {
      if (!child.get("name").asText().equals("pipes"))
        continue;
      System.out.println("Found pipes gameobject, deserializing it...");
      JsonNode pipes = child.get("components").get(PipeGenerator.class.getName()).get("pipes");
      ArrayList<PipeInfo> infos = new ArrayList<>();
      for (JsonNode info_json : pipes) {
        try {
          PipeInfo info = (PipeInfo) mapper.convertValue(info_json, PipeInfo.class);
          infos.add(info);

        } catch (Exception e) {
          System.err.printf("Issue parsing PipeInfo %s\n", e.getMessage());
        }
      }
      // Create the pipes
      int max_id = 0;
      for (PipeInfo info : infos) {
        PipeTile pipe;
        if (info.file_name.contains("entrance")) {
          pipe = new PipeEntranceTile(controller, pipe_entrance_file_name, info.position, info.id);
        } else if (info.file_name.contains("connection")) {
          pipe = new PipeConnectorTile(controller, pipe_connector_file_name, info.position, info.id);
        } else {
          System.err.printf("Found not recognized pipe type %s\n", info.file_name);
          continue;
        }
        AddTile(pipe);
        this.pipes.put(info.id, pipe);
        if (max_id < info.id)
          max_id = info.id;
      }
      last_id = max_id;
      // Connect them
      for (PipeInfo info : infos) {
        PipeTile pipe = GetPipeTile(info.id);
        if (pipe == null)
          continue;
        for (int id : info.connections) {
          PipeTile other = GetPipeTile(id);
          if (other == null)
            continue;
          pipe.Connect(other);
        }
      }
      return;
    }
  }
}
