package com.locochoco.game;

import java.awt.event.KeyEvent;

import javax.vecmath.Vector2d;
import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class EyesController extends Component {

  private InputAPI inputs;
  private SpriteRenderer eyes;

  public double movement_radius;

  public void OnCreated() {
    inputs = GameEngine.getGameEngine().getInputs();
    movement_radius = 1;
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    Renderer r = getGameObject().getRenderer();
    if (r instanceof SpriteRenderer eyes)
      this.eyes = eyes;
    else
      System.err.println("This component was attached to a non SpriteRenderer object! Check if this is intended!");
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
    double horizontal_movement = inputs.GetKeyPressed(KeyEvent.VK_LEFT) ? -1 : 0;
    horizontal_movement += inputs.GetKeyPressed(KeyEvent.VK_RIGHT) ? 1 : 0;
    double vertical_movement = inputs.GetKeyPressed(KeyEvent.VK_UP) ? -1 : 0;
    vertical_movement += inputs.GetKeyPressed(KeyEvent.VK_DOWN) ? 1 : 0;
    Vector2d movement_dir = new Vector2d(horizontal_movement, vertical_movement);
    if (movement_dir.length() != 0)
      movement_dir.normalize();
    movement_dir.scale(movement_radius);
    if (eyes != null)
      eyes.SetCenter(new Point2d(movement_dir));
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }
}
