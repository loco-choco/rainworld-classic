package com.locochoco.editor;

public abstract class EditorMode {
  protected EditorController controller;

  public EditorMode(EditorController controller) {
    this.controller = controller;
  }

  public abstract void OnEnterMode();

  public abstract void OnExitMode();

  public abstract void OnLoopMode();
}
