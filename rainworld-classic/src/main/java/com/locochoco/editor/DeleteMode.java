package com.locochoco.editor;

import com.locochoco.gameengine.*;

public class DeleteMode extends EditorSubmode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;

  public DeleteMode(EditorMode<?> mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
  }

  public void OnEnterSubmode() {
    System.out.println("\tDelete Mode");
    was_mouse_clicked = false;
  }

  public void OnLoopSubmode() {
    Tile tile;
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    if (mouse_clicked && !was_mouse_clicked) {
      tile = mode.GetTileUnderCursor();
      if (tile != null)
        mode.RemoveTile(tile);
    }
  }

  public void OnExitSubmode() {
  }

}
