package com.locochoco.editor;

import java.awt.event.KeyEvent;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class EditorUI extends Component {
  private static EditorUI instance;
  private InputAPI inputs;
  private TextRenderer mode_status;
  private EditorController editor;

  public void OnCreated() {
    instance = this;
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
    instance = null;
  }

  public static Point2d MousePosition() {
    if (instance == null)
      return new Point2d();
    Point2d mouse_pos = new Point2d(instance.inputs.GetMousePos());
    Point2d camera_pos = instance.getGameObject().getTransform().getGlobalPosition();
    mouse_pos.add(camera_pos);
    mouse_pos.setX(mouse_pos.getX() - 70);
    mouse_pos.setY(mouse_pos.getY() - 70);
    return mouse_pos;
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

    double speed = 70;
    int horizontal_movement = 0;
    int vertical_movement = 0;
    horizontal_movement += inputs.GetKeyPressed(KeyEvent.VK_LEFT) ? -1 : 0;
    horizontal_movement += inputs.GetKeyPressed(KeyEvent.VK_RIGHT) ? 1 : 0;
    vertical_movement += inputs.GetKeyPressed(KeyEvent.VK_UP) ? -1 : 0;
    vertical_movement += inputs.GetKeyPressed(KeyEvent.VK_DOWN) ? 1 : 0;

    Point2d new_pos = new Point2d(getGameObject().getTransform().getGlobalPosition());
    new_pos.setX(new_pos.getX() + horizontal_movement * speed * delta_time);
    new_pos.setY(new_pos.getY() + vertical_movement * speed * delta_time);
    getGameObject().getTransform().setPosition(new_pos);
  }

  public void LateUpdate(double delta_time) {
  }
}
