package com.locochoco.editor;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

enum PipeSubMode {
  NONE,
  SPAWNER,
  CONNECTOR,
  MOVE,
  DELETE
}

public class PipeMode extends EditorMode<PipeSubMode> {

  boolean was_any_pressed;
  private int last_id;

  private HashMap<Integer, PipeTile> pipes;

  public PipeMode(EditorController controller, InputAPI inputs) {
    super(controller, inputs, PipeSubMode.NONE);
    AddSubmode(PipeSubMode.SPAWNER, new PipeSpawnMode(controller, this, inputs));
    AddSubmode(PipeSubMode.CONNECTOR, new PipeConnectorMode(this, inputs));
    AddSubmode(PipeSubMode.MOVE, new MoveMode(controller, this, inputs));
    AddSubmode(PipeSubMode.DELETE, new DeleteMode(this, inputs));
    last_id = 0;
    pipes = new HashMap<>();
  }

  public void OnEnterMode() {
    System.out.println("Pipe Mode");
    SwitchMode(PipeSubMode.NONE);
    was_any_pressed = true;
  }

  public void OnLoopMode() {
    boolean connector = inputs.GetKeyPressed(KeyEvent.VK_C);
    boolean spawner = inputs.GetKeyPressed(KeyEvent.VK_S);
    boolean delete = inputs.GetKeyPressed(KeyEvent.VK_D);
    boolean move = inputs.GetKeyPressed(KeyEvent.VK_M);
    boolean clear = inputs.GetKeyPressed(KeyEvent.VK_DELETE);
    boolean exit = inputs.GetKeyPressed(KeyEvent.VK_ESCAPE);

    PipeSubMode new_mode = GetCurrentSubmode();
    if (!was_any_pressed) {
      switch (GetCurrentSubmode()) {
        case PipeSubMode.NONE:
          if (connector)
            new_mode = PipeSubMode.CONNECTOR;
          else if (spawner)
            new_mode = PipeSubMode.SPAWNER;
          else if (move)
            new_mode = PipeSubMode.MOVE;
          else if (delete)
            new_mode = PipeSubMode.DELETE;
          if (clear) {
            ClearAllTiles();
          }
          break;
        default:
          if (exit)
            new_mode = PipeSubMode.NONE;
          break;
      }
    }
    was_any_pressed = connector || spawner || exit || delete || move || clear;
    SwitchMode(new_mode);

    super.OnLoopMode();
  }

  public void OnExitMode() {
  }

  public PipeEntranceTile AddEntrance(Point2d position) {
    last_id++;
    PipeEntranceTile pipe = new PipeEntranceTile(controller, position, last_id);
    AddTile(pipe);
    pipes.put(last_id, pipe);
    System.out.printf("Added entrance %s\n", last_id);
    return pipe;
  }

  public PipeConnectorTile AddConnector(Point2d position) {
    last_id++;
    PipeConnectorTile pipe = new PipeConnectorTile(controller, position, last_id);
    AddTile(pipe);
    pipes.put(last_id, pipe);
    System.out.printf("Added entrance %s\n", last_id);
    return pipe;
  }

  public PipeTile GetPipeTile(int id) {
    return pipes.get(id);
  }

}
