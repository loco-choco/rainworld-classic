package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxRenderer;

public class ObjectTile extends Tile {
  private String object_file_path;
  protected String generator_name;

  public ObjectTile(Point2d start, Point2d end, Color tile_color, String object_file_path) {
    super(start, end, tile_color);
    this.object_file_path = object_file_path;
    this.generator_name = ObjectGenerator.class.getName();
  }

  protected void FieldsToSave(JsonGenerator generator, ObjectMapper mapper) throws Exception {
    generator.writePOJOField("position", representation.getTransform().getGlobalPosition());
    generator.writePOJOField("file_name", object_file_path);
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
    try {
      generator.writeStartObject();

      generator.writePOJOField("name", "object");

      generator.writeFieldName("components");
      generator.writeStartObject();

      generator.writeFieldName(generator_name);
      generator.writeStartObject();
      FieldsToSave(generator, mapper);
      generator.writeEndObject();

      generator.writeEndObject();

      generator.writeEndObject();
    } catch (Exception e) {
      System.err.println("Issue serializing wall: " + e.getMessage());
    }
  }
}
