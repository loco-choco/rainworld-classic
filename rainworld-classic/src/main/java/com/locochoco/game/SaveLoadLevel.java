package com.locochoco.game;

import java.awt.event.KeyEvent;
import java.io.IOException;

import com.locochoco.gameengine.*;

public class SaveLoadLevel extends Component {

  private InputAPI input;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    input = GameEngine.getGameEngine().getInputs();
  }

  public void OnDestroyed() {
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
    if (input.GetKeyPressed(KeyEvent.VK_S)) {
      try {
        GameEngine.getGameEngine().getLevel().SaveLevelToJson("save.json");
      } catch (IOException ioe) {
        System.err.printf("Issues saving the level!: %s\n", ioe.getMessage());
      }
    } else if (input.GetKeyPressed(KeyEvent.VK_L)) {
      GameEngine.getGameEngine().LoadLevel("save.json");
    }
  }

  public void LateUpdate(double delta_time) {
  }
}
