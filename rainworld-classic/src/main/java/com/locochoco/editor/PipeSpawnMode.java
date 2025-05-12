package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class PipeSpawnMode extends EditorSubmode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private EditorController controller;
  private Tile selected_pipe;

  public PipeSpawnMode(EditorController controller, PipeMode mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
    this.controller = controller;
  }

  public void OnEnterSubmode() {
    System.out.println("\tPipe Spawn Mode");
    was_mouse_clicked = false;
  }

  public void OnExitSubmode() {
  }

  public void OnLoopSubmode() {
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    Tile pressed_pipe;
    if (mouse_clicked && !was_mouse_clicked) {
      pressed_pipe = mode.GetTileUnderCursor();
      if (selected_pipe == null && pressed_pipe == null) {
        ((PipeMode) mode).AddEntrance(inputs.GetMousePos());
      }
    }
    was_mouse_clicked = mouse_clicked;
  }

}
