package com.locochoco.editor;

import java.awt.event.KeyEvent;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class PipeSpawnMode extends EditorSubmode {

  enum PipeTypes {
    ENTRANCE,
    NEXT_ROOM,
    SPAWNER
  }

  private int current_type;
  private InputAPI inputs;
  private boolean was_mouse_clicked;
  private boolean was_next_pipe;
  private EditorController controller;
  private Tile selected_pipe;

  public PipeSpawnMode(EditorController controller, PipeMode mode, InputAPI inputs) {
    super(mode);
    this.inputs = inputs;
    this.controller = controller;
    current_type = 0;
  }

  public void OnEnterSubmode() {
    System.out.println("\tPipe Spawn Mode");
    was_mouse_clicked = false;
    current_type = 0;
  }

  public void OnExitSubmode() {
  }

  public String GetStatus() {
    return PipeTypes.values()[current_type].name();
  }

  public void OnLoopSubmode() {
    boolean next_pipe = inputs.GetKeyPressed(KeyEvent.VK_T);
    if (next_pipe && !was_next_pipe) {
      current_type = (current_type + 1) % PipeTypes.values().length;
      System.out.printf("\t\t Pipe selected: %s\n", PipeTypes.values()[current_type]);
    }

    was_next_pipe = next_pipe;
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    Tile pressed_pipe;
    if (mouse_clicked && !was_mouse_clicked) {
      pressed_pipe = mode.GetTileUnderCursor();
      if (selected_pipe == null && pressed_pipe == null) {
        PipeMode pipe_mode = (PipeMode) mode;
        switch (PipeTypes.values()[current_type]) {
          case ENTRANCE:
            pipe_mode.AddEntrance(EditorUI.MousePosition());
            break;
          case NEXT_ROOM:
            pipe_mode.AddNextRoom(EditorUI.MousePosition());
            break;
          case SPAWNER:
            pipe_mode.AddSpawner(EditorUI.MousePosition());
            break;
        }
      }
    }
    was_mouse_clicked = mouse_clicked;
  }

}
