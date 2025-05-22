package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class MoveMode extends EditorSubmode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private Tile moving_tile;
  private Point2d moving_tile_og_pos;
  private Point2d initial_mouse_pos;
  private EditorController controller;

  public MoveMode(EditorController controller, EditorMode<?> mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
    this.controller = controller;
  }

  public void OnEnterSubmode() {
    System.out.println("\tMove Mode");
    was_mouse_clicked = false;
    moving_tile = null;
  }

  public void OnLoopSubmode() {
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    if (mouse_clicked && !was_mouse_clicked) {
      if (moving_tile == null) {
        moving_tile = mode.GetTileUnderCursor();
        if (moving_tile != null) {
          moving_tile_og_pos = moving_tile.GetGameObject().getTransform().getGlobalPosition();
          initial_mouse_pos = EditorUI.MousePosition();
        }
      } else
        moving_tile = null;
    }
    was_mouse_clicked = mouse_clicked;
    if (moving_tile != null) {
      Point2d movement = new Point2d(EditorUI.MousePosition());
      movement.sub(initial_mouse_pos);
      Point2d new_pos = new Point2d(moving_tile_og_pos);
      new_pos.add(controller.RoundClosestPoint(movement));
      moving_tile.GetGameObject().getTransform().setGlobalPosition(new_pos);
    }
  }

  public void OnExitSubmode() {
    if (moving_tile != null)
      moving_tile.GetGameObject().getTransform().setGlobalPosition(moving_tile_og_pos);
  }

}
