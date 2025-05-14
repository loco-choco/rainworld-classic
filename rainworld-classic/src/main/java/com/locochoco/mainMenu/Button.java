package com.locochoco.mainMenu;

import com.locochoco.gameengine.*;

public abstract class Button extends Component {

  private InputAPI inputs;
  private Collider collider;
  private boolean was_mouse_clicked;

  public void OnCreated() {
    inputs = GameEngine.getGameEngine().getInputs();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    collider = getGameObject().getCollider();
    was_mouse_clicked = false;
  }

  public void OnDestroyed() {
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public abstract void OnButtonPressed();

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
    boolean mouse_click = inputs.GetMouseLeftClick();
    if (mouse_click && !was_mouse_clicked
        && CollisionMath.CheckPointCollision(collider, inputs.GetMousePos()))
      OnButtonPressed();
    was_mouse_clicked = mouse_click;
  }

  public void LateUpdate(double delta_time) {
  }
}
