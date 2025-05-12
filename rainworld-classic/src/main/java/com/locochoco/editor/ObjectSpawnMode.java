package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class ObjectSpawnMode extends EditorSubmode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private EditorController controller;

  private String object_file_path;
  private Color tile_color;

  public ObjectSpawnMode(EditorController controller, EditorMode<?> mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
    this.controller = controller;
    object_file_path = "objects/creatures/slugcat.json";
    tile_color = Color.YELLOW;
  }

  public void OnEnterSubmode() {
    System.out.println("\tObject Spawn Mode");
    was_mouse_clicked = false;
  }

  public void OnExitSubmode() {
  }

  public void OnLoopSubmode() {
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    if (mouse_clicked && !was_mouse_clicked) {
      Point2d mouse_pos = inputs.GetMousePos();
      ObjectTile object = new ObjectTile(controller.RoundClosestPointDown(mouse_pos),
          controller.RoundClosestPointUp(mouse_pos), tile_color, object_file_path);
      mode.AddTile(object);
    }
    was_mouse_clicked = mouse_clicked;
  }

}
