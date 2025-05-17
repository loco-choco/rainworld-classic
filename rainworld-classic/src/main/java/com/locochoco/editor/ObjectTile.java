package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
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

  public ObjectTile() {
    super(new Point2d(0, 0), new Point2d(10, 10), Color.RED);
    this.object_file_path = "";
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

  public void ReadFromJson(JsonNode node, ObjectMapper mapper) {
    Point2d pos = (Point2d) mapper.convertValue(node.get("position"), Point2d.class);
    representation.getTransform().setGlobalPosition(pos);
    object_file_path = node.get("file_name").asText();
  }

}
