package com.locochoco.editor;

import java.awt.event.KeyEvent;

import com.locochoco.gameengine.*;

public class EditorUI extends Component {

  private InputAPI inputs;
  private TextRenderer mode_status;
  private EditorController editor;

  public void OnCreated() {
    inputs = GameEngine.getGameEngine().getInputs();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    mode_status = (TextRenderer) getGameObject().findFirstChild("mode_ui").getRenderer();
    editor = EditorController.GetInstance();
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

    // Mode UI
    String mode = editor.GetCurrentMode();
    String status = editor.GetModeStatus();
    if (!status.isEmpty())
      mode = String.format("%s > %s", mode, status);
    mode_status.SetText(mode);

  }

  public void LateUpdate(double delta_time) {
  }
}
