package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxRenderer;

public class PipeTile extends Tile {
  private int id;
  private int exit_id;
  private PipeMode mode;

  public PipeTile(EditorController controller, PipeMode mode, Point2d position, int id) {
    super(controller.RoundClosestPointDown(position), controller.RoundClosestPointUp(position));
    this.id = id;
    this.mode = mode;
    try {
      // spawner = representation.addComponent(new CreatureSpawner());
      // spawner.file_name = "objects/creatures/slugcat.json";
      // spawner.setEnabled(false);
    } catch (Exception e) {
      System.err.println("Couldn't add component");
    }
    BoxRenderer renderer = (BoxRenderer) (representation.getRenderer());
    renderer.layer = "foreground";
    renderer.SetColor(Color.RED);
  }

  public int GetId() {
    return id;
  }

  public void AttachExit(int id) {
    if (exit_id != id) {
      PipeTile pipe = mode.GetPipeTile(exit_id);
      if (pipe != null)
        pipe.exit_id = 0;
    }
    exit_id = id;
  }

  public int GetAttachedExit() {
    return exit_id;
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
    // spawner.setEnabled(true);
    representation.Deserialize(generator, mapper);
    // spawner.setEnabled(false);
  }
}
