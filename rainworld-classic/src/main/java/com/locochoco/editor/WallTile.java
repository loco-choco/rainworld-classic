package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WallTile extends Tile {
  WallSpawner wall;

  public WallTile(Point2d start, Point2d end) {
    super(start, end);
    try {
      wall = representation.addComponent(new WallSpawner());
      wall.file_name = "objects/enviroments/wall.json";
      wall.setEnabled(false);
    } catch (Exception e) {
      System.err.println("Couldn't add component");
    }
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
    wall.setEnabled(true);
    representation.Deserialize(generator, mapper);
    wall.setEnabled(false);
  }
}
