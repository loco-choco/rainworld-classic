package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxRenderer;

public class PipeEntranceTile extends PipeTile {

  public PipeEntranceTile(EditorController controller, String file_name, Point2d position, int id) {
    super(controller, file_name, position, id);
    BoxRenderer renderer = (BoxRenderer) (representation.getRenderer());
    renderer.layer = "foreground";
    renderer.SetWidth(5);
    renderer.SetHeight(5);
    renderer.SetCenter(null);
    renderer.SetColor(Color.RED);
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
    // spawner.setEnabled(true);
    representation.Serialize(generator, mapper);
    // spawner.setEnabled(false);
  }
}
