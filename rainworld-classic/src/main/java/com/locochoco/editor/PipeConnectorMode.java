package com.locochoco.editor;

import java.util.ArrayList;

import com.locochoco.gameengine.*;

public class PipeConnectorMode extends EditorSubmode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private ArrayList<PipeTile> connection_pipes;
  private PipeTile last_pipe;

  public PipeConnectorMode(PipeMode mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
    connection_pipes = new ArrayList<>();
  }

  public void OnEnterSubmode() {
    System.out.println("\tPipe Connector Mode");
    was_mouse_clicked = false;
    last_pipe = null;
  }

  public void OnExitSubmode() {
    for (PipeTile pipe : connection_pipes) {
      pipe.ReciprocalDisconnectAll();
      mode.RemoveTile(pipe);
    }
    connection_pipes.clear();
  }

  public void OnLoopSubmode() {
    PipeMode mode = (PipeMode) this.mode;
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    PipeTile pressed_pipe;

    if (mouse_clicked && !was_mouse_clicked) {
      pressed_pipe = (PipeTile) mode.GetTileUnderCursor();
      if (last_pipe == null && pressed_pipe != null && pressed_pipe instanceof PipeEntranceTile) {
        last_pipe = pressed_pipe;
        System.out.printf("Starting Connection at %s\n", last_pipe.GetId());
      } else if (last_pipe != null && pressed_pipe == null) {
        PipeTile connector = mode.AddConnector(inputs.GetMousePos());
        last_pipe.ReciprocalConnection(connector);
        System.out.printf("Attaching Connector %s -> %s\n", last_pipe.GetId(),
            connector.GetId());
        connection_pipes.add(connector);
        last_pipe = connector;
      } else if (last_pipe != null && pressed_pipe != null && pressed_pipe instanceof PipeEntranceTile) {
        last_pipe.ReciprocalConnection(pressed_pipe);
        System.out.printf("Ending Connection with %s -> %s\n", last_pipe.GetId(), pressed_pipe.GetId());
        last_pipe = null;
        connection_pipes.clear();
      }
    }
    was_mouse_clicked = mouse_clicked;
  }

}
