package com.locochoco.game;

import com.locochoco.gameengine.*;

public class ConnectionPipe extends Pipe {
  private Renderer flash;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    super.Start();
    flash = getGameObject().findFirstChild("flash").getRenderer();
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
    flash.enabled = this.GetObjectsBeingPiped().size() > 0;
  }

  public void LateUpdate(double delta_time) {
  }

}
