package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxRenderer;

public class CreatureSpawnerTile extends Tile {
  private CreatureSpawner spawner;

  public CreatureSpawnerTile(EditorController controller, Point2d position) {
    super(controller.RoundClosestPointDown(position), controller.RoundClosestPointUp(position));

    try {
      spawner = representation.addComponent(new CreatureSpawner());
      spawner.file_name = "objects/creatures/slugcat.json";
      spawner.setEnabled(false);
    } catch (Exception e) {
      System.err.println("Couldn't add component");
    }
    BoxRenderer renderer = (BoxRenderer) (representation.getRenderer());
    renderer.SetColor(Color.YELLOW);
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
    spawner.setEnabled(true);
    representation.Deserialize(generator, mapper);
    spawner.setEnabled(false);
  }
}
