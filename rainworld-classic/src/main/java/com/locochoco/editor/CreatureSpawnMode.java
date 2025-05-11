package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class CreatureSpawnMode extends EditorMode {

  private InputAPI inputs;
  private boolean was_mouse_clicked;

  public CreatureSpawnMode(EditorController controller, InputAPI inputs) {
    super(controller);
    this.inputs = inputs;
  }

  public void OnEnterMode() {
    System.out.println("Creature Spawn Mode");
    was_mouse_clicked = false;
  }

  public void OnExitMode() {
  }

  public void OnLoopMode() {
    boolean mouse_clicked = inputs.GetMouseLeftClick();
    if (mouse_clicked && !was_mouse_clicked) {
      CreatureSpawnerTile spawner = new CreatureSpawnerTile(controller, inputs.GetMousePos());
      controller.AddTile(spawner);
    }
    was_mouse_clicked = mouse_clicked;
  }

}
