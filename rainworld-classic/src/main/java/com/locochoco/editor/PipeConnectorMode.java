package com.locochoco.editor;

import com.locochoco.gameengine.*;

public class PipeConnectorMode extends EditorSubmode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private PipeTile selected_pipe;

  public PipeConnectorMode(PipeMode mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
  }

  public void OnEnterSubmode() {
    System.out.println("\tPipe Connector Mode");
    was_mouse_clicked = false;
  }

  public void OnExitSubmode() {
  }

  public void OnLoopSubmode() {
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    PipeTile pressed_pipe;
    if (mouse_clicked && !was_mouse_clicked) {
      pressed_pipe = (PipeTile) mode.GetTileUnderCursor();
      if (selected_pipe == null && pressed_pipe != null) {
        selected_pipe = pressed_pipe;
      } else if (selected_pipe != null && pressed_pipe != null && selected_pipe != pressed_pipe) {
        selected_pipe.AttachExit(pressed_pipe.GetId());
        pressed_pipe.AttachExit(selected_pipe.GetId());
        System.out.printf("Attaching pipes %s -> %s\n", selected_pipe.GetId(),
            pressed_pipe.GetId());
        selected_pipe = null;
      }
    }
    was_mouse_clicked = mouse_clicked;
  }

}
