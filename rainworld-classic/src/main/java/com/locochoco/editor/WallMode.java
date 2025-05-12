package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class WallMode extends EditorSubmode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private Point2d wall_start_position;
  private Point2d wall_end_position;
  private EditorController controller;

  public WallMode(EditorController controller, EditorMode<?> mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
    this.controller = controller;
  }

  public void OnEnterSubmode() {
    System.out.println("\tWall Mode");
    wall_start_position = null;
    wall_end_position = null;
    was_mouse_clicked = false;
  }

  public void OnExitSubmode() {
  }

  public void OnLoopSubmode() {
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    if (mouse_clicked && !was_mouse_clicked) {
      if (wall_start_position == null)
        wall_start_position = inputs.GetMousePos();
      else if (wall_end_position == null) {
        wall_end_position = inputs.GetMousePos();

        if (wall_start_position.getX() > wall_end_position.getX()
            && wall_start_position.getY() > wall_end_position.getY()) {
          Point2d tmp = wall_end_position;
          wall_end_position = wall_start_position;
          wall_start_position = tmp;
        }
      }
    }
    was_mouse_clicked = mouse_clicked;
    if (wall_start_position != null && wall_end_position != null) {
      WallTile wall = new WallTile(controller.RoundClosestPointDown(wall_start_position),
          controller.RoundClosestPointUp(wall_end_position));
      mode.AddTile(wall);
      wall_start_position = null;
      wall_end_position = null;
    }
  }

}
