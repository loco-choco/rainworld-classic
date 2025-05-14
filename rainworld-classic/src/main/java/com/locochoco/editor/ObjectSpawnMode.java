package com.locochoco.editor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

class ObjectTypes {
  public String file_path;
  public Color color;

  public ObjectTypes(String file_path, Color color) {
    this.file_path = file_path;
    this.color = color;
  }

  public String GetFilePath() {
    return file_path;
  }

  public Color GetColor() {
    return color;
  }
}

public class ObjectSpawnMode extends EditorSubmode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private boolean was_next_object;
  private EditorController controller;

  private ArrayList<ObjectTypes> types;
  private int current_object_selected;

  public ObjectSpawnMode(EditorController controller, EditorMode<?> mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
    this.controller = controller;
    current_object_selected = 0;
    types = new ArrayList<>();
    types.add(new ObjectTypes("objects/creatures/slugcat.json", Color.YELLOW));
    types.add(new ObjectTypes("objects/items/pebble.json", Color.LIGHT_GRAY));
    types.add(new ObjectTypes("objects/items/fruit.json", Color.MAGENTA));
  }

  public void OnEnterSubmode() {
    System.out.println("\tObject Spawn Mode");
    was_mouse_clicked = false;
    was_next_object = false;
    current_object_selected = 0;
    System.out.printf("\t\tObject selected: %s\n", types.get(current_object_selected).GetFilePath());
  }

  public void OnExitSubmode() {
  }

  public void OnLoopSubmode() {
    boolean next_object = inputs.GetKeyPressed(KeyEvent.VK_T);
    if (next_object && !was_next_object) {
      current_object_selected = (current_object_selected + 1) % types.size();
      System.out.printf("\t\tObject selected: %s\n", types.get(current_object_selected).GetFilePath());
    }
    was_next_object = next_object;

    boolean mouse_clicked = inputs.GetMouseLeftClick();
    if (mouse_clicked && !was_mouse_clicked && mode.GetTileUnderCursor() == null) {
      ObjectTypes type = types.get(current_object_selected);
      Point2d mouse_pos = inputs.GetMousePos();
      ObjectTile object = new ObjectTile(controller.RoundClosestPointDown(mouse_pos),
          controller.RoundClosestPointUp(mouse_pos), type.GetColor(), type.GetFilePath());
      mode.AddTile(object);
    }
    was_mouse_clicked = mouse_clicked;
  }

}
