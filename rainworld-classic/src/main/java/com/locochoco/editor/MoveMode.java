package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class MoveMode extends EditorMode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private Tile moving_tile;
  private Point2d moving_tile_og_pos;
  private Point2d initial_mouse_pos;

  public MoveMode(EditorController controller, InputAPI inputs) {
    super(controller);
    this.inputs = inputs;
  }

  public void OnEnterMode() {
    System.out.println("Move Mode");
    was_mouse_clicked = false;
    moving_tile = null;
  }

  public void OnLoopMode() {
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    if (mouse_clicked && !was_mouse_clicked) {
      if (moving_tile == null) {
        moving_tile = controller.GetTileUnderCursor();
        if (moving_tile != null) {
          moving_tile_og_pos = moving_tile.GetGameObject().getTransform().getGlobalPosition();
          initial_mouse_pos = inputs.GetMousePos();
        }
      } else
        moving_tile = null;
    }
    was_mouse_clicked = mouse_clicked;
    if (moving_tile != null) {
      Point2d movement = new Point2d(inputs.GetMousePos());
      movement.sub(initial_mouse_pos);
      Point2d new_pos = new Point2d(moving_tile_og_pos);
      new_pos.add(controller.RoundClosestPoint(movement));
      moving_tile.GetGameObject().getTransform().setGlobalPosition(new_pos);
    }
  }

  public void OnExitMode() {
    if (moving_tile != null)
      moving_tile.GetGameObject().getTransform().setGlobalPosition(moving_tile_og_pos);
  }

}
