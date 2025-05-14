package com.locochoco.game;

import java.awt.event.KeyEvent;

import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public class PlayerUI extends Component {

  private InputAPI inputs;

  public void OnCreated() {
    inputs = GameEngine.getGameEngine().getInputs();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
  }

  public void OnDestroyed() {
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
    // Return to main menu
    if (inputs.GetKeyPressed(KeyEvent.VK_HOME))
      GameEngine.getGameEngine().LoadLevel("levels/mainMenu/main.json");
  }

  public void LateUpdate(double delta_time) {
  }
}
