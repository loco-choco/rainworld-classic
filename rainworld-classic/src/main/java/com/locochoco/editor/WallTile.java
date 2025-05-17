package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxCollider;
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

  public WallTile() {
    super();
    width = 0;
    height = 0;
    this.generator_name = WallGenerator.class.getName();
  }

  protected void FieldsToSave(JsonGenerator generator, ObjectMapper mapper) throws Exception {
    super.FieldsToSave(generator, mapper);
    generator.writePOJOField("width", width);
    generator.writePOJOField("height", height);
  }

  public void ReadFromJson(JsonNode node, ObjectMapper mapper) {
    super.ReadFromJson(node, mapper);
    width = node.get("width").asInt();
    height = node.get("height").asInt();
    BoxRenderer renderer = (BoxRenderer) representation.getRenderer();
    renderer.SetWidth(width);
    renderer.SetHeight(height);
    renderer.SetCenter(null);
    BoxCollider collider = (BoxCollider) representation.getCollider();
    collider.setShape(width, height);
    collider.setCenter(null);
  }

}
