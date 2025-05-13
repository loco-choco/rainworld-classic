package com.locochoco.game;

import java.awt.Color;

import com.locochoco.gameengine.*;

public class ConnectionPipe extends Pipe {
  private BoxRenderer flash;

  public void OnCreated() {
    super.OnCreated();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    flash = (BoxRenderer) getGameObject().findFirstChild("flash").getRenderer();
    flash.SetColor(Color.WHITE);
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
    flash.setEnabled(this.GetObjectsBeingPiped().size() > 0);
  }

  public void LateUpdate(double delta_time) {
  }

}
