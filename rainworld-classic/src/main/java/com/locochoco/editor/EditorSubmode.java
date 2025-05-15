package com.locochoco.editor;

public abstract class EditorSubmode {
  protected EditorMode<?> mode;

  public EditorSubmode(EditorMode<?> mode) {
    this.mode = mode;
  }

  public abstract void OnEnterSubmode();

  public abstract void OnExitSubmode();

  public abstract void OnLoopSubmode();

  public String GetStatus() {
    return "";
  }
}
