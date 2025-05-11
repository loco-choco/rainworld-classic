package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WallTile extends Tile {

  public WallTile(Point2d start, Point2d end) {
    super(start, end);
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
    representation.Deserialize(generator, mapper);
  }
}
