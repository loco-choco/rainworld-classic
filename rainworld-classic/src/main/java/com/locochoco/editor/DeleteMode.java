package com.locochoco.editor;

import com.locochoco.gameengine.*;

public class DeleteMode extends EditorMode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;

  public DeleteMode(EditorController controller, InputAPI inputs) {
    super(controller);
    this.inputs = inputs;
  }

  public void OnEnterMode() {
    System.out.println("Delete Mode");
    was_mouse_clicked = false;
  }

  public void OnLoopMode() {
    Tile tile;
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    if (mouse_clicked && !was_mouse_clicked) {
      tile = controller.GetTileUnderCursor();
      if (tile != null)
        controller.RemoveTile(tile);

    }
  }

  public void OnExitMode() {
  }

}
