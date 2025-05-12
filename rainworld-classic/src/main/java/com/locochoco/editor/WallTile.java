package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxRenderer;

public class WallTile extends ObjectTile {
  private int width;
  private int height;

  public WallTile(Point2d start, Point2d end, String wall_file_path) {
    super(start, end, Color.CYAN, wall_file_path);
    BoxRenderer renderer = (BoxRenderer) representation.getRenderer();
    width = renderer.width;
    height = renderer.height;
    generator_name = WallGenerator.class.getName();
  }

  protected void FieldsToSave(JsonGenerator generator, ObjectMapper mapper) throws Exception {
    super.FieldsToSave(generator, mapper);
    generator.writePOJOField("width", width);
    generator.writePOJOField("height", height);
  }

}
