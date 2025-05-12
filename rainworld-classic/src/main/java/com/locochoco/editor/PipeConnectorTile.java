package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxRenderer;

public class PipeConnectorTile extends PipeTile {

  public PipeConnectorTile(EditorController controller, Point2d position, int id) {
    super(controller, position, id);
    BoxRenderer renderer = (BoxRenderer) (representation.getRenderer());
    renderer.layer = "foreground";
    renderer.SetWidth(5);
    renderer.SetHeight(5);
    renderer.SetCenter(null);
    renderer.SetColor(Color.YELLOW);
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
    // spawner.setEnabled(true);
    representation.Deserialize(generator, mapper);
    // spawner.setEnabled(false);
  }
}
