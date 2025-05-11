package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class WallMode extends EditorMode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private Point2d wall_start_position;
  private Point2d wall_end_position;

  public WallMode(EditorController controller, InputAPI inputs) {
    super(controller);
    this.inputs = inputs;
  }

  public void OnEnterMode() {
    System.out.println("Wall Mode");
    wall_start_position = null;
    wall_end_position = null;
    was_mouse_clicked = false;
  }

  public void OnExitMode() {
  }

  public void OnLoopMode() {
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
      controller.AddTile(wall);
      wall_start_position = null;
      wall_end_position = null;
    }
  }

}
